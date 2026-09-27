package academy.fiveletters.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import academy.fiveletters.game.model.GameStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Статистика игр")
class GameStatisticsTest {

    @Test
    @DisplayName("Новая статистика не содержит сыгранных партий")
    void newStatisticsIsEmpty() {
        GameStatistics statistics = new GameStatistics();
        assertThat(statistics.getGamesPlayed()).isEqualTo(0);
        assertThat(statistics.getWins()).isEqualTo(0);
        assertThat(statistics.getLosses()).isEqualTo(0);
    }

    @Test
    @DisplayName("Завершённые партии учитываются в статистике")
    void completedGamesAreRecorded() {
        GameStatistics statistics = new GameStatistics();
        statistics.recordGame(GameStatus.WIN);
        statistics.recordGame(GameStatus.WIN);
        statistics.recordGame(GameStatus.LOSE);
        assertThat(statistics.getGamesPlayed()).isEqualTo(3);
        assertThat(statistics.getWins()).isEqualTo(2);
        assertThat(statistics.getLosses()).isEqualTo(1);
    }

    @Test
    @DisplayName("Незавершённая партия отклоняется без изменения статистики")
    void unfinishedGameIsRejectedWithoutChangingStatistics() {
        GameStatistics statistics = new GameStatistics();
        assertThatThrownBy(() -> statistics.recordGame(GameStatus.IN_PROGRESS))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(statistics.getGamesPlayed()).isEqualTo(0);
        assertThat(statistics.getWins()).isEqualTo(0);
        assertThat(statistics.getLosses()).isEqualTo(0);
    }

    @Test
    @DisplayName("Статус null отклоняется без изменения статистики")
    void nullStatusIsRejectedWithoutChangingStatistics() {
        GameStatistics statistics = new GameStatistics();
        assertThatThrownBy(() -> statistics.recordGame(null)).isInstanceOf(IllegalArgumentException.class);
        assertThat(statistics.getGamesPlayed()).isEqualTo(0);
        assertThat(statistics.getWins()).isEqualTo(0);
        assertThat(statistics.getLosses()).isEqualTo(0);
    }
}
