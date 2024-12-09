package dev.sussolino.juicyregions.regions.event.handlers.entity;

import dev.sussolino.juicyregions.regions.Region;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;

@Getter
public class RegionEntityDamageByEntityEvent extends RegionEntityDamagedEvent {

    private final Entity damager;

    public RegionEntityDamageByEntityEvent(Region region, Entity victim, EntityDamageEvent.DamageCause cause, Entity damager) {
        super(region, victim, cause);
        this.damager = damager;
    }
}