package academy.fiveletters.game.dictionary;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Словарь слов")
class WordDictionaryTest {

    @Test
    @DisplayName("Слово неправильной длины отклоняется")
    void wordWithWrongLengthIsRejected() {
        List<String> list = List.of("озеро", "кот");
        assertThatThrownBy(() -> new WordDictionary(list)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Слово с нерусскими буквами отклоняется")
    void wordWithNonRussianLettersIsRejected() {
        List<String> list = List.of("ozero");
        assertThatThrownBy(() -> new WordDictionary(list)).isInstanceOf(IllegalArgumentException.class);
    }
}
