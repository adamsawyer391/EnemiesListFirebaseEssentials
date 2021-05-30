package com.cosmic.firebaseauthentication.auth;

public class RegisterHelperMethods {

    public boolean containsLowerCase(String password){
        char c;
        for(int i = 0; i < password.length(); i++){
            c = password.charAt(i);
            if (Character.isLowerCase(c)){
                return true;
            }
        }
        return false;
    }

    public boolean containsUpperCase(String password){
        char c;
        for (int i = 0; i < password.length(); i++){
            c = password.charAt(i);
            if (Character.isUpperCase(c)){
                return true;
            }
        }
        return false;
    }

    public boolean containsSpecialCharacter(String password){
        char c, s;
        Character[] special = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '`', '~', '?', '_'};
        for (int i =0; i < password.length(); i++){
            c = password.charAt(i);
            for (Character character : special) {
                s = character;
                if (c == s) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsNumber(String password){
        char c;
        for (int i = 0; i < password.length(); i++){
            c = password.charAt(i);
            if (Character.isDigit(c)){
                return true;
            }
        }
        return false;
    }

}
