package dev.sussolino.juicyregions.regions.command;

import com.github.retrooper.packetevents.util.Vector3d;
import dev.sussolino.juicyregions.config.ConfigUtils;
import dev.sussolino.juicyregions.regions.Region;
import dev.sussolino.juicyregions.regions.flags.FlagRegistry;
import dev.sussolino.juicyregions.regions.flags.api.Flag;
import dev.sussolino.juicyregions.regions.flags.impl.BooleanFlag;
import dev.sussolino.juicyregions.regions.flags.impl.LocationFlag;
import dev.sussolino.juicyregions.spawn.Spawn;
import dev.sussolino.juicyregions.system.reflection.Sussolino;
import dev.sussolino.juicyregions.system.utils.Cmd;
import dev.sussolino.juicyregions.system.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static dev.sussolino.juicyregions.regions.Region.getRegion;
import static dev.sussolino.juicyregions.regions.Region.getRegionNames;

@Sussolino
public class RegionCommand extends Cmd implements TabCompleter {

    public static final HashMap<Player, HashMap<Integer, Vector3d>> PRE_REGION_POSITIONS = new HashMap<>();

    @Override
    protected void command(Player p, String[] args) {
        switch (args.length) {
            case 1 -> {
                switch (args[0]) {
                    case "pos1", "pos2" -> {
                        int index = args[0].equalsIgnoreCase("pos1") ? 0 : 1;

                        Vector3d vec = new Vector3d(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());

                        PRE_REGION_POSITIONS.computeIfAbsent(p, k -> new HashMap<>()).put(index, vec);

                        MessageUtils.sendMessage(p, "Setupped (Position = %s)".formatted(index), false);
                    }
                }
            }
            case 2 -> {
                String regionName = args[1];
                switch (args[0]) {
                    case "create" -> {
                        HashMap<Integer, Vector3d> positionsMap = PRE_REGION_POSITIONS.get(p);
                        if (positionsMap == null || positionsMap.size() != 2) {
                            MessageUtils.sendMessage(p, "Sei slowed + reverb?", true);
                            return;
                        }
                        Vector3d SET_0 = positionsMap.get(0);
                        Vector3d SET_1 = positionsMap.get(1);

                        Region region = new Region(regionName, p.getWorld().getName(),
                                Math.min(SET_0.getX(), SET_1.getX()), Math.max(SET_0.getX(), SET_1.getX()),
                                Math.min(SET_0.getY(), SET_1.getY()), Math.max(SET_0.getY(), SET_1.getY()),
                                Math.min(SET_0.getZ(), SET_1.getZ()), Math.max(SET_0.getZ(), SET_1.getZ()));
                        region.create(p);

                        PRE_REGION_POSITIONS.remove(p);
                    }
                    case "delete" -> {
                        Region RG = getRegion(regionName);
                        if (RG == null) {
                            MessageUtils.sendMessage(p, "Miu Miu, uccido la tua Famiglia", true);
                            return;
                        }
                        RG.delete();
                        MessageUtils.sendMessage(p, "RG = %s Sgozzata!".formatted(regionName), true);
                    }
                    case "set-spawn" -> {
                        Region RG = getRegion(regionName);
                        if (RG == null) {
                            MessageUtils.sendMessage(p, "Miu Miu, uccido la tua Famiglia", true);
                            return;
                        }
                        Spawn spawn = Region.FlagType.SPAWN;
                        spawn.setFlagStatus(RG, p.getLocation());
                        MessageUtils.sendMessage(p, ConfigUtils.SPAWN_MESSAGES_SET.getString(), false);
                    }
                    case "spawn" -> {
                        Region RG = getRegion(regionName);
                        if (RG == null) {
                            MessageUtils.sendMessage(p, "Miu Miu, uccido la tua Famiglia", true);
                            return;
                        }
                        Spawn spawn = Region.FlagType.SPAWN;
                        spawn.teleport(p, RG);
                    }
                }
            }
            case 4 -> {
                String regionName = args[1];

                Flag<?> flag = FlagRegistry.getFlag(p, args[2]);
                if (flag == null) return;


                Region RG = getRegion(regionName);

                if (RG == null) {
                    MessageUtils.sendMessage(p, "Miu Miu, sei down te, devi creare la region prima che sgozzi a te", true);
                    return;
                }

                switch (args[0]) {
                    case "flag" -> {
                        if (flag instanceof LocationFlag loc) {
                            loc.setFlagStatus(RG, p.getLocation());
                            MessageUtils.sendMessage(p, "Region = %s  Flag = %s  Action = %s".formatted(regionName, flag.getName(), p.getLocation()), true)
                            ;
                            return;
                        }
                        flag.setFromString(RG, args[3]);
                        MessageUtils.sendMessage(p, "Region = %s  Flag = %s  Action = %s".formatted(regionName, flag.getName(), flag.getFromString(args[3])), true);
                    }
                    case "add-command" -> {
                        String commandStr = String.join(" ", Arrays.copyOfRange(args, 3, args.length));

                        RG.addCommand(flag, commandStr);

                        MessageUtils.sendMessage(p, "ADD-COMMAND -> '%s' FLAG= '%s' RG= '%s'".formatted(commandStr, flag.getName(), regionName), true);
                    }
                }
            }

        }


        //region exempt region flag player
      /*
        else if (args.length == 5) {

            String regionName = args[1];
            Region.FlagType flag = Region.FlagType.valueOf(args[3]);
            Player target = Bukkit.getPlayerExact(args[4]);

            if (target == null) {
                p.sendMessage(ColorUtils.color(ConfigUtils.PLAYER_OFFLINE.getErrorString()));
                return;
            }

            if (args[0].equals("exempt")) {
                if (regionName.equals("*")) {
                    //Region.setFullExempted(flag, target);
                    MessageUtils.sendMessage(p, "EZ (*)", false);
                }
                else {
                    Region RG = Region.getRegion(regionName);

                    assert RG != null;

                    //RG.setExempted(flag, target);

                    MessageUtils.sendMessage(p, "EZ (%s)".formatted(regionName), false);
                }
            }
        }
       */
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] arg) {
        List<String> COMMANDS = new ArrayList<>();

        if (arg.length == 1) {
            COMMANDS.addAll(List.of("pos1", "pos2", "create", "delete", "flag", "exempt", "set-spawn", "spawn", "add-command"));
        } else if (arg[0].equals("delete") || arg[0].equals("set-spawn") || arg[0].equals("spawn")) {
            if (arg.length == 2) COMMANDS.addAll(getRegionNames());
        } else if (arg[0].equals("flag") || arg[0].equals("exempt")) {
            if (arg.length == 2) COMMANDS.addAll(getRegionNames());
            else if (arg.length == 3)
                COMMANDS.addAll(FlagRegistry.getNames());
            else if (arg.length == 4) {
                Flag<?> flag = FlagRegistry.getFlag(arg[3]);
                if (flag != null) {
                    if (flag instanceof BooleanFlag)
                        COMMANDS.addAll(List.of("true", "false"));
                    else {
                        COMMANDS.add(flag.getDefaultValue().toString());
                    }
                }
            }
        }

        return COMMANDS;
    }
}
