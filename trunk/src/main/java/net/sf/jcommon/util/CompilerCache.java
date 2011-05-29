package net.sf.jcommon.util;

/**
 * A cache for compiled objects. Every object has a source and a destination after
 * compilation.
 */
public interface CompilerCache<S, D> {

    /** Compile a source into a destination.
     * @param s the source
     * @return the destination
     */
    D compile(S s);

    /** Returns the compiled object from cache if in there or compiles the
     * source, puts it in the cache and returns it.
     * @param s the source
     * @return the destination
     */
    D getCachedCompiled(S s);

    /**
     * Returns if the given source is newer than the destination.
     * @param s the source
     * @param d the destination
     * @return true if the given source is newer than the destination, false otherwise
     */
    boolean isNewer(S s, D d);
}
