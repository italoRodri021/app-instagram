package com.italo.instagram.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.italo.instagram.R;
import com.italo.instagram.model.Post;

import java.util.ArrayList;
import java.util.List;

public class StoryPreviewActivity extends AppCompatActivity {

    private final List<SlideModel> modelList = new ArrayList<>();
    private Post post;
    private List<String> listImageUrl = new ArrayList<>();
    private ImageSlider imageSlider;
    private ImageView imagePhotoUser;
    private TextView textNameUser;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_preview);

        iniComponents();
        configInterface();

    }

    public void configInterface() {

        Glide.with(this).load(post.getUrlPhotoUser()).into(imagePhotoUser);
        textNameUser.setText(post.getNameUser());

        for (String URL : listImageUrl) {

            SlideModel sm = new SlideModel(URL, "", ScaleTypes.CENTER_INSIDE);
            modelList.add(sm);
            imageSlider.setImageList(modelList);
            imageSlider.startSliding(8000);

        }


    }

    public void iniComponents() {

        imageSlider = findViewById(R.id.imageSlidePreviewStory);
        imagePhotoUser = findViewById(R.id.imageUsuarioPreviewStory);
        textNameUser = findViewById(R.id.textNomeUsuarioPreviewStory);
        progressBar = findViewById(R.id.progressBarStory);

        Intent i = getIntent();
        post = (Post) i.getSerializableExtra("DATA_POST");
        listImageUrl = post.getListUrlPhotos();

    }

}