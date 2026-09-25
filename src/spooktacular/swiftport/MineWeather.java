package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineWeather.swift.
 *  Original: Sources/Ultimate/MineWeather.swift (21023 chars, 562 lines).
 *  Swift types: MineWeatherKind, MineWeatherDirector, MineDustDevil, MineDripStorm, MineEmberStorm, MineHeatFlicker, MineSporeFall, MineFrostBreath, MineFogBank, MineGoldRain, MineLightningStorm, MineRainbowVeil
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineWeather {
    private MineWeather() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineWeather.swift";
    public static final int SWIFT_LINES = 562;
    public static final List<String> SWIFT_TYPES = List.of("MineWeatherKind", "MineWeatherDirector", "MineDustDevil", "MineDripStorm", "MineEmberStorm", "MineHeatFlicker", "MineSporeFall", "MineFrostBreath", "MineFogBank", "MineGoldRain", "MineLightningStorm", "MineRainbowVeil", "MineWeatherOverlay", "MineWeatherShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
