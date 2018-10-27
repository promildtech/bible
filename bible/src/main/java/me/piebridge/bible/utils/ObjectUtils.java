package me.piebridge.bible.utils;

/**
 * Created by thom on 16/7/21.
 */
public class ObjectUtils {

    private ObjectUtils() {

    }

    /**
     * I'd like name it as equals, but sonar complains
     *
     * @see java.util.Objects#equals(Object, Object)
     */
    public static boolean equals(Object a, Object b) { // NOSONAR
        return (a == b) || (a != null && a.equals(b));
    }

}