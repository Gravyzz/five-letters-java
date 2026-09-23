package academy.fiveletters.game;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GameRules {
    private static final Pattern RUSSIAN_LETTERS_PATTERN = Pattern.compile("[а-я]+");
    private final WordDictionary dictionary;

    public GameRules(WordDictionary dictionary) {
        if (dictionary == null) {
            throw new IllegalArgumentException("Словарь не может быть null");
        }

        this.dictionary = dictionary;
    }

    public GuessResult applyGuess(GameSession session, String guess) {
        String normalizedGuess = validateAndNormalizeGuess(session, guess);
        List<LetterStatus> letterStatuses = LetterMatcher.match(session.getAnswer(), normalizedGuess);
        GuessResult result = new GuessResult(normalizedGuess, letterStatuses);
        session.recordGuess(result);
        return result;
    }

    private String validateAndNormalizeGuess(GameSession session, String guess) {
        if (session == null) {
            throw new IllegalArgumentException("Сессия не должна быть null");
        }

        if (session.getStatus() != GameStatus.IN_PROGRESS) {
            throw new IllegalStateException("Недопустимое состояние сессии");
        }

        if (guess == null) {
            throw new IllegalArgumentException("Попытка не должна быть null");
        }
        String normalizedGuess = guess.toLowerCase(Locale.ROOT);
        if (normalizedGuess.length() != GameConfig.WORD_LENGTH) {
            throw new IllegalArgumentException("Длина слова должна быть равна " + GameConfig.WORD_LENGTH);
        }

        Matcher matcher = RUSSIAN_LETTERS_PATTERN.matcher(normalizedGuess);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Слово должно содержать только допустимые русские буквы");
        }

        if (!dictionary.contains(normalizedGuess)) {
            throw new IllegalArgumentException("Отсутствует слово в словаре");
        }
        return normalizedGuess;
    }
}
