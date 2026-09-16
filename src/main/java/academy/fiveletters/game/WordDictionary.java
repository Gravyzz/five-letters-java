package academy.fiveletters.game;

import java.util.List;
import java.util.regex.Pattern;

public final class WordDictionary {
    private final List<String> words;

    private static final Pattern VALID_WORD_PATTERN = Pattern.compile("[а-я]{" + GameConfig.WORD_LENGTH + "}");

    public WordDictionary(List<String> words) {
        if (words == null || words.isEmpty()) {
            throw new IllegalArgumentException("Словарь должен быть задан и содержать хотя бы одно слово");
        }
        for (String word : words) {
            if (word == null || !VALID_WORD_PATTERN.matcher(word).matches()) {
                throw new IllegalArgumentException("Некорректное слово в словаре: " + word);
            }
        }
        this.words = List.copyOf(words);
    }

    public int size() {
        return words.size();
    }

    public List<String> getWords() {
        return words;
    }
}
