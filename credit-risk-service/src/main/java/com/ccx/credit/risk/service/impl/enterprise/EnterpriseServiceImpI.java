package com.ccx.credit.risk.service.impl.enterprise;

import com.ccx.credit.risk.api.enterprise.EnterpriseApi;
import com.ccx.credit.risk.mapper.enterprise.EnterpriseMapper;
import com.ccx.credit.risk.mapper.enterprise.ReportMapper;
import com.ccx.credit.risk.model.enterprise.Enterprise;
import com.ccx.credit.risk.model.enterprise.EnterpriseNature;
import com.ccx.credit.risk.model.enterprise.Report;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service("enterpriseApi")
public class EnterpriseServiceImpI implements EnterpriseApi{
	@Autowired
	EnterpriseMapper dao ;

	@Autowired
	ReportMapper reportMapper ;
	
	@Override
	public void insert(Enterprise enterprise) {
		dao.insert(enterprise);
	}
	
	@Override
	public void deleteById(Integer id) {
		//逻辑删除：企业信息
		dao.updateDeleteFlag(id);

		//2、通过企业id查询所有报表id
		List<Report> reportList = reportMapper.findByEnterpriseId(id);

		if (reportList != null && reportList.size()>0) {
			for (Report report:reportList) {
				if (report != null) {
					//3、删除报表
					reportMapper.updateDeleteFlagById(report.getId());
				}
			}
		}

		//4、删除关联信息
		dao.deleteRelationByEnterpriseId(id);
	}
	
	@Override
	public void deleteRelationByEnterpriseId(Integer id) {
		dao.deleteRelationByEnterpriseId(id);
	}

	@Override
	public void update(Enterprise enterprise) {
		dao.update(enterprise);
	}

	@Override
	public void updateBySelect(Enterprise enterprise) {
		dao.updateBySelect(enterprise);
	}

	@Override
	public Enterprise findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<Map<String, Object>> findAllReportTime(Integer enterpriseId) {
		return dao.findAllReportTime(enterpriseId);
	}

	@Override
	public PageInfo<Enterprise> findRated() {
		List<Enterprise> list = dao.findRated();
		PageInfo<Enterprise> pages = new PageInfo<>(list);
		return pages;
	}

	@Override
	public int findByCreditCode(String creditCode) {
		return dao.findByCreditCode(creditCode);
	}

	@Override
	public int findByName(String name) {
		return dao.findByName(name);
	}

	@Override
	public PageInfo<Enterprise> findAll(Map<String,Object> params) {
		List<Enterprise> list = dao.findAll(params);
		PageInfo<Enterprise> pages = new PageInfo<>(list);
		return pages;
	}
	
	@Override
	public PageInfo<Enterprise> findAllUnCommit(Map<String, Object> params) {
		List<Enterprise> list = dao.findAllUnCommit(params);
		PageInfo<Enterprise> pages = new PageInfo<>(list);
		return pages;
	}
	
	@Override
	public PageInfo<Enterprise> findAllCommit(Map<String, Object> params) {
		List<Enterprise> list = dao.findAllCommit(params);
		PageInfo<Enterprise> pages = new PageInfo<>(list);
		return pages;
	}

	@Override
	public PageInfo<Enterprise> findAllRated() {
		List<Enterprise> list = dao.findAllRated();
		PageInfo<Enterprise> pages = new PageInfo<>(list);
		return pages;
	}

	@Override
	public EnterpriseNature findNatureById(Integer id) {
		return dao.findNatureById(id);
	}

	@Override
	public List<EnterpriseNature> findAllNature() {
		return dao.findAllNature();
	}
	
	@Override
	public PageInfo<Map<String, Object>> findAllEvaluate(Map<String, Object> params) {
		List<Map<String, Object>> list = dao.findAllEvaluate(params);
		PageInfo<Map<String, Object>> pages = new PageInfo<>(list);
		return pages;
	}

	@Override
	public PageInfo<Map<String, Object>> findEvaluateHistory(Map<String, Object> params) {
		List<Map<String, Object>> list = dao.findEvaluateHistory(params);
		PageInfo<Map<String, Object>> pages = new PageInfo<>(list);
		return pages;
	}
	
	@Override
	public Map<String, String> selectEnterpriseIndexAndRules(Integer id) {
		return dao.selectEnterpriseIndexAndRules(id);
	}

	@Override
	public int selectRegularIndex(Integer id) {
		return dao.selectRegularIndex(id);
	}

	@Override
	public void updateEnterpriseType(Integer enterpriseId) {
		dao.updateEnterpriseType(enterpriseId);
	}

}
