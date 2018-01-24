package com.ccx.credit.risk.service.impl.enterprise;

import com.ccx.credit.risk.api.enterprise.ReportApi;
import com.ccx.credit.risk.manager.report.CommonGainReportValue;
import com.ccx.credit.risk.mapper.enterprise.EnterpriseMapper;
import com.ccx.credit.risk.mapper.enterprise.EnterpriseReportDataStoreMapper;
import com.ccx.credit.risk.mapper.enterprise.EnterpriseReportStateMapper;
import com.ccx.credit.risk.mapper.enterprise.ReportMapper;
import com.ccx.credit.risk.model.User;
import com.ccx.credit.risk.model.enterprise.Enterprise;
import com.ccx.credit.risk.model.enterprise.EnterpriseReportDataStore;
import com.ccx.credit.risk.model.enterprise.EnterpriseReportState;
import com.ccx.credit.risk.model.enterprise.Report;
import com.ccx.credit.risk.util.ControllerUtil;
import com.ccx.credit.risk.util.JsonUtils;
import com.ccx.credit.risk.util.StringUtils;
import com.ccx.credit.risk.utils.CommonMethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service("reportApi")
public class ReportServiceImpI implements ReportApi{

	@Autowired
	ReportMapper dao;

	@Autowired
	EnterpriseMapper enterpriseMapper;

	@Autowired
	EnterpriseReportStateMapper reportStateMapper;

	@Autowired
	EnterpriseReportDataStoreMapper reportDataStoreMapper;

	@Autowired
	private CommonGainReportValue reportValue;

	@Override
	public void momentSaveOrUpdate (HttpServletRequest request, Report report, Integer enterpriseId) {
		//创建人
		User user = ControllerUtil.getSessionUser(request);

		String creatorName = "";

		if (user != null) {
			creatorName = user.getLoginName();
		}

		//进程间传过来的报表id
		Integer reportId = report.getId();
		//企业信息
		Enterprise enterprise = enterpriseMapper.findById(enterpriseId);
		//通过企业id查询当前企业的所有报表时间
		List<Map<String, Object>> reportTimeList = enterpriseMapper.findAllReportTime(enterpriseId);

		//报表状态：未完成
		report.setState(0);

		//1、报表概况表-新增
		if (reportId == 0) {
			report.setId(null);
			report.setCreatorName(creatorName);
			report.setCreateDate(new Date());

			dao.insert(report);

			//新增企业关联表
			Map<String, Object> enterpriseData = CommonMethodUtils.getUpdateEnterprise(true, enterprise, report.getId(), reportTimeList);

			//执行-dao层方法
			updateEnterpriseData(enterpriseData);
		} else {
			//报表概况表-更新
			report.setId(reportId);
			report.setUpdateDate(new Date());

			dao.update(report);

			//更新企业状态
			Map<String, Object> enterpriseData = CommonMethodUtils.getUpdateEnterprise(false, enterprise, null, reportTimeList);

			//执行-dao层方法
			updateEnterpriseData(enterpriseData);
		}

	}

