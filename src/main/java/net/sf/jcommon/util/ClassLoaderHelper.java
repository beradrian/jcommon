package net.sf.jcommon.util;

import java.util.*;
import java.util.zip.*;
import java.io.*;

/**
 * Class with some static methods to recursively search classes in folders and
 * archives.
 *
 */
public class ClassLoaderHelper {

    /**
     * File filter used for Java classes
     */
    private static FileFilter classFilter = new FileFilter() {
        public boolean accept(File f) {
            return !f.isDirectory() && f.getName().endsWith(".class")
                    //exclude inner classes
                    && (f.getName().indexOf('$') < 0);
        }
    };

    /**
     * File filter used for directories
     */
    private static FileFilter dirFilter = new FileFilter() {
        public boolean accept(File f) {
            return f.isDirectory();
        }
    };

    /**
     * this class has only static methods; cannot be instantiated
     */
    private ClassLoaderHelper() {
    }

    /**
     * Adds to a collection the classes in the specified path.
     *
     * @param path      the class path to be searched
     * @param pkg       the package with which the classes will be prefixed;
     *                  useful when we search into directories to make a distinction between
     *                  class path and package; initially must be the empty string
     * @param asg       the class from which the searched classes must be assignable;
     *                  if null this argument is ignored and all the classes are returned
     * @param recursive if true the path is searched recursive
     * @param level     the level of depth, initially 0
     * @param result    where the find classes are stored
     */
    protected static void getClasses(File path, String pkg,
                                     Class<?> asg, boolean recursive, int level, Collection<String> result) {
        // handles directories
        if (path.isDirectory()) {
            File childs[] = path.listFiles(classFilter);
            if (childs != null) {
                for (File child : childs) {
                    result.add(
                            pkg + (pkg.length() > 0 ? "." : "")
                            + child.getName()
                            // remove the class post-fix
                            .substring(0, child.getName().lastIndexOf(".class"))
                    );
                }
            }
            if (recursive) {
                childs = path.listFiles(dirFilter);
                if (childs != null) {
                    for (int i = 0; i < childs.length; i++) {
                        getClasses(childs[i],
                                pkg + (pkg.length() > 0 ? "." : "") + childs[i].getName(),
                                asg, recursive, level + 1, result);
                    }
                }
            }
        }
        // handles archives
        else if (path.getName().endsWith(".jar")
                || path.getName().endsWith(".zip")) {
            try {
                for (Enumeration<? extends ZipEntry> e = new ZipFile(path).entries(); e.hasMoreElements();) {
                    ZipEntry zent = e.nextElement();
                    if (!zent.isDirectory() && zent.getName().endsWith(".class")
                            && (zent.getName().indexOf('$') < 0)
                            && (!recursive || (zent.getName().indexOf("/") >= 0))
                            ) {
                        result.add(zent.getName()
                                .substring(0, zent.getName().lastIndexOf(".class"))
                                .replace('/', '.'));
                    }
                }
            }
            catch (ZipException exc) {
            }
            catch (IOException exc) {
            }
        }
    }

    /**
     * Returns the classes in the specified path.
     *
     * @param path      the class path to be searched
     * @param asg       the class from which the searched classes must be assignable;
     *                  if null this argument is ignored and all the classes are returned
     * @param recursive if true the path is searched recursive
     */
    public static String[] getClasses(File path, Class<?> asg, boolean recursive) {
        Collection<String> x = new HashSet<String>();
        getClasses(path, "", asg, recursive, 0, x);
        String r[] = new String[x.size()];
        int i = 0;
        for (Object clazz : x) {
            r[i++] = clazz.toString();
        }
        return r;
    }

}