package com.xbb.constant;

/**
 * 地址常量
 */
public class URLConstants {

    public static final String EAST_MONEY_URL = "http://77.push2.eastmoney.com/api/qt/clist/get?cb=jQuery112406285914" +
            "172501668_1590386857513&pn=1&pz=5000&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&fid=f2" +
            "43&fs=b%3AMK0354&fields=f1%2Cf152%2Cf2%2Cf3%2Cf12%2Cf13%2Cf14%2Cf227%2Cf228%2Cf229%2Cf230%2Cf231%2Cf232" +
            "%2Cf233%2Cf234%2Cf235%2Cf236%2Cf237%2Cf238%2Cf239%2Cf240%2Cf241%2Cf242%2Cf26%2Cf243&_=1590386857527";

    public static final String JSL_URL = "https://www.jisilu.cn/data/cbnew/cb_list_new/?___jsl=LST___t="
            + System.currentTimeMillis(); // 可转债列表

    public static final String PRE_LIST = "https://www.jisilu.cn/data/cbnew/pre_list/?___jsl=LST___t="
            + System.currentTimeMillis();  // 待发转债数据

    public static final String INDEX_PERFORMANCE = "https://www.jisilu.cn/data/idx_performance/list/?___jsl=LST___t="
            + System.currentTimeMillis(); // 指数涨幅


    public static final String JSL_BOND_DETAIL_URL = "https://www.jisilu.cn/data/cbnew/detail_hist/%s?___jsl=LST___t="  + System.currentTimeMillis(); ;
}