	/**
	 * 保存财务报表信息-报表概况信息，报表子表信息
	 * @param request
	 * @param enterpriseId
	 * @param report 报表概况数据
	 * @param reportType 报表类型
	 * @param sheetIdsMap 子表id
	 * @param reportSonDataMap 报表子表数据
	 */
	@Override
	public void saveReport(HttpServletRequest request, Integer enterpriseId, Report report, Integer reportType, Map<String, Integer> sheetIdsMap,
			  Map<String, List<EnterpriseReportDataStore>> reportSonDataMap) {
		//创建人
		User user = ControllerUtil.getSessionUser(request);
		String creatorName = "";

		if (user != null) {
			creatorName = user.getLoginName();
		}

		//进程间传过来的报表id
		Integer reportId = report.getId();
		//企业信息
		Enterprise enterprise = enterpriseMapper.findById(enterpriseId);
		//通过企业id查询当前企业的所有报表时间
		List<Map<String, Object>> reportTimeList = enterpriseMapper.findAllReportTime(enterpriseId);

		//1、报表概况表-新增
		if (reportId == 0) {
			report.setId(null);
			report.setCreatorName(creatorName);
			report.setCreateDate(new Date());
			//完成状态-已完成
			report.setState(1);

			dao.insert(report);

			//新增企业关联表/更新企业表
			Map<String, Object> enterpriseData = CommonMethodUtils.getUpdateEnterprise(true, enterprise, report.getId(), reportTimeList);
			//执行-dao层方法
			updateEnterpriseData(enterpriseData);
			//获取报表状态list
			List<EnterpriseReportState> enterpriseReportStateList = CommonMethodUtils.getReportSonStateList(sheetIdsMap, report.getId(), reportType);

			if (enterpriseReportStateList != null && enterpriseReportStateList.size() > 0) {
				//批量添加报表状态
				reportStateMapper.batchInsert(enterpriseReportStateList);
			}
		} else {
			//报表概况表-更新
			report.setId(reportId);
			report.setUpdateDate(new Date());
			//完成状态-已完成
			report.setState(1);

			dao.update(report);

			//更新企业状态
			Map<String, Object> enterpriseData = CommonMethodUtils.getUpdateEnterprise(false, enterprise, null, reportTimeList);
			//执行-dao层方法
			updateEnterpriseData(enterpriseData);
			//处理财务报表-删除
			dealReportDeleteFun(reportId, sheetIdsMap, reportType);
		}

		//2、报表子表信息
		//报表子表数据集
		List<EnterpriseReportDataStore> reportDataList = getReportSonData(reportSonDataMap, report.getId(), creatorName);

		if (reportDataList != null && reportDataList.size() > 0) {
			//批量插入报表数据
			reportDataStoreMapper.batchInsert(reportDataList);
		}
	}

	/**
	 * 更新企业、企业-报表关联表
	 * @param enterpriseData
	 */
	public void updateEnterpriseData(Map<String, Object> enterpriseData) {
		if (enterpriseData != null && enterpriseData.size() > 0) {
			//新增企业-报表关联表
			Object entRelation = enterpriseData.get("entRelation");
			if (entRelation != null) {
				Map<String, Integer> mapParam = (Map<String, Integer>) entRelation;
				//企业报表关联表
				dao.insertRelation(mapParam);
			}

			//更新企业表
			Object ent = enterpriseData.get("enterprise");
			if (ent != null) {
				Enterprise updateEnt = (Enterprise) ent;
				enterpriseMapper.updateBySelect(updateEnt);
			}
		}
	}

	/**
	 * 处理报表子表删除功能
	 * @param reportId
	 * @param sheetIdsMap
	 * @param reportType
	 */
	public void dealReportDeleteFun(Integer reportId, Map<String, Integer> sheetIdsMap, Integer reportType){
		//逻辑删除报表子表信息
		CommonMethodUtils.updateReportDataStoreState(reportDataStoreMapper, reportId);

		//物理删除报表子表状态记录
		List<EnterpriseReportState> reportSonStateList = reportStateMapper.selectByReportId(reportId);

		if (reportSonStateList != null && reportSonStateList.size() > 0) {
			for (EnterpriseReportState reportSonState:reportSonStateList) {
				reportStateMapper.deleteByPrimaryKey(reportSonState.getId());
			}
		}

		//获取报表状态list
		List<EnterpriseReportState> enterpriseReportStateList = CommonMethodUtils.getReportSonStateList(sheetIdsMap, reportId, reportType);

		if (enterpriseReportStateList != null && enterpriseReportStateList.size() > 0) {
			//批量添加报表状态
			reportStateMapper.batchInsert(enterpriseReportStateList);
		}
	}


