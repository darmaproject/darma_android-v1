package com.darma.wallet.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.darma.wallet.Config;
import com.darma.wallet.bean.LanguageType;
import com.wallet.utils.StringUtils;

import java.util.Locale;

public class LanguageUtil {


    private static final String TAG = "LanguageUtil";


    public static String getSelectLanguage(Context context) {

        return StringUtils.getStringFromSp(context, Config.SELECT_LANGUAGE, LanguageType.SYSTEM.getLocale());
    }


    @SuppressWarnings("deprecation")

    public static void changeAppLanguage(Context context, String newLanguage) {

        if (TextUtils.isEmpty(newLanguage)) {

            return;

        }

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();


        Locale locale = getLocaleByLanguage(newLanguage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }

//        configuration.setLocale(locale);

        // updateConfiguration

        DisplayMetrics dm = resources.getDisplayMetrics();

        resources.updateConfiguration(configuration, dm);

    }

    public static String getAppLanguage(Context context) {

        String language = getSelectLanguage(context);
        if (LanguageType.SYSTEM.getLocale().equals(language)) {
            return Locale.getDefault().getLanguage();
        }
        return language;
    }

    public static Locale getLocaleByLanguage(String language) {


        if (LanguageType.SYSTEM.getLocale().equals(language)) {
            return Locale.getDefault();
        }

        if (language.contains("-")) {

            int index = language.indexOf("-");

            String lang = language.substring(0, index);
            String country = language.substring(index + 1);

            return new Locale(lang, country);
        } else {
            return new Locale(language);
        }

    }


    public static Context attachBaseContext(Context context, String language) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            return updateResources(context, language);

        } else {

            return context;

        }

    }


    @TargetApi(Build.VERSION_CODES.N)

    private static Context updateResources(Context context, String language) {

        Resources resources = context.getResources();

        Locale locale = LanguageUtil.getLocaleByLanguage(language);


        Configuration configuration = resources.getConfiguration();

        configuration.setLocale(locale);

        configuration.setLocales(new LocaleList(locale));

        return context.createConfigurationContext(configuration);

    }

}