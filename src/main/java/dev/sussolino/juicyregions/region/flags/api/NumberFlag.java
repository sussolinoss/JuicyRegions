package dev.sussolino.juicyregions.region.flags.api;

import org.jetbrains.annotations.NotNull;

public abstract class NumberFlag<T extends Number> extends Flag<T> {

    protected NumberFlag(String name, @NotNull T defaultValue) {
        super(name, defaultValue);
    }

}
