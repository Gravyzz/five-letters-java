package academy.fiveletters.bonus;

import static org.assertj.core.api.Assertions.assertThat;

import academy.fiveletters.support.CliRunner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Аргументы запуска")
class LaunchArgumentsConsoleTest {

    @Test
    @DisplayName("Некорректный seed не роняет приложение")
    void invalidSeedDoesNotCrash() {

        CliRunner.Result result = CliRunner.runWithInput("", "--seed", "abc");
        assertThat(result.exitCode()).isZero();
        assertThat(result.stdout()).contains("Должно быть целое число");
    }
}
