package com.ccx.credit.risk.model.asset;

import com.ccx.credit.risk.util.DateUtils;
import com.ccx.credit.risk.util.StringUtils;

import java.util.Date;

/**
 * 资产-操作记录实体
 * @author xzd
 * @date 2017/7/27
 */
public class AssetOperate{
	private Integer id;

	//操作人
    private String operator;

    //操作时间
    private Date operateTime;

    //操作日志记录
    private String operateRecord;
	
    //资产id
    private Integer assetId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperateTime() throws Exception {
		if (!StringUtils.isEmpty(operateTime)) {
			return DateUtils.formatDate(operateTime);
		}else{
			return null;
		}
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getOperateRecord() {
		return operateRecord;
	}

	public void setOperateRecord(String operateRecord) {
		this.operateRecord = operateRecord;
	}

	public Integer getAssetId() {
		return assetId;
	}

	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}
    
}
