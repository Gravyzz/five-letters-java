package academy.fiveletters.game.dictionary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Загрузка словарей")
class DictionaryLoaderTest {

    @ParameterizedTest
    @DisplayName("Дополнительный словарь не пуст и входит в основной")
    @ValueSource(
            strings = {
                "/difficulty-easy.txt",
                "/difficulty-medium.txt",
                "/difficulty-hard.txt",
                "/category-animals.txt",
                "/category-food.txt",
                "/category-nature.txt"
            })
    void additionalDictionaryIsSubsetOfDefault(String resourcePath) {
        WordDictionary fullDictionary = DictionaryLoader.loadDefaultDictionary();
        WordDictionary additionalDictionary = DictionaryLoader.loadDictionary(resourcePath);
        assertThat(additionalDictionary.getWords()).isNotEmpty();
        assertThat(fullDictionary.getWords()).containsAll(additionalDictionary.getWords());
    }

    @Test
    @DisplayName("Отсутствующий словарь отклоняется")
    void missingDictionaryIsRejected() {
        assertThatThrownBy(() -> DictionaryLoader.loadDictionary("/missing.txt"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("/missing.txt");
    }
}
