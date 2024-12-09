package dev.sussolino.juicyregions.region.flags.impl;

import dev.sussolino.juicyregions.region.Region;
import dev.sussolino.juicyregions.region.flags.api.Flag;
import dev.sussolino.juicyregions.yml.regions.RegionsYml;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListFlag extends Flag<List<String>> {

    public ListFlag(String name, @NotNull List<String> defaultValue) {
        super(name, defaultValue);
    }

    public ListFlag(Region.FlagType flagType) {
        super(flagType.getName(), new ArrayList<>());
    }

    @Override
    public @Nullable List<String> unmarshal(Object o) {
        if (o instanceof List<?> list) {
            return list.stream()
                    .filter(item -> item instanceof String)
                    .map(item -> (String) item)
                    .collect(Collectors.toList());
        }
        return null;
    }

    public boolean contains(final String value) {
       return this.defaultValue.contains(value);
    }

    public void remove(final String value) {
        this.defaultValue.remove(value);
    }

    public void add(final String value) {
        this.defaultValue.add(value);
    }

    @Override
    public Object marshal(List<String> strings) {
        return strings;
    }
}
