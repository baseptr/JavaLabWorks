package com.esdc.lab2;


import com.esdc.lab2.service.OrderProcessor;
import com.esdc.lab2.service.StatsProcessor;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        System.out.println("Hello at Java Caffe!");
        System.out.println("Available commands:");
        System.out.println("To see the menu write: menu");
        System.out.println("To do order write: order followed by space and coffee ids separated by space. For example: order 1 2 3 and Your Name");
        System.out.println("To see stats list write: stats list");
        System.out.println("To see stats by id write: stats followed by space and stat id. For example: stats 1");
        System.out.println("To exit write: exit");


        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        while (isRunning) {

            String command = scanner.nextLine().trim();

            if (command.equals("menu")) {
                System.out.println("Showing menu...");
                OrderProcessor.Instance.printMenu();

            } else if (command.startsWith("order ")) {
                // format: order 1 3 5 6 Viktor
                // last token is the name, all tokens in between are coffee ids
                String[] parts = command.substring("order ".length()).trim().split("\\s+");
                if (parts.length < 2) {
                    System.out.println("Invalid order format. Example: order 1 3 5 Viktor");
                } else {
                    String name = parts[parts.length - 1];
                    String[] idTokens = java.util.Arrays.copyOf(parts, parts.length - 1);
                    System.out.println("Ordering coffee...");
                    OrderProcessor.Instance.doOrder(name, idTokens);
                }

            } else if (command.equals("stats list")) {
                System.out.println("Showing stats list...");
                StatsProcessor.Instance.printStatsMenu();

            } else if (command.startsWith("stats ")) {
                // format: stats 1
                String idPart = command.substring("stats ".length()).trim();
                try {
                    int statId = Integer.parseInt(idPart);
                    System.out.println("Showing stats...");
                    StatsProcessor.Instance.showStats(statId);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid stats id: " + idPart);
                }

            } else if (command.equals("exit")) {
                System.out.println("Exiting...");
                isRunning = false;

            } else {
                System.out.println("Unknown command. Please try again.");
            }
        }
    }
}
