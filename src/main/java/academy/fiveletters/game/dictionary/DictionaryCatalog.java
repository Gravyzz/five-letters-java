package academy.fiveletters.game.dictionary;

import java.util.EnumMap;
import java.util.Map;

public class DictionaryCatalog {
    private final WordDictionary fullDictionary;
    private final Map<Difficulty, WordDictionary> difficultyDictionaries;
    private final Map<WordCategory, WordDictionary> categoryDictionaries;

    public DictionaryCatalog() {
        fullDictionary = DictionaryLoader.loadDefaultDictionary();

        difficultyDictionaries = new EnumMap<>(Difficulty.class);
        for (Difficulty difficulty : Difficulty.values()) {
            WordDictionary dictionary = DictionaryLoader.loadDictionary(difficulty.getResourcePath());
            difficultyDictionaries.put(difficulty, dictionary);
        }

        categoryDictionaries = new EnumMap<>(WordCategory.class);
        for (WordCategory category : WordCategory.values()) {
            WordDictionary dictionary = DictionaryLoader.loadDictionary(category.getResourcePath());
            categoryDictionaries.put(category, dictionary);
        }
    }

    public WordDictionary getFullDictionary() {
        return fullDictionary;
    }

    public WordDictionary getDifficultyDictionary(Difficulty difficulty) {
        WordDictionary dictionary = difficultyDictionaries.get(difficulty);
        if (dictionary == null) {
            throw new IllegalStateException("Не загружен словарь сложности: " + difficulty);
        }
        return dictionary;
    }

    public WordDictionary getCategoryDictionary(WordCategory category) {
        WordDictionary dictionary = categoryDictionaries.get(category);
        if (dictionary == null) {
            throw new IllegalStateException("Не загружен словарь категории: " + category);
        }
        return dictionary;
    }
}
