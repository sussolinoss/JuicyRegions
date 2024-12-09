package dev.sussolino.juicyregions.player.events.impl;

import dev.sussolino.juicyregions.player.RegionPlayer;
import dev.sussolino.juicyregions.player.events.RegionEvent;
import dev.sussolino.juicyregions.region.Region;
import lombok.Getter;

@Getter
public class RegionChangeEvent extends RegionEvent {

    private Region lastRegion;

    public RegionChangeEvent(RegionPlayer player, Region region, Region lastRegion) {
        super(player, region);
        this.lastRegion = lastRegion;
    }

    @Override
    public void cancel() {
        this.player.cancel();
    }
}
