-- 数据库初始化脚本

-- 创建数据库
CREATE DATABASE seckill;
-- 使用数据库
use seckill;
-- 创建秒杀库存表
DROP TABLE IF EXISTS seckill;
CREATE TABLE seckill(
`seckill_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品id',
`name` VARCHAR(120) NOT NULL COMMENT '商品名称',
`number` INT NOT NULL COMMENT '库存数量',
`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀创建时间',
`start_time` TIMESTAMP NOT NULL DEFAULT 0 COMMENT '秒杀开启时间',
`end_time` TIMESTAMP NOT NULL DEFAULT 0 COMMENT '秒杀结束时间',
PRIMARY KEY (seckill_id),
KEY idx_start_time(start_time),
KEY idx_end_time(end_time),
key idx_create_time(create_time)
)ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT '秒杀库存表';

-- 初始化数据
INSERT INTO 
	seckill(name,number,start_time,end_time) 
VALUES 
	('2000元秒杀iphoneX',100,'2018-12-07 00:00:00','2018-12-08 00:00:00'),
	('1000元秒杀华为mate',100,'2018-12-07 00:00:00','2018-12-08 00:00:00'),
	('500元秒杀ipad',100,'2018-12-07 00:00:00','2018-12-08 00:00:00'),
	('300元秒杀红米',100,'2018-12-07 00:00:00','2018-12-08 00:00:00');

-- 秒杀成功明细表
-- 用户登录认证相关信息
DROP TABLE IF EXISTS success_killed;
CREATE TABLE success_killed(
`seckill_id` BIGINT NOT NULL COMMENT '秒杀商品id',
`user_phone` BIGINT NOT NULL COMMENT '用户手机号',
`state` TINYINT NOT NULL DEFAULT -1 COMMENT '秒杀状态标示：-1：无效，0：成功未付款，1：已付款，2：已发货',
`create_time` TIMESTAMP NOT NULL COMMENT '明细创建时间',
PRIMARY KEY(seckill_id,user_phone),
KEY idx_create_time(create_time)
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT '秒杀成功明细表';