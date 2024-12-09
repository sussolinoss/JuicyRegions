package dev.sussolino.juicyregions.yml.regions;

import dev.sussolino.juicyapi.file.BukkitFile;
import dev.sussolino.juicyregions.region.Region;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class RegionsYml extends BukkitFile {

    public RegionsYml(JavaPlugin plugin) {
        super("regions", plugin);
    }

    public boolean exist(final String name) {
       return Region.getRegionNames().contains(name);
    }

    public void remove(final String name) {
        List<String> regions = new ArrayList<>(this.getRegions());
        regions.remove(name);

        getConfig().set("regions", regions);
        getConfig().set(name, null);

        reload();
    }

    public void add(final String name) {
        List<String> regions = new ArrayList<>(this.getRegions());
        regions.add(name);

        getConfig().set("regions", regions);
        reload();
    }

    public List<String> getRegions() {
        return getConfig().getStringList("regions");
    }

    public boolean getRegion() {
        return false;
    }
}
