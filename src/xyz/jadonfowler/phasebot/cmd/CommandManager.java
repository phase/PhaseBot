package xyz.jadonfowler.phasebot.cmd;

import java.util.ArrayList;

import org.spacehq.packetlib.Session;

import xyz.jadonfowler.phasebot.cmd.chat.JavaScript;
import xyz.jadonfowler.phasebot.cmd.chat.Ruby;
import xyz.jadonfowler.phasebot.cmd.chat.Say;
import xyz.jadonfowler.phasebot.cmd.fun.Derp;
import xyz.jadonfowler.phasebot.cmd.fun.Swing;
import xyz.jadonfowler.phasebot.cmd.position.Look;
import xyz.jadonfowler.phasebot.cmd.position.Move;
import xyz.jadonfowler.phasebot.cmd.position.Patrol;
import xyz.jadonfowler.phasebot.cmd.position.Set;
import xyz.jadonfowler.phasebot.cmd.position.Teleport;

public class CommandManager {
	private ArrayList<Command> commands;

	public CommandManager() {
		commands = new ArrayList<Command>();
		new JavaScript();
		new Ruby();
		new Say();
		new Derp();
		new Swing();
		new Look();
		new Move();
		new Patrol();
		new Set();
		new Teleport();
		new Respawn();
	}

	public Command addCommand(Command c) {
		commands.add(c);
		return c;
	}
	
	public void performCommand(String in, String[] args, Session s) {
		for (Command c : commands) {
			if(c.getCommand().equalsIgnoreCase(args[0])){
				c.exec(in, args, s);
			}
		}
	}
	
	public ArrayList<Command> getCommands() {
		return commands;
	}
}
