package com.ccx.credit.risk.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ccx.credit.risk.api.MobileCodeApi;
import com.ccx.credit.risk.util.DateUtils;
import com.ccx.credit.risk.util.EnumSysManage;
import com.ccx.credit.risk.util.HttpClientUtil;
import com.ccx.credit.risk.util.MD5;
import com.ccx.credit.risk.util.PropertiesUtil;
import com.ccx.credit.risk.util.RedisCacheUtil;
import com.ccx.credit.risk.util.UsedUtil;


@Service
public class MobileCodeServiceImpl implements MobileCodeApi{
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MobileCodeServiceImpl.class);
	private final static String SEND_SMS = "ABS:ccx_credit_risk:SEND_SMS_";
	private final static String PW_SERIESERR = "ABS:ccx_credit_risk:PW_SERIESERR_";
	
	private static final String CREDITRISK_SENDSMS_ACCOUNT = PropertiesUtil.getProperty("credit_risk_sendsms_account");
	private static final String CREDITRISK_SENDSMS_PRIVATEKEY = PropertiesUtil.getProperty("credit_risk_sendsms_privateKey");
	private static final String CREDITRISK_SENDSMS_URL= PropertiesUtil.getProperty("credit_risk_sendSms_url");
	private static final Integer CREDITRISK_SENDSMS_COUNT = Integer.parseInt(PropertiesUtil.getProperty("credit_risk_sendSms_count"));
	private static final Integer CREDITRISK_PW_SERIESERR_COUNT= Integer.parseInt(PropertiesUtil.getProperty("credit_risk_pw_seriesErr_count"));
	@Override
	public String sendVerifyCode(String uname,String phone, String template, String vCode, String smsRedisKey, Integer maxSendCount) throws Exception {
		if (!UsedUtil.verifyParams(uname, phone)) {
			return EnumSysManage.RC_PARAMETER_NULL.getCode();
		}
		if(template == null){
			template = "您的短信验证码为VCODE,请在5分钟内输入,客服电话010-57602240【中诚信征信】";
		}
		if(vCode == null){
			vCode = UsedUtil.calRandomScore(999999, 100000);
		}
		if(smsRedisKey == null){
			smsRedisKey = SEND_SMS + uname+":"+phone;
		}
		if(maxSendCount == null){
			maxSendCount = CREDITRISK_SENDSMS_COUNT;
		}
		JSONObject smsJson=null;
			smsJson = (JSONObject) RedisCacheUtil.getRedis(smsRedisKey);
		Integer sendCount = 0;
		if (smsJson == null) {
			// 未发送过验证码
			smsJson = new JSONObject();
			smsJson.put("sendCount", 1);
		} else {
			// 已经发过验证码
			sendCount = smsJson.getInteger("sendCount") + 1;
			smsJson.put("sendCount", sendCount);
		}
		long sendTime = System.currentTimeMillis();
		smsJson.put("errorCount", 0);
		smsJson.put("sendTime", sendTime);
		smsJson.put("phone", phone);
		smsJson.put("vcode", vCode);

		String resCode = null;
		if (sendCount <= maxSendCount) {
			
			String message = template.replace("VCODE", vCode);
			String reqTid = String.valueOf(System.currentTimeMillis());
			StringBuffer signStr = new StringBuffer();
			signStr.append("account").append(CREDITRISK_SENDSMS_ACCOUNT);
			signStr.append("message").append(message);
			signStr.append("mobile").append(phone);
			signStr.append("reqTid").append(reqTid);
			signStr.append(CREDITRISK_SENDSMS_PRIVATEKEY);

			StringBuffer smsUrl = new StringBuffer(CREDITRISK_SENDSMS_URL);
			smsUrl.append("?account=").append(CREDITRISK_SENDSMS_ACCOUNT);
			smsUrl.append("&message=").append(message);
			smsUrl.append("&mobile=").append(phone);
			smsUrl.append("&reqTid=").append(reqTid);
			smsUrl.append("&sign=").append(MD5.encryption(signStr.toString()).toUpperCase());
			
			LOGGER.info("发送短信验证码：url---"+smsUrl.toString());
			LOGGER.info("发送短信验证码：begin---");
			StopWatch watch = new StopWatch();
			watch.start();
			String result = HttpClientUtil.sendGetSSLRequest(smsUrl.toString(), "UTF-8");
			watch.stop();
			LOGGER.info("---耗时："+watch.getTotalTimeMillis()+"ms---result："+result);
			if (result != null && result.equals("通信失败")) {
				LOGGER.info("---发送失败---接口返回信息：" + result);
				resCode = EnumSysManage.RC_FAILED.getCode();
			}else{
				JSONObject resultJson = JSON.parseObject(result);
				if (resultJson.getString("resCode").equals("0000")) {
					RedisCacheUtil.setRedis(smsRedisKey, smsJson, DateUtils.getSmsFailSecond(1));
					resCode = EnumSysManage.SUCCESS.getCode();
					LOGGER.info("---发送成功---保存验证码发送信息：" + smsJson);
				} else {
					resCode = EnumSysManage.RC_REG_CODE_SEND_ERROR.getCode();
					LOGGER.info("---发送失败---接口返回非0000：" + result);
				}
			}
			LOGGER.info("发送短信验证码：---end---");
		} else {
			LOGGER.info("发送短信验证码---发送次数---已超限：" + sendCount);
			resCode = EnumSysManage.RC_REG_CODE_COUNT_MORE.getCode();
		}
		
		if(resCode == null){
			return EnumSysManage.RC_FAILED.getCode();
		}
		return resCode;

	}

	@Override
	public String checkVerifyCode(String uname,String phone, String vcode, String smsRedisKey, Integer failMillisecond, Integer maxErrorCount) throws Exception {
		if (!UsedUtil.verifyParams(phone, vcode)) {
			return EnumSysManage.RC_PARAMETER_NULL.getCode();
		}
		
		if(smsRedisKey == null){
			smsRedisKey = SEND_SMS + uname+":"+phone;
		}
		
		if(failMillisecond == null){
			failMillisecond = 1000 * 60 * 1;//1分钟
		}
		
		if(maxErrorCount == null){
			maxErrorCount = CREDITRISK_SENDSMS_COUNT;
		}
		
		JSONObject smsJson =(JSONObject) RedisCacheUtil.getRedis(smsRedisKey);
		// 1.校验当天是否发送验证码
		if (smsJson != null) {
			// 已经发过验证码
			LOGGER.info("校验短信验证码：获取服务器验证码信息：" + smsJson);
			long sendTime = smsJson.getLongValue("sendTime");
			Integer errorCount = smsJson.getInteger("errorCount");

			long validTime = System.currentTimeMillis();
			long diffTime = validTime - sendTime;
			String resCode = null;

			// 2.校验验证码是否过期
			if (diffTime > failMillisecond) {
				LOGGER.info("校验短信验证码：验证码过期");
				resCode = EnumSysManage.RC_REG_CODE_TIMEOUT.getCode();
			}
			// 3.校验手机号是否匹配（理论来说可省略此步骤，方便以后拓展）
			if (!phone.equals(smsJson.getString("phone"))) {
				LOGGER.info("校验短信验证码：手机号不正确");
				resCode = EnumSysManage.RC_REG_CODE_ERROR.getCode();
			}
			// 4.校验验证码是否匹配
			if (!vcode.equals(smsJson.getString("vcode"))) {
				LOGGER.info("校验短信验证码：验证码不正确");
				resCode = EnumSysManage.RC_REG_CODE_ERROR.getCode();
			}
			// 5.如前面步骤未通过，更新错误次数
			if (null != resCode) {
				errorCount = errorCount + 1;
				smsJson.put("errorCount", errorCount);
				RedisCacheUtil.setRedis(smsRedisKey, smsJson);
				
				LOGGER.info("校验短信验证码：保存验证码校验信息：" + smsJson);
				// 6.校验错误次数是否超过限定最大值
				if (errorCount >= maxErrorCount) {
					LOGGER.info("校验短信验证码：错误"+maxErrorCount+"次及以上");
					return EnumSysManage.RC_REG_CODE_ERROR_MORE.getCode();
				} else {
					return resCode;
				}
			}else{
//				smsJson.put("vcode", "");
				RedisCacheUtil.setRedis(smsRedisKey, smsJson);
			}
			// 6.提示校验通过
			return EnumSysManage.SUCCESS.getCode();

		} else {
			// 未发送验证码
			return EnumSysManage.RC_REG_CODE_NULL.getCode();
		}

	}

	@Override
	public String seriesErrCountVerify(String uname, String type,Integer failMillisecond, Integer maxErrorCount, Boolean cleanFlag)
			throws Exception {

		String key=PW_SERIESERR+uname;
		String typeErrorCount=type+"_errorCount";
		
		if (!UsedUtil.verifyParams(uname)) {
			return EnumSysManage.RC_PARAMETER_NULL.getCode();
		}
		if(!cleanFlag){
			// 清空 当日连续密码错误统计
			Long flag=	RedisCacheUtil.del(key);
			if(flag>0){
				return EnumSysManage.SUCCESS.getCode();
			}
		}else{
			if(failMillisecond == null){
				failMillisecond = 1000 * 60 * 60 * 24;
			}
			if(maxErrorCount == null){
				maxErrorCount = CREDITRISK_PW_SERIESERR_COUNT;
			}
			JSONObject smsJson =(JSONObject) RedisCacheUtil.getRedis(key);
			// 1.校验当天是否连续输入密码错误
			if (smsJson != null) {
				LOGGER.info("校验连续输入密码错误：获取服务器验证码信息：" + smsJson);
				Integer errorCount =smsJson.getInteger(typeErrorCount);
				errorCount = errorCount + 1;
				smsJson.put(typeErrorCount, errorCount);
				RedisCacheUtil.setRedis(key, smsJson);
				// 6.校验错误次数是否超过限定最大值
				if (errorCount >= maxErrorCount) {
					LOGGER.info("校验短信验证码：错误"+maxErrorCount+"次及以上");
					return EnumSysManage.FAILE.getCode();
				} else {
					return EnumSysManage.SUCCESS.getCode();
				}
			} else {
				// 校验当天是否连续输入密码错误
				smsJson=new JSONObject();
				smsJson.put(typeErrorCount, 1);
				RedisCacheUtil.setRedis(key, smsJson);
				return EnumSysManage.SUCCESS.getCode();
			}
			
		}
		return EnumSysManage.FAILE.getCode();
	}
}
