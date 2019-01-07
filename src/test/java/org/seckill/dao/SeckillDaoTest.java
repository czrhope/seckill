package org.seckill.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/*
 * 配置spring和Junit整合，junit启动时加载springIOC容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit spring配置文件位置
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
	
	//注入Dao实现类
	@Resource
	private SeckillDao seckillDao;
	
	@Test
	public void testQueryById() {
		Seckill seckill = seckillDao.queryById(1000l);
		System.out.println(seckill.getName());
		System.out.println(seckill);
	}
	
	@Test
	public void testQueryAll() {
		List<Seckill> seckillList = seckillDao.queryAll(0, 100);
		for(Seckill seckill:seckillList) {
			System.out.println(seckill);
		}
	}
	
	@Test
	public void testReduceNumber() {
		Date killTime = new Date();
		int updateCount = seckillDao.reduceNumber(1000l, killTime);
		System.out.println(updateCount);
	}

}
