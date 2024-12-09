package dev.sussolino.juicyregions.regions.event.handlers.base;

import dev.sussolino.juicyregions.regions.Region;
import dev.sussolino.juicyregions.regions.event.handlers.RegionEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@Setter
public class RegionMoveEvent extends RegionEvent {

    private final Player player;
    private Location from;
    private Location to;

    public RegionMoveEvent(Region region, Player player, Location from, Location to) {
        super(region);
        this.player = player;
        this.from = from;
        this.to = to;
    }
}