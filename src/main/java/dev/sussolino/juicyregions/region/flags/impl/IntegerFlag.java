package dev.sussolino.juicyregions.region.flags.impl;

import dev.sussolino.juicyregions.region.flags.api.NumberFlag;
import dev.sussolino.juicyregions.region.flags.api.NumberFlag;
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
        return o instanceof Integer i ? i : o instanceof Number n ? n.intValue() : o instanceof String s ? Integer.parseInt(s) : 0;
    }

    public Object marshal(Integer t) {
        return t;
    }
}
