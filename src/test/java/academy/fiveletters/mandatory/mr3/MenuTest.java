package academy.fiveletters.mandatory.mr3;

import static org.assertj.core.api.Assertions.assertThat;

import academy.fiveletters.game.DictionaryLoader;
import academy.fiveletters.game.GameConfig;
import academy.fiveletters.game.GameFactory;
import academy.fiveletters.game.WordDictionary;
import academy.fiveletters.support.CliRunner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: меню и режим автопроверки. */
@DisplayName("MR3. Меню и детерминированный режим")
class MenuTest {

    @Test
    @DisplayName("Некорректный пункт меню не роняет программу")
    void invalidMenuChoiceDoesNotCrash() {
        CliRunner.Result result = CliRunner.runWithInput("9\n0\n");
        assertThat(result.exitCode()).isZero();
        assertThat(result.stdout()).contains("Неверный пункт меню\n1 - Новая игра");
        assertThat(result.stdout()).endsWith("Выход" + System.lineSeparator());
    }

    @Test
    @DisplayName("Можно сыграть несколько партий подряд без перезапуска")
    void severalGamesInARow() {
        WordDictionary dictionary = DictionaryLoader.loadDefaultDictionary();
        String answer =
                GameFactory.startGame(dictionary, GameConfig.MAX_ATTEMPTS, 42).getAnswer();
        String stdin = "1\n" + answer + "\n1\n" + answer + "\n0\n";
        CliRunner.Result result = CliRunner.runWithInput(stdin, "--seed", "42");
        assertThat(result.exitCode()).isEqualTo(0);
        assertThat(result.stdout()).containsSubsequence("Победа!", "1 - Новая игра", "Победа!");
    }

    @Test
    @DisplayName("Детерминированный режим даёт предсказуемый вывод для автопроверки")
    void deterministicModeProducesPredictableOutput() {
        WordDictionary dictionary = DictionaryLoader.loadDefaultDictionary();
        String answer =
                GameFactory.startGame(dictionary, GameConfig.MAX_ATTEMPTS, 42).getAnswer();
        String stdin = "1\n" + answer + "\n0\n";
        CliRunner.Result result = CliRunner.runWithInput(stdin, "--seed", "42");
        assertThat(result.exitCode()).isEqualTo(0);
        assertThat(result.stdout()).contains("✅✅✅✅✅ " + answer);
        assertThat(result.stdout()).contains("Победа!");
    }
}
