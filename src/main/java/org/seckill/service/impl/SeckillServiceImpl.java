package org.seckill.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

@Service
public class SeckillServiceImpl implements SeckillService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	//MD5盐值字符串
	private final String slat = "annJH!@#g;g*&((_+(_ahglahdfHONN&*7782";
	
	@Autowired
	private SeckillDao seckillDao;
	
	@Autowired
	private SuccessKilledDao successKilledDao;
	
	@Autowired
	private RedisDao redisDao;

	@Override
	public List<Seckill> getSeckillList() {
		List<Seckill> seckillList = seckillDao.queryAll(0, 4);
		return seckillList;
	}

	/**
	 * 通过redistemplate获取秒杀列表
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	@Override
	public List<Seckill> getSeckillListByRedisTemplate(String key,Long start,Long end){
		List<Seckill> seckillList = redisDao.getSeckillListByRedisTemplate(key, start, end);
		if(seckillList == null || seckillList.isEmpty()) {
			//如果redis中没有列表数据，则从数据库中获取并保存到redis中
			seckillList = seckillDao.queryAll(0, 4);
			redisDao.updateSeckillListByRedisTemplate("seckillList", seckillList);
		}
		return seckillList;
	}
	
	@Override
	public Seckill getById(Long seckillId) {
		Seckill seckill = seckillDao.queryById(seckillId);
		return seckill;
	}

	@Override
	public Exposer exportSeckillUrl(Long seckillId) {
		
		//首先去redis缓存中去拿
		Seckill seckill = redisDao.getSeckillByJedis(seckillId);
		//如果没有，在去数据库中拿
		if(seckill == null) {
			seckill = seckillDao.queryById(seckillId);
			//如果数据库中还没有则直接返回
			if(seckill == null) {
				return new Exposer(false, seckillId);
			}
			//从数据库中取出来后放入Redis缓存中
			redisDao.putSeckillByJedis(seckill);
		}
		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		Date currentTime = new Date();
		if(currentTime.getTime() < startTime.getTime()
				|| currentTime.getTime() > endTime.getTime()) {
			return new Exposer(false,seckillId,currentTime.getTime(),startTime.getTime(),endTime.getTime());
		}
		String md5 = getMD5(seckillId);
		return new Exposer(true, md5, seckillId, currentTime.getTime(),startTime.getTime(),endTime.getTime());
	}

	/**
	 * 生成MD5字符串
	 * @param seckillId 秒杀商品id
	 * @return md5字符串
	 */
	private String getMD5(Long seckillId) {
		String base = seckillId + "/" + slat;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}
	
	/**
	 * 使用注解控制事务方法的优点：
	 * 1：开发团队达成一致约定，明确标注事务方法的变成风格。
	 * 2：保证事务方法的执行时间尽可能的短，即事务方法内尽量不要有除了数据库操作以外的操作，如http请求。
	 * 3：不是所有的方法都需要事务，如果只有一条修改操作，或者只读操作，则不需要事务控制。
	 */
	@Override
	@Transactional
	public SeckillExecution executeSeckill(Long seckillId, Long userPhone, String md5)
			throws SeckillException,SeckillCloseException,RepeatKillException{
		
		try {
			if(md5 == null || !md5.equals(getMD5(seckillId))) {
				throw new SeckillException("System Error!!!!!");
			}
			//先执行插入操作可以先过滤掉一部分重复秒杀
			//记录购买行为
			int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
			if(insertCount <= 0) {
				//如果没有插入记录，重复秒杀
				throw new RepeatKillException("RepeatKill!!!!");
			}else {
				//插入成功后执行减库存操作
				Date now = new Date();
				int updateCount = seckillDao.reduceNumber(seckillId, now);
				if(updateCount <= 0) {
					//没有更新记录，秒杀处于关闭状态（此时商品没有库存或者当前时间不在秒杀时间之内）
					throw new SeckillCloseException("Seckill is close!!!!");
				}else {
					SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
					return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
				}
			}
		} catch (SeckillCloseException seckillCloseException) {
			throw seckillCloseException;
		} catch (RepeatKillException repeatKillException) {
			throw repeatKillException;
		} catch (Exception e){
			logger.error(e.getMessage());
			//所有的编译期异常转换为运行期异常
			throw new SeckillException("seckill inner error:"+e.getMessage());
		}
	}

	/**
	 * 由于已经在mysql存储过程中使用了事务，因此在这里不需要开启spring的事务控制
	 */
	@Override
	public SeckillExecution executeSeckillByProcedure(Long seckillId, Long userPhone, String md5){
		
		if(md5 == null || !md5.equals(getMD5(seckillId))) {
			return new SeckillExecution(seckillId, SeckillStateEnum.SYSTEM_ERROR);
		}
		Map<String,Object> paramMap = new HashMap<String,Object>();
		Date seckillTime = new Date();
		paramMap.put("seckillId", seckillId);
		paramMap.put("userPhone", userPhone);
		paramMap.put("seckillTime", seckillTime);
		paramMap.put("result", null);
		try {
			//执行完存存储过程以后map中的result被赋值
			seckillDao.seckillByProcedure(paramMap);
			//获取map中result的值判断执行结果
			int result = (int) paramMap.get("result");
			if(result == 0) {
				return new SeckillExecution(seckillId, SeckillStateEnum.FAILED);
			}else if(result == -1) {
				return new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
			}else if(result == 1) {
				SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
				return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
			}else {
				return new SeckillExecution(seckillId, SeckillStateEnum.SYSTEM_ERROR);
			}
		}catch(Exception e) {
			logger.error(e.getMessage());
			return new SeckillExecution(seckillId, SeckillStateEnum.SYSTEM_ERROR);
		}
	}
}
