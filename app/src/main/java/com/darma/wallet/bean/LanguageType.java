package com.darma.wallet.bean;


public enum LanguageType {


    SYSTEM("system", ""),


    ENGLISH("en", "English"),

    JAPAN("ja","日本語"),
    CHINESE("zh-CN", "简体中文"),
    CHINESE_HK("zh-HK","繁體中文");

    private String language;
    private String locale;



    LanguageType(String locale, String language) {

        this.locale = locale;
        this.language = language;

    }


    public String getLocale() {

        return locale == null ? "" : locale;

    }

    public String getLanguage() {

        return language == null ? "" : language;
    }
}
