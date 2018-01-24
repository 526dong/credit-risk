package com.ccx.credit.risk.service.impl.asset;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ccx.credit.risk.api.asset.AbsAssetApi;
import com.ccx.credit.risk.mapper.asset.AbsAssetAssistInfoMapper;
import com.ccx.credit.risk.mapper.asset.AbsAssetMapper;
import com.ccx.credit.risk.mapper.asset.AbsAssetsRepaymentMapper;
import com.ccx.credit.risk.model.User;
import com.ccx.credit.risk.model.asset.*;
import com.ccx.credit.risk.util.DateUtils;
import com.ccx.credit.risk.util.JsonUtils;
import com.ccx.credit.risk.util.MyRuntimeException;
import com.ccx.credit.risk.util.StringUtils;
import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("absAssetApi")
/**
 * @Description 资产管理
 * @author Created by xzd on 2017/12/8.
 */
public class AbsAssetServiceImpl implements AbsAssetApi {
	private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(AbsAssetServiceImpl.class);
	
	@Autowired
    private AbsAssetMapper absAssetMapper;

	@Autowired
	private AbsAssetAssistInfoMapper assistInfoMapper;
	
	@Autowired
	private AbsAssetsRepaymentMapper absAssetsRepaymentMapper;
		
	//主键获取	
	@Override	
	public AbsAsset getById(HttpServletRequest request, Integer id) {
		//资产信息
		AbsAsset absAsset = absAssetMapper.selectByPrimaryKey(id);
		request.setAttribute("asset", absAsset);
		//处理资产辅助信息
		splitAssetAssistInfo(request, absAsset, assistInfoMapper.getByAssetId(id));

		return absAsset;
	}

	/**
	 * 处理资产辅助信息
	 * @param request
	 * @param absAsset 资产
	 * @param assistInfo 资产辅助信息
	 */
	public void splitAssetAssistInfo(HttpServletRequest request, AbsAsset absAsset, AbsAssetAssistInfo assistInfo) {
		//处理企业基本信息
		dealEntBaseInfo(request, absAsset.getType(), assistInfo);
		//处理增级企业
		dealEntEnhance(request, assistInfo);
		//处理增信措施
		dealEntEnhanceCredit(request, assistInfo);
		//处理现金流
		dealCashFlow(request, absAsset.getId());
	}

	/**
	 * 基本企业信息
	 * @param request
	 * @param assistInfo
	 */
	public void dealEntBaseInfo (HttpServletRequest request, Integer assetType, AbsAssetAssistInfo assistInfo) {
		//基本企业信息集合
		List<AssetEnterpriseBaseInfo> baseInfoList = new ArrayList<>();
		JSONArray jsonArray = new JSONArray();

		if (0 == assetType) {
			//承租企业
			jsonArray = JSONArray.parseArray(assistInfo.getEnterpriseTenant());
			baseInfoList = getEntBaseInfoList(jsonArray, false, null);
		} else if (1 == assetType) {
			//卖方、买方
			AssetEnterpriseBaseInfo baseInfo = dealEntSellerAndBuyer(assistInfo);
			baseInfoList.add(baseInfo);
		} else {
			//借款企业
			jsonArray = JSONArray.parseArray(assistInfo.getEnterpriseBorrow());
			baseInfoList = getEntBaseInfoList(jsonArray, false, null);
		}

		if (baseInfoList != null && baseInfoList.size() > 0) {
			request.setAttribute("baseInfoList", baseInfoList);
		}
	}

	/**
	 * 获取企业基本信息集合
	 * @param jsonArray
	 * @param isSellerOrBuyer 保理债权-买方和卖方
	 * @param businessType 业务类型
	 * @return
	 */
	public List<AssetEnterpriseBaseInfo> getEntBaseInfoList(JSONArray jsonArray, boolean isSellerOrBuyer, Integer businessType){
		//企业基本信息集合
		List<AssetEnterpriseBaseInfo> baseInfoList = new ArrayList<>();
		//deal list null
		if (jsonArray == null || jsonArray.size() == 0) {
			throw new MyRuntimeException("创建资产报错：卖方企业和买方企业为空");
		}
		//保理债权
		if (isSellerOrBuyer) {
			if (jsonArray.size() == 2) {
				AssetEnterpriseBaseInfo baseInfo = new AssetEnterpriseBaseInfo();
				//买方
				AssetEnterpriseBaseInfo buyerEnt = JsonUtils.parse(jsonArray.get(1).toString(), AssetEnterpriseBaseInfo.class);
				baseInfo.setEntId(buyerEnt.getBuyerEntId());
				baseInfo.setEntName(buyerEnt.getBuyerEntName());
				baseInfo.setEntRateResult(buyerEnt.getBuyerEntRateResult());
				baseInfo.setEntShadowRateResult(buyerEnt.getBuyerEntShadowRateResult());
				baseInfo.setEntProvince(buyerEnt.getBuyerEntProvince());
				baseInfo.setEntIndustry1(buyerEnt.getBuyerEntIndustry1());
				baseInfo.setEntIndustry2(buyerEnt.getBuyerEntIndustry2());
				baseInfoList.add(baseInfo);

				//有追索权时，比较买方、卖方和增级企业
				if (businessType != null) {
					if (businessType == 8 || businessType == 10) {
						AssetEnterpriseBaseInfo sellerEnt = JsonUtils.parse(jsonArray.get(0).toString(), AssetEnterpriseBaseInfo.class);
						baseInfoList.add(sellerEnt);
					}
				}
			}
		} else {
			//租赁债权、贷款债权
			for (int i = 0; i < jsonArray.size(); i++) {
				AssetEnterpriseBaseInfo baseInfo = JsonUtils.parse(jsonArray.get(i).toString(), AssetEnterpriseBaseInfo.class);
				baseInfoList.add(baseInfo);
			}
		}

		return baseInfoList;
	}

