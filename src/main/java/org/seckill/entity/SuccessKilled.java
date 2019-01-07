package org.seckill.entity;

import java.sql.Date;

/**
 * 秒杀成功记录
 * @author chenzhangrun
 */
public class SuccessKilled {

	private Long seckillId;
	private Long userPhone;
	private Short state;
	private Date createTime;
	//多对一
	private Seckill secKill;
	public Long getSeckillId() {
		return seckillId;
	}
	public void setSeckillId(Long seckillId) {
		this.seckillId = seckillId;
	}
	public Long getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(Long userPhone) {
		this.userPhone = userPhone;
	}
	public Short getState() {
		return state;
	}
	public void setState(Short state) {
		this.state = state;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Seckill getSecKill() {
		return secKill;
	}
	public void setSecKill(Seckill secKill) {
		this.secKill = secKill;
	}
	@Override
	public String toString() {
		return "SuccessKilled [seckillId=" + seckillId + ", userPhone=" + userPhone + ", state=" + state
				+ ", createTime=" + createTime + ", secKill=" + secKill + "]";
	}
	
	
}
