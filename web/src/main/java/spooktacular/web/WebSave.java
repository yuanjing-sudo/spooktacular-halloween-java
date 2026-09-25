package spooktacular.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.teavm.jso.browser.Storage;

import spooktacular.systems.GameStore;

/** localStorage mirror for GameStore blobs + a few web-manager keys.
 *  JSO is isolated here; GameStore itself stays headless-testable. Slot
 *  encoding: entries {@code k=v} joined by \u0002 with backslash escapes. */
public final class WebSave {
    private static final String SLOT = "spooky-web-v1";
    private WebSave() {
    }

    public static Map<String, String> extra = new HashMap<String, String>();

    private static String esc(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' || c == '=' || c == '\u0002') {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static void persist(GameStore store) {
        Map<String, String> all = new HashMap<String, String>(store.snapshot());
        for (Map.Entry<String, String> e : extra.entrySet()) {
            all.put("web." + e.getKey(), e.getValue());
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> e : all.entrySet()) {
            if (!first) {
                sb.append('\u0002');
            }
            first = false;
            sb.append(esc(e.getKey())).append('=').append(esc(e.getValue()));
        }
        try {
            Storage.getLocalStorage().setItem(SLOT, sb.toString());
        } catch (Throwable ignored) {
        }
    }

    public static void restore(GameStore store) {
        String raw;
        try {
            raw = Storage.getLocalStorage().getItem(SLOT);
        } catch (Throwable t) {
            return;
        }
        if (raw == null || raw.isEmpty()) {
            return;
        }
        Map<String, String> blobs = new HashMap<String, String>();
        extra.clear();
        ArrayList<String> parts = split(raw);
        for (int i = 0; i < parts.size(); i++) {
            String[] kv = splitKey(parts.get(i));
            if (kv == null) {
                continue;
            }
            if (kv[0].startsWith("web.")) {
                extra.put(kv[0].substring(4), kv[1]);
            } else {
                blobs.put(kv[0], kv[1]);
            }
        }
        store.restore(blobs);
    }

    private static ArrayList<String> split(String raw) {
        ArrayList<String> out = new ArrayList<String>();
        StringBuilder cur = new StringBuilder();
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            if (c == '\\' && i + 1 < raw.length()) {
                cur.append(raw.charAt(i + 1));
                i++;
            } else if (c == '\u0002') {
                out.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        out.add(cur.toString());
        return out;
    }

    private static String[] splitKey(String part) {
        StringBuilder k = new StringBuilder();
        for (int i = 0; i < part.length(); i++) {
            char c = part.charAt(i);
            if (c == '\\' && i + 1 < part.length()) {
                k.append(part.charAt(i + 1));
                i++;
            } else if (c == '=') {
                return new String[] { k.toString(), part.substring(i + 1) };
            } else {
                k.append(c);
            }
        }
        return null;
    }
}