	/**
	 * 处理买方和卖方
	 * @param assistInfo
	 * @return
	 */
	public AssetEnterpriseBaseInfo dealEntSellerAndBuyer(AbsAssetAssistInfo assistInfo) {
		AssetEnterpriseBaseInfo baseInfo = new AssetEnterpriseBaseInfo();
		//卖方
		String enterpriseSeller = assistInfo.getEnterpriseSeller();
		if (StringUtils.isEmpty(enterpriseSeller)) {
			throw new MyRuntimeException("资产创建报错：资产卖方为空");
		}
		AssetEnterpriseBaseInfo seller = JsonUtils.parse(enterpriseSeller, AssetEnterpriseBaseInfo.class);
		setBaseEnt(baseInfo, seller, false);
		//买方
		String enterpriseBuyer = assistInfo.getEnterpriseBuyer();
		if (StringUtils.isEmpty(enterpriseBuyer)) {
			throw new MyRuntimeException("资产创建报错：资产买方为空");
		}
		AssetEnterpriseBaseInfo buyer = JsonUtils.parse(enterpriseBuyer, AssetEnterpriseBaseInfo.class);
		setBaseEnt(baseInfo, buyer, true);

		return baseInfo;
	}

	/**
	 * baseEnt赋值
	 * @param baseInfo
	 * @param sellerOrBuyer
	 * @param isBuyer
	 */
	public void setBaseEnt(AssetEnterpriseBaseInfo baseInfo, AssetEnterpriseBaseInfo sellerOrBuyer, boolean isBuyer){
		if (isBuyer) {
			//买方
			baseInfo.setBuyerEntId(sellerOrBuyer.getBuyerEntId());
			baseInfo.setBuyerEntName(sellerOrBuyer.getBuyerEntName());
			baseInfo.setBuyerEntRateResult(sellerOrBuyer.getBuyerEntRateResult());
			baseInfo.setBuyerEntShadowRateResult(sellerOrBuyer.getBuyerEntShadowRateResult());
			baseInfo.setBuyerEntProvince(sellerOrBuyer.getBuyerEntProvince());
			baseInfo.setBuyerEntIndustry1(sellerOrBuyer.getBuyerEntIndustry1());
			baseInfo.setBuyerEntIndustry2(sellerOrBuyer.getBuyerEntIndustry2());
		} else {
			//卖方
			baseInfo.setEntId(sellerOrBuyer.getEntId());
			baseInfo.setEntName(sellerOrBuyer.getEntName());
			baseInfo.setEntRateResult(sellerOrBuyer.getEntRateResult());
			baseInfo.setEntShadowRateResult(sellerOrBuyer.getEntShadowRateResult());
			baseInfo.setEntProvince(sellerOrBuyer.getEntProvince());
			baseInfo.setEntIndustry1(sellerOrBuyer.getEntIndustry1());
			baseInfo.setEntIndustry2(sellerOrBuyer.getEntIndustry2());
		}
	}

	/**
	 * 增级企业
	 * @param request
	 * @param assistInfo
	 */
	public void dealEntEnhance(HttpServletRequest request, AbsAssetAssistInfo assistInfo) {
		//增级企业
		String enterpriseEnhance = assistInfo.getEnterpriseEnhance();

		if (!StringUtils.isEmpty(enterpriseEnhance)) {
			//增级企业集合
			List<AssetEnterpriseEnhanceInfo> enhanceInfoList = getEnhanceInfoList(enterpriseEnhance);

			if (enhanceInfoList != null && enhanceInfoList.size() > 0) {
				request.setAttribute("enhanceInfoList", enhanceInfoList);
			}
		}

	}

	/**
	 * 增级企业集合
	 * @param enhanceInfo
	 * @return
	 */
	public List<AssetEnterpriseEnhanceInfo> getEnhanceInfoList(String enhanceInfo){
		//增级企业集合
		List<AssetEnterpriseEnhanceInfo> enhanceInfoList = new ArrayList<>();
		JSONArray jsonArray = JSONArray.parseArray(enhanceInfo);
		//deal null
		if (jsonArray == null || jsonArray.size() == 0) {
			return enhanceInfoList;
		}

		for (int i = 0; i < jsonArray.size(); i++) {
			AssetEnterpriseEnhanceInfo enterpriseEnhanceInfo =
					JsonUtils.parse(jsonArray.get(i).toString(), AssetEnterpriseEnhanceInfo.class);
			enhanceInfoList.add(enterpriseEnhanceInfo);
		}

		return enhanceInfoList;
	}

	/**
	 * 增信措施
	 * @param request
	 * @param assistInfo
	 */
	public void dealEntEnhanceCredit(HttpServletRequest request, AbsAssetAssistInfo assistInfo) {
		//增信措施集合
		List<AssetEnhanceCredit> enhanceCreditList = new ArrayList<>();
		JSONArray jsonArray = JSONArray.parseArray(assistInfo.getOtherMeasureCreditEnhance());

		if (jsonArray != null && jsonArray.size() > 0) {
			for (int i = 0; i < jsonArray.size(); i++) {
				AssetEnhanceCredit enhanceCredit =
						JsonUtils.parse(jsonArray.get(i).toString(), AssetEnhanceCredit.class);
				enhanceCreditList.add(enhanceCredit);
			}
		}

		if (enhanceCreditList != null && enhanceCreditList.size() > 0) {
			request.setAttribute("enhanceCreditList", enhanceCreditList);
		}
	}

