package academy.fiveletters.game;

import java.util.List;

public final class LetterMatcher {

    private LetterMatcher() {}

    public static List<LetterStatus> match(String answer, String guess) {
        if (answer == null || answer.length() != GameConfig.WORD_LENGTH) {
            throw new IllegalArgumentException("Некорректное слово ответа");
        }

        if (guess == null || guess.length() != GameConfig.WORD_LENGTH) {
            throw new IllegalArgumentException("Некорректное слово попытки");
        }

        LetterStatus[] letterStatuses = new LetterStatus[GameConfig.WORD_LENGTH];
        boolean[] usedOccurrences = new boolean[GameConfig.WORD_LENGTH];

        for (int i = 0; i < GameConfig.WORD_LENGTH; i++) {
            if (answer.charAt(i) == guess.charAt(i)) {
                letterStatuses[i] = LetterStatus.CORRECT;
                usedOccurrences[i] = true;
            }
        }

        for (int guessIndex = 0; guessIndex < GameConfig.WORD_LENGTH; guessIndex++) {
            if (letterStatuses[guessIndex] == LetterStatus.CORRECT) {
                continue;
            }
            letterStatuses[guessIndex] = LetterStatus.ABSENT;
            for (int answerIndex = 0; answerIndex < GameConfig.WORD_LENGTH; answerIndex++) {
                if (!usedOccurrences[answerIndex] && answer.charAt(answerIndex) == guess.charAt(guessIndex)) {
                    usedOccurrences[answerIndex] = true;
                    letterStatuses[guessIndex] = LetterStatus.PRESENT;
                    break;
                }
            }
        }

        return List.of(letterStatuses);
    }
}
