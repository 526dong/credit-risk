package com.ccx.credit.risk.api.index;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ccx.credit.risk.model.element.ModelElement;

public interface ElementApi {

    ModelElement selectByPrimaryKey(Integer id);

    //通过行业modelId查找因素id
    List<ModelElement> getListByModelId(Integer modelId);
}