	/**
	 * 处理现金流
	 * @param request
	 * @param assetId 资产id
	 */
	public void dealCashFlow(HttpServletRequest request, Integer assetId){
		//cash flow list
		List<AbsAssetsRepayment> myCashFlowList = absAssetsRepaymentMapper.findCashFlowByAssetId(assetId);
		//deal cash flow list
		List<AbsAssetsRepayment> cashFlowList = new ArrayList<>();

		if (myCashFlowList != null && myCashFlowList.size() > 0) {
			for (AbsAssetsRepayment repayment:myCashFlowList) {
				//支付日
				String myRepaymentDate = repayment.getRepaymentDate();
				if (!StringUtils.isEmpty(myRepaymentDate) && myRepaymentDate.length() >= 10) {
					String repaymentDate = myRepaymentDate.replace("\'","");
					repayment.setRepaymentDate(repaymentDate.substring(0, 10));
				}
				cashFlowList.add(repayment);
			}

			request.setAttribute("cashFlowList", cashFlowList);
			request.setAttribute("cashFlow", JsonUtils.toJson(cashFlowList));
		}
	}

	@Override
	public int findByApplyCode(String applyCode) {
		return absAssetMapper.findByApplyCode(applyCode);
	}

	@Override
	public int validateName(String name) {
		return absAssetMapper.validateName(name);
	}

	@Override
	public int validateCode(String code) {
		return absAssetMapper.validateCode(code);
	}

	//获取无参list
	@Override	
	public List<AbsAsset> getList() {
		return null;	
	}	
		
	//获取有参数list	
	@Override	
	public List<AbsAsset> getList(AbsAsset model) {	
		return null;	
	}

	//获取带分页list
	@Override
	public PageInfo<AbsAsset> getPageList(Map<String,Object> params) {
		List<AbsAsset> list = absAssetMapper.findAll(params);
		PageInfo<AbsAsset> pages = new PageInfo<AbsAsset>(list);
		return pages;
	}

	@Override
	public List<Map<String, Object>> findAllRateEnt() {
		return absAssetMapper.findAllRateEnt();
	}

	@Override
	public List<Map<String, Object>> findAllBusinessType(Integer assetType) {
		return absAssetMapper.findAllBusinessType(assetType);
	}

