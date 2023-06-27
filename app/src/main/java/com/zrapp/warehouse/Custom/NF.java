package com.zrapp.warehouse.Custom;

import java.text.NumberFormat;
import java.util.Locale;

public class NF {
    static Locale locale = new Locale("vi", "VN");
    static NumberFormat nf = NumberFormat.getInstance(locale);

    public static String format(long number) {
        return nf.format(number) +" đ";
    }
}
