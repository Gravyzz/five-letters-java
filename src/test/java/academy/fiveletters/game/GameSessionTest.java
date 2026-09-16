package academy.fiveletters.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GameSessionTest {

    @Test
    void zeroMaxAttemptsIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new GameSession("озеро", 0));
    }

    @Test
    void nullAnswerIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new GameSession(null, 5));
    }

    @Test
    void answerWithWrongLengthIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new GameSession("кот", 5));
    }

    @Test
    void newSessionHasExpectedInitialState() {
        GameSession session = new GameSession("озеро", 6);

        assertEquals("озеро", session.getAnswer());
        assertEquals(6, session.getMaxAttempts());
        assertEquals(0, session.getAttemptsUsed());
        assertTrue(session.getAttemptsHistory().isEmpty());
        assertEquals(GameStatus.IN_PROGRESS, session.getStatus());
    }
}
