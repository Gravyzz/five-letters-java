package academy.fiveletters.bonus;

import static org.assertj.core.api.Assertions.assertThat;

import academy.fiveletters.game.DictionaryLoader;
import academy.fiveletters.game.GameConfig;
import academy.fiveletters.game.GameFactory;
import academy.fiveletters.game.WordDictionary;
import academy.fiveletters.support.CliRunner;
import org.junit.jupiter.api.Test;

class StatisticsConsoleTest {

    @Test
    void statisticsAreAccumulatedDuringRun() {
        WordDictionary dictionary = DictionaryLoader.loadDefaultDictionary();
        String answer =
                GameFactory.startGame(dictionary, GameConfig.MAX_ATTEMPTS, 42).getAnswer();
        String stdin = "2\n1\n" + answer + "\n2\n0\n";
        CliRunner.Result result = CliRunner.runWithInput(stdin, "--seed", "42");
        assertThat(result.stdout())
                .containsSubsequence(
                        "Сыграно партий: 0",
                        "Побед: 0",
                        "Поражений: 0",
                        "Победа!",
                        "Сыграно партий: 1",
                        "Побед: 1",
                        "Поражений: 0");
        assertThat(result.exitCode()).isZero();
    }
}
