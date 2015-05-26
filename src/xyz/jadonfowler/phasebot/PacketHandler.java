package xyz.jadonfowler.phasebot;

import java.util.UUID;

import org.spacehq.mc.protocol.data.game.values.ClientRequest;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientRequestPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerDestroyEntitiesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerUpdateHealthPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.spacehq.packetlib.event.session.DisconnectedEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;

import xyz.jadonfowler.phasebot.entity.Entity;
import xyz.jadonfowler.phasebot.entity.Player;

public class PacketHandler extends SessionAdapter {
	@Override
	public void packetReceived(PacketReceivedEvent event) {
		if (event.getPacket() instanceof ServerJoinGamePacket) {
			event.getSession().send(new ClientChatPacket("PhaseBot has joined the game."));
			PhaseBot.getBot().entityId = event.<ServerJoinGamePacket> getPacket().getEntityId();
		} else if (event.getPacket() instanceof ServerPlayerPositionRotationPacket) {
			PhaseBot.getBot().pos.x = event.<ServerPlayerPositionRotationPacket> getPacket().getX();
			PhaseBot.getBot().pos.y = event.<ServerPlayerPositionRotationPacket> getPacket().getY();
			PhaseBot.getBot().pos.z = event.<ServerPlayerPositionRotationPacket> getPacket().getZ();
			PhaseBot.getBot().pitch = event.<ServerPlayerPositionRotationPacket> getPacket().getPitch();
			PhaseBot.getBot().yaw = event.<ServerPlayerPositionRotationPacket> getPacket().getYaw();
			System.out.println("Err, My Position: " + PhaseBot.getBot().pos.x + "," + PhaseBot.getBot().pos.y + ","
					+ PhaseBot.getBot().pos.z);
			event.getSession().send(
					new ClientPlayerPositionRotationPacket(false, PhaseBot.getBot().pos.x, PhaseBot.getBot().pos.y,
							PhaseBot.getBot().pos.z, PhaseBot.getBot().pitch, PhaseBot.getBot().yaw));
		} else if (event.getPacket() instanceof ServerSpawnObjectPacket) {
			int entityId = event.<ServerSpawnObjectPacket> getPacket().getEntityId();
			double x = event.<ServerSpawnObjectPacket> getPacket().getX();
			double y = event.<ServerSpawnObjectPacket> getPacket().getY();
			double z = event.<ServerSpawnObjectPacket> getPacket().getZ();
			String type = event.<ServerSpawnObjectPacket> getPacket().getType().toString();
			System.out.println(new Entity(entityId, type, x, y, z));
		} else if (event.getPacket() instanceof ServerSpawnPlayerPacket) {
			int entityId = event.<ServerSpawnPlayerPacket> getPacket().getEntityId();
			double x = event.<ServerSpawnPlayerPacket> getPacket().getX();
			double y = event.<ServerSpawnPlayerPacket> getPacket().getY();
			double z = event.<ServerSpawnPlayerPacket> getPacket().getZ();
			UUID u = event.<ServerSpawnPlayerPacket> getPacket().getUUID();
			System.out.println(new Player(entityId, u, x, y, z));
		} else if (event.getPacket() instanceof ServerSpawnMobPacket) {
			int entityId = event.<ServerSpawnMobPacket> getPacket().getEntityId();
			double x = event.<ServerSpawnMobPacket> getPacket().getX();
			double y = event.<ServerSpawnMobPacket> getPacket().getY();
			double z = event.<ServerSpawnMobPacket> getPacket().getZ();
			String type = event.<ServerSpawnMobPacket> getPacket().getType().toString();
			System.out.println(new Entity(entityId, type, x, y, z));
		} else if (event.getPacket() instanceof ServerDestroyEntitiesPacket) {
			for (int i : event.<ServerDestroyEntitiesPacket> getPacket().getEntityIds()) {
				if (Entity.entities.containsKey(i))
					Entity.byId(i).remove();
			}
		} else if (event.getPacket() instanceof ServerUpdateHealthPacket) {
			if (event.<ServerUpdateHealthPacket> getPacket().getHealth() <= 0) {
				event.getSession().send(new ClientRequestPacket(ClientRequest.RESPAWN));
			}
		} else if (event.getPacket() instanceof ServerChatPacket) {

			Message message = event.<ServerChatPacket> getPacket().getMessage();
			System.out.println(message.getFullText());
			try {
				String c = message.getFullText().split(": ")[1];
				if (!c.startsWith("."))
					return;
				if (!(message.getFullText().contains("Phase") || message.getFullText().contains("Voltz")
						|| message.getFullText().contains("chibill") || message.getFullText().contains("tyler"))) {
					// event.getSession().send(new
					// ClientChatPacket("You're not my master! D:"));
					return;
				}
				System.out.println("Performing command: " + c);
				PhaseBot.getCommandManager().performCommand(c.split(" ")[0].replace(".", ""), c.split(" "),
						event.getSession());
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void disconnected(DisconnectedEvent event) {
		System.out.println("Disconnected: " + Message.fromString(event.getReason()).getFullText());
	}
}
