package com.italo.instagram.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.italo.instagram.config.ConfigFirebase;
import com.italo.instagram.config.UserFirebase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post implements Serializable {

    private String typePost;
    private String idPost;
    private String idUser;
    private String idFriend;
    private String name;
    private String nameUser;
    private String urlPhotoUser;
    private String description;
    private String dateHour;
    private int countLike;
    private List<String> listUrlPhotos;

    public void saveNewPost(String TYPE, DatabaseReference database) {

        Map<String, Object> map = new HashMap<>();
        map.put("idPost", getIdPost());
        map.put("idUser", getIdUser());
        map.put("name", getName());
        map.put("nameUser", getNameUser());
        map.put("urlPhotoUser", getUrlPhotoUser());
        map.put("description", getDescription());
        map.put("dateHour", getDateHour());
        map.put("listUrlPhotos", getListUrlPhotos());

        // -> Verificando se o tipo da postagem é story ou feed

        if (TYPE.equals("FEED")) { // -> FEED

            DatabaseReference feed = database // -> Salvando postagem no feed
                    .child("Feed")
                    .child(getIdFriend());
            feed.child(getIdPost()).updateChildren(map);

            DatabaseReference postCurrentUser = database // -> Salvando postagem para o usuario
                    .child("PostsUser")
                    .child(getIdUser());
            postCurrentUser.child(getIdPost()).updateChildren(map);

        } else { // -> STORY

            DatabaseReference feed = database // -> Salvando postagem no story
                    .child("Storys")
                    .child(getIdFriend())
                    .child(getIdUser());
            feed.updateChildren(map);

            DatabaseReference postCurrentUser = database // -> Salvando story para o usuario
                    .child("StorysUser")
                    .child(getIdUser());
            postCurrentUser.updateChildren(map);

        }
    }

    public void saveLikesPost() {

        String idUser = UserFirebase.getIdUser();
        DatabaseReference database = ConfigFirebase.getDatabase(); // Salvando Curtidas

        database.child("User").child("Profile").child(idUser)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        User user = snapshot.getValue(User.class);

                        Map<String, Object> map = new HashMap<>();
                        map.put("idPost", getIdPost());
                        map.put("idUser", user.getIdUser());
                        map.put("nameUser", user.getNameUser());
                        map.put("urlPhotoUser", user.getUrlPhoto());

                        database.child("PostsLikes")
                                .child(getIdPost())
                                .child(idUser)
                                .updateChildren(map);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.d("ERROR", error.getMessage());
                    }
                });


    }

    public void updateLikePost(String response, String idCurrentUser) {

        DatabaseReference database = ConfigFirebase.getDatabase(); // -> Atualizando contador de likes
        Map<String, Object> map = new HashMap<>();

        if (response.equals("LIKED")) {

            map.put("countLike", getCountLike() + 1); // -> Recuperando curtidas e add 1 like

            database.child("PostsLikes")
                    .child(getIdPost())
                    .updateChildren(map);

        } else {


            map.put("countLike", getCountLike() - 1); // -> Recuperando curtidas e removendo 1 like

            database.child("PostsLikes")
                    .child(getIdPost())
                    .updateChildren(map);

            database.child("PostsLikes")
                    .child(getIdPost())
                    .child(idCurrentUser).removeValue();

        }

    }


    // _____________________________Getter and Setter_______________________________________________

    public String getTypePost() {
        return typePost;
    }

    public void setTypePost(String typePost) {
        this.typePost = typePost;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
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

    public String getUrlPhotoUser() {
        return urlPhotoUser;
    }

    public void setUrlPhotoUser(String urlPhotoUser) {
        this.urlPhotoUser = urlPhotoUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateHour() {
        return dateHour;
    }

    public void setDateHour(String dateHour) {
        this.dateHour = dateHour;
    }

    public int getCountLike() {
        return countLike;
    }

    public void setCountLike(int countLike) {
        this.countLike = countLike;
    }

    public List<String> getListUrlPhotos() {
        return listUrlPhotos;
    }

    public void setListUrlPhotos(List<String> listUrlPhotos) {
        this.listUrlPhotos = listUrlPhotos;
    }
}
