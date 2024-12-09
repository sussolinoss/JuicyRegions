package dev.sussolino.juicyregions.regions.event.handlers.entity;

import dev.sussolino.juicyregions.regions.Region;
import dev.sussolino.juicyregions.regions.event.handlers.RegionEvent;
import lombok.Getter;
import org.bukkit.entity.Entity;

@Getter
public class RegionEntitySpawnEvent extends RegionEvent {

    private final Entity entity;

    public RegionEntitySpawnEvent(Region region, Entity entity) {
        super(region);
        this.entity = entity;
    }
}