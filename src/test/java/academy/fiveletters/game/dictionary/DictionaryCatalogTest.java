package academy.fiveletters.game.dictionary;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Каталог словарей")
public class DictionaryCatalogTest {

    @Test
    @DisplayName("Словарь сложности загружается один раз")
    void difficultyDictionaryIsCached() {
        DictionaryCatalog catalog = new DictionaryCatalog();

        WordDictionary firstDictionary = catalog.getDifficultyDictionary(Difficulty.EASY);
        WordDictionary secondDictionary = catalog.getDifficultyDictionary(Difficulty.EASY);

        assertThat(secondDictionary).isSameAs(firstDictionary);
    }

    @Test
    @DisplayName("Словарь категории загружается один раз")
    void categoryDictionaryIsCached() {
        DictionaryCatalog catalog = new DictionaryCatalog();

        WordDictionary firstDictionary = catalog.getCategoryDictionary(WordCategory.ANIMALS);
        WordDictionary secondDictionary = catalog.getCategoryDictionary(WordCategory.ANIMALS);

        assertThat(secondDictionary).isSameAs(firstDictionary);
    }
}
