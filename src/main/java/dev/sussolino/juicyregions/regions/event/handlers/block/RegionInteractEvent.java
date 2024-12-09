package dev.sussolino.juicyregions.regions.event.handlers.block;

import dev.sussolino.juicyregions.regions.Region;
import dev.sussolino.juicyregions.regions.event.handlers.RegionEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class RegionInteractEvent extends RegionEvent {

    private final Player player;
    private final Block block;
    private final ItemStack item;
    private final Action action;
    private Event.Result useInteractedBlock;
    private Event.Result useItemInHand;

    public RegionInteractEvent(Region region, Player player, Block block, ItemStack item, Action action) {
        super(region);
        this.player = player;
        this.block = block;
        this.item = item;
        this.action = action;
    }
}