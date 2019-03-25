package org.seckill.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/*
 * 配置spring和Junit整合，junit启动时加载springIOC容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit spring配置文件位置
@ContextConfiguration({"classpath:spring/spring-dao.xml"
						,"classpath:spring/spring-service.xml"
						,"classpath:spring/spring-redis.xml"})
public class SeckillServiceTest {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillService seckillService;

	@Test
	public void testGetSeckillList() {
		List<Seckill> seckillList = seckillService.getSeckillList();
		logger.info("seckillList={}", seckillList);
	}

	@Test
	public void testGetById() {
		Seckill seckill = seckillService.getById(1000l);
		logger.info("seckill={}", seckill);
	}

	@Test
	public void testSeckillLogic() {
		Long id = 1000l;
		Exposer exposer = seckillService.exportSeckillUrl(id);
		if(exposer.isExposed()) {
			logger.info("exposer={}", exposer);
			Long userPhone = 18888888888l;
			String md5 = exposer.getMd5();
			try {
				SeckillExecution execution = seckillService.executeSeckill(id, userPhone, md5);
				logger.info("result={}", execution);
			} catch (RepeatKillException e){
				logger.error(e.getMessage());
			} catch (SeckillCloseException e){
				logger.error(e.getMessage());
			}	
		}else {
			logger.info("exposer={}", exposer);
		}
	}

}
