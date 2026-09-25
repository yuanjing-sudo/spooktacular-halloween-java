package spooktacular.web;

/** Seeded RNG — exact algorithm twin of spooktacular.engine.Engine.RNG
 *  (src/spooktacular/engine/Engine.java). Java-11 syntax for TeaVM.
 *  Relies on Java long overflow == UInt64 wrap, identical in WASM i64. */
public final class WasmRng {
    private long state;

    public WasmRng(long seed) {
        state = seed == 0 ? 0x9E3779B97F4A7C15L : seed;
    }

    public long next() {
        state += 0x6D2B79F5L;
        long z = state;
        z = (z ^ (z >>> 15)) * (z | 1);
        z = z ^ (z + ((z ^ (z >>> 7)) * (z | 61)));
        return z ^ (z >>> 14);
    }

    public double nextDouble() {
        return (next() >>> 11) / (double) (1L << 53);
    }

    public int nextInt(int n) {
        if (n <= 0) {
            return 0;
        }
        return (int) (nextDouble() * n) % n;
    }
}
