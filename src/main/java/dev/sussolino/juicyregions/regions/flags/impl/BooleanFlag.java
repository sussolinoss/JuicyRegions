package dev.sussolino.juicyregions.regions.flags.impl;

import dev.sussolino.juicyregions.regions.Region;
import dev.sussolino.juicyregions.regions.flags.api.Flag;
import org.jetbrains.annotations.NotNull;

public class BooleanFlag extends Flag<Boolean> {

    public BooleanFlag(Region.FlagType type) {
        super(type.name(), type.isAllowed());
    }


    public BooleanFlag(String name, boolean defaultValue) {
        super(name, defaultValue);
    }

    public BooleanFlag(String name, @NotNull Boolean defaultValue) {
        super(name, defaultValue);
    }

    public BooleanFlag(String name) {
        super(name, false);
    }


    public Boolean unmarshal(Object o) {
        return o instanceof Boolean b ? b : o instanceof String s ? Boolean.parseBoolean(s) : null;
    }

    public Object marshal(Boolean t) {
        return t;
    }

}
