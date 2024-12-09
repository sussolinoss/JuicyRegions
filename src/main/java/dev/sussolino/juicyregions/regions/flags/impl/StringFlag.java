package dev.sussolino.juicyregions.regions.flags.impl;

import dev.sussolino.juicyregions.regions.flags.api.Flag;
import org.jetbrains.annotations.NotNull;

public class StringFlag extends Flag<String> {

    public StringFlag(String name,@NotNull String defaultValue) {
        super(name, defaultValue);
    }

    public StringFlag(String name) {
        super(name, "");
    }


    public String unmarshal(Object o) {
        return o instanceof String s ? s : null;
    }

    public Object marshal(String t) {
        return t;
    }
}
