package dev.sussolino.juicyregions.regions.flags.api;

import dev.sussolino.juicyregions.config.file.RegionYaml;
import dev.sussolino.juicyregions.regions.Region;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public abstract class Flag<T> {

    private final String name;
    @Getter(onMethod_ = {@NotNull})
    protected final T defaultValue;

    public Flag(String name, @NotNull T defaultValue) {
        this.name = name.toLowerCase().replace("_", "-");
        this.defaultValue = defaultValue;
    }

    public String path() {
        return ".flags." + getName();
    }

    public T getFlagStatus(Region region) {
        String string = RegionYaml.getConfig().getString(region.getName() + path());
        if (string == null) return defaultValue;
        return getFromString(string);
    }

    public T getFromString(String string) {
        T unmarshal = unmarshal(string);
        if (unmarshal == null) {
            System.out.println("default");
            return defaultValue;
        }
        return unmarshal;
    }

    public void resetFlagStatus(Region region) {
        setFlagStatus(region, defaultValue);
    }

    public void setFromString(Region region, String action) {
        setFlagStatus(region, getFromString(action));
    }

    public void setFlagStatus(Region region, T action) {
        Object marshal = marshal(action);
        RegionYaml.getConfig().set(region.getName() + path(), marshal != null ? marshal : marshal(defaultValue));
        RegionYaml.reload();
    }

    @Nullable
    public abstract T unmarshal(Object o);

    public abstract Object marshal(T t);

    public boolean isExempted(Region region, Player p) {
        boolean exempted = p.hasPermission("juicyregions.bypass.%s%s".formatted(region.getName(), path()));

        return exempted || isFullExempted(p);
    }

    public boolean isFullExempted(Player p) {
        return p.hasPermission("juicyregions.bypass.*%s".formatted(path()));
    }

}
