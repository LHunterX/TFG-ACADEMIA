package com.academia.app.utils;


import at.favre.lib.crypto.bcrypt.BCrypt;

public abstract class PassUtils {

    public static String hash(String pass) {
        return BCrypt.withDefaults().hashToString(12, pass.toCharArray());
    }

    public static boolean check(String pass, String hash) {
        return BCrypt.verifyer().verify(pass.toCharArray(), hash).verified;
    }

}
