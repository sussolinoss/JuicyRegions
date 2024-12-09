package dev.sussolino.juicyregions.regions.event.handlers;

import dev.sussolino.juicyregions.regions.Region;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class RegionEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Region region;
    private boolean cancelled = false;


    public RegionEvent(Region region) {
        super(false);
        this.region = region;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

}
