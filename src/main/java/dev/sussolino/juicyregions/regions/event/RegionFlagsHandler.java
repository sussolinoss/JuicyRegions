package dev.sussolino.juicyregions.regions.event;

import dev.sussolino.juicyregions.regions.Region;
import dev.sussolino.juicyregions.regions.event.handlers.base.RegionEnterEvent;
import dev.sussolino.juicyregions.regions.event.handlers.base.RegionExitEvent;
import dev.sussolino.juicyregions.regions.event.handlers.base.RegionMoveEvent;
import dev.sussolino.juicyregions.regions.event.handlers.block.RegionBlockBreakEvent;
import dev.sussolino.juicyregions.regions.event.handlers.block.RegionBlockPlaceEvent;
import dev.sussolino.juicyregions.regions.event.handlers.block.RegionInteractEvent;
import dev.sussolino.juicyregions.regions.event.handlers.entity.RegionEntityDamageByEntityEvent;
import dev.sussolino.juicyregions.regions.event.handlers.entity.RegionEntityDamagedEvent;
import dev.sussolino.juicyregions.regions.event.handlers.entity.RegionEntitySpawnEvent;
import dev.sussolino.juicyregions.regions.event.handlers.explosion.RegionBlockExplodeEvent;
import dev.sussolino.juicyregions.regions.event.handlers.explosion.RegionEntityExplodeEvent;
import dev.sussolino.juicyregions.regions.event.handlers.saturation.RegionSaturationChangeEvent;
import dev.sussolino.juicyregions.system.reflection.BukkitListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;

import static dev.sussolino.juicyregions.regions.Region.FlagType;

@BukkitListener
public class RegionFlagsHandler implements Listener {


    /**
     * BLOCK ACTIONS
     */

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(final RegionBlockBreakEvent e) {
        if (e.getRegion().isFlagActive(FlagType.BREAK)) return;
        e.setCancelled(true);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(final RegionBlockPlaceEvent e) {
        if (e.getRegion().isFlagActive(FlagType.BUILD)) return;
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockInteract(final RegionInteractEvent e) {
        final Region rg = e.getRegion();
        final Action action = e.getAction();
        final boolean BLOCK_RIGHT_CLICK = !rg.isFlagActive(FlagType.BLOCK_RIGHT_CLICK) && action == Action.RIGHT_CLICK_BLOCK;
        final boolean BLOCK_LEFT_CLICK = !rg.isFlagActive(FlagType.BLOCK_LEFT_CLICK) && action == Action.LEFT_CLICK_BLOCK;
        final boolean RIGHT_CLICK_AIR = !rg.isFlagActive(FlagType.AIR_RIGHT_CLICK) && action == Action.RIGHT_CLICK_AIR;
        final boolean LEFT_CLICK_AIR = !rg.isFlagActive(FlagType.AIR_LEFT_CLICK) && action == Action.LEFT_CLICK_AIR;
        final boolean cancel = BLOCK_RIGHT_CLICK || BLOCK_LEFT_CLICK || RIGHT_CLICK_AIR || LEFT_CLICK_AIR;
        if (!cancel) return;
        e.setCancelled(true);
    }

    /**
     * ENTITY ACTIONS
     */

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamaged(final RegionEntityDamagedEvent e) {
        final Region rg = e.getRegion();
        final EntityDamageEvent.DamageCause cause = e.getCause();
        final boolean NO_FALL = rg.isFlagActive(FlagType.NO_FALL) && cause.equals(EntityDamageEvent.DamageCause.FALL);
        final boolean FIRE_DAMAGE = rg.isFlagActive(FlagType.DAMAGE_FIRE) && cause.equals(EntityDamageEvent.DamageCause.FIRE);
        final boolean cancel = NO_FALL || FIRE_DAMAGE;

        if (!cancel) return;

        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(final RegionEntityDamageByEntityEvent e) {

        if (e.getRegion().isFlagActive(FlagType.PVP)) return;
        e.setCancelled(true);
    }

    /**
     * Base
     */

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRegionMove(final RegionMoveEvent e) {
        final boolean MOVE = !e.getRegion().isFlagActive(FlagType.MOVE);
        final boolean MOVE_ON_WATER = !e.getRegion().isFlagActive(FlagType.MOVE_ON_WATER) && e.getPlayer().isInWater();

        if (MOVE_ON_WATER) FlagType.SPAWN.teleport(e.getPlayer(), e.getRegion());

        if (!MOVE) return;

        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRegionMove(final RegionEnterEvent e) {
        final boolean ENTER = !e.getRegion().isFlagActive(FlagType.ENTER);

        e.getRegion().executeCommand(FlagType.ENTER.get());
        if (!ENTER) return;

        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRegionMove(final RegionExitEvent e) {
        final boolean EXIT = !e.getRegion().isFlagActive(FlagType.EXIT);

        e.getRegion().executeCommand(FlagType.EXIT.get());
        if (!EXIT)
            return;

        e.setCancelled(true);
    }


    /**
     * Explosion
     */

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplosion(final RegionBlockExplodeEvent e) {
        final boolean flagActive = e.getRegion().isExplosionInRange(e.getBlock().getLocation(), e.getRadius()) && !e.getRegion().isFlagActive(FlagType.EXPLOSION);
        if (flagActive)
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplosion(final RegionEntityExplodeEvent e) {
        final boolean flagActive = e.getRegion().isExplosionInRange(e.getEntity().getLocation(), e.getRadius()) && !e.getRegion().isFlagActive(FlagType.EXPLOSION);
        if (flagActive)
            e.setCancelled(true);
    }

    /**
     * Saturation
     */

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplosion(final RegionSaturationChangeEvent e) {
        final boolean LOSS_SATURATION = e.getRegion().isFlagActive(FlagType.LOSS_SATURATION);
        if (LOSS_SATURATION)
            e.setCancelled(true);
    }

    /**
     * Mob Spawn
     */

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplosion(final RegionEntitySpawnEvent e) {
        final boolean MOB_SPAWN = e.getRegion().isFlagActive(FlagType.MOB_SPAWN);
        if (MOB_SPAWN)
            e.setCancelled(true);
    }
}
