package academy.fiveletters.bonus;

import static org.assertj.core.api.Assertions.assertThat;

import academy.fiveletters.game.GameConfig;
import academy.fiveletters.game.GameFactory;
import academy.fiveletters.game.dictionary.DictionaryLoader;
import academy.fiveletters.game.dictionary.WordDictionary;
import academy.fiveletters.support.CliRunner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Повтор игры")
class ReplayConsoleTest {
    @Test
    @DisplayName("Replay воспроизводит игру без открытия меню")
    void replayReproducesGameWithoutOpeningMenu() {
        WordDictionary dictionary = DictionaryLoader.loadDefaultDictionary();
        String answer =
                GameFactory.startGame(dictionary, GameConfig.MAX_ATTEMPTS, 42).getAnswer();
        String wrongGuess = dictionary.getWords().get(0);
        if (wrongGuess.equals(answer)) {
            wrongGuess = dictionary.getWords().get(1);
        }
        CliRunner.Result result = CliRunner.runWithInput("", "--replay", "42", wrongGuess, answer);
        assertThat(result.stdout()).containsSubsequence(wrongGuess, "✅✅✅✅✅ " + answer, "Победа!");
        assertThat(result.exitCode()).isZero();
        assertThat(result.stdout()).doesNotContain("1 - Новая игра");
        assertThat(result.stdout()).contains("Осталось попыток: 4");
    }
}
