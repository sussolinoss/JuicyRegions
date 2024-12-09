package dev.sussolino.juicyregions.spawn;


import dev.sussolino.juicyregions.config.ConfigUtils;
import dev.sussolino.juicyregions.regions.Region;
import dev.sussolino.juicyregions.regions.flags.impl.LocationFlag;
import dev.sussolino.juicyregions.system.utils.ColorUtils;
import dev.sussolino.juicyregions.system.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Spawn extends LocationFlag {


    public Spawn() {
        super("spawn");
    }

    public void teleport(Player p, Region region) {
        Location location = getFlagStatus(region);
        if (location == null) {
            p.sendMessage(ColorUtils.color(ConfigUtils.SPAWN_MESSAGES_ERROR.getErrorString()));
            return;
        }
        p.teleport(location);
        MessageUtils.sendMessage(p, ConfigUtils.SPAWN_MESSAGES_TELEPORT.getString(), false);
    }
}
