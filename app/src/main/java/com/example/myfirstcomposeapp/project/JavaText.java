package com.example.myfirstcomposeapp.project;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaText {
    public static SpannableString matcherSearchTitle(int color, String text, String[] keyword) {

        SpannableString s = new SpannableString(text);

        for (int i = 0; i < keyword.length; i++) {

            Pattern p = Pattern.compile(keyword[i]);

            Matcher m = p.matcher(s);

            while (m.find()) {

                int start = m.start();

                int end = m.end();

                s.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
        }
        return s;
    }
}
