package academy.fiveletters.game;

import java.util.List;

public final class GuessResult {

    private final String guess;
    private final List<LetterStatus> letterStatuses;

    public GuessResult(String guess, List<LetterStatus> letterStatuses) {
        if (guess == null || guess.length() != GameConfig.WORD_LENGTH) {
            throw new IllegalArgumentException("Попытка должна быть задана и иметь длину " + GameConfig.WORD_LENGTH);
        }

        if (letterStatuses == null || letterStatuses.size() != GameConfig.WORD_LENGTH) {
            throw new IllegalArgumentException(
                    "Список должен существовать и его размер  должен быть  " + GameConfig.WORD_LENGTH);
        }
        this.guess = guess;
        this.letterStatuses = List.copyOf(letterStatuses);
    }

    public String getGuess() {
        return guess;
    }

    public List<LetterStatus> getLetterStatuses() {
        return letterStatuses;
    }
}
