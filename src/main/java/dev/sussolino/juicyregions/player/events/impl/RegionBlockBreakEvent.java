package dev.sussolino.juicyregions.player.events.impl;

import com.github.retrooper.packetevents.util.Vector3i;
import dev.sussolino.juicyregions.player.RegionPlayer;
import dev.sussolino.juicyregions.player.events.RegionEvent;
import dev.sussolino.juicyregions.region.Region;
import lombok.Getter;

@Getter
public class RegionBlockBreakEvent extends RegionEvent {

    private final Vector3i block;

    public RegionBlockBreakEvent(RegionPlayer player, Region region, Vector3i block) {
        super(player, region);
        this.block = block;
    }

    @Override
    public void cancel() {

    }
}
