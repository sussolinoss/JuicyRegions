package dev.sussolino.juicyregions.region;

import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3i;
import dev.sussolino.juicyregions.api.RegionAPI;
import dev.sussolino.juicyregions.player.RegionPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.atomic.AtomicReference;

public class RegionEventsHandler implements Listener {

    public RegionEventsHandler(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    private void onBlockPlace(final BlockPlaceEvent event) {
        final RegionPlayer player = this.getPlayer(event.getPlayer());
        final Location location = event.getBlock().getLocation();

        this.block(event, location, player, Region.FlagType.BUILD);
    }

    @EventHandler
    private void onBlockBreak(final BlockBreakEvent event) {
        final RegionPlayer player = this.getPlayer(event.getPlayer());
        final Location location = event.getBlock().getLocation();

        this.block(event, location, player, Region.FlagType.BREAK);
    }

    @EventHandler
    private void onInteract(final PlayerInteractEvent event) {
        final RegionPlayer player = this.getPlayer(event.getPlayer());

        if (event.getInteractionPoint() == null && event.getClickedBlock() == null) return;

        final Location location = event.getInteractionPoint() == null ? event.getClickedBlock().getLocation() : event.getInteractionPoint();

        if (location == null) return;

        this.block(event, location, player, Region.FlagType.BREAK);
    }

    /**
     *
     *    Events Util || RegionPlayer
     *
     */

    private RegionPlayer getPlayer(final Player player) {
        return RegionAPI.getPlayer(player.getEntityId());
    }

    /**
     *
     *    Events Util || Block
     *
     */

    private void block(final Event event, final Location block, final RegionPlayer player, final Region.FlagType flag) {
        final Region region = player.getRegionProcessor().finder(new Vector3d(block.getX(), block.getY(), block.getZ()));

        if (region == null) return;

        cancel(event, player, region, flag);
    }

    /**
     *
     *    Events Util || Global
     *
     */

    private void cancel(final Event event, final RegionPlayer player, Region rg, final Region.FlagType flag) {
        if (event instanceof Cancellable e) {
            final boolean can = rg.isFlagActive(flag);
            final boolean exempt = player.isExempt(flag.name());

            e.setCancelled(!can && !exempt);
        }
    }
}
