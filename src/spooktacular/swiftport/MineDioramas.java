package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineDioramas.swift.
 *  Original: Sources/Ultimate/MineDioramas.swift (36736 chars, 944 lines).
 *  Swift types: MineRockBand, MineTimberFrame, MineRailTrack, MineDarkWater, MineDioramaMeadow, MineDioramaDirt, MineDioramaStone, MineDioramaDeepstone, MineDioramaCrystal, MineDioramaMagma, MineDioramaCubeRoom, MineDioramaSpikeRoom
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineDioramas {
    private MineDioramas() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineDioramas.swift";
    public static final int SWIFT_LINES = 944;
    public static final List<String> SWIFT_TYPES = List.of("MineRockBand", "MineTimberFrame", "MineRailTrack", "MineDarkWater", "MineDioramaMeadow", "MineDioramaDirt", "MineDioramaStone", "MineDioramaDeepstone", "MineDioramaCrystal", "MineDioramaMagma", "MineDioramaCubeRoom", "MineDioramaSpikeRoom", "MineDioramaOrbRoom", "MineDioramaForge", "MineDioramaVault", "MineCoinMound", "MineDioramaWispGrove", "MineDioramaLake", "MineDioramaEntrance", "MineDioramaMagmaHeart", "MineLayerDiorama", "MineDioramaPayday", "MineDioramaRebirth", "MineDioramaFrost", "MineDioramaShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
