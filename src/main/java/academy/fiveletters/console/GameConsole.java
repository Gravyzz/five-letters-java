package academy.fiveletters.console;

import academy.fiveletters.game.DictionaryLoader;
import academy.fiveletters.game.Difficulty;
import academy.fiveletters.game.GameConfig;
import academy.fiveletters.game.GameFactory;
import academy.fiveletters.game.GameRules;
import academy.fiveletters.game.GameSession;
import academy.fiveletters.game.GameStatistics;
import academy.fiveletters.game.GameStatus;
import academy.fiveletters.game.GuessResult;
import academy.fiveletters.game.LetterStatus;
import academy.fiveletters.game.WordCategory;
import academy.fiveletters.game.WordDictionary;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

public class GameConsole {
    private static final int MIN_REPLAY_ARGUMENTS = 3;
    private static final int MIN_ATTEMPTS_WITH_FEW_FORM = 2;
    private static final int MAX_ATTEMPTS_WITH_FEW_FORM = 4;

    public void run(String[] args) {

        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        WordDictionary fullDictionary = DictionaryLoader.loadDefaultDictionary();
        GameRules rules = new GameRules(fullDictionary);
        Random random = new Random();
        GameStatistics statistics = new GameStatistics();

        if (args.length > 0 && "--replay".equals(args[0])) {
            runReplay(args, fullDictionary, rules);
            return;
        }
        if (!areLaunchArgumentsValid(args)) {
            return;
        }
        runMenu(scanner, args, fullDictionary, rules, random, statistics);
    }

    private void playGame(Scanner scanner, GameRules rules, GameSession session) {
        System.out.println("Для подсказки введите: ?\n" + "Подсказка доступна один раз за партию");
        boolean hintUsed = false;
        while (session.getStatus() == GameStatus.IN_PROGRESS) {
            System.out.println("Введите слово: ");
            if (!scanner.hasNextLine()) {
                return;
            }
            String guess = scanner.nextLine();
            if ("?".equals(guess)) {
                if (!hintUsed) {

                    System.out.println(ConsoleColors.yellow(
                            "Подсказка: первая буква — " + session.getAnswer().charAt(0)));
                    hintUsed = true;

                } else {
                    System.out.println(ConsoleColors.yellow("Подсказка уже использована"));
                }
                continue;
            }
            try {
                GuessResult result = rules.applyGuess(session, guess);
                System.out.println(formatResult(result));
                System.out.println("Осталось попыток: " + (session.getMaxAttempts() - session.getAttemptsUsed()));
            } catch (IllegalArgumentException exception) {
                System.out.println(ConsoleColors.red(String.valueOf(exception.getMessage())));
            }
        }

        printGameOutcome(session);
    }

