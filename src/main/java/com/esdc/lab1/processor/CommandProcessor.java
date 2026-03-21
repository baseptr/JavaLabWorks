package com.esdc.lab1.processor;

import com.esdc.lab1.client.ApiHttpClient;
import com.esdc.lab1.command.Command;
import com.esdc.lab1.command.impl.*;

import java.util.HashMap;

public class CommandProcessor {
    private final HashMap<String, Command> cmd = new HashMap<>();
    private final ApiHttpClient client;

    public CommandProcessor(ApiHttpClient client) {
        this.client = client;
        cmd.put("get-all", new GetAllCommand(client));
        cmd.put("get", new GetCommand(client));
        cmd.put("post", new PostCommand(client));
        cmd.put("put", new PutCommand(client));
        cmd.put("delete", new DeleteCommand(client));
        cmd.put("tg-me", new GetMeCommand(client));
        cmd.put("tg-updates", new GetUpdatesCommand(client));
        cmd.put("tg-send", new SendMessageCommand(client));

    }

    public Command get(String command) {
        return cmd.get(command);
    }
    public String getAll(){
        StringBuilder sb = new StringBuilder();
        for (var entry : cmd.entrySet())
        {
            sb.append(entry.getKey()).append(" ");
        }
        return sb.toString();
    }
}
