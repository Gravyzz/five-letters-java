package academy.fiveletters.game;

public final class GameStatistics {
    private int wins;
    private int losses;

    public void recordGame(GameStatus status) {
        if (status == null || status == GameStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Учитывается только завершённая партия");
        }
        if (status == GameStatus.WIN) {
            wins++;
        } else {
            losses++;
        }
    }

    public int getGamesPlayed() {
        return wins + losses;
    }

    public int getLosses() {
        return losses;
    }

    public int getWins() {
        return wins;
    }
}
