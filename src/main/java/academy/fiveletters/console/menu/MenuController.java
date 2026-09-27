package academy.fiveletters.console.menu;

import academy.fiveletters.console.runner.ConsoleGameRunner;
import academy.fiveletters.game.GameConfig;
import academy.fiveletters.game.GameFactory;
import academy.fiveletters.game.GameStatistics;
import academy.fiveletters.game.dictionary.DictionaryCatalog;
import academy.fiveletters.game.dictionary.Difficulty;
import academy.fiveletters.game.dictionary.WordCategory;
import academy.fiveletters.game.dictionary.WordDictionary;
import academy.fiveletters.game.model.GameSession;
import academy.fiveletters.game.model.GameStatus;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

public final class MenuController {
    private final Scanner scanner;
    private final String[] args;
    private final DictionaryCatalog catalog;
    private final ConsoleGameRunner gameRunner;
    private final Random random;
    private final GameStatistics statistics = new GameStatistics();

    public MenuController(
            Scanner scanner, String[] args, DictionaryCatalog catalog, ConsoleGameRunner gameRunner, Random random) {
        this.scanner = scanner;
        this.args = args;
        this.catalog = catalog;
        this.gameRunner = gameRunner;
        this.random = random;
    }

    private GameSession createSession(WordDictionary dictionary) {
        if (args.length == 0) {
            return GameFactory.startGame(dictionary, GameConfig.MAX_ATTEMPTS, random.nextLong());
        } else if (args.length == 2 && "--seed".equals(args[0])) {
            return GameFactory.startGame(dictionary, GameConfig.MAX_ATTEMPTS, Long.parseLong(args[1]));
        } else {
            throw new IllegalArgumentException("Неподходящий формат запуска");
        }
    }

    private void printStatistics() {
        System.out.println("Сыграно партий: " + statistics.getGamesPlayed());
        System.out.println("Побед: " + statistics.getWins());
        System.out.println("Поражений: " + statistics.getLosses());
    }

    private Optional<Difficulty> chooseDifficulty() {
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

    private void playAndRecordGame(GameSession session) {
        gameRunner.play(scanner, session);
        if (session.getStatus() != GameStatus.IN_PROGRESS) {
            statistics.recordGame(session.getStatus());
        }
    }

    private Optional<WordCategory> chooseCategory() {
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

    public void run() {
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

            switch (choice) {
                case "0" -> {
                    System.out.println("Выход");
                    return;
                }
                case "1" -> {
                    System.out.println("Новая игра");
                    GameSession session = createSession(catalog.getFullDictionary());
                    playAndRecordGame(session);
                }
                case "2" -> {
                    printStatistics();
                }
                case "3" -> {
                    Optional<Difficulty> difficultySelection = chooseDifficulty();

                    if (difficultySelection.isEmpty()) {
                        continue;
                    }

                    Difficulty difficulty = difficultySelection.get();

                    WordDictionary difficultyDictionary = catalog.getDifficultyDictionary(difficulty);

                    GameSession session = createSession(difficultyDictionary);
                    playAndRecordGame(session);
                }
                case "4" -> {
                    Optional<WordCategory> wordCategorySelection = chooseCategory();

                    if (wordCategorySelection.isEmpty()) {
                        continue;
                    }

                    WordCategory wordCategory = wordCategorySelection.get();

                    WordDictionary categoryDictionary = catalog.getCategoryDictionary(wordCategory);

                    GameSession session = createSession(categoryDictionary);
                    playAndRecordGame(session);
                }
                default -> System.out.println("Неверный пункт меню");
            }
        }
    }
}
