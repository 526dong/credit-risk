package com.ccx.credit.risk.service.impl.enterprise;

import com.ccx.credit.risk.mapper.enterprise.EnterpriseReportDataStoreMapper;
import com.ccx.credit.risk.mapper.enterprise.EnterpriseReportStateMapper;
import com.ccx.credit.risk.model.enterprise.EnterpriseReportDataStore;
import com.ccx.credit.risk.api.enterprise.EnterpriseReportDataStoreApi;
import com.ccx.credit.risk.model.enterprise.EnterpriseReportState;
import com.ccx.credit.risk.utils.CommonMethodUtils;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
	
@Service("enterpriseReportDataStoreApi")
public class EnterpriseReportDataStoreServiceImpl implements EnterpriseReportDataStoreApi {
		
	@Autowired	
    private EnterpriseReportDataStoreMapper enterpriseReportDataStoreMapper;

	@Autowired
	private EnterpriseReportStateMapper enterpriseReportStateMapper;
		
	//主键获取
	@Override	
	public EnterpriseReportDataStore getById(Integer id) {	
		return enterpriseReportDataStoreMapper.selectByPrimaryKey(id);	
	}

	@Override
	public List<EnterpriseReportDataStore> findByReportId(Integer reportType) {
		return enterpriseReportDataStoreMapper.findByReportId(reportType);
	}

	//获取无参list
	@Override	
	public List<EnterpriseReportDataStore> getList() {	
		return null;	
	}	
		
	//获取有参数list
	@Override	
	public List<EnterpriseReportDataStore> getList(EnterpriseReportDataStore model) {	
		return null;	
	}	
		
	//获取带分页list
	@Override	
	public List<EnterpriseReportDataStore> getPageList(Page<EnterpriseReportDataStore> page) {
		return null;	
	}	
		
	//通过条件获取
	@Override	
	public EnterpriseReportDataStore getByModel(EnterpriseReportDataStore model) {	
		return null;	
	}	
	
	//保存对象
	@Override	
	public int save(EnterpriseReportDataStore model) {	
		return enterpriseReportDataStoreMapper.insert(model);	
	}

	/*插入报表数据*/
	@Override
	public void batchInsert(Map<String, Integer> sheetIdMap, List<EnterpriseReportDataStore> list, Integer reportId, Integer reportType) {
		enterpriseReportDataStoreMapper.batchInsert(list);

		List<EnterpriseReportState> reportSonStateList = CommonMethodUtils.getReportSonStateList(sheetIdMap, reportId, reportType);

		if (reportSonStateList != null && reportSonStateList.size() > 0) {
			enterpriseReportStateMapper.batchInsert(reportSonStateList);
		}
	}
	
	@Override
	public int updateDeleteColumn(Integer id) {
		return enterpriseReportDataStoreMapper.updateDeleteFlagById(id);
	}

	//删除对象
	@Override	
	public int deleteById(Integer id) {	
		return enterpriseReportDataStoreMapper.deleteByPrimaryKey(id);	
	}	
		
	//其他查询
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}
}
