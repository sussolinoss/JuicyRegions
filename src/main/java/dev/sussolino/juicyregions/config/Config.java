package dev.sussolino.juicyregions.config;


import dev.sussolino.juicyregions.SuckMyHugeCock;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

public class Config implements SuckMyHugeCock {

    @Getter
    private static FileConfiguration config;

    public static void reload() {
        INSTANCE.saveConfig();
        INSTANCE.reloadConfig();
    }

    public static void init() {
        INSTANCE.saveDefaultConfig();
        reload();
        config = INSTANCE.getConfig();
    }

    public static void save() {
        INSTANCE.saveConfig();
    }
}
