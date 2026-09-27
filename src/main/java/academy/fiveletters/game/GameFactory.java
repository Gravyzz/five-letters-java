package academy.fiveletters.game;

import academy.fiveletters.game.dictionary.WordDictionary;
import academy.fiveletters.game.model.GameSession;
import java.util.Random;

public final class GameFactory {

    private GameFactory() {}

    public static GameSession startGame(WordDictionary dictionary, int maxAttempts, long seed) {
        if (dictionary == null) {
            throw new IllegalArgumentException("Словарь отсутствует");
        }
        Random random = new Random(seed);
        int index = random.nextInt(dictionary.size());
        String answer = dictionary.getWords().get(index);
        return new GameSession(answer, maxAttempts);
    }
}
