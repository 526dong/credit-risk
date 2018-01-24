package com.ccx.credit.risk.service.impl.index;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccx.credit.risk.api.index.ElementApi;
import com.ccx.credit.risk.mapper.element.ModelElementMapper;
import com.ccx.credit.risk.model.element.ModelElement;

@Service("elementApi")
public class ElementServiceImpI implements ElementApi{

	@Autowired
	private ModelElementMapper dao;

	@Override
	public ModelElement selectByPrimaryKey(Integer id) {
		return dao.selectByPrimaryKey(id);
	}

	//通过行业modelId查找因素
	@Override
	public List<ModelElement> getListByModelId(Integer modelId) {
		return dao.getListByModelId(modelId);
	}

}
