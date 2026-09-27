package academy.fiveletters.bonus;

import static org.assertj.core.api.Assertions.assertThat;

import academy.fiveletters.game.GameConfig;
import academy.fiveletters.game.GameFactory;
import academy.fiveletters.game.dictionary.DictionaryLoader;
import academy.fiveletters.game.dictionary.WordDictionary;
import academy.fiveletters.support.CliRunner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Подсказка")
class HintConsoleTest {
    @Test
    @DisplayName("Подсказка доступна один раз и не расходует попытку")
    void hintIsAvailableOnceAndDoesNotConsumeAttempt() {
        WordDictionary dictionary = DictionaryLoader.loadDefaultDictionary();
        String answer =
                GameFactory.startGame(dictionary, GameConfig.MAX_ATTEMPTS, 42).getAnswer();
        String stdin = "1\n?\n?\n" + answer + "\n0\n";
        CliRunner.Result result = CliRunner.runWithInput(stdin, "--seed", "42");
        assertThat(result.stdout())
                .containsSubsequence(
                        "Для подсказки введите: ?",
                        "Подсказка: первая буква — " + answer.charAt(0),
                        "Подсказка уже использована",
                        "Осталось попыток: 5",
                        "Победа!");
        assertThat(result.exitCode()).isZero();
    }
}
