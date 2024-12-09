package dev.sussolino.juicyregions.player.processor;

import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPosition;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionAndRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import dev.sussolino.juicyapi.java.SampleList;
import dev.sussolino.juicyapi.packet.Packet;
import dev.sussolino.juicyapi.packet.PacketType;
import dev.sussolino.juicyregions.player.RegionPlayer;
import dev.sussolino.juicyregions.player.events.impl.RegionChangeEvent;
import dev.sussolino.juicyregions.player.events.impl.RegionJoinEvent;
import dev.sussolino.juicyregions.player.events.impl.RegionQuitEvent;
import dev.sussolino.juicyregions.player.processor.model.PlayerProcessor;
import dev.sussolino.juicyregions.region.Region;
import dev.sussolino.juicyregions.system.JuicyLocation;
import lombok.Getter;

import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.Long.MAX_VALUE;

@Getter
public class MovementProcessor extends PlayerProcessor {

    public MovementProcessor(RegionPlayer player) {
        super(player);
    }

    private float yaw, pitch;
    private float lastYaw, lastPitch;
    private boolean onGround, lastOnGround;
    private Vector3d position, lastPosition;


    public void process(Packet packet) {
        final boolean movement =
                packet.is(PacketType.PacketClient.PLAYER_FLYING) ||
                        packet.is(PacketType.PacketClient.PLAYER_POSITION_AND_ROTATION) ||
                        packet.is(PacketType.PacketClient.PLAYER_POSITION) ||
                        packet.is(PacketType.PacketClient.PLAYER_ROTATION);

        if (!movement) return;

        /*
         *
         *     MOVEMENT LOGIC
         *
         */

        boolean onGround = false;
        float yaw = Float.MAX_VALUE, pitch = Float.MAX_VALUE;
        Vector3d position = null;

        if (packet.is(PacketType.PacketClient.PLAYER_FLYING) || packet.is(PacketType.PacketClient.PLAYER_POSITION_AND_ROTATION)) {
            final WrapperPlayClientPlayerFlying wrap = new WrapperPlayClientPlayerFlying(packet.getReceivePacket());
            final Location l = wrap.getLocation();

            if (wrap.hasPositionChanged()) {
                position = l.getPosition();
                onGround = wrap.isOnGround();
            }

            if (wrap.hasRotationChanged()) {
                yaw = l.getYaw();
                pitch = l.getPitch();
            }
        }
        if (packet.is(PacketType.PacketClient.PLAYER_POSITION)) {
            final WrapperPlayClientPlayerPosition wrap = new WrapperPlayClientPlayerPosition(packet.getReceivePacket());

            position = wrap.getPosition();
            onGround = wrap.isOnGround();
        }
        if (packet.is(PacketType.PacketClient.PLAYER_ROTATION)) {
            final WrapperPlayClientPlayerRotation wrap = new WrapperPlayClientPlayerRotation(packet.getReceivePacket());

            yaw = wrap.getYaw();
            pitch = wrap.getPitch();
        }

        /*
         *
         *     UPDATE LOGIC
         *
         */

        if (!packet.is(PacketType.PacketClient.PLAYER_ROTATION)) {
            this.lastOnGround = this.onGround;
            this.onGround = onGround;
        }

        if (position != null) {
            this.lastPosition = this.position;
            this.position = position;
        }

        if (yaw != Float.MAX_VALUE && pitch != Float.MAX_VALUE) {
            this.lastYaw = this.yaw;
            this.lastPitch = this.pitch;

            this.yaw = yaw;
            this.pitch = pitch;
        }
    }
}
