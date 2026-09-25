package academy.fiveletters.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class GameSessionTest {

    @Test
    void zeroMaxAttemptsIsRejected() {
        assertThatThrownBy(() -> new GameSession("озеро", 0)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nullAnswerIsRejected() {
        assertThatThrownBy(() -> new GameSession(null, 5)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void answerWithWrongLengthIsRejected() {
        assertThatThrownBy(() -> new GameSession("кот", 5)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void newSessionHasExpectedInitialState() {
        GameSession session = new GameSession("озеро", 6);

        assertThat(session.getAnswer()).isEqualTo("озеро");
        assertThat(session.getMaxAttempts()).isEqualTo(6);
        assertThat(session.getAttemptsUsed()).isEqualTo(0);
        assertThat(session.getAttemptsHistory()).isEmpty();
        assertThat(session.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
    }

    @Test
    void acceptedGuessUpdatesSession() {
        GameSession session = new GameSession("озеро", 6);
        GuessResult result = new GuessResult("арбуз", LetterMatcher.match("озеро", "арбуз"));
        session.recordGuess(result);
        assertThat(session.getAttemptsUsed()).isEqualTo(1);
        assertThat(session.getAttemptsHistory()).containsExactly("арбуз");
        assertThat(session.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
    }

    @Test
    void correctGuessOnLastAttemptWins() {
        GameSession session = new GameSession("озеро", 1);
        GuessResult result = new GuessResult("озеро", LetterMatcher.match("озеро", "озеро"));
        session.recordGuess(result);
        assertThat(session.getAttemptsUsed()).isEqualTo(1);
        assertThat(session.getAttemptsHistory()).containsExactly("озеро");
        assertThat(session.getStatus()).isEqualTo(GameStatus.WIN);
    }

    @Test
    void wrongGuessOnLastAttemptLoses() {
        GameSession session = new GameSession("озеро", 1);
        GuessResult result = new GuessResult("арбуз", LetterMatcher.match("озеро", "арбуз"));
        session.recordGuess(result);
        assertThat(session.getAttemptsUsed()).isEqualTo(1);
        assertThat(session.getAttemptsHistory()).containsExactly("арбуз");
        assertThat(session.getStatus()).isEqualTo(GameStatus.LOSE);
    }

    @Test
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
