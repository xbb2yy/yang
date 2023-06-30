package com.xbb.entity;

import com.xbb.constant.BondConstant;
import com.xbb.table.AbstractTableEntity;
import com.xbb.table.TableProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BondDailyDetail implements AbstractTableEntity {

    private String bondId; // 转债代码

    @TableProperty(name = BondConstant.LAST_CHG_DT, index = 0)
    private String lastChgDt; // 日期

    @TableProperty(name = BondConstant.BOND_PRICE, index = 1)
    private Double price; // 价格

    @TableProperty(name = BondConstant.YTM_RT, index = 2)
    private String ytmRt; // 到期收益率

    @TableProperty(name = BondConstant.PREMIUM_RT, index = 3)
    private String premiumRt; // 转股溢价率

    @TableProperty(name = BondConstant.TURNOVER_RT, index = 4)
    private Double turnoverRt; // 换手率

    private Double convertPrice; // 转股价

    private Double volume; // 成交量


    @Override
    public String getIdentityColumnName() {
        return BondConstant.LAST_CHG_DT;
    }
}
