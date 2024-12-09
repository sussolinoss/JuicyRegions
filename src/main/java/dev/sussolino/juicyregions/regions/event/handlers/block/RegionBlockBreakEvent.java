package dev.sussolino.juicyregions.regions.event.handlers.block;

import dev.sussolino.juicyregions.regions.Region;
import dev.sussolino.juicyregions.regions.event.handlers.RegionEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@Getter
@Setter
public class RegionBlockBreakEvent extends RegionEvent {

    private final Player player;
    private final Block block;
    private boolean dropItems;
    private int expToDrop;

    public RegionBlockBreakEvent(Region region, Player player, Block block) {
        super(region);
        this.player = player;
        this.block = block;
    }
}