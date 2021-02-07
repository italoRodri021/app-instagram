package com.italo.instagram.model;

import com.google.firebase.database.DatabaseReference;
import com.italo.instagram.config.ConfigFirebase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {

    private String idUser;
    private String idFriend;
    private String email;
    private String password;
    private String name;
    private String nameUser;
    private String site;
    private String bioDescription;
    private String urlPhoto;
    private Integer countPosts;
    private Integer countFollowers; // -> Seguidores
    private Integer countFollowing; // -> Seguindo

    public void saveDataUser() {

        DatabaseReference user = ConfigFirebase.getDatabase();
        Map<String, Object> map = new HashMap<>();
        map.put("idUser", getIdUser());
        map.put("email", getEmail());
        map.put("name", getName());
        map.put("nameUser", getNameUser());
        map.put("site", getSite());
        map.put("bioDescription", getBioDescription());
        map.put("countPosts", getCountPosts());
        map.put("countFollowers", getCountFollowers());
        map.put("countFollowing", getCountFollowing());

        user.child("User")
                .child("Profile")
                .child(getIdUser())
                .updateChildren(map);
    }

    public void updateDataUser() {

        DatabaseReference user = ConfigFirebase.getDatabase();
        Map<String, Object> map = new HashMap<>();
        map.put("name", getName());
        map.put("nameUser", getNameUser());
        map.put("site", getSite());
        map.put("bioDescription", getBioDescription());

        user.child("User")
                .child("Profile")
                .child(getIdUser())
                .updateChildren(map);

    }

    public void saveUpdatePhoto() {

        DatabaseReference user = ConfigFirebase.getDatabase();
        Map<String, Object> map = new HashMap<>();
        map.put("urlPhoto", getUrlPhoto());

        user.child("User")
                .child("Profile")
                .child(getIdUser())
                .updateChildren(map);

    }

    public void saveDataFollow() {

        DatabaseReference follow = ConfigFirebase.getDatabase();
        Map<String, Object> map = new HashMap<>();
        map.put("idFriend", getIdFriend());
        map.put("idUser", getIdUser());
        map.put("name", getName());
        map.put("nameUser", getNameUser());
        map.put("urlPhoto", getUrlPhoto());

        follow.child("Followers")
                .child(getIdFriend())
                .child(getIdUser())
                .updateChildren(map);

    }

    public void updateCountFollowers() { // -> Atualizando Seguidores do amigo

        DatabaseReference follow = ConfigFirebase.getDatabase();
        Map<String, Object> map = new HashMap<>();
        map.put("countFollowers", getCountFollowers());

        follow.child("User")
                .child("Profile")
                .child(getIdUser())
                .updateChildren(map);
    }

    public void updateCountFollowing() { // -> Atualizando Seguindo do usuario logado

        DatabaseReference follow = ConfigFirebase.getDatabase();
        Map<String, Object> map = new HashMap<>();
        map.put("countFollowing", getCountFollowing());

        follow.child("User")
                .child("Profile")
                .child(getIdUser())
                .updateChildren(map);
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdFriend() {
        return idFriend;
    }

    public void setIdFriend(String idFriend) {
        this.idFriend = idFriend;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getBioDescription() {
        return bioDescription;
    }

    public void setBioDescription(String bioDescription) {
        this.bioDescription = bioDescription;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public Integer getCountPosts() {
        return countPosts;
    }

    public void setCountPosts(Integer countPosts) {
        this.countPosts = countPosts;
    }

    public Integer getCountFollowers() {
        return countFollowers;
    }

    public void setCountFollowers(Integer countFollowers) {
        this.countFollowers = countFollowers;
    }

    public Integer getCountFollowing() {
        return countFollowing;
    }

    public void setCountFollowing(Integer countFollowing) {
        this.countFollowing = countFollowing;
    }
}
