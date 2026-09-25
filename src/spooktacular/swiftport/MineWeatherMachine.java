package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineWeatherMachine.swift.
 *  Original: Sources/Ultimate/MineWeatherMachine.swift (6438 chars, 138 lines).
 *  Swift types: MineWeatherBeacon, MineBeaconGuide, MineWeatherConsoleView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineWeatherMachine {
    private MineWeatherMachine() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineWeatherMachine.swift";
    public static final int SWIFT_LINES = 138;
    public static final List<String> SWIFT_TYPES = List.of("MineWeatherBeacon", "MineBeaconGuide", "MineWeatherConsoleView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
