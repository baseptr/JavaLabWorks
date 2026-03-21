package com.esdc.lab1;

import com.esdc.lab1.command.Command;
import com.esdc.lab1.processor.CommandProcessor;

import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        CommandProcessor cp = new CommandProcessor();
        Scanner sc = new Scanner(System.in);
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
