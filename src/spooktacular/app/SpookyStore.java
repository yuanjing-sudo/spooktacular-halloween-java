package spooktacular.app;

import java.util.*;

/** Crash-safe persisted store — Java expression of Sources/Ultimate/SpookyStore.swift.
 *  Versioned keys, corruption recovery (bad payloads reset to defaults),
 *  day-string helpers for streaks. Backed by a .properties file, no deps. */
public final class SpookyStore {
    private final java.nio.file.Path file;
    private final Properties props = new Properties();
    public static final int VERSION = 1;

    public SpookyStore(java.nio.file.Path file) {
        this.file = file;
        load();
    }

    public static SpookyStore inUserHome() {
        java.nio.file.Path p = java.nio.file.Path.of(
            System.getProperty("user.home"), ".spooktacular-java.properties");
        return new SpookyStore(p);
    }

    private void load() {
        try {
            if (java.nio.file.Files.exists(file)) {
                try (var in = java.nio.file.Files.newInputStream(file)) {
                    props.load(in);
                }
                int v = Integer.parseInt(props.getProperty("version", "1"));
                if (v != VERSION) { props.clear(); props.setProperty("version", String.valueOf(VERSION)); }
            } else {
                props.setProperty("version", String.valueOf(VERSION));
            }
        } catch (Exception e) {
            props.clear(); // corruption recovery: reset to defaults instead of crashing
            props.setProperty("version", String.valueOf(VERSION));
        }
    }

    public synchronized void save() {
        try {
            if (file.getParent() != null) java.nio.file.Files.createDirectories(file.getParent());
            try (var out = java.nio.file.Files.newOutputStream(file)) {
                props.store(out, "Spooktacular Java Edition v" + VERSION);
            }
        } catch (Exception ignored) {}
    }

    public synchronized int getInt(String key, int def) {
        try { return Integer.parseInt(props.getProperty(key, String.valueOf(def))); }
        catch (Exception e) { return def; }
    }

    public synchronized void setInt(String key, int v) { props.setProperty(key, String.valueOf(v)); }

    public synchronized String getString(String key, String def) {
        return props.getProperty(key, def);
    }

    public synchronized void setString(String key, String v) { props.setProperty(key, v); }

    public static String dayString(java.time.LocalDate d) { return d.toString(); }
    public static String todayString() { return dayString(java.time.LocalDate.now()); }

    public synchronized void dangerZoneReset() { props.clear(); props.setProperty("version", String.valueOf(VERSION)); save(); }
}
