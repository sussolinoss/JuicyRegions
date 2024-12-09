package dev.sussolino.juicyregions.player.events.impl;

import dev.sussolino.juicyregions.player.RegionPlayer;
import dev.sussolino.juicyregions.player.events.RegionEvent;
import dev.sussolino.juicyregions.region.Region;

public class RegionPvPEvent extends RegionEvent {

    public RegionPvPEvent(RegionPlayer player, Region region) {
        super(player, region);
    }

    @Override
    public void cancel() {

    }
}
