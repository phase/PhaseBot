package xyz.jadonfowler.phasebot.cmd.chat;

import java.util.Arrays;

import org.spacehq.packetlib.Session;

import xyz.jadonfowler.phasebot.PhaseBot;
import xyz.jadonfowler.phasebot.cmd.Command;
import xyz.jadonfowler.phasebot.util.UUIDFetcher;

public class UUIDCommand extends Command {

	@Override
	public void exec(String in, String[] args, Session s) {
		try {
			UUIDFetcher uf = new UUIDFetcher(Arrays.asList(args[1]));
			PhaseBot.getBot().say(uf.call().get(args[1]).toString());
		} catch (Exception e) {
		}
	}

	@Override
	public String getCommand() {
		return "uuid";
	}

	@Override
	public String getDescription() {
		return "Get the UUID of a Player";
	}

}
