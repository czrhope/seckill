-- 秒杀执行存储过程
DELIMITER $$ -- 将结束符从 ; 转换成 $$
-- 定义存储过程
-- in代表传入参数，out代表传出参数
CREATE PROCEDURE `seckill`.`execute_seckill`
(in v_seckill_id BIGINT, in v_user_phone BIGINT,
in v_seckill_time TIMESTAMP, out r_result INT)
	BEGIN
		DECLARE insert_count INT DEFAULT 0;
		START TRANSACTION;
		INSERT IGNORE INTO success_killed
			(seckill_id,user_phone,state,create_time)
		VALUES
			(v_seckill_id,v_user_phone,0,v_seckill_time);
		SELECT ROW_COUNT() INTO insert_count;	-- 查询上一条sql语句的执行影响行数并保存至insert_count变量中
		IF (insert_count = 0) THEN
			ROLLBACK;
			SET r_result = -1; -- 如果插入记录失败，则返回-1，代表重复秒杀
		ELSEIF (insert_count < 0) THEN
			ROLLBACK;
			SET r_result = -2; -- 如果执行影响行数小于0，则代表sql执行出错，返回系统错误
		ELSE	-- 如果插入成功，则开始减库存操作
			UPDATE seckill
			SET number = number - 1
			WHERE seckill_id = v_seckill_id
				AND start_time < v_seckill_time
				AND end_time > v_seckill_time
				AND number > 0;
			SELECT ROW_COUNT() INTO insert_count;	-- 查询上一条sql语句的执行影响行数并保存至insert_count变量中
			IF (insert_count = 0) THEN
				ROLLBACK;
				SET r_result = 0;
			ELSEIF (insert_count < 0) THEN
				ROLLBACK;
				SET r_result = -2;
			ELSE
				COMMIT;
				SET r_result = 1;
			END IF;
		END IF;
	END;
$$ -- 存储过程定义结束
DELIMITER ; -- 将结束符更换回;

SET @r_result = -3; -- 定义一个变量用于接收存储过程返回值
CALL execute_seckill(1000,16754773901,NOW(),@r_result); -- 调用存储过程
SELECT @r_result; -- 查询返回值是否改变
