package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineLighting.swift.
 *  Original: Sources/Ultimate/MineLighting.swift (27951 chars, 664 lines).
 *  Swift types: MineFlicker, MineLight, MineLightKind, MineLayerLighting, MineLayerLightingGuide, MineTorchFlame, MineLanternGlow, MineLavaGlow, MineCrystalPool, MineLightCone, MineConeShape, MineVignette
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineLighting {
    private MineLighting() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineLighting.swift";
    public static final int SWIFT_LINES = 664;
    public static final List<String> SWIFT_TYPES = List.of("MineFlicker", "MineLight", "MineLightKind", "MineLayerLighting", "MineLayerLightingGuide", "MineTorchFlame", "MineLanternGlow", "MineLavaGlow", "MineCrystalPool", "MineLightCone", "MineConeShape", "MineVignette", "MineWarningPulse", "MineLayerRig", "MineLightingShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
