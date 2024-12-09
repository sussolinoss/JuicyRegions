package dev.sussolino.juicyregions;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.*;
import dev.sussolino.juicyapi.packet.Packet;
import dev.sussolino.juicyregions.commands.RegionCommand;
import dev.sussolino.juicyregions.player.RegionPlayer;
import dev.sussolino.juicyregions.player.manager.RegionPlayerManager;
import dev.sussolino.juicyregions.region.Region;
import dev.sussolino.juicyregions.region.RegionEventsHandler;
import dev.sussolino.juicyregions.region.flags.FlagRegistry;
import dev.sussolino.juicyregions.yml.regions.RegionsYml;
import dev.sussolino.juicyregions.yml.settings.SettingsYml;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@SuppressWarnings("all")
public final class Juicy implements PacketListener, Listener {

    private final JavaPlugin plugin;

    public static RegionsYml REGIONS;
    public static SettingsYml SETTINGS;

    public static RegionPlayerManager PLAYER_MANAGER;

    private final RegionCommand command;
    private final RegionEventsHandler events;

    public Juicy(final JavaPlugin plugin) {
        this.plugin = plugin;

        this.REGIONS = new RegionsYml(plugin);
        this.SETTINGS = new SettingsYml(plugin);

        this.PLAYER_MANAGER = new RegionPlayerManager();

        Region.FlagType.init();

        FlagRegistry.flags.forEach(f -> System.out.println("Registered flag: " + f.getName()));

        (this.command = new RegionCommand(plugin)).register("region", true);

        this.events = new RegionEventsHandler(plugin);

        PacketEvents.getAPI().getEventManager().registerListener(this, PacketListenerPriority.MONITOR);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPlayer() == null || !(event.getPlayer() instanceof Player p)) return;


        final Packet packet = new Packet(event);
        final RegionPlayer player = this.PLAYER_MANAGER.add(event.getUser(), p);

        if (player == null || packet == null) return;

        player.handle(packet);
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        if (event.getPlayer() == null || !(event.getPlayer() instanceof Player p)) return;

        final Packet packet = new Packet(event);
        final RegionPlayer player = this.PLAYER_MANAGER.add(event.getUser(), p);

        if (player == null || packet == null) return;

        player.handle(packet);
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        this.PLAYER_MANAGER.remove(event.getPlayer().getEntityId());
    }
}
