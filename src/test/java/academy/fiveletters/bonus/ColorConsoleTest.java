package academy.fiveletters.bonus;

import static org.assertj.core.api.Assertions.assertThat;

import academy.fiveletters.game.DictionaryLoader;
import academy.fiveletters.game.GameConfig;
import academy.fiveletters.game.GameFactory;
import academy.fiveletters.game.WordDictionary;
import academy.fiveletters.support.CliRunner;
import org.junit.jupiter.api.Test;

class ColorConsoleTest {
    @Test
    void coloredMessagesArePrinted() {
        WordDictionary dictionary = DictionaryLoader.loadDefaultDictionary();
        String answer =
                GameFactory.startGame(dictionary, GameConfig.MAX_ATTEMPTS, 42).getAnswer();
        String stdin = "1\n?\nкот\n" + answer + "\n0\n";
        CliRunner.Result result = CliRunner.runWithInput(stdin, "--seed", "42");
        assertThat(result.stdout())
                .containsSubsequence(
                        "\u001B[33mПодсказка: первая буква — " + answer.charAt(0) + "\u001B[0m",
                        "\u001B[31mДлина слова должна быть равна 5\u001B[0m",
                        "\u001B[32mПобеда! Слово угадано за 1 попытку\u001B[0m");
        assertThat(result.exitCode()).isZero();
    }
}
