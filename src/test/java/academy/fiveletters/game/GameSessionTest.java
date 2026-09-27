package academy.fiveletters.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import academy.fiveletters.game.model.GameSession;
import academy.fiveletters.game.model.GameStatus;
import academy.fiveletters.game.model.GuessResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Игровая сессия")
class GameSessionTest {

    @Test
    @DisplayName("Нулевое количество попыток отклоняется")
    void zeroMaxAttemptsIsRejected() {
        assertThatThrownBy(() -> new GameSession("озеро", 0)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Ответ null отклоняется")
    void nullAnswerIsRejected() {
        assertThatThrownBy(() -> new GameSession(null, 5)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Ответ неправильной длины отклоняется")
    void answerWithWrongLengthIsRejected() {
        assertThatThrownBy(() -> new GameSession("кот", 5)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Новая сессия имеет ожидаемое начальное состояние")
    void newSessionHasExpectedInitialState() {
        GameSession session = new GameSession("озеро", 6);

        assertThat(session.getAnswer()).isEqualTo("озеро");
        assertThat(session.getMaxAttempts()).isEqualTo(6);
        assertThat(session.getAttemptsUsed()).isEqualTo(0);
        assertThat(session.getAttemptsHistory()).isEmpty();
        assertThat(session.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("Принятая попытка обновляет сессию")
    void acceptedGuessUpdatesSession() {
        GameSession session = new GameSession("озеро", 6);
        GuessResult result = new GuessResult("арбуз", LetterMatcher.match("озеро", "арбуз"));
        session.recordGuess(result);
        assertThat(session.getAttemptsUsed()).isEqualTo(1);
        assertThat(session.getAttemptsHistory()).containsExactly("арбуз");
        assertThat(session.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("Правильная последняя попытка завершает игру победой")
    void correctGuessOnLastAttemptWins() {
        GameSession session = new GameSession("озеро", 1);
        GuessResult result = new GuessResult("озеро", LetterMatcher.match("озеро", "озеро"));
        session.recordGuess(result);
        assertThat(session.getAttemptsUsed()).isEqualTo(1);
        assertThat(session.getAttemptsHistory()).containsExactly("озеро");
        assertThat(session.getStatus()).isEqualTo(GameStatus.WIN);
    }

    @Test
    @DisplayName("Неправильная последняя попытка завершает игру поражением")
    void wrongGuessOnLastAttemptLoses() {
        GameSession session = new GameSession("озеро", 1);
        GuessResult result = new GuessResult("арбуз", LetterMatcher.match("озеро", "арбуз"));
        session.recordGuess(result);
        assertThat(session.getAttemptsUsed()).isEqualTo(1);
        assertThat(session.getAttemptsHistory()).containsExactly("арбуз");
        assertThat(session.getStatus()).isEqualTo(GameStatus.LOSE);
    }

    @Test
    @DisplayName("После победы новая попытка отклоняется без изменения сессии")
    void guessAfterWinIsRejectedWithoutChangingSession() {
        GameSession session = new GameSession("озеро", 6);
        GuessResult result1 = new GuessResult("озеро", LetterMatcher.match("озеро", "озеро"));
        session.recordGuess(result1);
        GuessResult result2 = new GuessResult("арбуз", LetterMatcher.match("озеро", "арбуз"));
        assertThatThrownBy(() -> session.recordGuess(result2)).isInstanceOf(IllegalStateException.class);
        assertThat(session.getAttemptsUsed()).isEqualTo(1);
        assertThat(session.getAttemptsHistory()).containsExactly("озеро");
        assertThat(session.getStatus()).isEqualTo(GameStatus.WIN);
    }

    @Test
    @DisplayName("После поражения новая попытка отклоняется без изменения сессии")
    void guessAfterLoseIsRejectedWithoutChangingSession() {
        GameSession session = new GameSession("озеро", 1);
        GuessResult result1 = new GuessResult("арбуз", LetterMatcher.match("озеро", "арбуз"));
        session.recordGuess(result1);
        GuessResult result2 = new GuessResult("лента", LetterMatcher.match("озеро", "лента"));
        assertThatThrownBy(() -> session.recordGuess(result2)).isInstanceOf(IllegalStateException.class);
        assertThat(session.getAttemptsUsed()).isEqualTo(1);
        assertThat(session.getAttemptsHistory()).containsExactly("арбуз");
        assertThat(session.getStatus()).isEqualTo(GameStatus.LOSE);
    }
}
