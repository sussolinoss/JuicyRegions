package dev.sussolino.juicyregions.regions.event.handlers.base;

import dev.sussolino.juicyregions.regions.Region;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RegionEnterEvent extends RegionMoveEvent {

    public RegionEnterEvent(Region region, Player player, Location from, Location to) {
        super(region,player,from,to);
    }
}