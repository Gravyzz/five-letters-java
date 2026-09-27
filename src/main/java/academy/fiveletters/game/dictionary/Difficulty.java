package academy.fiveletters.game.dictionary;

public enum Difficulty {
    EASY("/difficulty-easy.txt"),
    MEDIUM("/difficulty-medium.txt"),
    HARD("/difficulty-hard.txt");

    private final String resourcePath;

    Difficulty(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getResourcePath() {
        return resourcePath;
    }
}
