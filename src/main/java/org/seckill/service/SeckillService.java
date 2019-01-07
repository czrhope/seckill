package org.seckill.service;

import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

/**
 * 秒杀业务接口
 * @author chenzhangrun
 */
public interface SeckillService {

	/**
	 * 查询所有秒杀商品
	 * @return
	 */
	public List<Seckill> getSeckillList();
	
	/**
	 * 通过redistemplate获取秒杀列表
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Seckill> getSeckillListByRedisTemplate(String key,Long start,Long end);
	
	/**
	 * 查询单个秒杀商品
	 * @param seckillId 秒杀商品id
	 * @return
	 */
	public Seckill getById(Long seckillId);
	
	/**
	 * 点击秒杀时如果可以秒杀则输出秒杀接口地址，如不能秒杀则输出秒杀开启时间和系统时间
	 * @param seckillId 商品id
	 */
	public Exposer exportSeckillUrl(Long seckillId);
	
	/**
	 * 执行秒杀操作
	 * @param seckillId 秒杀id
	 * @param userPhone 秒杀用户手机号
	 * @param md5 md5加密字符串
	 * @return 秒杀执行结果
	 * @throws SeckillException 秒杀异常
	 * @throws SeckillCloseException 秒杀关闭异常
	 * @throws RepeatKillException 重复秒杀异常
	 */
	public SeckillExecution executeSeckill(Long seckillId,Long userPhone,String md5)
		throws SeckillException,SeckillCloseException,RepeatKillException;
	
	/**
	 * 利用mysql的存储过程执行秒杀操作
	 * @param seckillId 秒杀id
	 * @param userPhone 秒杀用户手机号
	 * @param md5 md5加密字符串
	 * @return 秒杀执行结果
	 * @throws SeckillException 秒杀异常
	 * @throws SeckillCloseException 秒杀关闭异常
	 * @throws RepeatKillException 重复秒杀异常
	 */
	public SeckillExecution executeSeckillByProcedure(Long seckillId,Long userPhone,String md5);


}
