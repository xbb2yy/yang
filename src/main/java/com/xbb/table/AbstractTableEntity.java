package com.xbb.table;

public interface AbstractTableEntity {

    /**
     * 唯一标识一行数据
     * @return
     */
    default String getIdentity() {
        return String.valueOf(hashCode());
    }

    /**
     * 数据唯一标识的列名
     * @return
     */
    String getIdentityColumnName();

    default AbstractTableEntity getInstance() {
        return this;
    }
}
