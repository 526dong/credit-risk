package com.ccx.credit.risk.service.impl.enterprise;

import com.ccx.credit.risk.api.enterprise.EnterpriseReportCloumnsFormulaApi;
import com.ccx.credit.risk.mapper.enterprise.EnterpriseReportCloumnsFormulaMapper;
import com.ccx.credit.risk.model.enterprise.EnterpriseReportCloumnsFormula;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
	
@Service("enterpriseReportCloumnsFormulaApi")
public class EnterpriseReportCloumnsFormulaServiceImpl implements EnterpriseReportCloumnsFormulaApi {
		
	@Autowired	
    private EnterpriseReportCloumnsFormulaMapper enterpriseReportCloumnsFormulaMapper;
		
	//主键获取
	@Override	
	public EnterpriseReportCloumnsFormula getById(Integer id) {
		return enterpriseReportCloumnsFormulaMapper.selectByPrimaryKey(id);	
	}	
		
	//获取无参list
	@Override	
	public List<EnterpriseReportCloumnsFormula> getList() {	
		return null;	
	}	
		
	//获取有参数list
	@Override	
	public List<EnterpriseReportCloumnsFormula> getList(EnterpriseReportCloumnsFormula model) {	
		return null;	
	}	
		
	//获取带分页list
	@Override	
	public List<EnterpriseReportCloumnsFormula> getPageList(Page<EnterpriseReportCloumnsFormula> page) {
		return null;	
	}	
		
	//通过条件获取
	@Override	
	public EnterpriseReportCloumnsFormula getByModel(EnterpriseReportCloumnsFormula model) {	
		return null;	
	}	
	
	//保存对象
	@Override	
	public int save(EnterpriseReportCloumnsFormula model) {	
		return enterpriseReportCloumnsFormulaMapper.insert(model);	
	}	
	
	//更新对象
	@Override	
	public int update(EnterpriseReportCloumnsFormula model) {	
		return enterpriseReportCloumnsFormulaMapper.updateByPrimaryKey(model);	
	}	
		
	//删除对象
	@Override	
	public int deleteById(Integer id) {	
		return enterpriseReportCloumnsFormulaMapper.deleteByPrimaryKey(id);	
	}	
		
	//其他查询
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}

}
