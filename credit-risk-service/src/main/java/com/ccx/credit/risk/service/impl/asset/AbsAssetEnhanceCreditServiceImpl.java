package com.ccx.credit.risk.service.impl.asset;
	
import com.ccx.credit.risk.api.asset.AbsAssetEnhanceCreditApi;
import com.ccx.credit.risk.mapper.asset.AbsAssetEnhanceCreditMapper;
import com.ccx.credit.risk.model.asset.AbsAssetEnhanceCredit;
import com.ccx.credit.risk.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;	
import java.util.*;	
	
@Service("absAssetEnhanceCreditApi")
public class AbsAssetEnhanceCreditServiceImpl implements AbsAssetEnhanceCreditApi {
		
	@Autowired	
    private AbsAssetEnhanceCreditMapper absAssetEnhanceCreditMapper;
		
	//主键获取	
	@Override	
	public AbsAssetEnhanceCredit getById(Integer id) {
		return absAssetEnhanceCreditMapper.selectByPrimaryKey(id);	
	}

	@Override
	public List<AbsAssetEnhanceCredit> findAllByPid(Integer pid) {
		return absAssetEnhanceCreditMapper.findAllByPid(pid);
	}

	//获取无参list	
	@Override	
	public List<AbsAssetEnhanceCredit> getList() {	
		return null;	
	}	
		
	//获取有参数list	
	@Override	
	public List<AbsAssetEnhanceCredit> getList(AbsAssetEnhanceCredit model) {	
		return null;	
	}	
		
	//获取带分页list	
	@Override	
	public List<AbsAssetEnhanceCredit> getPageList(Page<AbsAssetEnhanceCredit> page) {
		return null;	
	}	
		
	//通过条件获取	
	@Override	
	public AbsAssetEnhanceCredit getByModel(AbsAssetEnhanceCredit model) {	
		return null;	
	}	
	
	//保存对象	
	@Override	
	public int save(AbsAssetEnhanceCredit model) {	
		return absAssetEnhanceCreditMapper.insert(model);	
	}	
	
	//更新对象	
	@Override	
	public int update(AbsAssetEnhanceCredit model) {	
		return absAssetEnhanceCreditMapper.updateByPrimaryKey(model);	
	}	
		
	//删除对象	
	@Override	
	public int deleteById(Integer id) {	
		return absAssetEnhanceCreditMapper.deleteByPrimaryKey(id);	
	}	
		
	//其他查询	
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}	
}	
