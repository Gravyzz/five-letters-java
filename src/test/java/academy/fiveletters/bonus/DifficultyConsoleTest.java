package academy.fiveletters.bonus;

import static org.assertj.core.api.Assertions.assertThat;

import academy.fiveletters.game.GameConfig;
import academy.fiveletters.game.GameFactory;
import academy.fiveletters.game.dictionary.DictionaryLoader;
import academy.fiveletters.game.dictionary.WordDictionary;
import academy.fiveletters.support.CliRunner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Выбор сложности")
class DifficultyConsoleTest {
    @ParameterizedTest
    @DisplayName("Выбранная сложность использует свой словарь")
    @CsvSource({"1, /difficulty-easy.txt", "2, /difficulty-medium.txt", "3, /difficulty-hard.txt"})
    void selectedDifficultyUsesItsDictionary(String difficultyChoice, String resourcePath) {
        WordDictionary dictionary = DictionaryLoader.loadDictionary(resourcePath);
        String answer =
                GameFactory.startGame(dictionary, GameConfig.MAX_ATTEMPTS, 42).getAnswer();
        String stdin = "3\n" + difficultyChoice + "\n" + answer + "\n0\n";
        CliRunner.Result result = CliRunner.runWithInput(stdin, "--seed", "42");
        assertThat(result.exitCode()).isZero();
        assertThat(result.stdout()).contains("✅✅✅✅✅ " + answer);
        assertThat(result.stdout()).contains("Победа!");
    }
}
