package com.ccx.credit.risk.service.impl.enterprise;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccx.credit.risk.api.enterprise.RegionApi;
import com.ccx.credit.risk.mapper.enterprise.RegionMapper;
import com.ccx.credit.risk.model.enterprise.Region;

@Service("regionApi")
public class RegionServiceImpI implements RegionApi{
	@Autowired
	RegionMapper dao ;

	@Override
	public Region findRegionById(Integer id) {
		return dao.findRegionById(id);
	}
	
	@Override
	public List<Region> findAllRegionByPid(Integer id) {
		return dao.findAllRegionByPid(id);
	}
	
	@Override
	public List<Map<String, String>> findAllRegionByPids(List<Long> ids) {
		return dao.findAllRegionByPids(ids);
	}

	@Override
	public List<Long> findRegionIds(List<Long> ids) {
		return dao.findRegionIds(ids);
	}

}
