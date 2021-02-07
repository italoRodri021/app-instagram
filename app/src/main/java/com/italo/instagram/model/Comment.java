package com.italo.instagram.model;

import com.google.firebase.database.DatabaseReference;
import com.italo.instagram.config.ConfigFirebase;

import java.util.HashMap;
import java.util.Map;

public class Comment {

    private String idPost;
    private String idComment;
    private String idUser;
    private String nameUser;
    private String urlPhoto;
    private String comment;

    public void saveNewComment() {

        DatabaseReference database = ConfigFirebase.getDatabase();

        Map<String, Object> map = new HashMap<>();
        map.put("idPost", getIdPost());
        map.put("idComment", getIdComment());
        map.put("idUser", getIdUser());
        map.put("nameUser", getNameUser());
        map.put("urlPhoto", getUrlPhoto());
        map.put("comment", getComment());

        database.child("Comments")
                .child(getIdPost())
                .child(getIdComment())
                .updateChildren(map);


    }


    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
