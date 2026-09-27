package academy.fiveletters.console.output;

import academy.fiveletters.game.model.GuessResult;
import academy.fiveletters.game.model.LetterStatus;

public final class ResultFormatter {
    public String format(GuessResult result) {
        StringBuilder builder = new StringBuilder();
        for (LetterStatus status : result.getLetterStatuses()) {
            if (status == LetterStatus.CORRECT) {
                builder.append("✅");
            } else if (status == LetterStatus.PRESENT) {
                builder.append("🟡");
            } else if (status == LetterStatus.ABSENT) {
                builder.append("❌");
            }
        }

        builder.append(" ");
        builder.append(result.getGuess());
        return builder.toString();
    }
}
