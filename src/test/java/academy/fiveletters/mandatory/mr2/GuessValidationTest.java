package academy.fiveletters.mandatory.mr2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import academy.fiveletters.game.GameFactory;
import academy.fiveletters.game.GameRules;
import academy.fiveletters.game.dictionary.WordDictionary;
import academy.fiveletters.game.model.GameSession;
import academy.fiveletters.game.model.GameStatus;
import academy.fiveletters.game.model.GuessResult;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** Обязательные тесты: валидация ввода. */
@DisplayName("MR2. Валидация ввода")
class GuessValidationTest {

    @ParameterizedTest
    @ValueSource(strings = {"дом", "домики", ""})
    @DisplayName("Слово не из 5 букв отклоняется: \"{0}\"")
    void wordOfWrongLengthIsRejected(String guess) {
        WordDictionary dictionary = new WordDictionary(List.of("озеро"));
        GameRules rules = new GameRules(dictionary);
        GameSession session = GameFactory.startGame(dictionary, 6, 0);
        assertThatThrownBy(() -> rules.applyGuess(session, guess)).isInstanceOf(IllegalArgumentException.class);
        assertThat(session.getAttemptsUsed()).isEqualTo(0);
        assertThat(session.getAttemptsHistory()).isEmpty();
        assertThat(session.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
    }

    @ParameterizedTest
    @ValueSource(strings = {"дом12", "дом!!", "до ма"})
    @DisplayName("Ввод с не-буквами отклоняется: \"{0}\"")
    void nonLetterInputIsRejected(String guess) {
        WordDictionary dictionary = new WordDictionary(List.of("озеро"));
        GameRules rules = new GameRules(dictionary);
        GameSession session = GameFactory.startGame(dictionary, 6, 0);
        assertThatThrownBy(() -> rules.applyGuess(session, guess)).isInstanceOf(IllegalArgumentException.class);
        assertThat(session.getAttemptsUsed()).isEqualTo(0);
        assertThat(session.getAttemptsHistory()).isEmpty();
        assertThat(session.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("Слово, которого нет в словаре, отклоняется")
    void wordOutsideDictionaryIsRejected() {
        WordDictionary dictionary = new WordDictionary(List.of("озеро"));
        GameRules rules = new GameRules(dictionary);
        GameSession session = GameFactory.startGame(dictionary, 6, 0);
        assertThatThrownBy(() -> rules.applyGuess(session, "арбуз")).isInstanceOf(IllegalArgumentException.class);
        assertThat(session.getAttemptsUsed()).isEqualTo(0);
        assertThat(session.getAttemptsHistory()).isEmpty();
        assertThat(session.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("Некорректный ввод не тратит попытку")
    void invalidInputDoesNotConsumeAttempt() {
        WordDictionary dictionary = new WordDictionary(List.of("озеро"));
        GameRules rules = new GameRules(dictionary);
        GameSession session = GameFactory.startGame(dictionary, 6, 0);
        assertThatThrownBy(() -> rules.applyGuess(session, "КОТ")).isInstanceOf(IllegalArgumentException.class);
        assertThat(session.getAttemptsUsed()).isEqualTo(0);
        assertThat(session.getAttemptsHistory()).isEmpty();
        assertThat(session.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("Ввод не зависит от регистра: \"ОЗЕРО\" и \"озеро\" обрабатываются одинаково")
    void inputIsCaseInsensitive() {
        WordDictionary dictionary = new WordDictionary(List.of("озеро"));
        GameRules rules = new GameRules(dictionary);
        GameSession session1 = GameFactory.startGame(dictionary, 6, 0);
        GameSession session2 = GameFactory.startGame(dictionary, 6, 0);
        GuessResult upperResult = rules.applyGuess(session1, "ОЗЕРО");
        GuessResult lowerResult = rules.applyGuess(session2, "озеро");
        assertThat(upperResult.getGuess()).isEqualTo("озеро");
        assertThat(lowerResult.getGuess()).isEqualTo("озеро");
        assertThat(upperResult.getLetterStatuses()).isEqualTo(lowerResult.getLetterStatuses());
        assertThat(session1.getStatus()).isEqualTo(GameStatus.WIN);
        assertThat(session2.getStatus()).isEqualTo(GameStatus.WIN);
    }
}
