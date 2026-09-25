package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineParticles.swift.
 *  Original: Sources/Ultimate/MineParticles.swift (25050 chars, 624 lines).
 *  Swift types: MineParticleShape, MineEmitterFlow, MineEmitterPreset, MineEmitterGuide, MineParticleField, Mote, MineTapBurst, MineTapDiamond, MineFountainView, MineCometTrail, MineParticleShowcaseView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineParticles {
    private MineParticles() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineParticles.swift";
    public static final int SWIFT_LINES = 624;
    public static final List<String> SWIFT_TYPES = List.of("MineParticleShape", "MineEmitterFlow", "MineEmitterPreset", "MineEmitterGuide", "MineParticleField", "Mote", "MineTapBurst", "MineTapDiamond", "MineFountainView", "MineCometTrail", "MineParticleShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
