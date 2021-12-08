package com.xbb.util;

import com.intellij.ui.NumberDocument;

import javax.swing.*;

/**
 * 限制输入框只能输入数字
 */
public class NumberField extends JTextField {

    public NumberField() {
        setDocument(new NumberDocument());
    }
}
