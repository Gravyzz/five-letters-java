package academy.fiveletters.mandatory.mr2;

import static org.assertj.core.api.Assertions.assertThat;

import academy.fiveletters.game.LetterMatcher;
import academy.fiveletters.game.LetterStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: раскраска букв. */
@DisplayName("MR2. Проверка букв")
class LetterMatchingTest {

    @Test
    @DisplayName("Базовый случай: загадано \"озеро\", ввод \"арбуз\" -> ❌🟡❌❌🟡")
    void basicCase() {
        assertThat(LetterMatcher.match("озеро", "арбуз"))
                .containsExactly(
                        LetterStatus.ABSENT,
                        LetterStatus.PRESENT,
                        LetterStatus.ABSENT,
                        LetterStatus.ABSENT,
                        LetterStatus.PRESENT);
    }

    @Test
    @DisplayName("Полное совпадение: загадано \"озеро\", ввод \"озеро\" -> ✅✅✅✅✅")
    void exactMatch() {
        assertThat(LetterMatcher.match("озеро", "озеро"))
                .containsExactly(
                        LetterStatus.CORRECT,
                        LetterStatus.CORRECT,
                        LetterStatus.CORRECT,
                        LetterStatus.CORRECT,
                        LetterStatus.CORRECT);
    }

    @Test
    @DisplayName("Повторяющиеся буквы: загадано \"сорок\", ввод \"оооом\" -> ❌✅❌✅❌")
    void repeatedLettersAreNotDoubleCounted() {
        assertThat(LetterMatcher.match("сорок", "оооом"))
                .containsExactly(
                        LetterStatus.ABSENT,
                        LetterStatus.CORRECT,
                        LetterStatus.ABSENT,
                        LetterStatus.CORRECT,
                        LetterStatus.ABSENT);
    }

    @Test
    @DisplayName("Ни одна буква не подошла: все позиции ❌")
    void noMatchingLetters() {
        assertThat(LetterMatcher.match("вихрь", "басня"))
                .containsExactly(
                        LetterStatus.ABSENT,
                        LetterStatus.ABSENT,
                        LetterStatus.ABSENT,
                        LetterStatus.ABSENT,
                        LetterStatus.ABSENT);
    }

    @Test
    @DisplayName("Свободное вхождение буквы используется только один раз \"отбор\", ввод \"около\" -> ✅❌🟡❌❌")
    void remainingOccurrenceIsUsedOnlyOnce() {
        assertThat(LetterMatcher.match("отбор", "около"))
                .containsExactly(
                        LetterStatus.CORRECT,
                        LetterStatus.ABSENT,
                        LetterStatus.PRESENT,
                        LetterStatus.ABSENT,
                        LetterStatus.ABSENT);
    }
}
