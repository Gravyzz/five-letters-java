package academy.fiveletters.game.dictionary;

public enum WordCategory {
    ANIMALS("/category-animals.txt"),
    FOOD("/category-food.txt"),
    NATURE("/category-nature.txt");

    private final String resourcePath;

    WordCategory(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getResourcePath() {
        return resourcePath;
    }
}
