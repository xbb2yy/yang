package com.xbb.table;


import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.util.*;

public abstract class JavaBeanTableModel<T extends AbstractTableEntity> extends DefaultTableModel {

    Map<String, AbstractTableEntity> map = new HashMap<>();
    public JavaBeanTableModel(Class<T> c) {
            initHeader(c);
    }


    private void initHeader(Class<? extends AbstractTableEntity> c) {
        Field[] fields = c.getDeclaredFields();
        Vector<String> vector = new Vector<>();
        long count = Arrays.stream(fields).filter(field -> {
            TableProperty annotation = field.getAnnotation(TableProperty.class);
            if (Objects.isNull(annotation)) return false;
            return !annotation.ignore();
        }).count();
        vector.setSize((int) count);
        for (Field field : fields) {
            TableProperty tableProperty = field.getAnnotation(TableProperty.class);
            if (Objects.isNull(tableProperty)) continue;
            if (tableProperty.ignore()) continue;
            vector.set(tableProperty.index(), tableProperty.name());
        }
        setColumnIdentifiers(vector);
    }

    public void insertEntityRow(T t) {

        Vector<Object> objects = new Vector<>();
        Field[] fields = t.getClass().getDeclaredFields();
        long count = Arrays.stream(fields).filter(field -> {
            TableProperty annotation = field.getAnnotation(TableProperty.class);
            if (Objects.isNull(annotation)) return false;
            return !annotation.ignore();
        }).count();
        objects.setSize((int) count);
        for (Field field : fields) {
            TableProperty tableProperty = field.getAnnotation(TableProperty.class);
            if (Objects.isNull(tableProperty)) continue;
            if (tableProperty.ignore()) continue;
            field.setAccessible(true);
            try {
                Object o = field.get(t);
                objects.set(tableProperty.index(), o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        AbstractTableEntity entity = map.get(t.getIdentity());
        if (Objects.isNull(entity)) {
            addRow(objects);
        }
        for (int i = 0; i < getRowCount(); i++) {
            Object valueAt = getValueAt(i, findColumn(t.getIdentityColumnName()));
            if (valueAt.equals(t.getIdentity())) {
                dataVector.set(i, objects);
                map.put(t.getIdentity(), t);
                fireTableRowsUpdated(0, getRowCount() - 1);
                return;
            }
        }
    }

    @Override
    public void removeRow(int row) {
        super.removeRow(row);
    }

    public Vector getColumnIdentifiers() {
        return columnIdentifiers;
    }
}
