package xyz.jadonfowler.phasebot.cmd.chat;

import org.spacehq.packetlib.Session;

import xyz.jadonfowler.phasebot.block.Entity;
import xyz.jadonfowler.phasebot.cmd.Command;

public class Entities extends Command{

	@Override
	public void exec(String in, String[] args, Session s) {
		for(Entity e : Entity.entities.values())
			System.out.println(e);
	}

	@Override
	public String getCommand() {
		return "entities";
	}

	@Override
	public String getDescription() {
		return "Print the entities";
	}

}
