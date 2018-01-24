package com.ccx.credit.risk.mapper.element;


import java.util.List;

import com.ccx.credit.risk.model.element.ModelElement;

public interface ModelElementMapper {

    ModelElement selectByPrimaryKey(Integer id);

    //通过行业modelId查找因素id
    List<ModelElement> getListByModelId(Integer modelId);
}