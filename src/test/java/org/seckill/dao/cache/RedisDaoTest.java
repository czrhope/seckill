package org.seckill.dao.cache;

import java.util.List;

import org.junit.Test;
/*
 * 配置spring和Junit整合，junit启动时加载springIOC容器
 */
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit spring配置文件位置
@ContextConfiguration({"classpath:spring/spring-dao.xml",
						"classpath:spring/spring-redis.xml"})
public class RedisDaoTest {

	@Autowired
	private RedisDao redisDao;
	
	@Autowired
	private SeckillDao seckillDao;
	
	private Seckill seckill;
	
	@Test
	public void testGetSeckillListByRedisTemplate() {
		List<Seckill> seckillList = redisDao.getSeckillListByRedisTemplate("seckillList", 0l, 3l);
		if(seckillList == null || seckillList.isEmpty()) {
			seckillList = seckillDao.queryAll(0, 4);
			Long updateResult = redisDao.updateSeckillListByRedisTemplate("seckillList", seckillList);
			System.out.println(updateResult);
		}
		System.out.println(seckillList);
	}
	@Test
	public void testGetSeckill() {
		Seckill resultSeckill = redisDao.getSeckillByJedis(1000l);
		System.out.println(resultSeckill);
	}

	@Test
	public void testPutSeckill() {
		seckill = new Seckill();
		seckill.setSeckillId(1000l);
		seckill.setName("iphone");
		String result = redisDao.putSeckillByJedis(seckill);
		System.out.println(result);
	}

}
