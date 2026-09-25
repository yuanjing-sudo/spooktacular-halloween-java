package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineGhostTheater.swift.
 *  Original: Sources/Ultimate/MineGhostTheater.swift (41711 chars, 1101 lines).
 *  Swift types: MineGhostKind, MineGhostPhase, MineGhostActor, MineGhostDirector, MineGhostBody, MineGhostSheet, MineGhostSprite, MineGhostFace, MineGhostEye, MineGhostProp, MineWailRings, RingPhase
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineGhostTheater {
    private MineGhostTheater() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineGhostTheater.swift";
    public static final int SWIFT_LINES = 1101;
    public static final List<String> SWIFT_TYPES = List.of("MineGhostKind", "MineGhostPhase", "MineGhostActor", "MineGhostDirector", "MineGhostBody", "MineGhostSheet", "MineGhostSprite", "MineGhostFace", "MineGhostEye", "MineGhostProp", "MineWailRings", "RingPhase", "MineWailFlash", "MineEctoplasmTrail", "MineHauntedMist", "MineSpectralGlow", "MineGhostScare", "MineGhostEncounterCard", "MineGhostRadar", "MineAmbientHaunt", "MineBooBurst", "MineElderTrio", "MineGhostChoir", "MineGhostShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
