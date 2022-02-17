package com.xbb.entity;

import com.xbb.constant.BondConstant;
import com.xbb.table.TableProperty;
import com.xbb.table.AbstractTableEntity;
import lombok.*;


/**
 * 集思录可转债对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class JSLConvertibleBond implements AbstractTableEntity {

    @TableProperty(name = BondConstant.BOND_CODE, index = 0)
    private String bondId; // 可转债代码

    @TableProperty(name = BondConstant.BOND_NAME, index = 1)
    private String bondNm; // 可转债名称

    @TableProperty(name = BondConstant.BOND_PRICE, index = 2)
    private String price; // 可转债价格

    @TableProperty(name = BondConstant.BOND_INCREASE_RATE, index = 3)

    private String increaseRt; // 可转债涨跌幅

    @TableProperty(name = BondConstant.STOCK_NAME, index = 4)
    private String stockNm; // 股票代码

    @TableProperty(name = BondConstant.STOCK_PRICE, index = 5)
    private String sprice; // 股票价格

    @TableProperty(name = BondConstant.STOCK_INCREASE_RATE, index = 6)
    private String sincreaseRt; // 股票涨跌幅

    @TableProperty(name = BondConstant.STOCK_PB, index = 7)
    private String pb; // 正股pb

    @TableProperty(name = BondConstant.CONVERT_PRICE, index = 8)
    private String convertPrice; // 转股价

    @TableProperty(name = BondConstant.CONVERT_VALUE, index = 9)
    private String convertValue; // 转股价值

    @TableProperty(name = BondConstant.PREMIUM_RT, index = 10)
    private String premiumRt; // 溢价率

    @TableProperty(name = BondConstant.DB_LOW, index = 11)
    private String dblow; // 双低值

    @TableProperty(name = "评级", index = 12)
    private String ratingCd; // 评级

    @TableProperty(name = "剩余年限", index = 13)
    private String yearLeft; // 剩余年限

    @TableProperty(name = "换手率", index = 14)
    private String turnoverRt; // 换手率

    @TableProperty(name = BondConstant.YTM_RT, index = 15)
    private String ytmRt; // 到期收益率

    @TableProperty(name = BondConstant.CURR_ISS_AMT, index = 16)
    private String currIssAmt;

    @TableProperty(name = BondConstant.LAST_TIME, index = 17)
    private String lastTime;

    private String bondNmTip; // 转债提示

    private String redeemIcon; // 强赎标记

    @Override
    public String getIdentity() {
        return bondId;
    }

    @Override
    public String getIdentityColumnName() {
        return BondConstant.BOND_CODE;
    }

}
