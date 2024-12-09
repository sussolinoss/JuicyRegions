package dev.sussolino.juicyregions.player.processor;

import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatCommand;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateSign;
import dev.sussolino.juicyapi.packet.Packet;
import dev.sussolino.juicyapi.packet.PacketType;
import dev.sussolino.juicyregions.player.RegionPlayer;
import dev.sussolino.juicyregions.player.events.RegionEvent;
import dev.sussolino.juicyregions.player.processor.model.PlayerProcessor;
import dev.sussolino.juicyregions.region.Region;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicReference;

@Getter
public class RegionProcessor extends PlayerProcessor {

    public RegionProcessor(RegionPlayer player) {
        super(player);
    }

    public void process(Packet packet) {
        final AtomicReference<RegionEvent> event = new AtomicReference<>();
        //todo quit
        if (packet.is(PacketType.PacketClient.PLAYER_POSITION_AND_ROTATION) || packet.is(PacketType.PacketClient.PLAYER_POSITION) || packet.is(PacketType.PacketClient.PLAYER_ROTATION) || packet.is(PacketType.PacketClient.PLAYER_FLYING)) {
            final MovementProcessor movement = this.player.getMovementProcessor();

            final Vector3d position = movement.getPosition();

            if (position == null) return;

            final Region previous = this.finder(position);

            if (previous != null) {
                final Region.FlagType flag = Region.FlagType.ENTER;

                final boolean exempt = this.player.isExempt(flag.name());
                final boolean canJoin = previous.isFlagActive(flag);
                final boolean cancel = !canJoin && !exempt;

                if (cancel) {
                    this.player.setback();
                    return;
                }
            }

            this.player.setRegion(previous);
        }
        /**
         *
         *    Sign
         *
         */
        if (packet.is(PacketType.PacketClient.UPDATE_SIGN)) {
            final WrapperPlayClientUpdateSign wrap = new WrapperPlayClientUpdateSign(packet.getReceivePacket());

            final Region.FlagType flag = Region.FlagType.SIGN;
            final Vector3i sign_position = wrap.getBlockPosition();
            final Region sign_region = this.finder(sign_position.toVector3d());

            if (sign_region == null) return;

            final boolean active = sign_region.isFlagActive(flag);
            final boolean exempt = this.player.isExempt(sign_region.getName(), flag.getName());

            packet.getReceivePacket().setCancelled(active && !exempt);
        }
        /**
         *
         *    Command
         *
         */
        if (packet.is(PacketType.PacketClient.CHAT_COMMAND)) {
            final WrapperPlayClientChatCommand wrap = new WrapperPlayClientChatCommand(packet.getReceivePacket());

            final Region rg = this.player.getRegion();

            if (rg == null) return;

            final Region.FlagType commands = Region.FlagType.COMMANDS;

            final boolean exempt = this.player.isExempt(commands.getName());
            final boolean cancel = commands.getList().contains(wrap.getCommand());

            packet.getReceivePacket().setCancelled(cancel && !exempt);
        }
    }
    
    public Region finder(final Vector3d position) {
        if (Region.getRegions().isEmpty()) return null;
        return Region
                .getRegions()
                .stream()
                .parallel()
                .filter(region -> region.inRegion(this.player.getWorld(), position))
                .findFirst().orElse(null);
    }
}
