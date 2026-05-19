package com.pharmacy.mapper;

import com.pharmacy.vo.ReportDayAggVO;
import com.pharmacy.vo.ReportDrugAggVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ReportMapper {
    List<ReportDrugAggVO> selectSalesByDrug(@Param("start") Date start,
                                           @Param("end") Date end,
                                           @Param("limit") Integer limit);

    List<ReportDayAggVO> selectSalesByDay(@Param("start") Date start,
                                         @Param("end") Date end);

    List<ReportDrugAggVO> selectPurchaseByDrug(@Param("start") Date start,
                                              @Param("end") Date end,
                                              @Param("limit") Integer limit);

    List<ReportDrugAggVO> selectLowStock(@Param("threshold") Integer threshold,
                                        @Param("limit") Integer limit);
}

