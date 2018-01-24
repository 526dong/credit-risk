package com.ccx.credit.risk.service.impl.assetlayer;


import com.ccx.credit.risk.api.assetlayer.LayerSetupApi;
import com.ccx.credit.risk.mapper.assetlayer.LayerAssetLevelMapper;
import com.ccx.credit.risk.mapper.assetlayer.LayerAssetsRelevantMapper;
import com.ccx.credit.risk.mapper.assetlayer.LayerMapper;
import com.ccx.credit.risk.mapper.assetlayer.LayerSetupMapper;
import com.ccx.credit.risk.mapper.assetsPackage.AssetsPackageMapper;
import com.ccx.credit.risk.model.User;
import com.ccx.credit.risk.model.asset.AbsAsset;
import com.ccx.credit.risk.model.assetlayer.Layer;
import com.ccx.credit.risk.model.assetlayer.LayerAssetLevel;
import com.ccx.credit.risk.model.assetlayer.LayerAssetsRelevant;
import com.ccx.credit.risk.model.assetlayer.LayerSetup;
import com.ccx.credit.risk.util.DateUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
	
/**
 * @author zhaotm
 */
@Service
public class LayerSetupServiceImpl implements LayerSetupApi {
		
	@Autowired	
    private LayerSetupMapper layerSetupMapper;

	@Autowired
	private LayerMapper layerMapper;

	@Autowired
	private LayerAssetsRelevantMapper layerAssetsRelevantMapper;

	@Autowired
	private AssetsPackageMapper assetsPackageMapper;

	@Autowired
	private LayerAssetLevelMapper layerAssetLevelMapper;

	//主键获取	
	@Override	
	public LayerSetup getById(Integer id) {
		return layerSetupMapper.selectByPrimaryKey(id);
	}	
		
	//获取无参list	
	@Override	
	public List<LayerSetup> getList() {	
		return null;	
	}	
		
	//获取有参数list	
	@Override	
	public List<LayerSetup> getList(LayerSetup model) {	
		return null;	
	}	
		
	//获取带分页list	
	@Override	
	public PageInfo<LayerSetup> getPageList(Map<String,Object> params) {
		return null;	
	}	
		
	//通过条件获取	
	@Override	
	public LayerSetup getByModel(LayerSetup model) {	
		return null;	
	}	
	
	//保存对象	
	@Override	
	public int save(LayerSetup model) {	
		return layerSetupMapper.insert(model);
	}	
	
	//更新对象	
	@Override	
	public int update(LayerSetup model) {	
		return layerSetupMapper.updateByPrimaryKey(model);
	}	
		
	//删除对象	
	@Override	
	public int deleteById(Integer id) {	
		return layerSetupMapper.deleteByPrimaryKey(id);
	}	
		
