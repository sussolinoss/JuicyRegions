package dev.sussolino.juicyregions.system.utils;

import dev.sussolino.juicyregions.config.ConfigUtils;
import org.bukkit.entity.Player;

public class MessageUtils {

    public static void sendMessage(Player p, String message, boolean error) {
        String prefix = error ? ConfigUtils.ERROR.getString() : ConfigUtils.PREFIX.getString();
        p.sendMessage(ColorUtils.color(prefix + message));
    }

    public static void sendDebug(Player p, String m) {
        p.sendMessage("[Debug] = " + m);
    }
}
