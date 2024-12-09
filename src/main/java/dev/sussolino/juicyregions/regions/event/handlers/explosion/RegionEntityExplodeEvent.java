package dev.sussolino.juicyregions.regions.event.handlers.explosion;

import dev.sussolino.juicyregions.regions.Region;
import dev.sussolino.juicyregions.regions.event.handlers.RegionEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

@Getter
@Setter
public class RegionEntityExplodeEvent extends RegionEvent {

    private final Entity entity;
    private final EntityType type;

    private float radius;
    private boolean fire;


    public RegionEntityExplodeEvent(Region region, Entity entity, EntityType type, float radius, boolean fire) {
        super(region);
        this.entity = entity;
        this.type = type;
        this.radius = radius;
        this.fire = fire;
    }
}