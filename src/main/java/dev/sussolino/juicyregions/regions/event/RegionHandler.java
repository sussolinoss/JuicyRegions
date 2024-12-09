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
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@BukkitListener
public class RegionHandler implements Listener {

    /**
     * BLOCK ACTIONS
     */

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(final BlockBreakEvent e) {
        final Player p = e.getPlayer();
        final Block block = e.getBlock();
        final Region region = Region.getWhereIs(block.getLocation());

        if (region == null) return;

        final RegionBlockBreakEvent event = new RegionBlockBreakEvent(region, p, block);
        event.setDropItems(e.isDropItems());
        event.setExpToDrop(e.getExpToDrop());

        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            e.setCancelled(true);
            return;
        }
        e.setDropItems(event.isDropItems());
        e.setExpToDrop(event.getExpToDrop());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(final BlockPlaceEvent e) {
        final Player p = e.getPlayer();
        final Block block = e.getBlock();
        final Region region = Region.getWhereIs(block.getLocation());

        if (region == null) return;

        final RegionBlockPlaceEvent event = new RegionBlockPlaceEvent(region, p, block);
        event.setCanBuild(e.canBuild());

        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            e.setCancelled(true);
            return;
        }
        e.setBuild(event.isCanBuild());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(final PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        final Region region = Region.getWherePlayerIs(p);
        final Block block = e.getClickedBlock();
        final ItemStack item = e.getItem();
        final Action action = e.getAction();

        if (region == null) return;

        final RegionInteractEvent event = new RegionInteractEvent(region, p, block, item, action);
        event.setUseInteractedBlock(e.useInteractedBlock());
        event.setUseItemInHand(e.useItemInHand());

        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            e.setCancelled(true);
            return;
        }

        e.setUseInteractedBlock(event.getUseInteractedBlock());
        e.setUseItemInHand(event.getUseItemInHand());
    }

    /**
     * ENTITY ACTIONS
     */

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamaged(final EntityDamageEvent e) {
        final Entity entity = e.getEntity();
        final Region region = Region.getWhereEntityIs(entity);

        if (region == null) return;

        final RegionEntityDamagedEvent event = new RegionEntityDamagedEvent(region, entity, e.getCause());
        event.setDamage(e.getDamage());

        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            e.setCancelled(true);
            return;
        }
        e.setDamage(event.getDamage());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent e) {
        final Entity victim = e.getEntity();
        final Entity damager = e.getDamager();
        final Region region = Region.getWhereEntityIs(victim);

        if (region == null) return;

        final RegionEntityDamageByEntityEvent event = new RegionEntityDamageByEntityEvent(region, victim, e.getCause(), damager);
        event.setDamage(e.getDamage());

        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            e.setCancelled(true);
            return;
        }

        e.setDamage(e.getDamage());
    }

    /**
     * Base
     */

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRegionBase(final PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        final Region region = Region.getWhereIs(p.getLocation());
        final Region toRegion = Region.getWhereIs(e.getTo());
        final Region fromRegion = Region.getWhereIs(e.getFrom());

        boolean enter = !Region.inSomeRegion(e.getFrom()) && toRegion != null;
        boolean exit = Region.inSomeRegion(e.getFrom()) && toRegion == null;
        boolean walkInside = Region.inSomeRegion(e.getFrom()) && toRegion != null;

        RegionMoveEvent event;

        if (enter) event = new RegionEnterEvent(toRegion, p, e.getFrom(), e.getTo());
        else if (exit) event = new RegionExitEvent(fromRegion, p, e.getFrom(), e.getTo());
        else if (walkInside) event = new RegionMoveEvent(region, p, e.getFrom(), e.getTo());
        else return;

        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            e.setCancelled(true);
            return;
        }
        e.setTo(event.getTo());
        e.setFrom(event.getFrom());

    }

    /**
     * Explosion
     */

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplosion(final BlockExplodeEvent e) {
        final Block block = e.getBlock();
        final BlockState state = e.getExplodedBlockState();
        final Region region = Region.getWhereIs(block.getLocation());

        final float radius = e.getYield();
        final List<Block> affectedBlocks = e.blockList();

        if (region == null) return;

        final RegionBlockExplodeEvent event = new RegionBlockExplodeEvent(region, block, state, affectedBlocks, radius);
        event.setYield(e.getYield());

        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            e.setCancelled(true);
            return;
        }
        e.setYield(event.getYield());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplosion(final ExplosionPrimeEvent e) {
        final Entity entity = e.getEntity();
        final EntityType type = e.getEntityType();
        final Region region = Region.getWhereIs(entity.getLocation());

        final float radius = e.getRadius();
        final boolean fire = e.getFire();

        if (region == null) return;

        final RegionEntityExplodeEvent event = new RegionEntityExplodeEvent(region, entity, type, radius, fire);

        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            e.setCancelled(true);
            return;
        }
        e.setRadius(event.getRadius());
        e.setFire(event.isFire());
    }

    /**
     * Saturation
     */

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSaturationChange(final FoodLevelChangeEvent e) {
        final Entity entity = e.getEntity();
        final Region region = Region.getWhereEntityIs(entity);
        final ItemStack item = e.getItem();
        final int foodLevel = e.getFoodLevel();

        if (region == null) return;

        final RegionSaturationChangeEvent event = new RegionSaturationChangeEvent(region, entity, item, foodLevel);

        Bukkit.getPluginManager().callEvent(event);


        if (event.isCancelled()) {
            e.setCancelled(true);
            return;
        }
        e.setFoodLevel(event.getFoodLvl());
    }

    /**
     * Entity Spawn
     */

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntitySpawn(final EntitySpawnEvent e) {
        final Entity entity = e.getEntity();
        final Region region = Region.getWhereEntityIs(entity);

        if (region == null) return;

        final RegionEntitySpawnEvent event = new RegionEntitySpawnEvent(region, entity);

        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) e.setCancelled(true);
    }
}
