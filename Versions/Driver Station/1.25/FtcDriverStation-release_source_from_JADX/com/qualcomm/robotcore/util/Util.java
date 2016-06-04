package com.qualcomm.robotcore.util;

import android.widget.TextView;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Util {
    public static String ASCII_RECORD_SEPARATOR = null;
    public static final String LOWERCASE_ALPHA_NUM_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";

    /* renamed from: com.qualcomm.robotcore.util.Util.1 */
    static class C01251 implements Comparator<File> {
        C01251() {
        }

        public /* synthetic */ int compare(Object obj, Object obj2) {
            return m333a((File) obj, (File) obj2);
        }

        public int m333a(File file, File file2) {
            return file.getName().compareTo(file2.getName());
        }
    }

    /* renamed from: com.qualcomm.robotcore.util.Util.2 */
    static class C01262 implements Runnable {
        final /* synthetic */ TextView f579a;
        final /* synthetic */ String f580b;

        C01262(TextView textView, String str) {
            this.f579a = textView;
            this.f580b = str;
        }

        public void run() {
            this.f579a.setText(this.f580b);
        }
    }

    static {
        ASCII_RECORD_SEPARATOR = "\u001e";
    }

    public static String getRandomString(int stringLength, String charSet) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < stringLength; i++) {
            stringBuilder.append(charSet.charAt(random.nextInt(charSet.length())));
        }
        return stringBuilder.toString();
    }

    public static void sortFilesByName(File[] files) {
        Arrays.sort(files, new C01251());
    }

    public static void updateTextView(TextView textView, String msg) {
        if (textView != null) {
            textView.post(new C01262(textView, msg));
        }
    }

    public static byte[] concatenateByteArrays(byte[] first, byte[] second) {
        Object obj = new byte[(first.length + second.length)];
        System.arraycopy(first, 0, obj, 0, first.length);
        System.arraycopy(second, 0, obj, first.length, second.length);
        return obj;
    }
}
