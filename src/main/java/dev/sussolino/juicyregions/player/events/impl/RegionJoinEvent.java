package dev.sussolino.juicyregions.player.events.impl;

import dev.sussolino.juicyregions.player.RegionPlayer;
import dev.sussolino.juicyregions.player.events.RegionEvent;
import dev.sussolino.juicyregions.region.Region;

public class RegionJoinEvent extends RegionEvent {

    public RegionJoinEvent(RegionPlayer player, Region region) {
        super(player, region);
    }

    @Override
    public void cancel() {
        this.player.setback();
    }
}
