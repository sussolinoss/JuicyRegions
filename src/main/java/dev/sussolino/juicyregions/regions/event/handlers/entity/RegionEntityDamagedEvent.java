package dev.sussolino.juicyregions.regions.event.handlers.entity;

import dev.sussolino.juicyregions.regions.Region;
import dev.sussolino.juicyregions.regions.event.handlers.RegionEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;

@Getter
@Setter
public class RegionEntityDamagedEvent extends RegionEvent {

    private final Entity victim;
    private final EntityDamageEvent.DamageCause cause;
    private double damage;

    public RegionEntityDamagedEvent(Region region, Entity victim, EntityDamageEvent.DamageCause cause) {
        super(region);
        this.victim = victim;
        this.cause = cause;
    }
}