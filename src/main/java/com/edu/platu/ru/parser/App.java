package com.edu.platu.ru.parser;

import java.util.Scanner;

public class App {
    private static final String PATH = "access.log";
    private static float ratioLimit;
    private static int procTimeLimit;
    private static String ar;
    private static Scanner console = new Scanner(System.in);


    public static void main(String[] args) {
        invokeParser(args);
    }

    private static void invokeParser(String[] args) {
        ratioLimit = -1;
        procTimeLimit = -1;
        try {
            for (int i = 0; i < args.length; i++) {
                if ("-u".equals(args[i])) {
                    ratioLimit = Float.parseFloat(args[i + 1]);
                } else if ("-t".equals(args[i])) {
                    procTimeLimit = Integer.parseInt(args[i + 1]);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Bad params.");
            userInput();
        } catch (NumberFormatException e) {
            System.out.println("Bad numeric params.");
            userInput();
        }

        if (-1 != ratioLimit && -1 != procTimeLimit) {
            LogParser logParser = new LogParser(PATH, procTimeLimit, ratioLimit);
            logParser.handle();
        }
        userInput();// вконце входим в режим ввода пользователем
    }

    private static void userInput() {
        ar = console.nextLine();
        if ("exit".equals(ar)) {
            System.exit(0);
        }
        invokeParser(ar.split(" "));
    }

}
