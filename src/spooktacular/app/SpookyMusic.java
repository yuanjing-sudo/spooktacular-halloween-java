package spooktacular.app;

/** Global music + sfx switchboard — Java expression of SpookyMusic.swift.
 *  The iOS app had 3 competing infinite players pointing at missing mp3s (silent
 *  no-ops) plus a generative loop. This class owns ALL music: a single enabled
 *  flag + named jingles, safe with no audio files (Mix-ready stub). No deps. */
public final class SpookyMusic {
    private static final SpookyMusic INSTANCE = new SpookyMusic();
    public static SpookyMusic shared() { return INSTANCE; }

    private boolean musicOn = true, sfxOn = true;
    private String currentTrack = "haunted-score";

    private SpookyMusic() {}

    public boolean isMusicOn() { return musicOn; }
    public boolean isSfxOn() { return sfxOn; }
    public void setMusicOn(boolean on) { musicOn = on; if (!on) currentTrack = "(stopped)"; else currentTrack = "haunted-score"; }
    public void setSfxOn(boolean on) { sfxOn = on; }
    public String currentTrack() { return currentTrack; }

    /** Play a one-shot jingle name (no-op if sfx off). Returns the name played or "". */
    public String jingle(String name) { return sfxOn ? name : ""; }

    public enum Bell { CAPTURE, CANDY, BREW, WIN, LOSE }
    public String bell(Bell b) { return jingle(b.name().toLowerCase()); }
}
