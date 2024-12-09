package dev.sussolino.juicyregions.player.events;

import dev.sussolino.juicyregions.player.RegionPlayer;
import dev.sussolino.juicyregions.region.Region;
import lombok.Getter;
import lombok.Setter;

@Setter
public abstract class RegionEvent {

    protected final Region region;
    protected final RegionPlayer player;

    @Getter
    public boolean cancelled;

    public RegionEvent(RegionPlayer player, Region region) {
        this.player = player;
        this.region = region;
    }
    public RegionEvent(RegionPlayer player, Region region, boolean cancelled) {
        this.player = player;
        this.region = region;
        this.cancelled = cancelled;
    }

    public RegionEvent setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
        return this;
    }


    public abstract void cancel();
}
