package com.xbb.entity;

import com.xbb.constant.BondConstant;
import com.xbb.table.TableProperty;
import com.xbb.table.AbstractTableEntity;
import lombok.*;

/**
 * 东方财富可转债对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EastMoneyConvertibleBond implements AbstractTableEntity {

    @TableProperty(name = BondConstant.BOND_CODE, index = 0)
    private String f12;

    @TableProperty(name = BondConstant.BOND_NAME, index = 1)
    private String f14;

    @TableProperty(name = BondConstant.BOND_PRICE, index = 2)
    private String f2;

    @TableProperty(name = BondConstant.BOND_INCREASE_RATE, index = 3)
    private String f3;

    @TableProperty(name = BondConstant.STOCK_NAME, index = 4)
    private String f234;

    @TableProperty(name = BondConstant.STOCK_PRICE, index = 5)
    private String f229;

    @TableProperty(name = BondConstant.STOCK_INCREASE_RATE, index = 6)
    private String f230;

    @TableProperty(name = BondConstant.PREMIUM_RT, index = 7)
    private String f237;

    @TableProperty(name = BondConstant.DB_LOW, index = 8)
    private String dBlow;

    @TableProperty(name = "上市日期", index = 9)
    private String f26;


    @Override
    public String getIdentity() {
        return f12;
    }

    @Override
    public String getIdentityColumnName() {
        return BondConstant.BOND_CODE;
    }
}
