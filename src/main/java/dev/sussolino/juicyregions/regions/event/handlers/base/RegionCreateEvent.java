package dev.sussolino.juicyregions.regions.event.handlers.base;

import dev.sussolino.juicyregions.regions.Region;
import dev.sussolino.juicyregions.regions.event.handlers.RegionEvent;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class RegionCreateEvent extends RegionEvent {

    private final Player player;

    public RegionCreateEvent(Region region, Player player) {
        super(region);
        this.player = player;
    }
}