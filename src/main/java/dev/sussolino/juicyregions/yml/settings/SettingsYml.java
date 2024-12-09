package dev.sussolino.juicyregions.yml.settings;

import dev.sussolino.juicyapi.file.BukkitFile;
import org.bukkit.plugin.java.JavaPlugin;

public class SettingsYml extends BukkitFile {

    public SettingsYml(JavaPlugin plugin) {
        super("settings", plugin);
    }
}
