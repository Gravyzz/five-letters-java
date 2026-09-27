package academy.fiveletters.console.runner;

import academy.fiveletters.console.output.ConsoleColors;
import academy.fiveletters.console.output.ResultFormatter;
import academy.fiveletters.game.GameRules;
import academy.fiveletters.game.model.GameSession;
import academy.fiveletters.game.model.GameStatus;
import academy.fiveletters.game.model.GuessResult;
import java.util.Scanner;

public final class ConsoleGameRunner {
    private static final int MIN_ATTEMPTS_WITH_FEW_FORM = 2;
    private static final int MAX_ATTEMPTS_WITH_FEW_FORM = 4;

    private final GameRules rules;
    private final ResultFormatter resultFormatter;

    public ConsoleGameRunner(GameRules rules, ResultFormatter resultFormatter) {
        this.rules = rules;
        this.resultFormatter = resultFormatter;
    }

    public void play(Scanner scanner, GameSession session) {
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
                System.out.println(resultFormatter.format(result));
                System.out.println("Осталось попыток: " + (session.getMaxAttempts() - session.getAttemptsUsed()));
            } catch (IllegalArgumentException exception) {
                System.out.println(ConsoleColors.red(String.valueOf(exception.getMessage())));
            }
        }

        printOutcome(session);
    }

    public void printOutcome(GameSession session) {
        int attemptsUsed = session.getAttemptsUsed();
        if (session.getStatus() == GameStatus.WIN) {
            System.out.println(
                    ConsoleColors.green("Победа! Слово угадано за " + attemptsUsed + " " + attemptsWord(attemptsUsed)));
        } else {
            System.out.println(ConsoleColors.red("Неудача. Загаданное слово: " + session.getAnswer()));
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
