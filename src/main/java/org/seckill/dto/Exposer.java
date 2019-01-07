package org.seckill.dto;

/**
 * 秒杀暴露控制
 * @author chenzhangrun
 */
public class Exposer {

	//是否开启秒杀
	private boolean exposed;
	
	//一种加密措施
	private String md5;
	
	//商品id
	private Long seckillId;
	
	//系统当前时间
	private Long now;
	
	private Long start;
	
	private Long end;

	public Exposer(boolean exposed, String md5, Long seckillId) {
		super();
		this.exposed = exposed;
		this.md5 = md5;
		this.seckillId = seckillId;
	}

	public Exposer(boolean exposed, Long seckillId, Long now, Long start, Long end) {
		super();
		this.seckillId = seckillId;
		this.exposed = exposed;
		this.now = now;
		this.start = start;
		this.end = end;
	}

	public Exposer(boolean exposed, Long seckillId) {
		super();
		this.exposed = exposed;
		this.seckillId = seckillId;
	}

	public Exposer(boolean exposed, String md5, Long seckillId, Long now, Long start, Long end) {
		super();
		this.exposed = exposed;
		this.md5 = md5;
		this.seckillId = seckillId;
		this.now = now;
		this.start = start;
		this.end = end;
	}

	public boolean isExposed() {
		return exposed;
	}

	public void setExposed(boolean exposed) {
		this.exposed = exposed;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public Long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(Long seckillId) {
		this.seckillId = seckillId;
	}

	public Long getNow() {
		return now;
	}

	public void setNow(Long now) {
		this.now = now;
	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getEnd() {
		return end;
	}

	public void setEnd(Long end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "Exposer [exposed=" + exposed + ", md5=" + md5 + ", seckillId=" + seckillId + ", now=" + now + ", start="
				+ start + ", end=" + end + "]";
	}
}
