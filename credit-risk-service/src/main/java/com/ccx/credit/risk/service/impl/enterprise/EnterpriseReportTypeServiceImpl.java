package com.ccx.credit.risk.service.impl.enterprise;
	
import com.ccx.credit.risk.api.enterprise.EnterpriseReportTypeApi;
import com.ccx.credit.risk.mapper.enterprise.EnterpriseReportTypeMapper;
import com.ccx.credit.risk.model.enterprise.EnterpriseReportType;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;	
import java.util.*;	
	
@Service("enterpriseReportTypeApi")
public class EnterpriseReportTypeServiceImpl implements EnterpriseReportTypeApi {
		
	@Autowired	
    private EnterpriseReportTypeMapper enterpriseReportTypeMapper;
		
	//主键获取
	@Override	
	public EnterpriseReportType getById(Integer id) {
		return enterpriseReportTypeMapper.selectByPrimaryKey(id);	
	}

	//通过主键获取名称
	@Override
	public String getNameById(Integer id) {
		return enterpriseReportTypeMapper.getNameById(id);
	}

	//获取无参list
	@Override	
	public List<EnterpriseReportType> getList() {	
		return enterpriseReportTypeMapper.selectAll();
	}	
		
	//获取有参数list
	@Override	
	public List<EnterpriseReportType> getList(EnterpriseReportType model) {	
		return null;	
	}	
		
	//获取带分页list
	@Override	
	public List<EnterpriseReportType> getPageList(Page<EnterpriseReportType> page) {
		return null;	
	}	
		
	//通过条件获取
	@Override	
	public EnterpriseReportType getByModel(EnterpriseReportType model) {	
		return null;	
	}	
	
	//保存对象
	@Override	
	public int save(EnterpriseReportType model) {	
		return enterpriseReportTypeMapper.insert(model);	
	}	
	
	//更新对象
	@Override	
	public int update(EnterpriseReportType model) {	
		return enterpriseReportTypeMapper.updateByPrimaryKey(model);	
	}	
		
	//删除对象
	@Override	
	public int deleteById(Integer id) {	
		return enterpriseReportTypeMapper.deleteByPrimaryKey(id);	
	}	
		
	//其他查询
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}
}
