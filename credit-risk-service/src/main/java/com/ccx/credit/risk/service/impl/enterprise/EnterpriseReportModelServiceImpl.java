package com.ccx.credit.risk.service.impl.enterprise;

import com.ccx.credit.risk.mapper.enterprise.EnterpriseReportModelMapper;
import com.ccx.credit.risk.model.enterprise.EnterpriseReportModel;
import com.ccx.credit.risk.api.enterprise.EnterpriseReportModelApi;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
	
@Service("enterpriseReportModelApi")
public class EnterpriseReportModelServiceImpl implements EnterpriseReportModelApi {
		
	@Autowired	
    private EnterpriseReportModelMapper enterpriseReportModelMapper;
		
	//主键获取
	@Override	
	public EnterpriseReportModel getById(Integer id) {	
		return enterpriseReportModelMapper.selectByPrimaryKey(id);	
	}

	//通过报表类型查询报表模板
	@Override
	public List<EnterpriseReportModel> selectByReportType(Integer reportType, Integer reportSonType) {
		return enterpriseReportModelMapper.selectByReportType(reportType, reportSonType);
	}

	//获取无参list
	@Override	
	public List<EnterpriseReportModel> getList() {	
		return null;	
	}	
		
	//获取有参数list
	@Override	
	public List<EnterpriseReportModel> getList(EnterpriseReportModel model) {	
		return null;	
	}	
		
	//获取带分页list
	@Override	
	public List<EnterpriseReportModel> getPageList(Page<EnterpriseReportModel> page) {
		return null;	
	}	
		
	//通过条件获取
	@Override	
	public EnterpriseReportModel getByModel(EnterpriseReportModel model) {	
		return null;	
	}	
	
	//保存对象
	@Override	
	public int save(EnterpriseReportModel model) {	
		return enterpriseReportModelMapper.insert(model);	
	}	
	
	//更新对象
	@Override	
	public int update(EnterpriseReportModel model) {	
		return enterpriseReportModelMapper.updateByPrimaryKey(model);	
	}	
		
	//删除对象
	@Override	
	public int deleteById(Integer id) {	
		return enterpriseReportModelMapper.deleteByPrimaryKey(id);	
	}	
		
	//其他查询
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}
}
