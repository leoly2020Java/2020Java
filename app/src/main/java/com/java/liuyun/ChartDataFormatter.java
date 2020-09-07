package com.java.liuyun;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;

public class ChartDataFormatter extends ValueFormatter {

    public List<String> names;

    public ChartDataFormatter(List<String> names) {
        this.names = names;
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        int pos = (int)(value - 0.9);
        if (0 <= pos && pos < names.size()) return names.get(pos);
        return "其它";
    }
}
