package dev.sussolino.juicyregions;

import dev.sussolino.juicyregions.config.Config;
import dev.sussolino.juicyregions.config.file.RegionYaml;
import dev.sussolino.juicyregions.regions.Region;
import dev.sussolino.juicyregions.regions.flags.FlagRegistry;
import dev.sussolino.juicyregions.regions.flags.api.Flag;
import dev.sussolino.juicyregions.system.reflection.ReflectionUtil;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class JuicyRegions extends JavaPlugin {

    @Getter
    private static JuicyRegions instance;

    @Getter
    private static LuckPerms luckPerms;

    @Override
    public void onEnable() {
        instance = this;

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) luckPerms = provider.getProvider();

        Config.init();
        RegionYaml.init();

        Arrays.stream(Region.FlagType.values())
                .map(Region.FlagType::get)
                .forEach(FlagRegistry::add);
        FlagRegistry.add(Region.FlagType.SPAWN);

        ReflectionUtil.register("dev.sussolino.juicyregions", this);
    }

    @Override
    public void onDisable() {
        instance = null;

        FlagRegistry.removeFlags(FlagRegistry.flags.toArray(new Flag[0]));
    }
}
