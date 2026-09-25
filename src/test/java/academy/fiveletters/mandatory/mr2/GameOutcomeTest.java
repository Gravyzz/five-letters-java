package academy.fiveletters.mandatory.mr2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import academy.fiveletters.game.GameFactory;
import academy.fiveletters.game.GameRules;
import academy.fiveletters.game.GameSession;
import academy.fiveletters.game.GameStatus;
import academy.fiveletters.game.WordDictionary;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: завершение партии. */
@DisplayName("MR2. Победа и поражение")
class GameOutcomeTest {

    @Test
    @DisplayName("Угаданное слово переводит сессию в статус WIN")
    void correctGuessWinsTheGame() {
        WordDictionary dictionary = new WordDictionary(List.of("озеро"));
        GameRules rules = new GameRules(dictionary);
        GameSession session = GameFactory.startGame(dictionary, 6, 0);
        rules.applyGuess(session, "озеро");
        assertThat(session.getStatus()).isEqualTo(GameStatus.WIN);
        assertThat(session.getAttemptsUsed()).isEqualTo(1);
        assertThat(session.getAttemptsHistory()).containsExactly("озеро");
    }

    @Test
    @DisplayName("После 6 неудачных попыток сессия переходит в статус LOSE")
    void sixFailedAttemptsLoseTheGame() {
        WordDictionary dictionary = new WordDictionary(List.of("озеро", "арбуз"));
        GameRules rules = new GameRules(dictionary);
        GameSession session = GameFactory.startGame(dictionary, 6, 0);
        String answer = session.getAnswer();
        String wrongGuess;
        if ("озеро".equals(answer)) {
            wrongGuess = "арбуз";
        } else {
            wrongGuess = "озеро";
        }
        for (int i = 0; i < 6; i++) {
            rules.applyGuess(session, wrongGuess);
        }
        assertThat(session.getStatus()).isEqualTo(GameStatus.LOSE);
        assertThat(session.getAttemptsUsed()).isEqualTo(6);
        assertThat(session.getAttemptsHistory()).hasSize(6);
    }

    @Test
    @DisplayName("При поражении показывается загаданное слово")
    void answerIsRevealedOnLoss() {
        WordDictionary dictionary = new WordDictionary(List.of("озеро", "арбуз"));
        GameRules rules = new GameRules(dictionary);
        GameSession session = GameFactory.startGame(dictionary, 1, 0);
        String answer = session.getAnswer();
        String wrongGuess;
        if ("озеро".equals(answer)) {
            wrongGuess = "арбуз";
        } else {
            wrongGuess = "озеро";
        }
        rules.applyGuess(session, wrongGuess);
        assertThat(session.getStatus()).isEqualTo(GameStatus.LOSE);
        assertThat(session.getAnswer()).isEqualTo(answer);
    }

    @Test
    @DisplayName("Завершённая партия больше не принимает попытки")
    void finishedGameRejectsFurtherGuesses() {
        WordDictionary dictionary = new WordDictionary(List.of("озеро"));
        GameRules rules = new GameRules(dictionary);
        GameSession session = GameFactory.startGame(dictionary, 1, 0);
        rules.applyGuess(session, "озеро");
        assertThatThrownBy(() -> rules.applyGuess(session, "озеро")).isInstanceOf(IllegalStateException.class);
        assertThat(session.getStatus()).isEqualTo(GameStatus.WIN);
        assertThat(session.getAttemptsUsed()).isEqualTo(1);
        assertThat(session.getAttemptsHistory()).containsExactly("озеро");
    }
}
