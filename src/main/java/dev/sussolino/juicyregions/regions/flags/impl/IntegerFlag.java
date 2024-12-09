package dev.sussolino.juicyregions.regions.flags.impl;

import dev.sussolino.juicyregions.regions.flags.api.NumberFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class IntegerFlag extends NumberFlag<Integer> {
    public IntegerFlag(String name,@NotNull Integer defaultValue) {
        super(name, defaultValue);
    }

    public IntegerFlag(String name) {
        super(name, 0);
    }

    @Nullable
    public Integer unmarshal(Object o) {
        return o instanceof Integer i ? i : o instanceof Number n ? n.intValue() : o instanceof String s ? Integer.parseInt(s) : null;
    }

    public Object marshal(Integer t) {
        return t;
    }
}
