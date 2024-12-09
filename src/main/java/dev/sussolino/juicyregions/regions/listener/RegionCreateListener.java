package dev.sussolino.juicyregions.regions.listener;

import com.github.retrooper.packetevents.util.Vector3d;
import dev.sussolino.juicyregions.regions.command.RegionCommand;
import dev.sussolino.juicyregions.system.reflection.BukkitListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

@BukkitListener
public class RegionCreateListener implements Listener {



    @EventHandler
    public void onInteract(final PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        final ItemStack item = p.getItemInHand();

        final HashMap<Player, HashMap<Integer, Vector3d>> SAVED_LOCATIONS = RegionCommand.PRE_REGION_POSITIONS;

        if (!p.hasPermission("juicyregions.admin")) return;
        if (!item.getType().equals(Material.GOLDEN_HOE)) return;
        if (e.getClickedBlock() == null) return;

        final Location newLocation = e.getClickedBlock().getLocation();

        Vector3d vec = new Vector3d(newLocation.getX(), newLocation.getY(), newLocation.getZ());

        int index = 0;

        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            p.sendMessage("First position set to " + debug(newLocation));
        } else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            p.sendMessage("Second position set to " + debug(newLocation));
            index = 1;
        }

        SAVED_LOCATIONS.computeIfAbsent(p, k -> new HashMap<>()).put(index, vec);

        e.setCancelled(true);
    }

    private String debug(Location location) {
        return String.format("(%d, %d, %d)", location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
