package dev.sussolino.juicyregions.regions.event.handlers.saturation;

import dev.sussolino.juicyregions.regions.Region;
import dev.sussolino.juicyregions.regions.event.handlers.RegionEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class RegionSaturationChangeEvent extends RegionEvent {

    private final Entity entity;
    private final ItemStack item;

    private int foodLvl;

    public RegionSaturationChangeEvent(Region region, Entity entity, ItemStack item, int foodLvl) {
        super(region);
        this.entity = entity;
        this.item = item;
        this.foodLvl = foodLvl;
    }
}
