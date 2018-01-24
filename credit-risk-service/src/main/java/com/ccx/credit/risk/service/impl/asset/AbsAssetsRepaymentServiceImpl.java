package com.ccx.credit.risk.service.impl.asset;
	
import com.ccx.credit.risk.api.asset.AbsAssetsRepaymentApi;
import com.ccx.credit.risk.mapper.asset.AbsAssetsRepaymentMapper;
import com.ccx.credit.risk.model.asset.AbsAssetsRepayment;
import com.ccx.credit.risk.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;	
import java.util.*;	
	
@Service("absAssetsRepaymentApi")
public class AbsAssetsRepaymentServiceImpl implements AbsAssetsRepaymentApi {
		
	@Autowired	
    private AbsAssetsRepaymentMapper absAssetsRepaymentMapper;
		
	//主键获取	
	@Override	
	public AbsAssetsRepayment getById(Integer id) {
		return absAssetsRepaymentMapper.selectByPrimaryKey(id);	
	}	
		
	//获取无参list	
	@Override	
	public List<AbsAssetsRepayment> getList() {	
		return null;	
	}	
		
	//获取有参数list	
	@Override	
	public List<AbsAssetsRepayment> getList(AbsAssetsRepayment model) {	
		return null;	
	}	
		
	//获取带分页list	
	@Override	
	public List<AbsAssetsRepayment> getPageList(Page<AbsAssetsRepayment> page) {
		return null;	
	}	
		
	//通过条件获取	
	@Override	
	public AbsAssetsRepayment getByModel(AbsAssetsRepayment model) {	
		return null;	
	}	
	
	//保存对象	
	@Override	
	public int save(AbsAssetsRepayment model) {	
		return absAssetsRepaymentMapper.insert(model);	
	}	
	
	//更新对象	
	@Override	
	public int update(AbsAssetsRepayment model) {	
		return absAssetsRepaymentMapper.updateByPrimaryKey(model);	
	}	
		
	//删除对象	
	@Override	
	public int deleteById(Integer id) {	
		return absAssetsRepaymentMapper.deleteByPrimaryKey(id);	
	}	
		
	//其他查询	
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}	
}	
