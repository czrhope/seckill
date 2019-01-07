package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

public interface SuccessKilledDao {

	/*
	 * 插入秒杀明细记录
	 */
	public int insertSuccessKilled(@Param("seckillId") Long seckillId,@Param("userPhone") Long userPhone);
	/*
	 * 查询秒杀明细记录
	 */
	public SuccessKilled queryByIdWithSeckill(@Param("seckillId") Long seckillId,@Param("userPhone") Long userPhone);
}
