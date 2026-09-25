package spooktacular.swiftport;

import java.util.List;

/** Java expression of ArcadeGames.swift.
 *  Original: Sources/Ultimate/ArcadeGames.swift (51939 chars, 1490 lines).
 *  Swift types: ArcadeRouter, ArcadeGameView, ArcadeFloat, ArcadePopIn, ArcadeGlowPulse, ArcadeShake, ArcadeSquash, ParticleBurst, BurstSeed, Floater, FloaterStack, TwinkleBackground
 *  Port: spooktacular.app.MiniGames (Memory Match + Pumpkin Smash). */
public final class ArcadeGames {
    private ArcadeGames() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/ArcadeGames.swift";
    public static final int SWIFT_LINES = 1490;
    public static final List<String> SWIFT_TYPES = List.of("ArcadeRouter", "ArcadeGameView", "ArcadeFloat", "ArcadePopIn", "ArcadeGlowPulse", "ArcadeShake", "ArcadeSquash", "ParticleBurst", "BurstSeed", "Floater", "FloaterStack", "TwinkleBackground", "TwinkleStar", "ArcadeHUD", "ArcadeStartCard", "ArcadeGameOverCard", "ArcadeShell", "MemoryCard", "MemoryMatchGameView", "MemoryCardFace", "PumpkinSmashGameView", "FallingTreat", "TreatKind", "CandyCatchGameView", "SpellDuelGameView");
    public static final String PORT = "spooktacular.app.MiniGames (Memory Match + Pumpkin Smash)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
