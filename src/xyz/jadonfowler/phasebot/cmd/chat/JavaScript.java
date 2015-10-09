package xyz.jadonfowler.phasebot.cmd.chat;

import javax.script.*;
import org.spacehq.packetlib.*;
import xyz.jadonfowler.phasebot.*;
import xyz.jadonfowler.phasebot.cmd.*;

public class JavaScript extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        StringBuilder text = new StringBuilder();
        for (String key : PhaseBot.getBot().getVariables().keySet()) {
            text.append("var " + key + " = \"" + PhaseBot.getBot().getVariables().get(key) + "\";\n");
        }
        for (int i = 1; i < args.length; i++) {
            text.append(args[i] + " ");
        }
        //PhaseBot.getConsole().println(text.toString());
        try {
            //PhaseBot.getConsole().println(engine.eval(text.toString()).toString());
            PhaseBot.getBot().getVariables().put("eval", engine.eval(text.toString()).toString());
            //s.send(new ClientChatPacket("js> " + engine.eval(text.toString())));
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    @Override public String getCommand() {
        return "js";
    }

    @Override public String getDescription() {
        return "A JavaScript interpreter";
    }
}