	/**
	 * 获取报表子表中的数据
	 * @param reportSonDataMap
	 * @param reportId
	 * @param creatorName
	 * @return
	 */
	public List<EnterpriseReportDataStore> getReportSonData(Map<String, List<EnterpriseReportDataStore>> reportSonDataMap, Integer reportId, String creatorName){
		//report data list
		List<EnterpriseReportDataStore> reportDataList = new ArrayList<>();

		//循环遍历参数map
		Iterator it = reportSonDataMap.entrySet().iterator();

		while(it.hasNext()) {
			Map.Entry<String, List<EnterpriseReportDataStore>> map = (Map.Entry<String, List<EnterpriseReportDataStore>>) it.next();

			//子表数据信息
			List<EnterpriseReportDataStore> reportSonDataList = map.getValue();

			if (reportSonDataList != null && reportSonDataList.size() > 0) {
				for (EnterpriseReportDataStore reportSonData:reportSonDataList) {
					reportSonData.setId(null);
					//报表id
					reportSonData.setReportId(reportId);
					reportSonData.setCreatorName(creatorName);
					reportSonData.setCreateDate(new Date());
					reportDataList.add(reportSonData);
				}
			}
		}

		return reportDataList;
	}

	@Override
	public void insertRelation(Map<String, Integer> map) {
		dao.insertRelation(map);
	}
	
	@Override
	public void deleteById(Integer enterpriseId, Integer reportId) {
		//逻辑删除：报表概况表记录-修改删除状态为已经删除
		dao.updateDeleteFlagById(reportId);

		//物理删除：企业-报表关系表
		dao.deleteRelationByReportId(reportId);

		//逻辑删除：财务报表记录
		CommonMethodUtils.updateReportDataStoreState(reportDataStoreMapper, reportId);

		//企业信息
		Enterprise ent = enterpriseMapper.findById(enterpriseId);
		//通过企业id查询当前企业的所有报表时间
		List<Map<String, Object>> reportTimeList = enterpriseMapper.findAllReportTime(enterpriseId);

		if (ent != null && ent.getReportId() != null) {
			//删除了ent中最新的报表
			if (reportId.equals(ent.getReportId())) {
				Map<String, Integer> lastedReportIdMap = CommonMethodUtils.compareReportTime(reportTimeList, enterpriseId);

				if (lastedReportIdMap != null && lastedReportIdMap.get("lastestReprotId") != null) {
					//将最新的报表id更新到企业表中
					ent.setReportId(lastedReportIdMap.get("lastestReprotId"));
				}
				enterpriseMapper.updateBySelect(ent);
			}
		}
	}

	@Override
	public void deleteRelationByReportId(Integer reportId) {
		dao.deleteRelationByReportId(reportId);
	}
	
	@Override
	public void update(Report report) {

		dao.update(report);
	}

	@Override
	public Report findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public int findByReportTimeAndCal(Integer enterpriseId, String reportTime, Integer cal) {
		return dao.findByReportTimeAndCal(enterpriseId, reportTime, cal);
	}

	@Override
	public List<Report> findByEnterpriseId(Integer enterpriseId) {
		return dao.findByEnterpriseId(enterpriseId);
	}

	@Override
	public int findNewReportByEnt(Integer enterpriseId) {
		return dao.findNewReportByEnt(enterpriseId);
	}

	//提交评级
	@Override
	public Map<String, Object> submiRate(String rateNo, Integer industryId2) throws Exception {
		return null;
	}

	//根据ids查报告
	@Override
	public List<Report> submitReportList(List<Integer> idList) {
		return dao.findReportListByIds(idList);
	}

    @Override
    public int calReportHashCode(String reportIds) {
		if (StringUtils.isNotBlank(reportIds)) {
			StringBuilder stringBuilder = new StringBuilder();
			String[] reportIdArr = reportIds.split(",");

			for (String reportId : reportIdArr) {
				stringBuilder.append(JsonUtils.toJson(reportValue.getBaseValue(Integer.parseInt(reportId))));
			}

			return stringBuilder.toString().hashCode();
		}
		return 0;
    }

    public static int doCalReportHashCode(String reportIds) {
		if (StringUtils.isNotBlank(reportIds)) {
			StringBuilder stringBuilder = new StringBuilder();
			List<Integer> reportIdList = new ArrayList<>();
			String[] reportIdArr = reportIds.split(",");

			for (String reportId : reportIdArr) {
				reportIdList.add(Integer.parseInt(reportId));
			}


			//TODO 计算hash值
			return 1;
		}
		return 0;
	}

}
