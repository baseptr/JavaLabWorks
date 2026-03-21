package com.esdc.lab1;

import com.esdc.lab1.client.ApiHttpClient;
import com.esdc.lab1.command.Command;
import com.esdc.lab1.processor.CommandProcessor;

import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        ApiHttpClient client = new ApiHttpClient();
        CommandProcessor cp = new CommandProcessor(client);
        Scanner sc = new Scanner(System.in);

        System.out.println("Available commands for testing: ");
        System.out.println(cp.getAll());
        while (true) {
            String in = sc.nextLine().trim();
            if ("exit".equals(in)) break;
            Command cmd = cp.get(in);
            if (cmd == null) {
                System.out.println("Command isn't supported");
                continue;
            }
            cmd.execute(sc);
        }
    }
}
