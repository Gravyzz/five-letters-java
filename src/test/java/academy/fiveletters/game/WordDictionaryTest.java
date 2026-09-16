package academy.fiveletters.game;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

class WordDictionaryTest {

    @Test
    void wordWithWrongLengthIsRejected() {
        List<String> list = List.of("озеро", "кот");
        assertThatThrownBy(() -> new WordDictionary(list)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void wordWithNonRussianLettersIsRejected() {
        List<String> list = List.of("ozero");
        assertThatThrownBy(() -> new WordDictionary(list)).isInstanceOf(IllegalArgumentException.class);
    }
}
