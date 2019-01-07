package org.seckill.web;

import java.util.Date;
import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value="/seckill")
public class SeckillController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SeckillService seckillService;
	/**
	 * 查询秒杀商品列表页
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET )
	public String list(Model model) {
		
		//List<Seckill> seckillList = seckillService.getSeckillList();
		List<Seckill> seckillList = seckillService.getSeckillListByRedisTemplate("seckillList", 0l, 3l);
		model.addAttribute("seckillList", seckillList);
		return "list";
	}
	/**
	 * 获得秒杀详情页
	 * @param seckillId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/{seckillId}/detail",method=RequestMethod.GET)
	public String detail(@PathVariable("seckillId")Long seckillId, Model model) {
		if(seckillId == null) {
			return "redirect:/seckill/seckill/list";
		}
		Seckill seckill = seckillService.getById(seckillId);
		if(seckill == null) {
			return "forward:/seckill/seckill/list";
		}
		model.addAttribute("seckill", seckill);
		return "detail";
	}
	
	/**
	 * 获得服务器当前时间
	 * @return
	 */
	@RequestMapping(value="/time/now",method=RequestMethod.GET)
	@ResponseBody
	public Long getCurrentTime() {
		
		Date date = new Date();
		return date.getTime();
	}
	
	/**
	 * 获得秒杀请求地址
	 * @param seckillId
	 * @return
	 */
	@RequestMapping(value="/{seckillId}/getSeckillURL",method=RequestMethod.POST,
			produces= {"application/json;charset=utf-8"})
	@ResponseBody
	public Exposer exportSeckillURL(@PathVariable("seckillId")Long seckillId) {
		Exposer exposer = null;
		try {			
			exposer = seckillService.exportSeckillUrl(seckillId);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			exposer = new Exposer(false, seckillId);
		}
		return exposer;
	}
	
	/**
	 * 执行秒杀
	 * @param md5
	 * @param seckillId
	 * @return
	 */
	@RequestMapping(value="/{seckillId}/{md5}/execution",method=RequestMethod.POST,
			produces= {"application/json;charset=utf-8"})
	@ResponseBody
	public SeckillExecution executeSeckill(@PathVariable("md5") String md5,
											@PathVariable("seckillId") Long seckillId,
											@CookieValue("seckillPhone") Long seckillPhone) {
		
		SeckillExecution seckillExecution;			
		seckillExecution = seckillService.executeSeckillByProcedure(seckillId, seckillPhone, md5);
		return seckillExecution;
	}
}
