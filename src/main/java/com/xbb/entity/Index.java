package com.xbb.entity;

import com.xbb.table.AbstractTableEntity;
import com.xbb.table.TableProperty;
import lombok.*;


/**
 * 指数涨幅
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Index implements AbstractTableEntity {


    public static final String 本周涨幅 = "本周涨幅";
    public static final String 本月涨幅 = "本月涨幅";
    public static final String 本季涨幅 = "本季涨幅";
    public static final String 本年涨幅 = "本年涨幅";
    public static final String 涨幅2021 = "2021涨幅";
    public static final String 涨幅2020 = "2020涨幅";
    public static final String 涨幅2019 = "2019涨幅";
    public static final String 涨幅2018 = "2018涨幅";
    public static final String 涨幅2017 = "2017涨幅";
    public static final String 涨幅2016 = "2016涨幅";
    public static final String 涨幅 = "涨幅";
    public static final String 现价 = "现价";

    @TableProperty(name = "名称", index = 0)
    private String indexNm;

    @TableProperty(name = 现价, index = 1)
    private String price;

    @TableProperty(name = 涨幅, index = 2)
    private String increaseRt;

    @TableProperty(name = 本周涨幅, index = 3)
    private String last_week_chg_pct;

    @TableProperty(name = 本月涨幅, index = 4)
    private String last_month_chg_pct;

    @TableProperty(name = 本季涨幅, index = 5)
    private String last_season_chg_pct;

    @TableProperty(name = 本年涨幅, index = 6)
    private String last_year_chg_pct;

    @TableProperty(name = 涨幅2021, index = 7)
    private String year_2021ChgPct;

    @TableProperty(name = 涨幅2020, index = 8)
    private String year_2020ChgPct;

    @TableProperty(name = 涨幅2019, index = 9)
    private String year_2019ChgPct;

    @TableProperty(name = 涨幅2018, index = 10)
    private String year_2018ChgPct;

    @TableProperty(name = 涨幅2017, index = 11)
    private String year_2017ChgPct;

    @TableProperty(name = 涨幅2016, index = 12)
    private String year_2016ChgPct;


    @Override
    public String getIdentity() {
        return indexNm;
    }

    @Override
    public String getIdentityColumnName() {
        return "名称";
    }

}
