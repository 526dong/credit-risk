package com.ccx.credit.risk.mapper.index;


import com.ccx.credit.risk.model.index.IndexFormula;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IndexFormulaMapper {

    IndexFormula selectByPrimaryKey(Integer id);


    //获取无分页列表
    List<IndexFormula> findFormulaList();

    //获取带分页list
    List<IndexFormula> getPageList(@Param("parentFlag") Integer parentFlag, @Param("name") String name);

    //通过父id和年份查询公式
    String getByParentIdAndYear(@Param("formulaId") Integer formulaId, @Param("len") Integer len);
}