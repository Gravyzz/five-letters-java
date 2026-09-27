package academy.fiveletters.game.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class DictionaryLoader {

    private static final String DEFAULT_DICTIONARY_RESOURCE = "/dictionary.txt";

    private DictionaryLoader() {}

    private static InputStream openResource(String resourcePath) {
        InputStream stream = DictionaryLoader.class.getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new IllegalStateException("Не найден словарь: " + resourcePath);
        }
        return stream;
    }

    public static WordDictionary loadDictionary(String resourcePath) {
        if (resourcePath == null || resourcePath.isBlank()) {
            throw new IllegalArgumentException("Путь к словарю не должен быть пустым");
        }
        try (BufferedReader reader =
                new BufferedReader(new InputStreamReader(openResource(resourcePath), StandardCharsets.UTF_8))) {

            List<String> words = new ArrayList<>();
            String line = reader.readLine();
            while (line != null) {
                String cleanedLine = line.strip();
                if (!(cleanedLine.isBlank() || cleanedLine.startsWith("#"))) {
                    words.add(cleanedLine);
                }
                line = reader.readLine();
            }

            return new WordDictionary(words);
        } catch (IOException exception) {
            throw new UncheckedIOException("Не удалось прочитать встроенный словарь", exception);
        }
    }

    public static WordDictionary loadDefaultDictionary() {
        return loadDictionary(DEFAULT_DICTIONARY_RESOURCE);
    }
}
