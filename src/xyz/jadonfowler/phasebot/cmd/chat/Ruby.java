package xyz.jadonfowler.phasebot.cmd.chat;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.spacehq.packetlib.Session;
import xyz.jadonfowler.phasebot.*;
import xyz.jadonfowler.phasebot.cmd.Command;

public class Ruby extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("jruby");
        StringBuilder text = new StringBuilder();
        for (String key : PhaseBot.getBot().getVariables().keySet()) {
            text.append("$" + key + " = \"" + PhaseBot.getBot().getVariables().get(key) + "\";\n");
        }
        for (int i = 1; i < args.length; i++) {
            text.append(args[i] + " ");
        }
        try {
            PhaseBot.getBot().getVariables().put("eval", engine.eval(text.toString()).toString());
            //s.send(new ClientChatPacket("ruby> " + engine.eval(text.toString())));
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    @Override public String getCommand() {
        return "ruby";
    }

    @Override public String getDescription() {
        return "Ruby interpreter";
    }
}
