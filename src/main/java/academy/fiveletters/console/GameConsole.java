package academy.fiveletters.console;

import academy.fiveletters.console.menu.MenuController;
import academy.fiveletters.console.output.ResultFormatter;
import academy.fiveletters.console.runner.ConsoleGameRunner;
import academy.fiveletters.console.runner.ReplayRunner;
import academy.fiveletters.game.GameRules;
import academy.fiveletters.game.dictionary.DictionaryCatalog;
import academy.fiveletters.game.dictionary.WordDictionary;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

public class GameConsole {

    private final ResultFormatter resultFormatter = new ResultFormatter();

    public void run(String[] args) {

        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        DictionaryCatalog catalog = new DictionaryCatalog();
        WordDictionary fullDictionary = catalog.getFullDictionary();
        GameRules rules = new GameRules(fullDictionary);
        ConsoleGameRunner gameRunner = new ConsoleGameRunner(rules, resultFormatter);
        ReplayRunner replayRunner = new ReplayRunner(fullDictionary, rules, resultFormatter, gameRunner);
        Random random = new Random();

        if (args.length > 0 && "--replay".equals(args[0])) {
            replayRunner.run(args);
            return;
        }
        if (!areLaunchArgumentsValid(args)) {
            return;
        }
        MenuController menuController = new MenuController(scanner, args, catalog, gameRunner, random);
        menuController.run();
    }

    private boolean areLaunchArgumentsValid(String[] args) {
        if (args.length == 0) {
            return true;
        } else if (args.length != 2 || !"--seed".equals(args[0])) {
            System.out.println("Неверный формат");
            return false;
        } else {
            try {
                Long.parseLong(args[1]);
                return true;
            } catch (NumberFormatException exception) {
                System.out.println("Должно быть целое число");
                return false;
            }
        }
    }
}
