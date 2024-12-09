package dev.sussolino.juicyregions.regions.event.handlers.explosion;

import dev.sussolino.juicyregions.regions.Region;
import dev.sussolino.juicyregions.regions.event.handlers.RegionEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.List;

@Getter
@Setter
public class RegionBlockExplodeEvent extends RegionEvent {

    private final List<Block> affectedBlocks;
    private final Block block;
    private final BlockState state;

    private final float radius;
    private float yield;

    public RegionBlockExplodeEvent(Region region, Block block, BlockState state, List<Block> affectedBlocks, float radius) {
        super(region);
        this.block = block;
        this.state = state;
        this.affectedBlocks = affectedBlocks;
        this.radius = radius;
    }
}