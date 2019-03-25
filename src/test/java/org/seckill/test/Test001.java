package org.seckill.test;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"
						,"classpath:spring/spring-service.xml"})
public class Test001 {

	@Test
	public void test() {
		ApplicationContext context =  new ClassPathXmlApplicationContext("spring/spring-dao.xml");
		SqlSessionFactoryBean sqlsessionFactoryBean = context.getBean(SqlSessionFactoryBean.class);
		try {
			SqlSessionFactory sessionFactory = sqlsessionFactoryBean.getObject();
			SqlSession sqlSession = sessionFactory.openSession();
			SeckillDao seckillDao = sqlSession.getMapper(SeckillDao.class);
			Seckill seckill = seckillDao.queryById(1000l);
			System.out.println(seckill);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
