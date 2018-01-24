package com.ccx.credit.risk.api.enterprise;

import java.util.List;
import java.util.Map;

import com.ccx.credit.risk.model.enterprise.Region;

/**
 * 初始化地区-省市县
 * @author xzd
 * @date 2017/6/28
 */
public interface RegionApi {
	Region findRegionById(Integer id);
	
	List<Region> findAllRegionByPid(Integer id);
	
	List<Map<String, String>> findAllRegionByPids(List<Long> ids);
	
	List<Long> findRegionIds(List<Long> ids);
	
}
