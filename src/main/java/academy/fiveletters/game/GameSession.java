package academy.fiveletters.game;

import java.util.ArrayList;
import java.util.List;

public final class GameSession {

    private final String answer;
    private final int maxAttempts;
    private int attemptsUsed;
    private final List<String> attemptsHistory;
    private GameStatus status;

    GameSession(String answer, int maxAttempts) {

        if (maxAttempts <= 0) {
            throw new IllegalArgumentException("Количество попыток должно быть положительным");
        }

        if (answer == null || answer.length() != GameConfig.WORD_LENGTH) {
            throw new IllegalArgumentException("Ответ должен быть задан и иметь длину " + GameConfig.WORD_LENGTH);
        }

        this.answer = answer;
        this.maxAttempts = maxAttempts;
        this.attemptsUsed = 0;
        this.attemptsHistory = new ArrayList<>();
        this.status = GameStatus.IN_PROGRESS;
    }

    public String getAnswer() {
        return answer;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getAttemptsUsed() {
        return attemptsUsed;
    }

    public GameStatus getStatus() {
        return status;
    }

    public List<String> getAttemptsHistory() {
        return List.copyOf(attemptsHistory);
    }

    void recordGuess(GuessResult result) {
        if (status != GameStatus.IN_PROGRESS) {
            throw new IllegalStateException("Нельзя добавить попытку в завершённую партию");
        }

        if (result == null) {
            throw new IllegalArgumentException("Результат попытки не может быть null");
        }

        String guess = result.getGuess();
        attemptsHistory.add(guess);
        attemptsUsed++;

        if (guess.equals(answer)) {
            status = GameStatus.WIN;
        } else if (attemptsUsed == maxAttempts) {
            status = GameStatus.LOSE;
        }
    }
}
