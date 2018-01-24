package com.ccx.credit.risk.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ccx.credit.risk.model.Organization;

public interface OrganizationMapper extends BaseMapper<Organization>{

	List<Organization> findAll();

}
