package dev.sussolino.juicyregions.player.events.impl;

import dev.sussolino.juicyregions.player.RegionPlayer;
import dev.sussolino.juicyregions.player.events.RegionEvent;
import dev.sussolino.juicyregions.region.Region;

public class RegionQuitEvent extends RegionEvent {

    public RegionQuitEvent(RegionPlayer player, Region region) {
        super(player, region);
    }

    @Override
    public void cancel() {

    }
}
