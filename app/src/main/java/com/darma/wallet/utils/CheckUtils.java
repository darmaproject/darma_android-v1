package com.darma.wallet.utils;

import com.wallet.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wallet.WalletConfig.MNEMONIC_WORD_SPLIT;

/**
 * Created by Darma Project on 2019/9/27.
 */
public class CheckUtils {

    /**
     * @param post
     * @return
     */
    public static boolean isLegalPost(String post) {

        try {
            int p = Integer.parseInt(post);
            if(1<=p&&p<=65535){

                return true;
            }
        }catch (Exception e){

        }

        return false;
    }
    /**
     * @param name
     * @return
     */
    public static boolean isLegalWalletName(String name) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9_\\@\\#\\$\\%\\^\\&\\*\\(\\)\\!\\,\\.\\?\\-\\+\\|\\=]{3,16}$");
        Matcher m = p.matcher(name);

        return m.matches()&&passwordLevel(name)>=1;
    }
    /**
     * @param pwd
     * @return
     */
    public static boolean isLegalPwd(String pwd) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9_\\@\\#\\$\\%\\^\\&\\*\\(\\)\\!\\,\\.\\?\\-\\+\\|\\=]{6,32}$");
        Matcher m = p.matcher(pwd);

        return m.matches()&&passwordLevel(pwd)>=1;
    }

    /**
     * @param key
     * @return
     */
    public static boolean isLegalPrivateKey(String key) {
        Pattern p = Pattern.compile("^[0-9a-fA-F]{64}$");
        Matcher m = p.matcher(key);


        return m.matches();
    }

    /**
     * @param key
     * @return
     */
    public static boolean isLegalViewOnlyKey(String key) {
        Pattern p = Pattern.compile("^[0-9a-fA-F]{64}$");
        Matcher m = p.matcher(key);
        return m.matches();
    }

    /**
     * @param words
     * @return
     */
    public static boolean isLegalMnemonicWords(String words) {
        if (StringUtils.isEmpty(words)) {

            return false;
        }

        try{

            String[] list = words.split(MNEMONIC_WORD_SPLIT);
            if(list.length==25){
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


        return false;
    }
    /**
     *
     * @param password
     * @return
     */
    public static int passwordLevel(String password) {
        char Modes = 0;
        char[] chars=password.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            Modes |= CharMode(chars[i]);
        }
        return bitTotal(Modes);
    }
    //CharMode
    public static int   CharMode(char in) {
        if (in >= 48 && in <= 57)//number
        {
            return 1;
        }
        if (in >= 65 && in <= 90) //Capital
        {
            return 2;
        }
        if (in >= 97 && in <= 122)//lowercase
        {
            return 4;
        }
        else {
            return 8;
        } //Special characters
    }

    //bitTotal
    public static int bitTotal(char num) {
        char  modes = 0;
        for (int i = 0; i < 4; i++) {
            if ((num & 1)!=0) modes++;
            num >>= 1;
        }
        return modes;
    }
}
