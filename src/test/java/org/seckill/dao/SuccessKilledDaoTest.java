package org.seckill.dao;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/*
 * 配置spring和Junit整合，junit启动时加载springIOC容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit spring配置文件位置
@ContextConfiguration({"classpath:/spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

	@Resource
	private SuccessKilledDao successKilledDao;
	
	@Test
	public void testInsertSuccessKilled() {
		int insertCount = successKilledDao.insertSuccessKilled(1000l, 18435145400l);
		System.out.println(insertCount);
	}

	@Test
	public void testQueryByIdWithSeckill() {
		SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1000l, 18435145400l);
		System.out.println(successKilled);
		System.out.println(successKilled.getSecKill());
	}

}
