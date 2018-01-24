package com.ccx.credit.risk.api;

import org.springframework.stereotype.Component;

@Component
public interface MobileCodeApi {
    /**
	 * 发送手机短信验证码
	 * @param phone				手机号（用户输入），非空
	 * @param template			短信模板（验证码位置使用VCODE替换）可为空，默认：验证码为VCODE，请在5分钟内输入。有问题请致电4001-858-939。
//	 * @param vCode				短信验证码，可为空，默认：100000~999999 随机数
	 * @param smsRedisKey		存储验证码的 redis key值， 可为空，默认：SEND_SMS_手机号
	 * @param maxSendCount	最多发送次数，可为空，默认：6
	 * @return	resCode 统一返回码
	 * @throws Exception 
	 */
	public String sendVerifyCode(String account,String phone, String template, String vCode, String smsRedisKey, Integer maxSendCount) throws Exception;
	
	/**
	 * 检查验证码是否正确
	 * @param phone				手机号（用户输入），非空
	 * @param vcode				短信验证码（用户输入），非空
	 * @param smsRedisKey		存储验证码的 redis key值， 可为空，默认：SEND_SMS_手机号
	 * @param failMillisecond		验证码过期时间（毫秒），可为空，默认：1000 * 60 * 5 
	 * @param maxErrorCount	最多错误次数，可为空，默认：5
	 * @return resCode 统一返回码
	 * @throws Exception 
	 */
	public String checkVerifyCode(String account,String phone, String vcode, String smsRedisKey, Integer failMillisecond, Integer maxErrorCount) throws Exception;
	
	/**
	 * 检查密码/手机验证码连续输入错误次数 ,统计
	 * @param seriesErrorKey 账号KEY，非空
	 * @param failMillisecond 验证码过期时间（毫秒），可为空，默认：1000 * 60 * 60 * 24
	 * @param maxErrorCount 最多错误次数，可为空，默认：5
	 * @param cleanFlag 是否清除当天连续次数
	 * @return
	 * @throws Exception
	 */
	public String seriesErrCountVerify(String seriesErrorKey , String type, Integer failMillisecond, Integer maxErrorCount,Boolean cleanFlag) throws Exception;
}
