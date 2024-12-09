package dev.sussolino.juicyregions.commands;

import com.github.retrooper.packetevents.util.Vector3i;
import dev.sussolino.juicyapi.color.ColorUtils;
import dev.sussolino.juicyapi.command.ICommand;
import dev.sussolino.juicyregions.Juicy;
import dev.sussolino.juicyregions.region.Region;
import dev.sussolino.juicyregions.region.flags.FlagRegistry;
import dev.sussolino.juicyregions.region.flags.api.Flag;
import dev.sussolino.juicyregions.yml.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegionCommand extends ICommand {

    private final Map<Integer, List<Vector3i>> positions = new ConcurrentHashMap<>();

    public RegionCommand(JavaPlugin manager) {
        super(manager);
    }

    @Override
    public void command(Player p, String[] args) {
        if (!p.hasPermission("regions.admin")) return;

        final int length = args.length;
        final int entityId = p.getEntityId();

        if (length < 2 || length > 4) return;

        final String command = args[0];
        final String regionName = args[1];

        final Region previous = Region.getRegion(regionName);

        final boolean exist = Juicy.REGIONS.exist(regionName);
        final boolean no_positions = !this.positions.containsKey(entityId) || this.positions.get(entityId).size() != 2;

        if (!exist && !(command.equals("create") || command.equals("set-position"))) return;

        switch (length) {
            case 2 -> {
                switch (command) {
                    case "create" -> {
                        if (exist) {
                            p.sendMessage(Settings.ALREADY__EXIST.getAsString());
                            return;
                        }

                        if (no_positions) {
                            p.sendMessage(Settings.NO__POSITIONS.getAsString());
                            return;
                        }

                        final Vector3i min = this.positions.get(entityId).get(0);
                        final Vector3i max = this.positions.get(entityId).get(1);

                        new Region(regionName, p.getWorld().getName(), min, max).save();

                        p.sendMessage(Settings.COMMAND_CREATE.getAsString("%region%", regionName));
                    }
                    case "delete" -> {
                        previous.delete();

                        p.sendMessage(Settings.COMMAND_DELETE.getAsString());
                    }
                    case "redefine" -> {
                        if (no_positions) {
                            p.sendMessage(Settings.NO__POSITIONS.getAsString());
                            return;
                        }

                        final Vector3i min = this.positions.get(entityId).get(0);
                        final Vector3i max = this.positions.get(entityId).get(1);

                        previous.redefine(min, max, p.getWorld().getName());

                        p.sendMessage(Settings.COMMAND_REDEFINE.getAsString());
                    }
                    case "set-position" -> {
                        int type;
                        try {
                            type = Integer.parseInt(regionName);
                        } catch (NumberFormatException e) {
                            p.sendMessage(Settings.INVALID__NUMBER.getAsString());
                            return;
                        }
                        if (type < 1 || type > 3) {
                            p.sendMessage(Settings.INVALID__NUMBER.getAsString());
                            return;
                        }

                        final List<Vector3i> vectors = this.positions.getOrDefault(entityId, new ArrayList<>());

                        final Location l = p.getLocation();

                        final int blockX = l.getBlockX();
                        final int blockY = l.getBlockY();
                        final int blockZ = l.getBlockZ();

                        vectors.add(type - 1, new Vector3i(blockX, blockY, blockZ));

                        this.positions.put(entityId, vectors);

                        p.sendMessage(Settings.COMMAND_POSITION.getAsString("%type%", String.valueOf(type)));
                    }
                }
            }
            case 4 -> {
                switch (command) {
                    case "set-flag" -> {
                        final Flag<?> flag = FlagRegistry.getFlag(args[2]);

                        if (flag == null) {
                            p.sendMessage(Settings.COMMAND_FLAG_NULL.getAsString());
                            return;
                        }

                        previous.setFlagStatusString(flag, args[3]);

                        final String message = Settings.COMMAND_FLAG_SET.getAsString()
                                .replace("%flag%", flag.getName())
                                .replace("%value%", args[3])
                                .replace("%region%", regionName);

                        p.sendMessage(ColorUtils.color(message));
                    }
                    case "add-exempt" -> {
                        final String playerName = args[2];
                        final String flagName = args[3];

                        final String message = Settings.COMMAND_EXEMPT.getAsString()
                                .replace("%flag%", flagName)
                                .replace("%player%", playerName)
                                .replace("%region%", regionName);

                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + playerName + " permission set regions." + regionName + "." + flagName);

                        p.sendMessage(ColorUtils.color(message));
                    }
                }
            }
        }
    }

    /*
     * /region create [regionName]
     * /region delete [regionName]
     * /region redefine [regionName]  (!) Deve aver settato le positions (!)
     *
     * /region set-position 1/2
     * /region set-flag [regionName] [flag] [value]
     * /region add-exempt [regionName] (playerName) [flag]
     */

    @Override
    public List<String> tab(Player p, String[] args) {
        if (args.length < 1 || args.length > 4) return List.of();

        String command = args[0];

        return switch (args.length) {
            case 1 -> List.of("create", "delete", "redefine", "set-position", "set-flag", "add-exempt");
            case 2 -> {
                final boolean position = command.equals("set-position");
                final boolean exempt = command.equals("add-exempt");

                if (position) yield List.of("1", "2");
                else if (exempt) yield Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
                else yield Region.getRegionNames().stream().toList();
            }
            case 3 -> {
                if (command.equals("set-flag") || command.equals("add-exempt")) {
                    yield FlagRegistry.flags.stream().map(Flag::getName).toList();
                }
                yield List.of();
            }
            default -> List.of();
        };
    }
}
