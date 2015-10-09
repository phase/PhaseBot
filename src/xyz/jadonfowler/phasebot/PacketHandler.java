package xyz.jadonfowler.phasebot;

import java.util.*;
import org.spacehq.mc.protocol.data.game.*;
import org.spacehq.mc.protocol.data.game.values.*;
import org.spacehq.mc.protocol.data.game.values.world.block.*;
import org.spacehq.mc.protocol.data.message.*;
import org.spacehq.mc.protocol.packet.ingame.client.*;
import org.spacehq.mc.protocol.packet.ingame.client.player.*;
import org.spacehq.mc.protocol.packet.ingame.server.*;
import org.spacehq.mc.protocol.packet.ingame.server.entity.*;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.*;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.*;
import org.spacehq.mc.protocol.packet.ingame.server.window.*;
import org.spacehq.mc.protocol.packet.ingame.server.world.*;
import org.spacehq.packetlib.event.session.*;
import xyz.jadonfowler.phasebot.entity.*;
import xyz.jadonfowler.phasebot.inventory.*;
import xyz.jadonfowler.phasebot.world.*;

public class PacketHandler extends SessionAdapter {

    @Override public void packetReceived(PacketReceivedEvent event) {
        if (event.getPacket() instanceof ServerJoinGamePacket) {
            event.getSession().send(new ClientChatPacket("PhaseBot has joined the game."));
            PhaseBot.getBot().entityId = event.<ServerJoinGamePacket> getPacket().getEntityId();
        }
        else if (event.getPacket() instanceof ServerPlayerPositionRotationPacket) {
            PhaseBot.getBot().pos.x = event.<ServerPlayerPositionRotationPacket> getPacket().getX();
            PhaseBot.getBot().pos.y = event.<ServerPlayerPositionRotationPacket> getPacket().getY();
            PhaseBot.getBot().pos.z = event.<ServerPlayerPositionRotationPacket> getPacket().getZ();
            PhaseBot.getBot().pitch = event.<ServerPlayerPositionRotationPacket> getPacket().getPitch();
            PhaseBot.getBot().yaw = event.<ServerPlayerPositionRotationPacket> getPacket().getYaw();
            System.out.println("Err, My Position: " + PhaseBot.getBot().pos.x + "," + PhaseBot.getBot().pos.y + ","
                    + PhaseBot.getBot().pos.z);
            event.getSession().send(new ClientPlayerPositionRotationPacket(false, PhaseBot.getBot().pos.x,
                    PhaseBot.getBot().pos.y, PhaseBot.getBot().pos.z, PhaseBot.getBot().pitch, PhaseBot.getBot().yaw));
        }
        else if (event.getPacket() instanceof ServerSpawnObjectPacket) {
            int entityId = event.<ServerSpawnObjectPacket> getPacket().getEntityId();
            double x = event.<ServerSpawnObjectPacket> getPacket().getX();
            double y = event.<ServerSpawnObjectPacket> getPacket().getY();
            double z = event.<ServerSpawnObjectPacket> getPacket().getZ();
            String type = event.<ServerSpawnObjectPacket> getPacket().getType().toString();
            new Entity(entityId, type, x, y, z);
        }
        else if (event.getPacket() instanceof ServerSpawnPlayerPacket) {
            int entityId = event.<ServerSpawnPlayerPacket> getPacket().getEntityId();
            double x = event.<ServerSpawnPlayerPacket> getPacket().getX();
            double y = event.<ServerSpawnPlayerPacket> getPacket().getY();
            double z = event.<ServerSpawnPlayerPacket> getPacket().getZ();
            UUID u = event.<ServerSpawnPlayerPacket> getPacket().getUUID();
            new Player(entityId, u, x, y, z);
        }
        else if (event.getPacket() instanceof ServerSpawnMobPacket) {
            int entityId = event.<ServerSpawnMobPacket> getPacket().getEntityId();
            double x = event.<ServerSpawnMobPacket> getPacket().getX();
            double y = event.<ServerSpawnMobPacket> getPacket().getY();
            double z = event.<ServerSpawnMobPacket> getPacket().getZ();
            String type = event.<ServerSpawnMobPacket> getPacket().getType().toString();
            new Entity(entityId, type, x, y, z);
        }
        else if (event.getPacket() instanceof ServerDestroyEntitiesPacket) {
            for (int i : event.<ServerDestroyEntitiesPacket> getPacket().getEntityIds()) {
                if (Entity.getEntities().containsKey(i)) {
                    Entity e = Entity.byId(i);
                    if (e instanceof Player) Player.players.remove((Player) e);
                    e.remove();
                }
            }
        }
        else if (event.getPacket() instanceof ServerUpdateHealthPacket) {
            if (event.<ServerUpdateHealthPacket> getPacket().getHealth() <= 0) {
                event.getSession().send(new ClientRequestPacket(ClientRequest.RESPAWN));
            }
        }
        else if (event.getPacket() instanceof ServerEntityPositionRotationPacket) {
            ServerEntityMovementPacket p = event.<ServerEntityPositionRotationPacket> getPacket();
            int id = p.getEntityId();
            Entity e = Entity.byId(id);
            e.x += p.getMovementX();
            e.y += p.getMovementY();
            e.z += p.getMovementZ();
            e.pitch = p.getPitch();
            e.yaw = p.getYaw();
        }
        else if (event.getPacket() instanceof ServerEntityTeleportPacket) {
            ServerEntityTeleportPacket p = event.<ServerEntityTeleportPacket> getPacket();
            int id = p.getEntityId();
            Entity e = Entity.byId(id);
            e.x = p.getX();
            e.y = p.getY();
            e.z = p.getZ();
            e.pitch = p.getPitch();
            e.yaw = p.getYaw();
        }
        else if (event.getPacket() instanceof ServerChunkDataPacket) {
            ServerChunkDataPacket p = event.<ServerChunkDataPacket> getPacket();
            new ChunkColumn(p.getX(), p.getZ(), p.getChunks());
        }
        else if (event.getPacket() instanceof ServerMultiChunkDataPacket) {
            for (int i = 0; i < event.<ServerMultiChunkDataPacket> getPacket().getColumns(); i++) {
                int chunkX = event.<ServerMultiChunkDataPacket> getPacket().getX(i);
                int chunkZ = event.<ServerMultiChunkDataPacket> getPacket().getZ(i);
                new ChunkColumn(chunkX, chunkZ, event.<ServerMultiChunkDataPacket> getPacket().getChunks(i));
            }
        }
        else if (event.getPacket() instanceof ServerMultiBlockChangePacket) {
            ServerMultiBlockChangePacket packet = event.<ServerMultiBlockChangePacket> getPacket();
            for (BlockChangeRecord r : packet.getRecords()) {
                Position p = r.getPosition();
                int id = r.getBlock();
                ChunkColumn.setBlock(p, id / 16); // Why is this 16? I don't
                                                  // think I should have to do
                                                  // anything with it.
                PhaseBot.getBot().setInteruptMoveAlong(true);
            }
        }
        else if (event.getPacket() instanceof ServerBlockChangePacket) {
            Position p = event.<ServerBlockChangePacket> getPacket().getRecord().getPosition();
            int id = event.<ServerBlockChangePacket> getPacket().getRecord().getBlock();
            ChunkColumn.setBlock(p, id / 16);
            PhaseBot.getBot().setInteruptMoveAlong(true);
        }
        else if (event.getPacket() instanceof ServerWindowItemsPacket) {
            ServerWindowItemsPacket p = event.<ServerWindowItemsPacket> getPacket();
            if (p.getWindowId() == 0) {// Player's inventory
                PhaseBot.getBot().setInventory(new Inventory(p.getItems()));
                System.out.println("Inventory recieved!");
            }
        }
        else if (event.getPacket() instanceof ServerSetSlotPacket) {
            ServerSetSlotPacket p = event.<ServerSetSlotPacket> getPacket();
            if (p.getWindowId() == 0) {// Player's Inventory
                PhaseBot.getBot().getInventory().setItem(p.getSlot(), p.getItem());
                if(p.getItem() != null)
                System.out.println("Slot " + p.getSlot() + " change to " + p.getItem().getId());
            }
        }
        else if (event.getPacket() instanceof ServerChatPacket) {
            Message message = event.<ServerChatPacket> getPacket().getMessage();
            try {
                ChatMessage m = new ChatMessage(message.getFullText());
                if (!m.isCommand()) return;
                ArrayList<String> owners = new ArrayList<String>();
                owners.add("Phase");
                owners.add("VoltzLive");
                if (!owners.contains(m.getSender())) {
                    event.getSession().send(new ClientChatPacket("/msg " + m.getSender() + " You are not my master!"));
                    return;
                }
                PhaseBot.getCommandManager().performCommand(m.getCommand(),
                        m.getMessage().split(PhaseBot.getPrefix())[1].split(" "), event.getSession());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override public void disconnected(DisconnectedEvent event) {
        System.out.println("Disconnected: " + Message.fromString(event.getReason()).getFullText());
    }
}
