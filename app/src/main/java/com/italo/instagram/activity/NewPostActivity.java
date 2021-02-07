package com.italo.instagram.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.italo.instagram.R;
import com.italo.instagram.adapter.AdapterNewPost;
import com.italo.instagram.adapter.AdapterThumbnails;
import com.italo.instagram.config.ConfigFirebase;
import com.italo.instagram.config.UserFirebase;
import com.italo.instagram.helper.Alerts;
import com.italo.instagram.helper.CurrentTimeDate;
import com.italo.instagram.helper.RecyclerClick;
import com.italo.instagram.model.Photo;
import com.italo.instagram.model.Post;
import com.italo.instagram.model.User;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewPostActivity extends AppCompatActivity implements Serializable {

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private static final int CAMERA = 200;
    private static final int GALERY = 100;
    private static int countPhoto = 0;
    private final List<Photo> listPhotos = new ArrayList<>();
    private final List<Photo> listPhotosDefault = new ArrayList<>();
    private final List<String> listUrl = new ArrayList<>();
    private List<ThumbnailItem> LIST_THUNBNAILS;
    private AdapterNewPost adapterPost;
    private AdapterThumbnails adapterThumbnails;

    private RecyclerView recyclerPhoto, recyclerThumb;
    private Button btnCamera, btnGalery;
    private TextView textCount;
    private TextInputEditText editDescription;
    private Button btnSendPost;
    private List<String> LIST_FRIENDS_ID;
    private String idCurrentUser, name, nameUser, urlPhotoUser, dateHour, TYPE_POST, TITLE_TOOBAR;
    private AlertDialog.Builder alertDialog;

    private DatabaseReference database;
    private StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        LIST_THUNBNAILS = new ArrayList<>();
        iniComponents();
        getDataUser();
        configInterface();
        removePhoto();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            Bitmap image = null;

            try {

                switch (requestCode) {
                    case CAMERA:
                        image = (Bitmap) data.getExtras().get("data");
                        break;
                    case GALERY:
                        Uri url = data.getData();
                        image = MediaStore.Images.Media.getBitmap(getContentResolver(), url);
                        break;
                }

                if (image != null) {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] imageBytes = baos.toByteArray();

                    String id = database.push().getKey();

                    Photo photo = new Photo();
                    photo.setId(id);
                    photo.setImageBytes(imageBytes);
                    photo.setImageBitmap(image);

                    listPhotos.add(photo); // -> Add a lista fotos multaveis
                    listPhotosDefault.add(photo);// -> Add a lista de fotos normais

                    adapterPost.notifyDataSetChanged();

                    countPhoto = listPhotos.size();
                    textCount.setText(countPhoto + "/10");

                    configFilter();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void configFilter() {

        recyclerThumb.addOnItemTouchListener(
                new RecyclerClick(this, recyclerThumb,
                        new RecyclerClick.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                ThumbnailItem thumbItem = LIST_THUNBNAILS.get(position);

                                for (Photo photo : listPhotos) {


                                    Bitmap image = photo.getImageBitmap(); // -> Recuperando imagem padrão
                                    Bitmap imageFilter = image.copy(image.getConfig(), true); // -> Transformando em mutavel

                                    /*
                                     * Recebendo filtro clicado e passando
                                     * para o bitmap da foto da lista de fotos
                                     */

                                    Filter filter = thumbItem.filter;
                                    photo.setImageBitmap(filter.processFilter(imageFilter));

                                }
                                adapterPost.notifyDataSetChanged();

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            }
                        }));

        getFilter();

    }

    public void getFilter() {

        // -> Limpar Itens
        ThumbnailsManager.clearThumbs();
        LIST_THUNBNAILS.clear();

        // -> Configurar foto sem filtros
        ThumbnailItem DEFAULT = new ThumbnailItem();

        Photo photo = listPhotos.get(0);
        DEFAULT.image = photo.getImageBitmap();
        DEFAULT.filterName = "Normal";

        ThumbnailsManager.addThumb(DEFAULT);

        // -> Listando todos os filtros
        List<Filter> listFilter = FilterPack.getFilterPack(getApplicationContext());

        for(Filter FILTER : listFilter){

            ThumbnailItem thumbItem = new ThumbnailItem();
            thumbItem.image = photo.getImageBitmap();
            thumbItem.filter = FILTER;
            thumbItem.filterName = FILTER.getName();

            ThumbnailsManager.addThumb(thumbItem);
        }
        LIST_THUNBNAILS.addAll(ThumbnailsManager.processThumbs(getApplicationContext()));
        adapterThumbnails.notifyDataSetChanged();

    }

    public void getDataUser() {

        DatabaseReference data = database.child("User").child("Profile").child(idCurrentUser);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    User u = snapshot.getValue(User.class);
                    name = u.getName();
                    nameUser = u.getNameUser();
                    urlPhotoUser = u.getUrlPhoto();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d("ERROR", error.getMessage());
            }
        });

    }

    public void configInterface() {

        btnCamera.setOnClickListener((v) -> {

            if (listPhotos.size() < 10) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(i, CAMERA);
                }
            }

        });

        btnGalery.setOnClickListener((v) -> {

            if (listPhotos.size() < 10) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(i, GALERY);
                }
            }

        });

        btnSendPost.setOnClickListener((v) -> {

            Snackbar.make(v, "Deseja fazer esta publicação?", BaseTransientBottomBar.LENGTH_LONG)
                    .setBackgroundTint(getResources().getColor(R.color.white))
                    .setTextColor(getResources().getColor(R.color.colorText))
                    .setAction("PUBLICAR", view -> {

                        sendNewPost(); // -> Chamando enviar publicacao
                    })
                    .show();

        });


    }

    public void sendNewPost() {

        if (listPhotos.size() > 0) {

            if (listPhotos.size() <= 10) {

                showProgress();
                String description = editDescription.getText().toString();

                Post post = new Post();
                String idPost = database.push().getKey();

                for (Photo photo : listPhotos) {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photo.getImageBitmap().compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] imageBytes = baos.toByteArray();

                    StorageReference imageRef = storage
                            .child("User")
                            .child("Profile")
                            .child(idCurrentUser).child(photo.getId() + ".JPEG");
                    UploadTask upload = imageRef.putBytes(imageBytes);

                    upload.addOnSuccessListener((taskSnapshot) -> {

                        imageRef.getDownloadUrl().addOnCompleteListener((task) -> {

                            String url = task.getResult().toString();
                            listUrl.add(url);

                            saveDataPosts(post, idPost, idCurrentUser, description);

                            if (task.isComplete()) {

                                finish();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);

                            }

                        });
                    });

                }

            } else {
                Alerts.showToast(this, "Você já escolheu o máximo de fotos permitidas!");
            }
        } else {
            Alerts.showToast(this, "Você já escolheu o máximo de fotos permitidas!");
        }

    }

    public void saveDataPosts(Post post, String idPost, String idUser, String description) {

        DatabaseReference databaseReference = database;

        for (String idFriend : LIST_FRIENDS_ID) {

            post.setIdFriend(idFriend);
            post.setIdPost(idPost);
            post.setIdUser(idUser);
            post.setName(name);
            post.setNameUser(nameUser);
            post.setUrlPhotoUser(urlPhotoUser);
            post.setDescription(description);
            post.setDateHour(dateHour);
            post.setListUrlPhotos(listUrl);
            post.setCountLike(0);
            post.saveNewPost(TYPE_POST, databaseReference);

        }

    }

    public void removePhoto() {

        recyclerPhoto.addOnItemTouchListener(
                new RecyclerClick(this, recyclerPhoto,
                        new RecyclerClick.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                                Photo photo = listPhotos.get(position);
                                listPhotos.remove(photo);
                                adapterPost.notifyDataSetChanged();

                                countPhoto = listPhotos.size();
                                textCount.setText(countPhoto + "/10");

                                if (listPhotos.size() < 1){

                                    // -> Limpar Itens
                                    ThumbnailsManager.clearThumbs();
                                    LIST_THUNBNAILS.clear();
                                    adapterThumbnails.notifyDataSetChanged();

                                }
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }));

    }

    public void showProgress() {

        alertDialog.setCancelable(false)
                .setView(R.layout.progress_alert_dialog)
                .create().show();

    }

    public void iniComponents() {

        textCount = findViewById(R.id.textContadorNovaPost);
        recyclerPhoto = findViewById(R.id.recyclerNovaPost);
        recyclerThumb = findViewById(R.id.recyclerThunbnails);
        editDescription = findViewById(R.id.editLegendaNovaPost);
        btnCamera = findViewById(R.id.btnCameraNovaPost);
        btnGalery = findViewById(R.id.btnGaleriaNovaPost);
        btnSendPost = findViewById(R.id.btnPublicarFotos);
        alertDialog = new AlertDialog.Builder(this);
        Toolbar toolbar = findViewById(R.id.toolbarNovaPostagem);

        Intent i = getIntent();
        TYPE_POST = i.getStringExtra("TYPE"); // -> Recuperando tipo da postagem
        TITLE_TOOBAR = i.getStringExtra("TITLE");
        LIST_FRIENDS_ID = (List<String>) i.getSerializableExtra("LIST_DATA_FRIENDS"); // -> Recuperando lista de seguidores

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(TITLE_TOOBAR);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapterPost = new AdapterNewPost(this, listPhotos);
        recyclerPhoto.setHasFixedSize(true);
        recyclerPhoto.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.HORIZONTAL, false));
        recyclerPhoto.setAdapter(adapterPost);

        adapterThumbnails = new AdapterThumbnails(this, LIST_THUNBNAILS);
        recyclerThumb.setHasFixedSize(true);
        recyclerThumb.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.HORIZONTAL, false));
        recyclerThumb.setAdapter(adapterThumbnails);

        idCurrentUser = UserFirebase.getIdUser();
        database = ConfigFirebase.getDatabase();
        storage = ConfigFirebase.getStorage();
        dateHour = CurrentTimeDate.dateHour();

        if (TYPE_POST.equals("STORY")){

            editDescription.setVisibility(View.GONE);

        }
    }

}