package dev.sussolino.juicyregions.region;

import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSettings;
import dev.sussolino.juicyregions.Juicy;
import dev.sussolino.juicyregions.region.flags.FlagRegistry;
import dev.sussolino.juicyregions.region.flags.api.Flag;
import dev.sussolino.juicyregions.region.flags.impl.BooleanFlag;
import dev.sussolino.juicyregions.region.flags.impl.ListFlag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class Region {

    private String world;
    private final String name;
    private int minX, maxX, minY, maxY, minZ, maxZ;

    public Region(String name, String world, Vector3i min, Vector3i max) {
        this.name = name;
        this.world = world;
        this.minX = Math.min(min.getX(), max.getX());
        this.minY = Math.min(min.getY(), max.getY());
        this.minZ = Math.min(min.getZ(), max.getZ());

        this.maxX = Math.max(max.getX(), min.getX());
        this.maxY = Math.max(max.getY(), min.getY());
        this.maxZ = Math.max(max.getZ(), min.getZ());
    }

    /**
     * REGION ACTIONS
     */

    public void redefine(Vector3i min, Vector3i max, String world) {
        this.world = world;
        this.minX = Math.min(min.getX(), max.getX());
        this.minY = Math.min(min.getY(), max.getY());
        this.minZ = Math.min(min.getZ(), max.getZ());

        this.maxX = Math.max(max.getX(), min.getX());
        this.maxY = Math.max(max.getY(), min.getY());
        this.maxZ = Math.max(max.getZ(), min.getZ());
        save();
    }

    public void redefine(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, String world) {
        this.world = world;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
        save();
    }

    public void delete() {
        FileConfiguration config = Juicy.REGIONS.getConfig();

        config.set(name, null);

        Juicy.REGIONS.reload();
    }

    public void save() {
        FileConfiguration config = Juicy.REGIONS.getConfig();

        config.set(name + ".world", world);
        config.set(name + ".minX", minX);
        config.set(name + ".minY", minY);
        config.set(name + ".minZ", minZ);

        config.set(name + ".maxX", maxX);
        config.set(name + ".maxY", maxY);
        config.set(name + ".maxZ", maxZ);

        Juicy.REGIONS.reload();

        FlagRegistry.flags.forEach(flag -> flag.resetFlagStatus(this));
    }

    /**
     * Commands
     */

    public List<String> getCommands(Flag<?> flag) {
        String path = name + "." + flag.path().replace(".", "") + ".commands";
        return Juicy.REGIONS.getConfig().getStringList(path);
    }

    public void addCommand(Flag<?> flag, String command) {
        String path = name + "." + flag.path().replace(".", "") + ".commands";
        List<String> commands = getCommands(flag);

        commands.add(command);

        Juicy.REGIONS.getConfig().set(path, command);
        Juicy.REGIONS.reload();
    }

    public void executeCommand(Flag<?> flag) {
        final List<String> commands = getCommands(flag);

        if (commands.isEmpty()) return;

        commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }

    /**
     * MIU MIU
     */

    public static List<Region> getRegions() {
        return getRegionNames().stream()
                .map(name -> {
                    FileConfiguration config = Juicy.REGIONS.getConfig();

                    String world = config.getString(name + ".world");
                    int minX = config.getInt(name + ".minX");
                    int maxX = config.getInt(name + ".maxX");
                    int minY = config.getInt(name + ".minY");
                    int maxY = config.getInt(name + ".maxY");
                    int minZ = config.getInt(name + ".minZ");
                    int maxZ = config.getInt(name + ".maxZ");

                    return new Region(name, world, new Vector3i(minX, minY, minZ), new Vector3i(maxX, maxY, maxZ));
                })
                .toList();
    }

    public static Set<String> getRegionNames() {
        return Juicy.REGIONS.getConfig().getKeys(false);
    }

    public static Region getRegion(String name) {

        if (!Juicy.REGIONS.exist(name)) return null;
        
        FileConfiguration config = Juicy.REGIONS.getConfig();

        String world = config.getString(name + ".world");
        int minX = config.getInt(name + ".minX");
        int maxX = config.getInt(name + ".maxX");
        int minY = config.getInt(name + ".minY");
        int maxY = config.getInt(name + ".maxY");
        int minZ = config.getInt(name + ".minZ");
        int maxZ = config.getInt(name + ".maxZ");

        return new Region(name, world, new Vector3i(minX, minY, minZ), new Vector3i(maxX, maxY, maxZ));
    }

    /**
     * LOCATION CHECKS
     */


    public static boolean inSomeRegion(Player p) {
        for (String regionName : getRegionNames()) {
            Region region = getRegion(regionName);
            return region != null && region.inRegion(p.getLocation());
        }
        return false;
    }

    public static boolean inSomeRegion(Location loc) {
        for (String regionName : getRegionNames()) {
            Region region = getRegion(regionName);

            return region != null && region.inRegion(loc);
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
        return inRegion(loc.getWorld().getName(), new Vector3d(loc.getX(), loc.getY(), loc.getZ()));
    }

    public boolean inRegion(String worldName, Vector3d position) {
        if (this.world.equals(worldName)) {
            double minX = position.getX() - 0.3;
            double maxX = position.getX() + 0.3;
            double minZ = position.getZ() - 0.3;
            double maxZ = position.getZ() + 0.3;

            double minY = position.getY();
            double maxY = position.getY() + 1.8;

            return maxX >= this.minX && minX <= this.maxX &&
                    maxY >= this.minY && minY <= this.maxY &&
                    maxZ >= this.minZ && minZ <= this.maxZ;
        }
        return false;
    }


    public boolean inRegion(Vector3d position) {
        double x = position.getX();
        double y = position.getY();
        double z = position.getZ();

        return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
    }

    public boolean isFlagActive(FlagType flagType) {
        return flagType.getAsBoolean().getFlagStatus(this);
    }


    /**
     * FLAGS CHECKS
     */

    @Getter
    @AllArgsConstructor
    public enum FlagType {
        MOB_SPAWN(false),

        BUILD(true),
        BREAK(true),
        INTERACT(true),

        DROWNIN(true),
        EXPLOSION(false),
        SATURATION(true),
        PVP(true),

        FALL(true),
        FIRE(true),

        MOVE(true),
        ENTER(true),
        EXIT(true),
        SIGN(false),

        DROP(true),

        COMMANDS(true);

        private final boolean allowed;

        private static final ConcurrentHashMap<FlagType, BooleanFlag> bool_flags = new ConcurrentHashMap<>();
        private static final ConcurrentHashMap<FlagType, ListFlag> list_flags = new ConcurrentHashMap<>();

        public BooleanFlag getAsBoolean() {
            return bool_flags.computeIfAbsent(this, flagType -> {
                BooleanFlag flag = new BooleanFlag(flagType.getName(), this.allowed);
                Boolean config_value = Juicy.REGIONS.getConfig().getBoolean(this.getName() + ".flags." + flagType.getName(), flagType.allowed);

                flag.setDefaultValue(config_value);

                return flag;
            });
        }

        public ListFlag getList() {
            return list_flags.computeIfAbsent(this, flagType -> {
                ListFlag flag = new ListFlag(flagType);
                String path = this.getName() + ".flags." + flagType.getName();
                List<String> config_value = Juicy.REGIONS.getConfig().getStringList(path);

                flag.setDefaultValue(config_value.isEmpty() ? new ArrayList<>() : config_value);

                return flag;
            });
        }

        public void register(final String type) {
            switch (type) {
                case "boolean" -> bool_flags.computeIfAbsent(this, BooleanFlag::new);
                case "list" -> list_flags.computeIfAbsent(this, ListFlag::new);
            }
        }

        public static void init() {
            final List<ListFlag> lists = List.of(COMMANDS.getList());
            final List<BooleanFlag> booleans = List.of(
                    PVP.getAsBoolean(),
                    DROP.getAsBoolean(),
                    SIGN.getAsBoolean(),
                    DROWNIN.getAsBoolean(),
                    INTERACT.getAsBoolean(),
                    SATURATION.getAsBoolean(),

                    EXPLOSION.getAsBoolean(),
                    MOB_SPAWN.getAsBoolean(),

                    MOVE.getAsBoolean(),
                    EXIT.getAsBoolean(),
                    ENTER.getAsBoolean(),

                    BUILD.getAsBoolean(),
                    BREAK.getAsBoolean(),

                    FIRE.getAsBoolean(),
                    FALL.getAsBoolean());

            lists.forEach(FlagRegistry::add);
            booleans.forEach(FlagRegistry::add);
        }

        public @NotNull String getName() {
            return this.name().toLowerCase().replace('_', '-');
        }
    }

    /**
     * FLAG
     */

    public <T> T getFlagStatus(Flag<T> flag) {
        return flag.getFlagStatus(this);
    }

    public <T> void setFlagStatus(Flag<T> flag, T action) {
        flag.setFlagStatus(this, action);
    }
    public <T> void setFlagStatusString(Flag<T> flag, String action) {
        if (flag instanceof ListFlag list) {
            list.add(action);
            Juicy.REGIONS.getConfig().set(this.getName() + ".flags." + list.getName(), list.getDefaultValue());
            Juicy.REGIONS.reload();
        }
        else flag.setFromString(this, action);
    }

    public <T> void resetFlagStatus(Flag<T> flag) {
        flag.resetFlagStatus(this);
    }
}
