package academy.fiveletters.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DictionaryLoaderTest {

    @ParameterizedTest
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
    void missingDictionaryIsRejected() {
        assertThatThrownBy(() -> DictionaryLoader.loadDictionary("/missing.txt"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("/missing.txt");
    }
}
