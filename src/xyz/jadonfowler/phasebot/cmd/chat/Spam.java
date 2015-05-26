package xyz.jadonfowler.phasebot.cmd.chat;

import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.Session;

import xyz.jadonfowler.phasebot.cmd.Command;

public class Spam extends Command {

	@Override
	public void exec(String in, String[] args, Session s) {
		StringBuilder text = new StringBuilder();
		for (int i = 2; i < args.length; i++) {
			text.append(args[i] + " ");
		}
		int j = Integer.parseInt(args[1]);
		for (int k = 0; k < j; k++) {
			s.send(new ClientChatPacket(text.toString()));
			try {
				Thread.sleep(850);
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public String getCommand() {
		return "spam";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
