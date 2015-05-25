package xyz.jadonfowler.phasebot.block;

import java.io.IOException;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

public class ServerPlayerDigBlockPacket implements Packet {

	
	
	@Override
	public void read(NetInput paramNetInput) throws IOException {

	}

	@Override
	public void write(NetOutput paramNetOutput) throws IOException {

	}

	@Override
	public boolean isPriority() {
		return false;
	}

}
