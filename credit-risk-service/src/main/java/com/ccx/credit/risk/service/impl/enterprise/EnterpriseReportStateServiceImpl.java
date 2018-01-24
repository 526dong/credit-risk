package com.ccx.credit.risk.service.impl.enterprise;
	
import com.ccx.credit.risk.api.enterprise.EnterpriseReportStateApi;
import com.ccx.credit.risk.mapper.enterprise.EnterpriseReportStateMapper;
import com.ccx.credit.risk.model.enterprise.EnterpriseReportState;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;	
import java.util.*;	
	
@Service("enterpriseReportStateApi")
public class EnterpriseReportStateServiceImpl implements EnterpriseReportStateApi {
		
	@Autowired	
    private EnterpriseReportStateMapper enterpriseReportStateMapper;
		
	//主键获取
	@Override	
	public EnterpriseReportState getById(Integer id) {
		return enterpriseReportStateMapper.selectByPrimaryKey(id);
	}	
		
	//获取无参list
	@Override	
	public List<EnterpriseReportState> getList() {	
		return null;	
	}	
		
	//获取有参数list
	@Override	
	public List<EnterpriseReportState> getList(EnterpriseReportState model) {	
		return null;	
	}	
		
	//获取带分页list
	@Override	
	public List<EnterpriseReportState> getPageList(Page<EnterpriseReportState> page) {
		return null;	
	}	
		
	//通过条件获取
	@Override	
	public EnterpriseReportState getByModel(EnterpriseReportState model) {	
		return null;	
	}	
	
	//保存对象
	@Override	
	public int save(EnterpriseReportState model) {	
		return enterpriseReportStateMapper.insert(model);	
	}	
	
	//更新对象
	@Override	
	public int update(EnterpriseReportState model) {	
		return enterpriseReportStateMapper.updateByPrimaryKey(model);	
	}	
		
	//删除对象
	@Override	
	public int deleteById(Integer id) {	
		return enterpriseReportStateMapper.deleteByPrimaryKey(id);	
	}	
		
	//其他查询
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}
}
