package academy.fiveletters.bonus;

import static org.assertj.core.api.Assertions.assertThat;

import academy.fiveletters.support.CliRunner;
import org.junit.jupiter.api.Test;

class LaunchArgumentsConsoleTest {

    @Test
    void invalidSeedDoesNotCrash() {

        CliRunner.Result result = CliRunner.runWithInput("", "--seed", "abc");
        assertThat(result.exitCode()).isZero();
        assertThat(result.stdout()).contains("Должно быть целое число");
    }
}
