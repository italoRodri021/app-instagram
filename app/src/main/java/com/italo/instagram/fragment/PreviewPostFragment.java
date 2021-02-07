package com.italo.instagram.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.italo.instagram.R;
import com.italo.instagram.model.Post;

public class PreviewPostFragment extends Fragment {

    private TextView textNameUser;
    private ImageView imagePhotoUser, imagePhotoPost;

    public PreviewPostFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_preview_post, container, false);

        iniComponents(v);

        // -> Recuperando dados da postagem
        Bundle b = getArguments();
        Post post = (Post) b.getSerializable("DATA_POST");

        textNameUser.setText(post.getNameUser());

        Uri photoUser = Uri.parse(post.getUrlPhotoUser());
        Glide.with(getContext()).load(photoUser).into(imagePhotoUser);

        Uri photoPost = Uri.parse(post.getListUrlPhotos().get(0));
        Glide.with(getContext()).load(photoPost).into(imagePhotoPost);

        return v;
    }

    public void iniComponents(View v) {

        textNameUser = v.findViewById(R.id.textNomeUsuarioPreviewPost);
        imagePhotoUser = v.findViewById(R.id.imageUsuarioPreviewAmigo);
        imagePhotoPost = v.findViewById(R.id.imageFotoPreviewAmigo);

    }


}