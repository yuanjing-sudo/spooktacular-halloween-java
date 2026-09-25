package spooktacular.systems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Browser GameStore replacement for the TeaVM build (same fully-qualified
 *  name + same public API as the desktop version, so QuestBoard,
 *  ExpeditionBoard and AchievementBoard compile UNCHANGED).
 *
 *  <p>Desktop uses java.util.prefs + URL-encoding; this uses an in-memory blob
 *  map with identical serialization (unit-separator-joined string arrays,
 *  {@code k=v;} maps). spooktacular.web.WebSave mirrors the blob map to
 *  browser localStorage, so progress survives reloads. Keys/ids in this game
 *  are ASCII, so URL-encoding is unnecessary (documented deviation). */
public class GameStore {
    public static final GameStore standard = new GameStore("spookycore");
    private final String node;
    private final Map<String, String> blobs = new HashMap<String, String>();

    public GameStore(String node) {
        this.node = node;
    }

    public void set(String key, int v) { blobs.put(key, Integer.toString(v)); }
    public void set(String key, long v) { blobs.put(key, Long.toString(v)); }
    public void set(String key, boolean v) { blobs.put(key, v ? "1" : "0"); }
    public void set(String key, double v) { blobs.put(key, Double.toString(v)); }
    public void set(String key, String v) { blobs.put(key, v == null ? "" : v); }

    public void setStrings(String key, Collection<String> v) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : v) {
            if (!first) {
                sb.append('\u001F');
            }
            first = false;
            sb.append(s);
        }
        blobs.put(key, sb.toString());
    }

    public void setMap(String key, Map<String, Integer> m) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Integer> e : m.entrySet()) {
            if (!first) {
                sb.append(';');
            }
            first = false;
            sb.append(e.getKey()).append('=').append(e.getValue());
        }
        blobs.put(key, sb.toString());
    }

    public int num(String key) { return num(key, 0); }
    public int num(String key, int def) {
        String v = blobs.get(key);
        if (v == null) {
            return def;
        }
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public long lng(String key, long def) {
        String v = blobs.get(key);
        if (v == null) {
            return def;
        }
        try {
            return Long.parseLong(v);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public boolean bool(String key, boolean def) {
        String v = blobs.get(key);
        if (v == null) {
            return def;
        }
        return v.equals("1");
    }

    public double dbl(String key, double def) {
        String v = blobs.get(key);
        if (v == null) {
            return def;
        }
        try {
            return Double.parseDouble(v);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public String str(String key, String def) {
        String v = blobs.get(key);
        return v == null ? def : v;
    }

    public boolean has(String key) { return blobs.containsKey(key); }

    public List<String> stringArray(String key) {
        List<String> out = new ArrayList<String>();
        String v = blobs.get(key);
        if (v == null || v.isEmpty()) {
            return out;
        }
        int start = 0;
        for (int i = 0; i <= v.length(); i++) {
            if (i == v.length() || v.charAt(i) == '\u001F') {
                out.add(v.substring(start, i));
                start = i + 1;
            }
        }
        return out;
    }

    public Map<String, Integer> map(String key) {
        Map<String, Integer> m = new LinkedHashMap<String, Integer>();
        String v = blobs.get(key);
        if (v == null || v.isEmpty()) {
            return m;
        }
        int start = 0;
        for (int i = 0; i <= v.length(); i++) {
            if (i == v.length() || v.charAt(i) == ';') {
                String pair = v.substring(start, i);
                int eq = pair.indexOf('=');
                if (eq > 0) {
                    try {
                        m.put(pair.substring(0, eq), Integer.parseInt(pair.substring(eq + 1)));
                    } catch (NumberFormatException ignored) {
                    }
                }
                start = i + 1;
            }
        }
        return m;
    }

    public void remove(String key) { blobs.remove(key); }

    /** Full blob snapshot for spooktacular.web.WebSave (localStorage mirror). */
    public Map<String, String> snapshot() { return new HashMap<String, String>(blobs); }

    /** Restore a snapshot produced by {@link #snapshot()}. */
    public void restore(Map<String, String> m) {
        blobs.clear();
        blobs.putAll(m);
    }
}
