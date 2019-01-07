package org.seckill.dao.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * redis操作类
 * @author chenzhangrun
 */
public class RedisDao {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final JedisPool jedisPool;
	
	@Autowired
	private RedisTemplate<String, Seckill> redisTemplate;
	
	//创建protostuff的约束对象
	private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
	
	public RedisDao(String host,int port) {
		jedisPool = new JedisPool(host, port);
	}
	
	/**
	 * 通过redistemplate获取秒杀列表
	 * @param key redis键值
	 * @param start 列表起始下标
	 * @param end 列表结束下标
	 */
	public List<Seckill> getSeckillListByRedisTemplate(String key,Long start,Long end) {
		List<Seckill> seckillList = new ArrayList<Seckill>();
		try {
			seckillList = redisTemplate.boundListOps(key).range(start,end);
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return seckillList;
	}
	
	/**
	 * 通过redisTemplate更新秒杀列表
	 * @param key redis键值
	 * @param seckillList 新的秒杀列表
	 */
	public Long updateSeckillListByRedisTemplate(String key,List<Seckill> seckillList) {
		//更新前先删除key
		redisTemplate.delete(key);
		//重新创建key
		BoundListOperations<String,Seckill> listOps = redisTemplate.boundListOps(key);
		//保存数组
		Seckill[] seckillArray = new Seckill[seckillList.size()];
		Long result = listOps.rightPushAll(seckillList.toArray(seckillArray));
		//设置key的过期时间
		listOps.expire(10l, TimeUnit.SECONDS);
		return result;
	}
	/**
	 * 通过Jedis获取秒杀对象
	 * @param seckillId
	 * @return
	 */
	public Seckill getSeckillByJedis(Long seckillId) {
		
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckillId;
				//从redis中取出数据
				byte[] bytes = jedis.get(key.getBytes());
				//用protostuff将字节数据反序列化为java对象
				if(bytes != null) {
					Seckill seckill = schema.newMessage();
					ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
					return seckill;
				}
			}finally {
				jedis.close();
			}
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 通过jedis保存秒杀对象
	 * @param seckill
	 * @return
	 */
	public String putSeckillByJedis(Seckill seckill) {
		
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckill.getSeckillId();
				//将java对象序列化为字节数组
				byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, 
						LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				//设置超时时间
				int timeout = 60 * 60;
				String result = jedis.setex(key.getBytes(), timeout, bytes);
				return result;
			}finally {
				jedis.close();
			}
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
}
