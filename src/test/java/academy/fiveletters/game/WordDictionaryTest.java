package academy.fiveletters.game;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class WordDictionaryTest {

    @Test
    void wordWithWrongLengthIsRejected() {
        List<String> list = List.of("озеро", "кот");
        assertThrows(IllegalArgumentException.class, () -> new WordDictionary(list));
    }

    @Test
    void wordWithNonRussianLettersIsRejected() {
        List<String> list = List.of("ozero");
        assertThrows(IllegalArgumentException.class, () -> new WordDictionary(list));
    }
}
