package com.ccx.credit.risk.service.impl.rate;

import org.springframework.beans.factory.annotation.Autowired;

import com.ccx.credit.risk.api.enterprise.EnterpriseApi;
import com.ccx.credit.risk.api.index.IndexApi;
import com.ccx.credit.risk.api.rate.EnterpriseRatingApi;

public class EnterpriseRatingServiceImpl implements EnterpriseRatingApi{

	@Autowired
	IndexApi iApi;
	
	@Autowired
	EnterpriseApi epApi;
	
	/*@Override
	public Double entpriseRate(String entId) {
		return null;
	}*/

}
