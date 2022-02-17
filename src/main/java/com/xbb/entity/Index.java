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


    @TableProperty(name = "名称", index = 0)
    private String indexNm;

    @TableProperty(name = "现价", index = 1)
    private String price;

    @TableProperty(name = "涨幅", index = 2)
    private String increaseRt;

    @TableProperty(name = "本周涨幅", index = 3)
    private String last_week_chg_pct;

    @TableProperty(name = "本月涨幅", index = 4)
    private String last_month_chg_pct;

    @TableProperty(name = "本季涨幅", index = 5)
    private String last_season_chg_pct;

    @TableProperty(name = "本年涨幅", index = 6)
    private String last_year_chg_pct;

    @TableProperty(name = "2021涨幅", index = 7)
    private String year_2021ChgPct;

    @TableProperty(name = "2020涨幅", index = 8)
    private String year_2020ChgPct;

    @TableProperty(name = "2019涨幅", index = 9)
    private String year_2019ChgPct;

    @TableProperty(name = "2018涨幅", index = 10)
    private String year_2018ChgPct;

    @TableProperty(name = "2017涨幅", index = 11)
    private String year_2017ChgPct;

    @TableProperty(name = "2016涨幅", index = 12)
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
