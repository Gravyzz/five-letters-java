package academy.fiveletters.console.runner;

import academy.fiveletters.console.output.ConsoleColors;
import academy.fiveletters.console.output.ResultFormatter;
import academy.fiveletters.game.GameConfig;
import academy.fiveletters.game.GameFactory;
import academy.fiveletters.game.GameRules;
import academy.fiveletters.game.dictionary.WordDictionary;
import academy.fiveletters.game.model.GameSession;
import academy.fiveletters.game.model.GameStatus;
import academy.fiveletters.game.model.GuessResult;

public final class ReplayRunner {
    private static final int MIN_REPLAY_ARGUMENTS = 3;
    private final WordDictionary dictionary;
    private final GameRules rules;
    private final ResultFormatter resultFormatter;
    private final ConsoleGameRunner gameRunner;

    public ReplayRunner(
            WordDictionary dictionary, GameRules rules, ResultFormatter resultFormatter, ConsoleGameRunner gameRunner) {
        this.dictionary = dictionary;
        this.rules = rules;
        this.resultFormatter = resultFormatter;
        this.gameRunner = gameRunner;
    }

    public void run(String[] args) {
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
                System.out.println(resultFormatter.format(result));
                System.out.println("Осталось попыток: " + (session.getMaxAttempts() - session.getAttemptsUsed()));
            } catch (IllegalArgumentException exception) {
                System.out.println(ConsoleColors.red(String.valueOf(exception.getMessage())));
            }
        }
        if (session.getStatus() == GameStatus.IN_PROGRESS) {
            System.out.println(ConsoleColors.yellow("replay закончился раньше партии"));
        } else {
            gameRunner.printOutcome(session);
        }
    }
}
