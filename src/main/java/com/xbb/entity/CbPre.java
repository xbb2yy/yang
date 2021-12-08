package com.xbb.entity;

import com.xbb.constant.BondConstant;
import com.xbb.table.AbstractTableEntity;
import com.xbb.table.TableProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 待发转债
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CbPre  implements AbstractTableEntity {

    @TableProperty(name = BondConstant.STOCK_NAME, index = 0)
    private String stockNm; // 股票名称

    @TableProperty(name = BondConstant.STOCK_INCREASE_RATE, index = 1)
    private String increaseRt; // 正股涨跌

    @TableProperty(name = BondConstant.STOCK_PRICE, index = 2)
    private String price;

    @TableProperty(name = "转股价", index = 3)
    private String convertPrice; // 转股价格

    @TableProperty(name = "百元股票含权", index = 4)
    private String cbAmount; // 百元股票含权

    @TableProperty(name = "配售10张股数", index = 5)
    private String apply10; // 配售10张所需股数

    @TableProperty(name = "发行规模", index = 6)
    private String amount; // 发行规模

    @TableProperty(name = "进展", index = 7)
    private String progressNm; // 方案进展名称

    @TableProperty(name = "进展日期", index = 8)
    private String progressDt; // 方案进展日期

    private String cbType; // 转债类型



    private String ratingCd; // 评级

    private String recordDt; // 股权登记日


    @Override
    public String getIdentity() {
        return stockNm;
    }

    @Override
    public String getIdentityColumnName() {
        return BondConstant.STOCK_NAME;
    }

    @Override
    public AbstractTableEntity getInstance() {
        return AbstractTableEntity.super.getInstance();
    }
}
