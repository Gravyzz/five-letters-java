package academy.fiveletters.mandatory.mr1;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import academy.fiveletters.game.DictionaryLoader;
import academy.fiveletters.game.WordDictionary;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: словарь. */
@DisplayName("MR1. Словарь")
class DictionaryTest {

    @Test
    @DisplayName("Словарь содержит не меньше 50 слов")
    void dictionaryContainsAtLeastFiftyWords() {
        WordDictionary dictionary = DictionaryLoader.loadDefaultDictionary();
        assertTrue(dictionary.size() >= 50);
    }

    @Test
    @DisplayName("Все слова словаря состоят ровно из 5 букв")
    void allWordsAreExactlyFiveLettersLong() {
        WordDictionary dictionary = DictionaryLoader.loadDefaultDictionary();
        for (String word : dictionary.getWords()) {
            assertTrue(word.length() == 5);
        }
    }

    @Test
    @DisplayName("Пустой словарь приводит к ошибке, а не к запуску игры без слова")
    void emptyDictionaryIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new WordDictionary(List.of()));
    }
}
