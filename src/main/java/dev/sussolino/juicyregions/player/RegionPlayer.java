package dev.sussolino.juicyregions.player;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport;
import dev.sussolino.juicyapi.color.ColorUtils;
import dev.sussolino.juicyapi.packet.Packet;
import dev.sussolino.juicyregions.player.events.RegionEvent;
import dev.sussolino.juicyregions.player.events.RegionEventHandler;
import dev.sussolino.juicyregions.player.processor.MovementProcessor;
import dev.sussolino.juicyregions.player.processor.RegionProcessor;
import dev.sussolino.juicyregions.region.Region;
import dev.sussolino.juicyregions.system.JuicyLocation;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public class RegionPlayer {

    private final User user;
    private final Player player;

    private Region region, lastRegion;

    private final RegionProcessor regionProcessor;
    private final MovementProcessor movementProcessor;

    private final List<RegionEventHandler> events = new CopyOnWriteArrayList<>();

    public RegionPlayer(User user, Player player) {
        this.user = user;
        this.player = player;

        this.movementProcessor = new MovementProcessor(this);
        this.regionProcessor = new RegionProcessor(this);
    }

    public void update(final PacketWrapper<?> wrap) {
        this.user.sendPacket(wrap);
    }

    public void sendMessage(String message) {
        this.user.sendMessage(ColorUtils.color(message));
    }

    public void receive(final RegionEvent event) {
        this.getEvents()
                .parallelStream()
                .forEach(handler -> handler.event(event));
    }

    public void receive(final RegionEvent... events) {
        for (RegionEvent event : events) {
            this.receive(event);
        }
    }

    public void handle(final Packet packet) {
        this.movementProcessor.process(packet);
        this.regionProcessor.process(packet);
    }

    public void addEvent(final RegionEventHandler... events) {
        this.events.addAll(List.of(events));
    }

    public void setback() {
        final float yaw = this.movementProcessor.getLastYaw();
        final float pitch = this.movementProcessor.getLastPitch();

        final Vector3d position = this.movementProcessor.getLastPosition();

        if (position != null) {
            final Location location = new Location(this.player.getWorld(), position.getX(), position.getY(), position.getZ(), yaw, pitch);

            this.player.teleportAsync(location);
        }
    }


    public boolean cancel() {
        setback();
        this.region = this.lastRegion;
        return true;
    }

    public String getWorld() {return player.getWorld().getName(); }
    public String getName() {
        return this.player.getName();
    }
    public int getEntityId() {
        return this.player.getEntityId();
    }

    public boolean isExempt(final String flag) {
        if (getRegion() == null) return false;
        return this.player.hasPermission("regions." + getRegion().getName() + "." + flag);
    }
    public boolean isExempt(final String name, final String flag) {
        return this.player.hasPermission("regions." + name + "." + flag);
    }
}
