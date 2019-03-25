//存放主要交互逻辑js代码
//javascript 模块化
var seckill = {
	//封装秒杀相关ajax的url
	URL:{
		getCurrentTimeURL:function(){
			return "/seckill/seckill/time/now";
		},
		getSeckillURL:function(seckillId){
			return "/seckill/seckill/"+seckillId+"/getSeckillURL";
		},
		executionURL:function(seckillId,md5){
			return "/seckill/seckill/"+seckillId+"/"+md5+"/executionByProcedure";
		}
	},
	exportSeckill:function(seckillId,seckillBox){
		seckillBox.hide()
			.html('<button class="btn btn-primary btn-lg" id="seckillBtn">开始秒杀</button>');
		$.post(seckill.URL.getSeckillURL(seckillId),{},function(exposer){
			var now = exposer.now;
			var start = exposer.start;
			var end = exposer.end;
			if(!exposer.exposed){
				//如果秒杀还未开启或者秒杀已经结束，则返回倒计时
				seckill.countdown(seckillId,now,start,end);
			}else{
				//秒杀开始，暴露秒杀地址
				var md5 = exposer.md5;
				var executionURL = seckill.URL.executionURL(seckillId,md5);
				//给按钮绑定秒杀事件
				$('#seckillBtn').one('click',function(){
					//1:先禁用按钮，一次点击过后禁用
					$(this).addClass('disabled');
					//2:发送秒杀请求执行秒杀
					$.post(executionURL,{},function(seckillExecution){
						var seckillState = seckillExecution.state;
						var stateInfo = seckillExecution.stateInfo;
						var state = seckillExecution.state;
						//显示秒杀结果
						if(state == null || state == -2){
							seckillBox.html('<span class="label label-danger">'+stateInfo+'</span>');
						}else if(state == -1){
							seckillBox.html('<span class="label label-warning">'+stateInfo+'</span>');
						}else if(state == 0){
							seckillBox.html('<span class="label label-danger">'+stateInfo+'</span>');
						}else{
							seckillBox.html('<span class="label label-success">'+stateInfo+'</span>');
						}
						
					});
				});
			}
			seckillBox.show();
		});
	},
	//验证手机号的格式
	validatePhone:function(phone){
		if(phone && phone.length == 11 && !isNaN(phone)){
			return true;
		}else{
			return false;
		}
	},
	//倒计时逻辑
	countdown:function(seckillId,currentTime,startTime,endTime){
		var seckillBox = $('#seckill-box');
		if(currentTime > endTime){
			//秒杀已经结束
			seckillBox.html('秒杀结束');
		}else if(currentTime < startTime){
			//秒杀未开始，开始计时
			var startTime = new Date(startTime);
			seckillBox.countdown(startTime,function(event){
				//设置时间格式
				var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒');
				seckillBox.html(format);
			}).on('finish.countdown',function(){
				//获取秒杀地址，控制显示逻辑，执行秒杀
				seckill.exportSeckill(seckillId,seckillBox);
			});
		}else{
			//正在秒杀
			seckill.exportSeckill(seckillId,seckillBox);
		}
	},
	//详情页秒杀逻辑
	detail:{
		init:function(params){
			//手机验证和登录，计时交互
			//规划我们的交互流程
			//在cookie中查找手机号，如果没有的话，则显示登录弹出层
			var seckillPhone = $.cookie('seckillPhone');
			var startTime = params['startTime'];
			var endTime = params['endTime'];
			var seckillId = params['seckillId'];
			//验证cookie中的手机号，如果没有通过验证，则弹出登录页面
			if(!seckill.validatePhone(seckillPhone)){
				//绑定手机号
				//控制输出
				var seckillPhoneModal = $('#seckillPhoneModal');
				seckillPhoneModal.modal({
					//显示弹出层
					show:true,
					backdrop:'static',//禁止位置关闭
					keyboard:false//关闭键盘事件
				});
				$('#seckillPhoneBtn').click(function(){
					var inputPhone = $('#seckillPhone').val();
					if(seckill.validatePhone(inputPhone)){
						//将手机号写入cookie
						$.cookie('seckillPhone',inputPhone,{expires:7,path:'/seckill'})
						//刷新页面
						window.location.reload();
					}else{
						$('#seckillPhoneMessage').hide().html('<label class="label label-danger">手机号错误</label>').show(300);
					}
				});
			}
			//进入秒杀页面后开始进行计时
			var startTime = params['startTime'];
			var endTime = params['endTime'];
			var seckillId = params['seckillId'];
			$.get(seckill.URL.getCurrentTimeURL(),{},function(currentTime){
				seckill.countdown(seckillId,currentTime,startTime,endTime);
			});
		}
	}
}