	/**
	 * 保存资产和资产辅助信息
	 * @param asset 资产
	 * @param baseEnt 基本企业信息-承租企业/卖方/买方/借款企业
	 * @param enhanceEnt 增级企业
	 * @param enhanceCredit 增信措施
	 */
	@Override	
	public void addOrUpdate(String asset, String baseEnt, String enhanceEnt, String enhanceCredit, String cashFlow) {
		//asset
		AbsAsset absAsset = new AbsAsset();
		JSONArray jsonArray = JSONArray.parseArray(asset);

		if (jsonArray.size() == 1) {
			//json format to entity
			absAsset = JsonUtils.parse(jsonArray.get(0).toString(), AbsAsset.class);
		}

		if (absAsset.getType() == 2) {
			//业务类型
			absAsset.setBusinessName(absAsset.getBusinessTypeName());
		}
		//投放日期
		absAsset.setPutDate(DateUtils.strToDate(absAsset.getPutDateStr()));

		if (StringUtils.isEmpty(absAsset.getId()) || "0".equals(absAsset.getId())) {
			//获取资产申请编号
			absAsset.setApplyCode(getApplyCode());
			//创建日期
			absAsset.setOperateDate(new Date());
			//获取创建人
			HttpServletRequest request =
					((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			User user = (User) request.getSession().getAttribute("risk_crm_user");
			if (user != null) {
				absAsset.setOperator(user.getLoginName());
			}
			//新增、返回id
			absAssetMapper.insert(absAsset);
			//处理资产辅助信息
			dealAssistInfo(absAsset, baseEnt, enhanceEnt, enhanceCredit, cashFlow, true);
		} else {
			//更新日期
			absAsset.setUpdateDate(new Date());
			//更新
			absAssetMapper.updateByPrimaryKey(absAsset);
			//处理资产辅助信息
			dealAssistInfo(absAsset, baseEnt, enhanceEnt, enhanceCredit, cashFlow, false);
		}
	}

	/**
	 * 处理资产辅助信息
	 * @param asset 资产
	 * @param baseEnt 企业基本信息-存储承租企业/买方、卖方/借款企业
	 * @param enhanceEnt 增级企业
	 * @param enhanceCredit 增信措施
	 * @param addOrUpdate 新增还是更新
	 */
	public void dealAssistInfo(AbsAsset asset, String baseEnt, String enhanceEnt, String enhanceCredit, String cashFlow, boolean addOrUpdate) {
		//资产辅助信息
		AbsAssetAssistInfo assistInfo = new AbsAssetAssistInfo();
		//资产id
		Integer assetId = asset.getId();
		//资产类型
		Integer assetType = asset.getType();

		if (!addOrUpdate) {
			assistInfo = assistInfoMapper.getByAssetId(assetId);
		}

		//获取资产辅助信息和资产信息
		getAssistInfo(asset, baseEnt, enhanceEnt, enhanceCredit, assistInfo);
		assistInfo.setAssetId(assetId);

		if (!addOrUpdate) {
			//删除已有资产辅助信息
			assistInfoMapper.deleteByAssetId(assetId);
			//删除已有的现金流
			absAssetsRepaymentMapper.deleteByAssetId(assetId);
		}
		assistInfo.setId(null);
		//保存资产辅助信息
		assistInfoMapper.insert(assistInfo);
		//批量存储现金流
		absAssetsRepaymentMapper.insertBatch(getCashFlowList(assetId, cashFlow));

		//条件筛选得到主体企业和影子企业
		AssetEnterpriseBaseInfo mainAndShadowEnt = getMainAndShadowEnt(assetType, asset.getBusinessType(), baseEnt, enhanceEnt);
		//更新资产表信息
		myUpdateAsset(asset, mainAndShadowEnt);
	}

	/**
	 * 获取资产辅助类信息和资产信息
	 * @param asset
	 * @param baseEnt
	 * @param enhanceEnt
	 * @param enhanceCredit
	 * @param assistInfo
	 */
	public void getAssistInfo(AbsAsset asset, String baseEnt, String enhanceEnt, String enhanceCredit, AbsAssetAssistInfo assistInfo){
		//资产类型
		Integer type = asset.getType();

		if (type == 0) {
			//用于更新到资产表
			asset.setTenantEnt(getBaseEntName(baseEnt));
			//承租企业
			assistInfo.setEnterpriseTenant(baseEnt);
		} else if (type == 1) {
			JSONArray jsonArray = JSONArray.parseArray(baseEnt);
			//null 判断
			if (jsonArray == null || jsonArray.size() == 0) {
				throw new MyRuntimeException("创建资产报错：卖方企业和买方企业为空");
			}
			AssetEnterpriseBaseInfo sellerEnt = new AssetEnterpriseBaseInfo();
			AssetEnterpriseBaseInfo buyerEnt = new AssetEnterpriseBaseInfo();
			if (jsonArray.size() == 2) {
				sellerEnt = JsonUtils.parse(jsonArray.get(0).toString(), AssetEnterpriseBaseInfo.class);
				buyerEnt = JsonUtils.parse(jsonArray.get(1).toString(), AssetEnterpriseBaseInfo.class);
			}
			//用于更新到资产表
			asset.setSellerEnt(getSellerEntName(sellerEnt));
			asset.setBuyerEnt(getBuyerEntName(buyerEnt));
			//卖方
			assistInfo.setEnterpriseSeller(JsonUtils.toJson(getSellerEnt(sellerEnt)));
			//买方
			assistInfo.setEnterpriseBuyer(JsonUtils.toJson(getBuyerEnt(buyerEnt)));
		} else {
			//用于更新到资产表
			asset.setBorrowEnt(getBaseEntName(baseEnt));
			//借款企业
			assistInfo.setEnterpriseBorrow(baseEnt);
		}

		assistInfo.setEnterpriseEnhance(enhanceEnt);
		assistInfo.setOtherMeasureCreditEnhance(enhanceCredit);
	}

	/**
	 * 获取现金流集合
	 * @param assetId 资产id
	 * @param cashFlow
	 * @return
	 */
	public List<AbsAssetsRepayment> getCashFlowList(Integer assetId, String cashFlow){
		//return cash flow list
		List<AbsAssetsRepayment> repaymentList = new ArrayList<>();
		//cash flow json
		JSONArray jsonArray = JSONArray.parseArray(cashFlow);
		//cash flow
		AbsAssetsRepayment repayment = new AbsAssetsRepayment();
		//null deal
		if (jsonArray == null || jsonArray.size() == 0) {
			throw new MyRuntimeException("资产创建失败：现金流为空！");
		}

		for (int i = 0; i < jsonArray.size(); i++) {
			repayment = JsonUtils.parse(jsonArray.get(i).toString(), AbsAssetsRepayment.class);
			//asset id
			repayment.setAssetsId(assetId);
			repaymentList.add(repayment);
		}

		return repaymentList;
	}

	/**
	 * 非事务方法-为了实现在同一事务中既可以执行添加，也可以执行更新
	 * 更新主体企业评级和影子评级
	 */
	public void myUpdateAsset(AbsAsset asset, AssetEnterpriseBaseInfo baseInfo){
		//主体企业赋值
		asset.setEntId(Integer.parseInt(baseInfo.getEntId()));
		asset.setEntName(baseInfo.getEntName());
		asset.setLevel(baseInfo.getEntRateResult());
		//影子企业赋值
		asset.setEntShadowId(Integer.parseInt(baseInfo.getBuyerEntId()));
		asset.setEntShadowName(baseInfo.getBuyerEntName());
		asset.setShadowLevel(baseInfo.getEntShadowRateResult());
		//省份、行业/一级、二级
		asset.setProvince(baseInfo.getEntProvince());
		asset.setIndustry1(baseInfo.getEntIndustry1());
		asset.setIndustry2(baseInfo.getEntIndustry2());

		asset.setOperateDate(new Date());

		absAssetMapper.updateByPrimaryKey(asset);
	}

	/**
	 * 基本企业信息
	 * @param baseEnt
	 * @return
	 */
	public String getBaseEntName(String baseEnt) {
		JSONArray jsonArray = JSONArray.parseArray(baseEnt);
		//承租企业/借款企业-可能是逗号隔开的集合
		String entName = "";
		//list deal null
		if (jsonArray == null || jsonArray.size() == 0) {
			return entName;
		}

		for (int i = 0; i < jsonArray.size(); i++) {
			AssetEnterpriseBaseInfo baseInfo =
					JsonUtils.parse(jsonArray.get(i).toString(), AssetEnterpriseBaseInfo.class);
			if (baseInfo != null){
				if(!StringUtils.isEmpty(baseInfo.getEntName())) {
					entName += baseInfo.getEntName() + ",";
				}
			}
		}

		return entName == "" ? entName : entName.substring(0, entName.length()-1);
	}

	/**
	 * 获取seller 卖方企业名称
	 * @param baseInfo
	 * @return
	 */
	public String getSellerEntName(AssetEnterpriseBaseInfo baseInfo){
		return baseInfo.getEntName();
	}

	/**
	 * 获取buyer 买方企业名称
	 * @param baseInfo
	 * @return
	 */
	public String getBuyerEntName(AssetEnterpriseBaseInfo baseInfo){
		return baseInfo.getBuyerEntName();
	}

	/**
	 * 获取seller 卖方
	 * @param seller
	 * @return
	 */
	public AssetEnterpriseBaseInfo getSellerEnt(AssetEnterpriseBaseInfo seller){
		AssetEnterpriseBaseInfo baseInfo = new AssetEnterpriseBaseInfo();
		setBaseEnt(baseInfo, seller, false);
		return baseInfo;
	}

	/**
	 * 获取buyer 买方
	 * @param buyer
	 * @return
	 */
	public AssetEnterpriseBaseInfo getBuyerEnt(AssetEnterpriseBaseInfo buyer){
		AssetEnterpriseBaseInfo baseInfo = new AssetEnterpriseBaseInfo();
		setBaseEnt(baseInfo, buyer, true);
		return baseInfo;
	}

	/**
	 * 获取主体企业和影子企业
	 * @param assetType 资产类型
	 * @param businessType 业务类型
	 * @param baseEnt 基本企业信息的评级结果
	 * @param enhanceEnt 增级企业信息的评级结果
	 * @return
	 */
	public AssetEnterpriseBaseInfo getMainAndShadowEnt(Integer assetType, Integer businessType, String baseEnt, String enhanceEnt){
		//rate result priority
		Map<String, Integer> priorityMap = getRateResultMap();
		//主体-排序后的基本企业和增级企业集合
		List<AssetEnterpriseBaseInfo> afterCompareEntList = getAfterCompareEntList(assetType, businessType, baseEnt, enhanceEnt, priorityMap, true);
		//主体企业
		AssetEnterpriseBaseInfo mainEnt = getMainOrShadowBaseEntInfo(afterCompareEntList, true, true, priorityMap);
		//影子-排序后的基本企业和增级企业集合
		List<AssetEnterpriseBaseInfo> afterCompareShadowEntList = getAfterCompareEntList(assetType, businessType, baseEnt, enhanceEnt, priorityMap, false);
		//影子企业
		AssetEnterpriseBaseInfo shadowEnt = getMainOrShadowBaseEntInfo(afterCompareShadowEntList, false, true, priorityMap);
		mainEnt.setBuyerEntId(shadowEnt.getEntId());
		mainEnt.setBuyerEntName(shadowEnt.getEntName());
		mainEnt.setEntShadowRateResult(shadowEnt.getEntShadowRateResult());

		//资产省份、行业/一级、二级
		AssetEnterpriseBaseInfo entProvinceAndIndustry = getEntProvinceAndIndustry(assetType, baseEnt, priorityMap);
		mainEnt.setEntProvince(entProvinceAndIndustry.getEntProvince());
		mainEnt.setEntIndustry1(entProvinceAndIndustry.getEntIndustry1());
		mainEnt.setEntIndustry2(entProvinceAndIndustry.getEntIndustry2());

		return mainEnt;
	}

	/**
	 * 获取两个排序后的企业的集合
	 * @param baseEnt
	 * @param enhanceEnt
	 * @param priorityMap
	 * @return
	 */
	public List<AssetEnterpriseBaseInfo> getAfterCompareEntList(Integer assetType, Integer businessType,
					String baseEnt, String enhanceEnt, Map<String, Integer> priorityMap, boolean isMain){
		//承租企业/借款企业和增级企业排序后的期望企业集合
		List<AssetEnterpriseBaseInfo> baseInfoList = new ArrayList<>();
		//企业基本信息
		AssetEnterpriseBaseInfo mainBaseEntInfo = new AssetEnterpriseBaseInfo();
		//2、保理债权-买方、卖方
		if (assetType == 1) {
			if (businessType == 8 || businessType == 10) {
				//有追索
				mainBaseEntInfo = dealMainOrShadowBaseEntInfo(getEntBaseInfoList(JSONArray.parseArray(baseEnt), true, businessType), priorityMap, isMain);
			} else {
				//无追索时只有一个买方企业
				List<AssetEnterpriseBaseInfo> entBaseInfoList = getEntBaseInfoList(JSONArray.parseArray(baseEnt), true, businessType);

				if (entBaseInfoList != null && entBaseInfoList.size() > 0) {
					mainBaseEntInfo = entBaseInfoList.get(0);
				}
			}
		} else {
			//1、租赁债权、贷款债权
			mainBaseEntInfo = dealMainOrShadowBaseEntInfo(getEntBaseInfoList(JSONArray.parseArray(baseEnt), false, null), priorityMap, isMain);
		}

		//基本企业
		baseInfoList.add(mainBaseEntInfo);

		if (!StringUtils.isEmpty(enhanceEnt)) {
			//增级企业
			AssetEnterpriseBaseInfo mainEnhanceEntInfo = dealMainOrShadowBaseEntInfo(switchEnhanceToBase(enhanceEnt), priorityMap, isMain);
			baseInfoList.add(mainEnhanceEntInfo);
		}

		return baseInfoList;
	}

	/**
	 * 将增级企业转化成基本企业
	 * @param enhanceEnt
	 * @return
	 */
	public List<AssetEnterpriseBaseInfo> switchEnhanceToBase(String enhanceEnt){
		//enhance ent list
		List<AssetEnterpriseEnhanceInfo>  enhanceInfoList = getEnhanceInfoList(enhanceEnt);
		//base ent
		List<AssetEnterpriseBaseInfo> baseInfoList = new ArrayList<>();

		for (AssetEnterpriseEnhanceInfo enhanceInfo:enhanceInfoList) {
			AssetEnterpriseBaseInfo baseInfo = new AssetEnterpriseBaseInfo();
			baseInfo.setEntId(enhanceInfo.getEnhanceEntId());
			baseInfo.setEntName(enhanceInfo.getEnhanceEntName());
			baseInfo.setEntDebtProportion(enhanceInfo.getEnhanceDebtProportion());
			baseInfo.setEntRateResult(enhanceInfo.getEnhanceRateResult());
			baseInfo.setEntShadowRateResult(enhanceInfo.getEnhanceShadowRateResult());
			baseInfoList.add(baseInfo);
		}

		return baseInfoList;
	}

	/**
	 * 获取企业评级结果的优先级map
	 * @return
	 */
	public Map<String, Integer> getRateResultMap() {
		//rate result priority
		Map<String, Integer> priorityMap = new HashMap<>();
		//all rate result
		List<Map<String, Object>> allRateResult = absAssetMapper.findAllRateResult();
		//list deal null
		if (allRateResult == null || allRateResult.size() == 0) {
			return priorityMap;
		}

		Iterator<Map<String, Object>> it = allRateResult.iterator();

		while(it.hasNext()) {
			Map<String, Object> next = it.next();
			priorityMap.put((String) next.get("name"), Integer.parseInt(next.get("priority").toString()));
		}

		return priorityMap;
	}

	/**
	 * 从基本企业信息中找到主体企业
	 * @param entBaseInfoList
	 * @param priorityMap
	 * @return
	 */
	public AssetEnterpriseBaseInfo dealMainOrShadowBaseEntInfo(List<AssetEnterpriseBaseInfo> entBaseInfoList, Map<String, Integer> priorityMap, boolean isMain){
		//my want entity
		AssetEnterpriseBaseInfo myWantEntity = new AssetEnterpriseBaseInfo();
		//list size
		Integer baseEntListSize = entBaseInfoList.size();
		//list deal null
		if (entBaseInfoList == null || baseEntListSize == 0) {
			return myWantEntity;
		}
		//ent list size = 1
		if (baseEntListSize == 1) {
			return entBaseInfoList.get(0);
		}
		//store all is 100 / remove is not 100
		List<AssetEnterpriseBaseInfo> isNotFullList = new ArrayList<>();
		//judge all is 100 or < 100
		Integer judgeCount = 0;
		for (AssetEnterpriseBaseInfo myBaseEnt:entBaseInfoList) {
			//担保比例
			String debtProportion = myBaseEnt.getEntDebtProportion();
			if ("100".equals(debtProportion)) {
				isNotFullList.add(myBaseEnt);
				judgeCount += 1;
			}
		}
		//条件选择主体企业
		if (judgeCount.equals(baseEntListSize)) {
			//1、企业担保比例均为100，选择评级级别最高的企业作为主体企业
			if (isMain) {
				myWantEntity = getMainOrShadowBaseEntInfo(entBaseInfoList, true, true, priorityMap);
			} else {
				myWantEntity = getMainOrShadowBaseEntInfo(entBaseInfoList, false, true, priorityMap);
			}
		} else if (judgeCount > 0 && judgeCount < baseEntListSize) {
			//2、企业担保比例有100也有<100，选择评级级别最高的企业作为主体企业
			if (isMain) {
				myWantEntity = getMainOrShadowBaseEntInfo(isNotFullList, true, true, priorityMap);
			} else {
				myWantEntity = getMainOrShadowBaseEntInfo(isNotFullList, false, true, priorityMap);
			}
		} else {
			//3、企业担保比例均<100，选择评级级别最低的企业作为主体企业
			if (isMain) {
				myWantEntity = getMainOrShadowBaseEntInfo(entBaseInfoList, true, false, priorityMap);
			} else {
				myWantEntity = getMainOrShadowBaseEntInfo(entBaseInfoList, false, false, priorityMap);
			}
		}

		return myWantEntity;
	}

	/**
	 * 获取基本企业信息中的主体企业
	 * @param entBaseInfoList
	 * @param isMain main or shadow
	 * @param isHighOrLow 递增、递减排序
	 * @param priorityMap 评级结果优先级排序
	 * @return
	 */
	public AssetEnterpriseBaseInfo getMainOrShadowBaseEntInfo(List<AssetEnterpriseBaseInfo> entBaseInfoList, final boolean isMain,
				final boolean isHighOrLow, final Map<String, Integer> priorityMap){
		//return ent
		AssetEnterpriseBaseInfo returnEnt = new AssetEnterpriseBaseInfo();
		//list deal null
		if (entBaseInfoList == null || entBaseInfoList.size() == 0) {
			return returnEnt;
		}
		//通过评级结果对企业进行排序，如果评级结果相同，那么就用姓名进行排序。
		Collections.sort(entBaseInfoList, new Comparator<AssetEnterpriseBaseInfo>(){
			@Override
			public int compare(AssetEnterpriseBaseInfo o1, AssetEnterpriseBaseInfo o2) {
				//评级结果
				String rateResult1 = "";
				String rateResult2 = "";

				if (isMain) {
					rateResult1 = o1.getEntRateResult();
					rateResult2 = o2.getEntRateResult();
				} else {
					rateResult1 = o1.getEntShadowRateResult();
					rateResult2 = o2.getEntShadowRateResult();
				}
				if (rateResult1.equals(rateResult2)) {
					//企业名称
					String entName1 = o1.getEntName();
					String entName2 = o2.getEntName();
					String[] nameArray = new String[2];
					nameArray[0] = entName1;
					nameArray[1] = entName2;

					return entName1.equals(sortEntName(nameArray)) ? -1 : 1 ;
				} else {
					return isHighOrLow ? (priorityMap.get(rateResult1) - priorityMap.get(rateResult2)) : (priorityMap.get(rateResult2) - priorityMap.get(rateResult1));
				}
			}
		});
		//get list first element
		returnEnt = entBaseInfoList.get(0);

		return returnEnt;
	}

	/**
	 * //获取省份和行业
	 * @param assetType
	 * @param baseEnt
	 * @param priorityMap
	 * @return
	 */
	public AssetEnterpriseBaseInfo getEntProvinceAndIndustry(Integer assetType, String baseEnt, Map<String, Integer> priorityMap){
		//获取省份和行业
		AssetEnterpriseBaseInfo baseInfo = new AssetEnterpriseBaseInfo();

		if (1 == assetType) {
			//资产类型为保理类资产，买方的省份和行业
			List<AssetEnterpriseBaseInfo> entBaseInfoList = getEntBaseInfoList(JSONArray.parseArray(baseEnt), true, null);

			if (entBaseInfoList != null && entBaseInfoList.size() > 0) {
				AssetEnterpriseBaseInfo buyerEnt = entBaseInfoList.get(0);
				baseInfo.setEntProvince(buyerEnt.getEntProvince());
				baseInfo.setEntIndustry1(buyerEnt.getEntIndustry1());
				baseInfo.setEntIndustry2(buyerEnt.getEntIndustry2());
			}
		} else {
			//资产类型为租赁类资产、贷款类资产，需要计算。
			/**
			 * 计算逻辑
			 * 以借款人的行业为资产行业，借款人所在行业/区域。
			 * 如果多个承租人、借款人，则以承担债务金额较大者为准；如金额相同则以级别较高者为准，如果级别相同则名称靠前为准。
			 */
			baseInfo = dealProvinceAndIndustryBaseEntInfo(getEntBaseInfoList(JSONArray.parseArray(baseEnt), false, null), priorityMap);
		}

		return baseInfo;
	}

	/**
	 * 从基本企业信息中找到主体企业的省份和行业
	 * @param entBaseInfoList
	 * @param priorityMap
	 * @return
	 */
	public AssetEnterpriseBaseInfo dealProvinceAndIndustryBaseEntInfo(List<AssetEnterpriseBaseInfo> entBaseInfoList, Map<String, Integer> priorityMap){
		//my want entity
		AssetEnterpriseBaseInfo myEntity = new AssetEnterpriseBaseInfo();
		//list size
		Integer baseEntListSize = entBaseInfoList.size();
		//list deal null
		if (entBaseInfoList == null || baseEntListSize == 0) {
			return myEntity;
		}
		//ent list size = 1
		if (baseEntListSize == 1) {
			return entBaseInfoList.get(0);
		}

		return getEntProvinceAndIndustryEnt(entBaseInfoList, priorityMap);
	}

	/**
	 * 获取基本企业信息中的主体企业
	 * @param entBaseInfoList
	 * @param priorityMap 评级结果优先级排序
	 * @return
	 */
	public AssetEnterpriseBaseInfo getEntProvinceAndIndustryEnt(List<AssetEnterpriseBaseInfo> entBaseInfoList, final Map<String, Integer> priorityMap){
		//return ent
		AssetEnterpriseBaseInfo returnEnt = new AssetEnterpriseBaseInfo();
		//list deal null
		if (entBaseInfoList == null || entBaseInfoList.size() == 0) {
			return returnEnt;
		}
		//如果多个承租人、借款人，则以承担债务金额较大者为准；如金额相同则以级别较高者为准，如果级别相同则名称靠前为准
		Collections.sort(entBaseInfoList, new Comparator<AssetEnterpriseBaseInfo>(){
			@Override
			public int compare(AssetEnterpriseBaseInfo o1, AssetEnterpriseBaseInfo o2) {
				String prop1 = o1.getEntDebtProportion();
				String prop2 = o2.getEntDebtProportion();
				if (prop1.equals(prop2)) {
					//评级结果
					String rateResult1 = o1.getEntRateResult();
					String rateResult2 = o2.getEntRateResult();
					if (rateResult1.equals(rateResult2)) {
						//企业名称
						String entName1 = o1.getEntName();
						String entName2 = o2.getEntName();
						String[] nameArray = new String[2];
						nameArray[0] = entName1;
						nameArray[1] = entName2;
						return entName1.equals(sortEntName(nameArray)) ? -1 : 1 ;
					} else {
						return  (priorityMap.get(rateResult1) - priorityMap.get(rateResult2));
					}
				} else {
					return Integer.parseInt(prop2) - Integer.parseInt(prop1);
				}

			}
		});
		//get list first element
		return entBaseInfoList.get(0);
	}

	/**
	 * 企业名称排序
	 * @param nameArray 企业名称集合
	 * @return 返回名称靠前的名字
	 */
	public String sortEntName(String[] nameArray){
		//设置比较器格式
		Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
		Arrays.sort(nameArray, cmp);
		return nameArray[0];
	}

	/**
	 * 获取资产申请编号-递归判断重复
	 * @return
	 */
	public String getApplyCode () {
		//资产申请编号：生成一个10位随机数
		String applyCode = ((int)((Math.random()*9+1)*1000000000))+"";
		int applyCodeCount = 0;

		try {
			//通过资产申请编号进行查询
			applyCodeCount = absAssetMapper.findByApplyCode(applyCode.trim());
			//有重复记录
			if (applyCodeCount > 0) {
				applyCode = getApplyCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return  applyCode;
	};

	//更新对象
	@Override	
	public int update(AbsAsset model) {	
		return absAssetMapper.updateByPrimaryKey(model);	
	}	
		
	//删除对象	
	@Override	
	public int deleteById(Integer id) {	
		return absAssetMapper.deleteByPrimaryKey(id);	
	}

	//根据资产类型统计行业地区等分布
	@Override
	public Map<String, Object> analyseDimCount(AssetCount assetCount) {
		Map<String, Object> map = new HashMap<>();
		Map<String, String> map1 = new HashMap<>();// 笔数map
		Map<String, String> map2 = new HashMap<>();// 金额map
		Map<String, String> map3 = new HashMap<>();// 剩余本金map
		List<Map<String, String>> strokeList = new ArrayList<>();// 笔数list
		List<Map<String, String>> putList = new ArrayList<>();// 投放金额list
		List<Map<String, String>> overPlusList = new ArrayList<>();// 剩余本金list
		List<AssetCount> list = new ArrayList<>();
		try {
			list = absAssetMapper.analyseDimCount(assetCount);//通过统计维度统计分布情况
			if (list.size() ==0) {
				logger.debug("未查询到通过资产类型统计行业等的分布结果！");
				map.put("status", 0);
				return map;
			} else {
				logger.debug("根据资产类型统计行业地区等分布结果："+JSON.toJSONString(list));
				for (int i = 0; i < list.size(); i++) {
					map1 = new HashMap<>();
					map1.put("name", list.get(i).getBothName());
					map1.put("value", list.get(i).getCount());
					strokeList.add(map1);
					map2 = new HashMap<>();
					map2.put("name", list.get(i).getBothName());
					map2.put("value", list.get(i).getPutMoney());
					putList.add(map2);
					map3 = new HashMap<>();
					map3.put("name", list.get(i).getBothName());
					map3.put("value", list.get(i).getAlmoney());
					overPlusList.add(map3);
				}
				map.put("status", 1);
				map.put("stroke", strokeList);
				map.put("put", putList);
				map.put("over", overPlusList);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Map<String, Object> assetMessCount(AssetCount assetCount) {
		Map<String, Object> map = new HashMap<>();
		double bondPrincipal = 0.00;
		double corpusTotal = 0.00;//资产池本金总额
		double overPlusTotalMoney = 0.00;//资产池剩余本金余额
		double singleCorpusMoney = 0.00;//单笔资产平均本金余额
		double heightCorpusMoney = 0.00;//单笔资产最高本金余额
		double lowCorpusMoney = 0.00;//单笔资产最低本金余额
		double weightingMeanTerm = 0.00;//加权平均期限
		double surplusMeanTerm = 0.00;//加权平均剩余期限
		int assetStrokeCount = 0;//资产笔数
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		//前一天
		Date date2=new Date();  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date2);  
        calendar.add(Calendar.DAY_OF_MONTH, -1);  
        date2 = calendar.getTime(); 
        String now = sdf.format(date2);
		try {
			List<AssetCount> list1 = absAssetMapper.assetMoneyCount(assetCount);
			assetStrokeCount = list1.size();
			if (assetStrokeCount != 0) {
				double[] arr = new double[assetStrokeCount];
				double[] overArr = new double[list1.size()];
				Date[] arrDate = new Date[assetStrokeCount];
				int[] allDays = new int[assetStrokeCount];//相差天数
				for (int i = 0; i < assetStrokeCount; i++) {
					bondPrincipal = list1.get(i).getBondPrincipal().doubleValue();
					arr[i] = bondPrincipal;
					corpusTotal = corpusTotal + bondPrincipal;//本金总额
					arrDate[i] = list1.get(i).getPutDate();//将投放日期放入集合中
					
				}
				singleCorpusMoney = corpusTotal/assetStrokeCount;//单笔资产平均本金余额
				String[] maxDayArr = new String[list1.size()];
				
				if (list1.size() != 0) {
					
					for (int i = 0; i < list1.size(); i++) {
						overPlusTotalMoney = overPlusTotalMoney + Double.valueOf(list1.get(i).getMoney());
						overArr[i]=Double.valueOf(list1.get(i).getMoney());
						
						String maxDay = list1.get(i).getMaxDay();//还款最大日期
						maxDayArr[i] = maxDay;
						Date maxDay1 = sdf.parse(maxDay);
						int days = (int) ((maxDay1.getTime() - arrDate[i].getTime()) / (24*3600*1000));
						allDays[i] = days;//相差天数
					}
					
				}
				/**
				 * 冒泡排序
				 */
				double temp;//定义一个临时变量
				double weightingMeanTerm1 = 0.00;//加权平均期限
		        for(int i=0;i<arr.length-1;i++){//冒泡     allDays和arr长度相同
		            for(int j=0;j<arr.length-i-1;j++){
		                if(arr[j+1]<arr[j]){
		                    temp = arr[j];
		                    arr[j] = arr[j+1]; 
		                    arr[j+1] = temp;
		                }
		            }
		        }
		        
		        double surplusMeanTerm1 = 0.00;
		        for (int i = 0; i < arr.length; i++) {
		        	 weightingMeanTerm1 = weightingMeanTerm1 + arr[i]/corpusTotal*allDays[i];//加权平均期限
		        	 Date date = new Date();
		        	 Date date1 = sdf.parse(maxDayArr[i]);
		        	 int days = (int) ((date1.getTime() - date.getTime()) / (24*3600*1000));//相差天数
		        	 surplusMeanTerm1 = surplusMeanTerm1 + overArr[i]/overPlusTotalMoney*days;
				}
		        
		        heightCorpusMoney = arr[arr.length-1];//单笔资产最高本金余额
		        lowCorpusMoney = arr[0];//单笔资产最低本金余额
		        
		        weightingMeanTerm = weightingMeanTerm1/365;//加权平均期限
		        surplusMeanTerm = surplusMeanTerm1/365;//加权平均剩余期限
			}else {
				logger.debug("通过统计维度统计资产信息没有查询到结果。");
				map.put("status", 0);
				map.put("now", now);
				return map;
			}
			
			map.put("corpusTotal", StringUtils.rounding(corpusTotal));//资产池本金总额
			map.put("overPlusTotalMoney", StringUtils.rounding(overPlusTotalMoney));//资产池剩余本金余额
			map.put("assetStrokeCount", assetStrokeCount);//资产笔数
			map.put("singleCorpusMoney", StringUtils.rounding(singleCorpusMoney));//单笔资产平均本金余额
			map.put("heightCorpusMoney", StringUtils.rounding(heightCorpusMoney));//单笔资产最高本金余额
			map.put("lowCorpusMoney", StringUtils.rounding(lowCorpusMoney));//单笔资产最低本金余额
			map.put("weightingMeanTerm", StringUtils.rounding(weightingMeanTerm));//加权平均期限
			map.put("surplusMeanTerm", StringUtils.rounding(surplusMeanTerm));//加权平均剩余期限
			map.put("now", now);
			map.put("status", 1);
		} catch (Exception e) {
			logger.debug("通过统计维度统计分布情况异常："+e);
		}
		return map;
	}	

	
}
