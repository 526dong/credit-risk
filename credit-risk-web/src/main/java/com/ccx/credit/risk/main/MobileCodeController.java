package com.ccx.credit.risk.main;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccx.credit.risk.api.MobileCodeApi;
import com.ccx.credit.risk.api.UserApi;
import com.ccx.credit.risk.model.User;
import com.ccx.credit.risk.util.EnumSysManage;
import com.ccx.credit.risk.util.PropertiesUtil;
import com.ccx.credit.risk.util.UsedUtil;


@Controller
@RequestMapping(value = "/validateCode")
public class MobileCodeController {
	private static final Logger logger = LoggerFactory.getLogger(MobileCodeController.class);
	@Autowired  
	private MobileCodeApi mobileCodeService;  
	@Autowired
	private UserApi userApi;
	private final static String KEY_CCX_CREDITRISK_WEB_REG_SMS = "ABS:ccx_credit_risk:REG_SMS_";
	private static final Integer CREDITRISK_SENDSMS_COUNT=Integer.parseInt(PropertiesUtil.getProperty("credit_risk_sendSms_count"));
	/**
	 * 获取验证码
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
    @RequestMapping(value="/getCode", method=RequestMethod.POST)  
    public Map<String,Object>  getCode(HttpServletRequest request) throws Exception{
		String uname = request.getParameter("username").trim(); 
		User sysUserBean = userApi.getUserByName(uname);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(UsedUtil.isNotNull(sysUserBean)){
			String phone=sysUserBean.getPhone();
			if(UsedUtil.isNotNull(phone)){
				String vCode = UsedUtil.calRandomScore(999999, 100000);
				String message = "您的短信验证码为VCODE,请在1分钟内输入,客服电话010-57602240【中诚信征信】";
				String regSmsKey = KEY_CCX_CREDITRISK_WEB_REG_SMS +uname+":"+ phone;
				logger.info("用户:"+uname+"----本次生成的手机短信验证码为[" + vCode + "]");  
				String resultCode = mobileCodeService.sendVerifyCode(uname,phone, message, vCode, regSmsKey, CREDITRISK_SENDSMS_COUNT);
				resultMap.put("code", resultCode);
				resultMap.put("msg", EnumSysManage.getMsg(resultCode));
			}else{
				resultMap.put("code",  EnumSysManage.FAILE.getCode());
				resultMap.put("msg", "账号无绑定手机号，请联系中诚信征信");
			}
		}else{
			resultMap.put("code", EnumSysManage.FAILE.getCode());
			resultMap.put("msg", "账号不存在");
		}
    	//查询用户列表
		return resultMap;
    }
	
	/**
	 * 用户名/手机号是否存在
	 * @param request
	 * @return
	 */
    @ResponseBody
    @RequestMapping(value="/selectByNameOrMobile", method=RequestMethod.POST)  
    public Map<String, Object> selectByNameOrMobile(HttpServletRequest request){
    	String unameOrMobile=request.getParameter("unameOrMobile") == null ? "" : request.getParameter("unameOrMobile").trim();
    	Map<String, Object> map=new HashMap<String, Object>();
		if ("".equals(unameOrMobile)) {
			map.put("code", EnumSysManage.FAILE.getCode());
		}else{
			User sysUserBean=null;
			try {
				sysUserBean = userApi.getUserByName(unameOrMobile);
				if (null != sysUserBean && null !=sysUserBean.getStatus() && null !=sysUserBean.getIsDel()) {
					//0正常；1删除
					long isDel = sysUserBean.getIsDel();
					//1:冻结 2：锁定
					long status=sysUserBean.getStatus();
					if(isDel == 1){
						map.put("code", EnumSysManage.FAILE.getCode());
						map.put("msg", "该用户已注销!");
					}else{
						if(status==1){
							map.put("code", EnumSysManage.FAILE.getCode());
							map.put("msg", "该用户已停用!");
						}else if(status==2){
							map.put("code", EnumSysManage.FAILE.getCode());
							map.put("msg", "该用户已锁定!");
						}else{
							map.put("code", EnumSysManage.SUCCESS.getCode());
						}
					}
				}else{
					map.put("code", EnumSysManage.FAILE.getCode());
					map.put("msg", "该用户不存在!");
				}
			} catch (Exception e) {
				map.put("code", EnumSysManage.FAILE.getCode());
				map.put("msg", EnumSysManage.SYSFAILE.getName());
				logger.error("检测该用户唯一性异常!，{}",e.getMessage()); 
			}
		}
  		return map;
    }
}
