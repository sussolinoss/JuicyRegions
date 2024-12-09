package dev.sussolino.juicyregions.region.flags.impl;

import dev.sussolino.juicyregions.region.flags.api.NumberFlag;
import org.jetbrains.annotations.NotNull;

public class DoubleFlag extends NumberFlag<Double> {
    public DoubleFlag(String name, @NotNull Double defaultValue) {
        super(name, defaultValue);
    }

    public DoubleFlag(String name) {
        super(name,0D);
    }

    public Double unmarshal(Object o) {
        return o instanceof Double d ? d : o instanceof Number n ? n.doubleValue() : o instanceof String s ? Double.parseDouble(s) : null;
    }

    public Object marshal(Double t) {
        return t;
    }
}
