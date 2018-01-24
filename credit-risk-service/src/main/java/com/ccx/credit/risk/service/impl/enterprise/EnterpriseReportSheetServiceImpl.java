package com.ccx.credit.risk.service.impl.enterprise;
	
import com.ccx.credit.risk.mapper.enterprise.EnterpriseReportSheetMapper;
import com.ccx.credit.risk.model.enterprise.EnterpriseReportSheet;
import com.ccx.credit.risk.api.enterprise.EnterpriseReportSheetApi;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;	
import java.util.*;	
	
@Service("enterpriseReportSheetApi")
public class EnterpriseReportSheetServiceImpl implements EnterpriseReportSheetApi {
		
	@Autowired	
    private EnterpriseReportSheetMapper enterpriseReportSheetMapper;
		
	//主键获取
	@Override	
	public EnterpriseReportSheet getById(Integer id) {	
		return enterpriseReportSheetMapper.selectByPrimaryKey(id);	
	}

	@Override
	public List<EnterpriseReportSheet> selectByReportType(Integer reportType) {
		return enterpriseReportSheetMapper.selectByReportType(reportType);
	}

	//通过报表类型、子表名称查询报表子表概况信息
	@Override
	public EnterpriseReportSheet selectByReportSonName(Integer reportType, String reportSonName) {
		return enterpriseReportSheetMapper.selectByReportSonName(reportType, reportSonName);
	}

	//获取无参list
	@Override	
	public List<EnterpriseReportSheet> getList() {	
		return null;	
	}	
		
	//获取有参数list
	@Override	
	public List<EnterpriseReportSheet> getList(EnterpriseReportSheet model) {	
		return null;	
	}	
		
	//获取带分页list
	@Override	
	public List<EnterpriseReportSheet> getPageList(Page<EnterpriseReportSheet> page) {
		return null;	
	}	
		
	//通过条件获取
	@Override	
	public EnterpriseReportSheet getByModel(EnterpriseReportSheet model) {	
		return null;	
	}	
	
	//保存对象
	@Override	
	public int save(EnterpriseReportSheet model) {	
		return enterpriseReportSheetMapper.insert(model);	
	}	
	
	//更新对象
	@Override	
	public int update(EnterpriseReportSheet model) {	
		return enterpriseReportSheetMapper.updateByPrimaryKey(model);	
	}	
		
	//删除对象
	@Override	
	public int deleteById(Integer id) {	
		return enterpriseReportSheetMapper.deleteByPrimaryKey(id);	
	}	
		
	//其他查询
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}
}
