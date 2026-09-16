package academy.fiveletters.game;

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

    private static InputStream openDefaultResource() {
        InputStream stream = DictionaryLoader.class.getResourceAsStream(DEFAULT_DICTIONARY_RESOURCE);
        if (stream == null) {
            throw new IllegalStateException("Приложение должно содержать встроенный словарь, но его нет");
        }
        return stream;
    }

    public static WordDictionary loadDefaultDictionary() {
        try (BufferedReader reader =
                new BufferedReader(new InputStreamReader(openDefaultResource(), StandardCharsets.UTF_8))) {

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
}
