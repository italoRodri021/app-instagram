package com.italo.instagram.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserFirebase {

    public static String getIdUser(){
        FirebaseAuth auth = ConfigFirebase.getAuth();
        return  auth.getCurrentUser().getUid();
    }

    public static FirebaseUser userFirebase(){
        FirebaseUser user = ConfigFirebase.getAuth().getCurrentUser();
        return user;
    }

}
