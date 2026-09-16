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
}
