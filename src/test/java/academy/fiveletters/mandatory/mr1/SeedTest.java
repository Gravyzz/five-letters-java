package academy.fiveletters.mandatory.mr1;

import static org.assertj.core.api.Assertions.assertThat;

import academy.fiveletters.game.GameFactory;
import academy.fiveletters.game.dictionary.WordDictionary;
import academy.fiveletters.game.model.GameSession;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: одинаковый seed обязан давать одинаковое загаданное слово. */
@DisplayName("MR1. Воспроизводимость по seed")
class SeedTest {

    @Test
    @DisplayName("Одинаковый seed даёт одинаковое загаданное слово")
    void sameSeedProducesSameAnswer() {
        WordDictionary dictionary = new WordDictionary(List.of("озеро", "арбуз", "отбор"));
        GameSession session1 = GameFactory.startGame(dictionary, 6, 42);
        GameSession session2 = GameFactory.startGame(dictionary, 6, 42);
        assertThat(session1.getAnswer()).isEqualTo(session2.getAnswer());
    }

    @Test
    @DisplayName("Разные seed'ы дают разные слова хотя бы иногда")
    void differentSeedsProduceDifferentAnswers() {
        WordDictionary dictionary = new WordDictionary(List.of("озеро", "арбуз", "отбор"));
        GameSession session1 = GameFactory.startGame(dictionary, 6, 0);
        boolean foundDifferent = false;
        for (int seed = 1; seed < 100; seed++) {
            GameSession session2 = GameFactory.startGame(dictionary, 6, seed);
            if (!session1.getAnswer().equals(session2.getAnswer())) {
                foundDifferent = true;
            }
        }
        assertThat(foundDifferent).isTrue();
    }
}
