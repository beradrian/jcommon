package net.sf.jcommon.util;

import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

/**
 * A cache for compiling files.
 */
public abstract class FileCompilerCache<D> implements CompilerCache<File, D> {

    private Map<File, D> cache = new HashMap<File, D>();
    private Map<D, Long> compilingDates = new HashMap<D, Long>();

    public D getCachedCompiled(File source) {
        if (!cache.containsKey(source)) {
            return putInCache(source);
        } else {
            D dest = cache.get(source);
            if (isNewer(source, dest)) {
                return putInCache(source);
            } else {
                return dest;
            }
        }
    }

    private D putInCache(File source) {
        D dest = compile(source);
        cache.put(source, dest);
        compilingDates.put(dest, new Date().getTime());
        return dest;
    }

    public boolean isNewer(File source, D d) {
        return !compilingDates.containsKey(d)
                || (source.lastModified() > compilingDates.get(d));
    }
}
