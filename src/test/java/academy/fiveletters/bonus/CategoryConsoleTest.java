package academy.fiveletters.bonus;

import static org.assertj.core.api.Assertions.assertThat;

import academy.fiveletters.game.DictionaryLoader;
import academy.fiveletters.game.GameConfig;
import academy.fiveletters.game.GameFactory;
import academy.fiveletters.game.WordDictionary;
import academy.fiveletters.support.CliRunner;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CategoryConsoleTest {
    @ParameterizedTest
    @CsvSource({"1, /category-animals.txt", "2, /category-food.txt", "3, /category-nature.txt"})
    void categoryUsesOwnAnswersAndAcceptsGuessFromFullDictionary(String categoryChoice, String resourcePath) {
        WordDictionary categoryDictionary = DictionaryLoader.loadDictionary(resourcePath);
        String answer = GameFactory.startGame(categoryDictionary, GameConfig.MAX_ATTEMPTS, 42)
                .getAnswer();
        String stdin = "4\n" + categoryChoice + "\nгвалт\n" + answer + "\n0\n";
        CliRunner.Result result = CliRunner.runWithInput(stdin, "--seed", "42");
        assertThat(result.exitCode()).isZero();
        assertThat(result.stdout()).contains("✅✅✅✅✅ " + answer);
        assertThat(result.stdout()).contains("Победа!");
        assertThat(result.stdout()).contains("гвалт");
        assertThat(result.stdout()).contains("Осталось попыток: 5");
        assertThat(result.stdout()).doesNotContain("Отсутствует слово в словаре");
    }
}
