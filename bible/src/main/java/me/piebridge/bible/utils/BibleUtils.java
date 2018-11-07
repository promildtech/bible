package me.piebridge.bible.utils;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import me.piebridge.bible.OsisItem;
import me.piebridge.bible.R;
import me.piebridge.bible.activity.AbstractReadingActivity;
import me.piebridge.bible.component.VersionComponent;
import me.piebridge.bible.provider.VersionProvider;

/**
 * Created by thom on 16/7/1.
 */
public class BibleUtils {

    private BibleUtils() {

    }

    public static String getBook(String osis) {
        int index = osis.indexOf('.');
        if (index > 0) {
            return osis.substring(0, index);
        } else {
            LogUtils.w("invalid osis: " + osis);
            return osis;
        }
    }

    public static String getChapter(String osis) {
        if (osis == null) {
            return null;
        }
        int index = osis.indexOf('.');
        if (index > 0) {
            return osis.substring(index + 1);
        } else {
            LogUtils.w("invalid osis: " + osis);
            return "1";
        }
    }

    public static String getChapter(String osis, Context context) {
        String chapter = getChapter(osis);
        if (VersionComponent.INTRO.equalsIgnoreCase(chapter)) {
            return context.getString(R.string.reading_chapter_intro);
        } else {
            return chapter;
        }
    }

    public static String getChapterVerse(Context context, Bundle bundle) {
        String osis = bundle.getString(AbstractReadingActivity.OSIS);
        String chapter = getChapter(osis, context);
        int verseStart = NumberUtils.parseInt(bundle.getString(AbstractReadingActivity.VERSE_START));
        int verseEnd = NumberUtils.parseInt(bundle.getString(AbstractReadingActivity.VERSE_END));
        if (verseStart > 0) {
            StringBuilder sb = new StringBuilder(chapter);
            sb.append(":");
            sb.append(verseStart);
            if (verseEnd > 0) {
                sb.append("-");
                sb.append(verseEnd);
            }
            return sb.toString();
        } else {
            return chapter;
        }
    }

    public static String getBookChapterVerse(String bookName, String chapterVerse) {
        return bookName + " " + chapterVerse;
    }

    public static String getFontPath(Context context) {
        File font;
        File[] dirs = ContextCompat.getExternalFilesDirs(context, null);
        if (dirs.length > 0 && dirs[0] != null) {
            font = new File(dirs[0], "custom.ttf");
            if (font.isFile()) {
                return font.getAbsolutePath();
            }
        }
        return null;
    }

    public static int[] getChapterVerse(String string) {
        int value = new BigDecimal(string).multiply(new BigDecimal(VersionProvider.THOUSAND)).intValue();
        int chapter = value / VersionProvider.THOUSAND;
        int verse = value % VersionProvider.THOUSAND;
        return new int[] {chapter, verse};
    }

    public static boolean isDemoVersion(String version) {
        switch (version) {
            case "cuvmpsdemo":
            case "cuvmptdemo":
            case "asvdemo":
                return true;
            default:
                return false;
        }
    }

    public static String removeDemo(String version) {
        switch (version) {
            case "cuvmpsdemo":
                return "cuvmps";
            case "cuvmptdemo":
                return "cuvmpt";
            case "asvdemo":
                return "asv";
            default:
                return version;
        }
    }

    public static boolean isCJK(String s) {
        for (char c : s.toCharArray()) {
            if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
                return true;
            }
        }
        return false;
    }

    public static String getVerse(String verses) {
        int index1 = verses.indexOf('-');
        int index2 = verses.indexOf(',');
        if (index1 > 0 && index2 > 0) {
            return verses.substring(0, Math.min(index1, index2));
        } else if (index1 > 0) {
            return verses.substring(0, index1);
        } else if (index2 > 0) {
            return verses.substring(0, index2);
        } else {
            return verses;
        }
    }

    public static String getLastVerse(String verses) {
        int index1 = verses.lastIndexOf('-');
        int index2 = verses.lastIndexOf(',');
        int index = Math.max(index1, index2);
        if (index < 0) {
            return verses;
        } else {
            return verses.substring(index + 1);
        }
    }

    public static void fixItems(ArrayList<OsisItem> items) {
        Iterator<OsisItem> it = items.iterator();
        while (it.hasNext()) {
            OsisItem item = it.next();
            if (TextUtils.isEmpty(item.chapter)) {
                it.remove();
            }
        }
    }

}