	//其他查询	
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}

	/**
	 * 保存分层设置
	 * @param request
	 * @param paramMap
	 * @param setup
	 */
	@Override
	public Map<String, Integer> saveLayerSet(HttpServletRequest request, Map<String, Object> paramMap, LayerSetup setup) throws ParseException {
		Integer newLayerId = -1;
		Integer newSetupId = -1;
		Integer layetId = setup.getLayerId();
		String status = (String) paramMap.get("status");
		Map<String, Integer> resultMap = new HashMap<>();
		this.formatLayerDate(paramMap, setup);

		if (-1 == layetId.intValue()) {
			//新的layer增加一条分层记录
			newLayerId = this.mySaveNewLayer(paramMap, null);

			//新的一条setup记录, 一条level记录
			setup.setLayerId(newLayerId);
			layerSetupMapper.insert(setup);
			newSetupId = setup.getId();
			this.mySaveNewLayerLevel(request, newLayerId);

		} else {
			if ("0".equals(status)) {
				//分层未提交，可以继续需改
				layerSetupMapper.updateByPrimaryKeySelective(setup);
				this.myDeleteLayerLevel(layetId);
				this.mySaveNewLayerLevel(request, layetId);
			} else {
				//提交过的分层，新增一条分层记录
				paramMap.put("status", "0");
				newLayerId = this.mySaveNewLayer(paramMap, layetId);
				setup.setId(null);
				setup.setLayerId(newLayerId);
				layerSetupMapper.insert(setup);
				newSetupId = setup.getId();
				this.mySaveNewLayerLevel(request, newLayerId);
				this.syncRelation(layetId, newLayerId);
			}

		}

		resultMap.put("newLayerId", newLayerId);
		resultMap.put("newSetupId", newSetupId);
		return resultMap;
	}

	/**
	 * 保证执行分层时都是最新的未分层状态，简化逻辑，不用做连续2次分层处理，
	 * 如果第一次分层，调用保存方法，生产一条未分层的记录，用未分层的记录提交
	 */
	@Override
	public Map<String, Object> saveLayerStart(Integer id) {
		Map<String, Object> resultMap = new HashMap<>();

		//调用传输接口


		//save
		this.mySaveLayerStart(id);

		return resultMap;
	}

	/**
	 * 分层id获取
	 *
	 * @param layerId
	 */
	@Override
	public LayerSetup getByLayerId(Integer layerId) {
		return layerSetupMapper.getByLayerId(layerId);
	}


	private void formatLayerDate(Map<String, Object> paramMap, LayerSetup setup) throws ParseException {
		String closePackageTimeStr = (String) paramMap.get("closePackageTimeStr");
		String foundTimeStr = (String) paramMap.get("foundTimeStr");
		String predictExpireTimeStr = (String) paramMap.get("predictExpireTimeStr");
		String firstRepaymentTimeStr = (String) paramMap.get("firstRepaymentTimeStr");


		setup.setClosePackageTime(DateUtils.parseStr2Date1(closePackageTimeStr));
		setup.setFoundTime(DateUtils.parseStr2Date1(foundTimeStr));
		setup.setPredictExpireTime(DateUtils.parseStr2Date1(predictExpireTimeStr));
		setup.setFirstRepaymentTime(DateUtils.parseStr2DYearMonth(firstRepaymentTimeStr));

        List<Map<String, Object>> capitalPricipalMapList = layerMapper.getCapitalPricipal(setup);
        BigDecimal[] totalRepaymentPricipalArr = new BigDecimal[capitalPricipalMapList.size()];
        BigDecimal[] cycArr = new BigDecimal[capitalPricipalMapList.size()];
        //剩余本金
        BigDecimal totalRepayment = new BigDecimal("0.00");
        //剩余本息
        BigDecimal totalRepaymentPricipal = new BigDecimal("0.00");
        //加权剩余年限
        BigDecimal avg = new BigDecimal("0.00");

        for (int i=0; i<capitalPricipalMapList.size(); i++) {
            BigDecimal repayment = (BigDecimal) capitalPricipalMapList.get(i).get("repaymentAmount");
            BigDecimal pricipal = (BigDecimal) capitalPricipalMapList.get(i).get("repaymentInterest");
			Date cycEnd = new Date(((Long) capitalPricipalMapList.get(i).get("repaymentDate")).longValue());

            //资产包剩余本金
            totalRepayment = totalRepayment.add(repayment);
            //每笔资产剩余本息
            totalRepaymentPricipalArr[i] = repayment.add(pricipal);
            //每笔资产剩余天数
            cycArr[i] = new BigDecimal( DateUtils.dateDiffDown(cycEnd, setup.getClosePackageTime()));
            //资产包剩余本息
            totalRepaymentPricipal = totalRepaymentPricipal.add(totalRepaymentPricipalArr[i]);
        }

        for (int i=0; i<capitalPricipalMapList.size(); i++) {
			BigDecimal tmp = totalRepaymentPricipalArr[i].divide(totalRepaymentPricipal, 2, BigDecimal.ROUND_HALF_UP).multiply(cycArr[i]).divide(BigDecimal.valueOf(365), 2, BigDecimal.ROUND_HALF_UP);
			avg = avg.add(tmp);
        }

        setup.setLeftCapital(totalRepayment);
        setup.setLeftPrincipal(totalRepaymentPricipal);
        setup.setAvgYear(avg);
	}

	@Override
	public Integer mySaveNewLayer(Map<String, Object> paramMap, Integer layerId) {
		String user = ((User)(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()).getSession().getAttribute("risk_crm_user")).getLoginName();
		String assetPakegeId = (String) paramMap.get("assetPakegeId");
		String status = (String) paramMap.get("status");

		Layer layer = new Layer();
		layer.setAssetPakegeId(Integer.parseInt(assetPakegeId));
		layer.setStatus(new Byte(status));
		layer.setFinishFlag((byte) 0);
		if (null != layerId) {
			//依然显示上次分层时间
			Layer layerBak = layerMapper.selectByPrimaryKey(layerId);
			layer.setLastLayerTime(layerBak.getLastLayerTime());
			layer.setLayerApplyNum(String.format("%09d", layerId));
		} else {
			//插入新的
			//layer.setLastLayerTime(new Date());
		}
		layer.setLastLayerUserName(user);
		layerMapper.insert(layer);

		return layer.getId();
	}

	@Override
	public void myDeleteLayerLevel(Integer layerId) {
		layerAssetLevelMapper.deleteByLayerId(layerId);
	}

	@Override
	public void mySaveNewLayerLevel(HttpServletRequest request, Integer layerId) {
		String[] levelIdArr = request.getParameterValues("levelId");
		String[] securityTypeArr = request.getParameterValues("securityType");
		String[] levelNameArr = request.getParameterValues("levelName");
		String[] capitalRateArr = request.getParameterValues("capitalRate");
		String[] expectEarningsRateArr = request.getParameterValues("expectEarningsRate");
		String[] isFloatArr = request.getParameterValues("isFloat");

		if (null == levelIdArr || levelIdArr.length == 0) {
			return;
		}

		List<LayerAssetLevel> list = new ArrayList<>();

		for (int i = 0; i < levelIdArr.length; i++) {
			LayerAssetLevel level = new LayerAssetLevel();
			level.setLayerId(layerId);
			level.setLevelId(levelIdArr[i]);
			level.setLayerName(levelNameArr[i]);
			level.setSecurityType(new Byte(securityTypeArr[i]));
			level.setCapitalRate(new BigDecimal(capitalRateArr[i]));
			level.setExpectEarningsRate(new BigDecimal(expectEarningsRateArr[i]));
			level.setIsFloat(new Byte(isFloatArr[i]));

			list.add(level);
		}
		layerAssetLevelMapper.insertList(list);
	}

	@Override
	public void myGenNewLevelByOldLayerId(Integer layerId, Integer newLayerId) {
		List<LayerAssetLevel> list = layerAssetLevelMapper.getListByLayerId(layerId);

		for (LayerAssetLevel level : list) {
			level.setId(null);
			level.setLayerId(newLayerId);
		}
		layerAssetLevelMapper.insertList(list);
	}

	private void mySaveLayerStart(Integer id) {

		Layer layer = layerMapper.selectByPrimaryKey(id);
		LayerSetup setup = layerSetupMapper.getByLayerId(layer.getId());
		String user = ((User)(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()).getSession().getAttribute("risk_crm_user")).getLoginName();
		Date date = new Date();
		layer.setStatus((byte) 1);
		List<AbsAsset> list = assetsPackageMapper.getAssetListById(layer.getAssetPakegeId());
		if (list.size() == 0) {
			throw new RuntimeException("包有问题");
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (0 ==i) {
				sb.append(list.get(i).getId());
			} else {
				sb.append(",").append(list.get(i).getId());
			}
		}
		setup.setAssetNum(list.size());
		setup.setAssetIds(sb.toString());
		layerMapper.updateByPrimaryKey(layer);
		layerSetupMapper.updateByPrimaryKey(setup);
	}

	private void syncRelation(Integer oldLayerId, Integer newLayerId) {
		List<LayerAssetsRelevant> list = layerAssetsRelevantMapper.getListByLayerId(oldLayerId);

		for (LayerAssetsRelevant layerAssetsRelevant : list) {
			layerAssetsRelevant.setLayerId(newLayerId);
			layerAssetsRelevant.setId(null);
		}
		if (list.size() > 0) {
			layerAssetsRelevantMapper.insetList(list);
		}
	}
}	
