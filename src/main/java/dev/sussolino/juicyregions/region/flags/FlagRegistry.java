package dev.sussolino.juicyregions.region.flags;

import dev.sussolino.juicyregions.region.Region;
import dev.sussolino.juicyregions.region.flags.api.Flag;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@UtilityClass
public class FlagRegistry {

    public final LinkedHashSet<Flag<?>> flags = new LinkedHashSet<>();

    public void add(final Flag<?> flag) {
        if (flags.contains(flag)) return;
        flags.add(flag);
        //Region.getRegions().forEach(region -> region.resetFlagStatus(flag));
    }

    public void remove(final Flag<?> flag) {
        flags.remove(flag);
        Region.getRegions().forEach(region -> region.setFlagStatus(flag, null));
    }

    public void addFlags(Flag<?>... flag) {
        Arrays.asList(flag).forEach(FlagRegistry::add);
    }

    public void removeFlags(Flag<?>... flag) {
        Arrays.asList(flag).forEach(FlagRegistry::remove);
    }

    @Nullable
    public Flag<?> getFlag(String name) {
        return FlagRegistry.flags
                .stream()
                .filter(flag -> flag.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public @NotNull Flag<?> getFlag(Player p, String name) {
        Flag<?> flag = getFlag(name);
        if (flag == null) p.sendMessage("Flag " + name + " not found");
        return flag;
    }

    public List<String> getNames() {
        return FlagRegistry.flags.stream().map(Flag::getName).toList();
    }


}