    private String formatResult(GuessResult result) {
        StringBuilder builder = new StringBuilder();
        List<LetterStatus> list = result.getLetterStatuses();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == LetterStatus.CORRECT) {
                builder.append("✅");
            } else if (list.get(i) == LetterStatus.PRESENT) {
                builder.append("\uD83D\uDFE1");
            } else if (list.get(i) == LetterStatus.ABSENT) {
                builder.append("❌");
            }
        }
        builder.append(" ");
        builder.append(result.getGuess());
        return builder.toString();
    }

    private GameSession createSession(String[] args, WordDictionary dictionary, Random random) {
        if (args.length == 0) {
            return GameFactory.startGame(dictionary, GameConfig.MAX_ATTEMPTS, random.nextLong());
        } else if (args.length == 2 && "--seed".equals(args[0])) {
            return GameFactory.startGame(dictionary, GameConfig.MAX_ATTEMPTS, Long.parseLong(args[1]));
        } else {
            throw new IllegalArgumentException("Неподходящий формат запуска");
        }
    }

    private void printStatistics(GameStatistics statistics) {
        System.out.println("Сыграно партий: " + statistics.getGamesPlayed());
        System.out.println("Побед: " + statistics.getWins());
        System.out.println("Поражений: " + statistics.getLosses());
    }

    private void printGameOutcome(GameSession session) {
        int attemptsUsed = session.getAttemptsUsed();
        if (session.getStatus() == GameStatus.WIN) {
            System.out.println(
                    ConsoleColors.green("Победа! Слово угадано за " + attemptsUsed + " " + attemptsWord(attemptsUsed)));
        } else {
            System.out.println(ConsoleColors.red("Неудача. Загаданное слово: " + session.getAnswer()));
        }
    }

    private void runReplay(String[] args, WordDictionary dictionary, GameRules rules) {
        if (args.length < MIN_REPLAY_ARGUMENTS) {
            System.out.println(ConsoleColors.red("Формат: --replay <seed> <попытки>"));
            return;
        }
        long seed;
        try {
            seed = Long.parseLong(args[1]);
        } catch (NumberFormatException exception) {
            System.out.println(ConsoleColors.red("Seed должен быть целым числом"));
            return;
        }

        GameSession session = GameFactory.startGame(dictionary, GameConfig.MAX_ATTEMPTS, seed);
        for (int i = 2; i < args.length && session.getStatus() == GameStatus.IN_PROGRESS; i++) {
            try {

                GuessResult result = rules.applyGuess(session, args[i]);
                System.out.println(formatResult(result));
                System.out.println("Осталось попыток: " + (session.getMaxAttempts() - session.getAttemptsUsed()));
            } catch (IllegalArgumentException exception) {
                System.out.println(ConsoleColors.red(String.valueOf(exception.getMessage())));
            }
        }
        if (session.getStatus() == GameStatus.IN_PROGRESS) {
            System.out.println(ConsoleColors.yellow("replay закончился раньше партии"));
        } else {
            printGameOutcome(session);
        }
    }

    private Optional<Difficulty> chooseDifficulty(Scanner scanner) {
        while (true) {
            System.out.print("""
        1 - Лёгкий уровень
        2 - Средний уровень
        3 - Сложный уровень
        0 - Назад
        """);

            if (!scanner.hasNextLine()) {
                return Optional.empty();
            }

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    return Optional.of(Difficulty.EASY);
                }
                case "2" -> {
                    return Optional.of(Difficulty.MEDIUM);
                }
                case "3" -> {
                    return Optional.of(Difficulty.HARD);
                }
                case "0" -> {
                    return Optional.empty();
                }
                default -> System.out.println("Ошибка выбора сложности");
            }
        }
    }

    private void playAndRecordGame(Scanner scanner, GameRules rules, GameSession session, GameStatistics statistics) {
        playGame(scanner, rules, session);
        if (session.getStatus() != GameStatus.IN_PROGRESS) {
            statistics.recordGame(session.getStatus());
        }
    }

    private Optional<WordCategory> chooseCategory(Scanner scanner) {
        while (true) {
            System.out.print("""
        1 - Животные
        2 - Еда
        3 - Природа
        0 - Назад
        """);
            if (!scanner.hasNextLine()) {
                return Optional.empty();
            }

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    return Optional.of(WordCategory.ANIMALS);
                }
                case "2" -> {
                    return Optional.of(WordCategory.FOOD);
                }
                case "3" -> {
                    return Optional.of(WordCategory.NATURE);
                }
                case "0" -> {
                    return Optional.empty();
                }
                default -> System.out.println("Ошибка выбора категории");
            }
        }
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

    private void runMenu(
            Scanner scanner,
            String[] args,
            WordDictionary fullDictionary,
            GameRules rules,
            Random random,
            GameStatistics statistics) {
        while (true) {
            System.out.println("1 - Новая игра");
            System.out.println("2 - Статистика");
            System.out.println("3 - Игра по сложности");
            System.out.println("4 - Игра по категории");
            System.out.println("0 - Выход");

            if (!scanner.hasNextLine()) {
                return;
            }

            String choice = scanner.nextLine();
            if ("0".equals(choice)) {
                System.out.println("Выход");
                break;
            } else if ("1".equals(choice)) {
                System.out.println("Новая игра");
                GameSession session = createSession(args, fullDictionary, random);
                playAndRecordGame(scanner, rules, session, statistics);

            } else if ("2".equals(choice)) {
                printStatistics(statistics);
            } else if ("3".equals(choice)) {
                Optional<Difficulty> difficultySelection = chooseDifficulty(scanner);

                if (difficultySelection.isEmpty()) {
                    continue;
                }

                Difficulty difficulty = difficultySelection.get();

                WordDictionary difficultyDictionary = DictionaryLoader.loadDictionary(difficulty.getResourcePath());

                GameSession session = createSession(args, difficultyDictionary, random);
                playAndRecordGame(scanner, rules, session, statistics);
            } else if ("4".equals(choice)) {
                Optional<WordCategory> wordCategorySelection = chooseCategory(scanner);

                if (wordCategorySelection.isEmpty()) {
                    continue;
                }

                WordCategory wordCategory = wordCategorySelection.get();

                WordDictionary categoryDictionary = DictionaryLoader.loadDictionary(wordCategory.getResourcePath());

                GameSession session = createSession(args, categoryDictionary, random);
                playAndRecordGame(scanner, rules, session, statistics);
            } else {
                System.out.println("Неверный пункт меню");
            }
        }
    }

    private String attemptsWord(int attempts) {
        if (attempts == 1) {
            return "попытку";
        } else if (attempts >= MIN_ATTEMPTS_WITH_FEW_FORM && attempts <= MAX_ATTEMPTS_WITH_FEW_FORM) {
            return "попытки";
        } else {
            return "попыток";
        }
    }
}
