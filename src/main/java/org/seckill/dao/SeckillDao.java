package org.seckill.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

public interface SeckillDao {

	/**
	 * 减库存
	 * @param seckillId
	 * @param killTime
	 * @return
	 */
	public int reduceNumber(@Param("seckillId") Long seckillId,@Param("killTime")Date killTime);
	
	/**
	 * 根据id查询秒杀商品
	 * @param seckillId
	 * @return
	 */
	public Seckill queryById(Long seckillId);
	
	/**
	 * 查询秒杀商品列表
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);

	/**
	 * 使用存储过程执行秒杀数据库操作
	 * @param paramMap
	 */
	public void seckillByProcedure(Map<String,Object> paramMap);

}
