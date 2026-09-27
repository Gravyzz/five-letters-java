package academy.fiveletters.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import academy.fiveletters.game.model.GuessResult;
import academy.fiveletters.game.model.LetterStatus;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Результат попытки")
public class GuessResultTest {

    @Test
    @DisplayName("Изменение исходного списка не изменяет результат")
    void changingSourceListDoesNotChangeResult() {
        List<LetterStatus> list = new ArrayList<>(List.of(
                LetterStatus.PRESENT,
                LetterStatus.ABSENT,
                LetterStatus.PRESENT,
                LetterStatus.PRESENT,
                LetterStatus.ABSENT));
        GuessResult result = new GuessResult("озеро", list);
        list.set(0, LetterStatus.ABSENT);
        assertThat(result.getLetterStatuses().get(0)).isEqualTo(LetterStatus.PRESENT);
    }

    @Test
    @DisplayName("Полученный список статусов нельзя изменить")
    void returnedLetterStatusesCannotBeModified() {
        List<LetterStatus> list = new ArrayList<>(List.of(
                LetterStatus.PRESENT,
                LetterStatus.ABSENT,
                LetterStatus.PRESENT,
                LetterStatus.PRESENT,
                LetterStatus.ABSENT));
        GuessResult result = new GuessResult("озеро", list);
        assertThatThrownBy(() -> result.getLetterStatuses().set(0, LetterStatus.ABSENT))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Недостаточное количество статусов букв отклоняется")
    void tooFewLetterStatusesAreRejected() {
        List<LetterStatus> list =
                List.of(LetterStatus.PRESENT, LetterStatus.PRESENT, LetterStatus.PRESENT, LetterStatus.ABSENT);
        assertThatThrownBy(() -> new GuessResult("озеро", list)).isInstanceOf(IllegalArgumentException.class);
    }
}
