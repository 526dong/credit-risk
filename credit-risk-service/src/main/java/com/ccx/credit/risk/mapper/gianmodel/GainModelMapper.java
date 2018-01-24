package com.ccx.credit.risk.mapper.gianmodel;


import com.ccx.credit.risk.manager.modelmessage.AsstesMsg;
import com.ccx.credit.risk.manager.modelmessage.CommonModlePakage;
import com.ccx.credit.risk.model.enterprise.Report;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * lilong
 */
public interface GainModelMapper {
	//获取资金信息
	List<Map<String,Object>>  getCashFlowList(@Param("pakegeId") Integer pakegeId,@Param("stime") String stime,@Param("etime") String etime);

	//获取资产名称
	List<Map<String,Object>>  getAssetsList(@Param("pakegeId") Integer pakegeId);

	/**
	 * 获取违约率
	 * @return
	 */
	List<Map<String,Object>>  getRateLevel();
	/**
	 * 获取回收率
	 * @return
	 */
	List<Map<String,Object>>  getRecoveryrates();

	/**
	 * 获取主体相关性
	 * @return
	 */
	List<Map<String,Object>>  getEnterpriseCorrelationIndex(Integer layerId);

	/**
	 * 获取相关性固定参数值
	 * @return
	 */
	List<Map<String,Object>>  getCorrelationIndexParam();

	/**
	 * 获取行业相关性
	 * @return
	 */
	List<Map<String,Object>>  getInsdustryCorrelationIndex();
	/**
	 * 获取分层资产相关信息
	 * @param packageId
	 * @return
	 */
	List<AsstesMsg> getAsstesMsg(Integer packageId);

	/**
	 * 获取分层级别信息
	 * @param layerId
	 * @return
	 */
	List<Map<String,Object>>  getLayerLevel(Integer layerId);

}
