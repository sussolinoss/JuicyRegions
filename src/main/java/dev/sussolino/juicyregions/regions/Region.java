package dev.sussolino.juicyregions.regions;

import dev.sussolino.juicyregions.SuckMyHugeCock;
import dev.sussolino.juicyregions.config.file.RegionYaml;
import dev.sussolino.juicyregions.regions.event.handlers.base.RegionCreateEvent;
import dev.sussolino.juicyregions.regions.flags.FlagRegistry;
import dev.sussolino.juicyregions.regions.flags.api.Flag;
import dev.sussolino.juicyregions.regions.flags.impl.BooleanFlag;
import dev.sussolino.juicyregions.spawn.Spawn;
import dev.sussolino.juicyregions.system.utils.MessageUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@SuppressWarnings("all")
public class Region implements SuckMyHugeCock {

    private final String name;
    private String world;
    private Double minX, maxX, minY, maxY, minZ, maxZ;

    public Region(String name, String world, Double minX, Double maxX, Double minY, Double maxY, Double minZ, Double maxZ) {
        this.name = name;
        this.world = world;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    /**
     * REGION ACTIONS
     */

    public void create(Player p) {
        if (minX != null && maxX != null && minY != null && maxY != null && minZ != null && maxZ != null) {
            if (RegionYaml.getConfig().get(name) != null) {
                MessageUtils.sendMessage(p, "Già esiste bro...", true);
                return;
            }
            RegionCreateEvent event = new RegionCreateEvent(this, p);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            event.getRegion().save();

            MessageUtils.sendMessage(p, "Region = " + this.name, false);
        } else MessageUtils.sendMessage(p, "Bro mi sa che manca qualcosa...", true);
    }

    private void redefine(Double minX, Double minY, Double minZ, Double maxX, Double maxY, Double maxZ, String world, Player player) {
        this.world = world;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
        save();
        MessageUtils.sendMessage(player, "Region Redefine = " + this.name, false);
    }

    public void delete() {
        RegionYaml.getConfig().set(name, null);
        RegionYaml.reload();
    }

    public void save() {
        RegionYaml.getConfig().set(name + ".world", world);
        RegionYaml.getConfig().set(name + ".minX", minX);
        RegionYaml.getConfig().set(name + ".maxX", maxX);

        RegionYaml.getConfig().set(name + ".minY", minY);
        RegionYaml.getConfig().set(name + ".maxY", maxY);

        RegionYaml.getConfig().set(name + ".minZ", minZ);
        RegionYaml.getConfig().set(name + ".maxZ", maxZ);

        FlagRegistry.flags.forEach(flag -> {
            flag.resetFlagStatus(this);
        });
        RegionYaml.reload();
    }

    /**
     * Commands
     */

    public List<String> getCommands(Flag<?> flag) {
        String path = name + "." + flag.path().replace(".", "") + ".commands";
        return RegionYaml.getConfig().getStringList(path);
    }

    public void addCommand(Flag<?> flag, String command) {
        String path = name + "." + flag.path().replace(".", "") + ".commands";
        List<String> commands = getCommands(flag);

        commands.add(command);

        RegionYaml.getConfig().set(path, command);
        RegionYaml.reload();
    }

    public void executeCommand(Flag<?> flag) {
        final List<String> commands = getCommands(flag);

        if (commands.isEmpty() || commands == null) return;

        commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }

    /**
     * MIU MIU
     */

    public static List<Region> getRegions() {
        return getRegionNames().stream().map(Region::getRegion).toList();
    }

    public static Set<String> getRegionNames() {
        return RegionYaml.getConfig().getKeys(false);
    }

    public static Region getRegion(String name) {

        if (RegionYaml.getConfig().get(name) == null) return null;

        String world = RegionYaml.getConfig().getString(name + ".world");
        Double minX = RegionYaml.getConfig().getDouble(name + ".minX");
        Double maxX = RegionYaml.getConfig().getDouble(name + ".maxX");
        Double minY = RegionYaml.getConfig().getDouble(name + ".minY");
        Double maxY = RegionYaml.getConfig().getDouble(name + ".maxY");
        Double minZ = RegionYaml.getConfig().getDouble(name + ".minZ");
        Double maxZ = RegionYaml.getConfig().getDouble(name + ".maxZ");

        return new Region(name, world, minX, maxX, minY, maxY, minZ, maxZ);
    }

    /**
     * LOCATION CHECKS
     */


    public static boolean inSomeRegion(Player p) {
        for (String regionName : getRegionNames()) {
            Region region = getRegion(regionName);
            return region.inRegion(p.getLocation());
        }
        return false;
    }

    public static boolean inSomeRegion(Location loc) {
        for (String regionName : getRegionNames()) {
            Region region = getRegion(regionName);

            return region.inRegion(loc);
        }
        return false;
    }

    public boolean isExplosionInRange(Location explosion, float radius) {
        for (double x = explosion.getX() - radius; x <= explosion.getX() + radius; x++) {
            for (double y = explosion.getY() - radius; y <= explosion.getY() + radius; y++) {
                for (double z = explosion.getZ() - radius; z <= explosion.getZ() + radius; z++) {
                    if (inRegion(new Location(explosion.getWorld(), x, y, z))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Nullable
    public static Region getWherePlayerIs(Player p) {
        return getWhereEntityIs(p);
    }

    @Nullable
    public static Region getWhereEntityIs(Entity entity) {
        return getWhereIs(entity.getLocation());
    }

    @Nullable
    public static Region getWhereIs(Location loc) {
        for (Region region : getRegions()) {
            if (region.inRegion(loc)) return region;
        }
        return null;
    }

    public boolean inRegion(Location loc) {
        if (loc.getWorld().getName().equalsIgnoreCase(world)) {
            double x = loc.getX();
            double y = loc.getY();
            double z = loc.getZ();

            return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
        }
        return false;
    }

    public boolean isFlagActive(FlagType flagType) {
        return flagType.get().getFlagStatus(this);
    }


    /**
     * FLAGS CHECKS
     */

    @Getter
    @AllArgsConstructor
    public enum FlagType {
        BUILD(false),
        BLOCK_RIGHT_CLICK(true),
        BLOCK_LEFT_CLICK(true),
        INTERACTION(true),
        MOB_SPAWN(false),
        BREAK(true),
        MOVE_ON_WATER(true),
        NO_FALL(false),
        DROWNIN(true),
        EXPLOSION(false),
        LOSS_SATURATION(true),
        PVP(true),
        DAMAGE_FIRE(true),
        MOVE(true),
        ENTER(true),
        EXIT(true),
        AIR_RIGHT_CLICK(true),
        AIR_LEFT_CLICK(true);
        public static final Spawn SPAWN = new Spawn();

        private final boolean allowed;

        private final ConcurrentHashMap<FlagType, BooleanFlag> flags = new ConcurrentHashMap<>();

        public BooleanFlag get() {
            return flags.computeIfAbsent(this, BooleanFlag::new);
        }

        public String getName() {
            return name();
        }
    }

    /**
     *
     *
     *      EXEMPT
     *
     *
     */

    /*
    public static void setFullExempted(Flag<?> flag, Player p) {
        String name = flag.getName();

        PlayerUtils.addPermission(p, "juicyregions.bypass.*%s".formatted(flag.path()));
    }
    public void setExempted(Flag<?> flag, Player p) {
        String name = flag.getName();

        PlayerUtils.addPermission(p, "juicyregions.bypass.%s%s".formatted(name, flag.path()));
    }
     */


    /**
     * FLAG
     */

    public <T> T getFlagStatus(Flag<T> flag) {
        return flag.getFlagStatus(this);
    }

    public <T> void setFlagStatus(Flag<T> flag, T action) {
        flag.setFlagStatus(this, action);
    }

    public <T> void resetFlagStatus(Flag<T> flag) {
        flag.resetFlagStatus(this);
    }
}
