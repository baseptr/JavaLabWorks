package com.esdc.lab1.processor;

import com.esdc.lab1.client.HttpClientWrapper;
import com.esdc.lab1.command.Command;
import com.esdc.lab1.command.impl.DeleteCommand;
import com.esdc.lab1.command.impl.GetAllCommand;
import com.esdc.lab1.command.impl.GetCommand;
import com.esdc.lab1.command.impl.PostCommand;
import com.esdc.lab1.command.impl.PutCommand;

import java.util.HashMap;

public class CommandProcessor {
    private final HashMap<String, Command> commands = new HashMap<>();

    public CommandProcessor() {
        HttpClientWrapper client = new HttpClientWrapper();
        commands.put("get-all", new GetAllCommand(client));
        commands.put("get", new GetCommand(client));
        commands.put("post", new PostCommand(client));
        commands.put("put", new PutCommand(client));
        commands.put("delete", new DeleteCommand(client));
    }

    public Command get(String command) {
        return commands.get(command);
    }
}
