package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineSectorDioramas.swift.
 *  Original: Sources/Ultimate/MineSectorDioramas.swift (15780 chars, 419 lines).
 *  Swift types: MineSectorDioramaNW, MineSectorDioramaNC, MineSectorDioramaNE, MineSectorDioramaHW, MineSectorDioramaHC, MineSectorDioramaHE, MineSectorDioramaSW, MineSectorDioramaSC, MineSectorDioramaSE, MineSectorScene, MineSectorMini, MineSectorShowcaseView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineSectorDioramas {
    private MineSectorDioramas() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineSectorDioramas.swift";
    public static final int SWIFT_LINES = 419;
    public static final List<String> SWIFT_TYPES = List.of("MineSectorDioramaNW", "MineSectorDioramaNC", "MineSectorDioramaNE", "MineSectorDioramaHW", "MineSectorDioramaHC", "MineSectorDioramaHE", "MineSectorDioramaSW", "MineSectorDioramaSC", "MineSectorDioramaSE", "MineSectorScene", "MineSectorMini", "MineSectorShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
