package com.italo.instagram.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.italo.instagram.R;
import com.italo.instagram.adapter.AdapterFeed;
import com.italo.instagram.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PreviewPostActivity extends AppCompatActivity {

    private Post post;
    private RecyclerView recyclerPost;
    private AdapterFeed adapter;
    private final List<Post> listPost = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_post);

        iniComponents();

    }

    public void iniComponents() {

        recyclerPost = findViewById(R.id.recyclerPreviewPost);
        Toolbar toolbar = findViewById(R.id.toolbarPreviewPost);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Publicações");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new AdapterFeed(getApplicationContext(), listPost);
        recyclerPost.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerPost.setHasFixedSize(true);
        recyclerPost.setAdapter(adapter);

        Intent i = getIntent();
        post = (Post) i.getSerializableExtra("DATA_POST");

        listPost.add(post);
        adapter.notifyDataSetChanged();
    }

}