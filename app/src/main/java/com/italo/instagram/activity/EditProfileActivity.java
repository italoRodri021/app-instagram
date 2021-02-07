package com.italo.instagram.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.italo.instagram.R;
import com.italo.instagram.config.ConfigFirebase;
import com.italo.instagram.config.UserFirebase;
import com.italo.instagram.model.User;

import java.io.ByteArrayOutputStream;

public class EditProfileActivity extends AppCompatActivity {

    private static final int CAMERA = 200;
    private static final int GALERY = 100;
    private ImageView imagePhoto;
    private TextView textEmail;
    private ImageButton btnSave;
    private Button btnSignAccout;
    private TextInputEditText editName, editNameUser, editSite, editBioDescription;
    private AlertDialog.Builder alertDialog;

    private String idUser;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        iniComponents();
        getDataUser();
        configInterface();
    }

    public void configInterface() {

        btnSave.setOnClickListener((v) -> {

            setDataUser();
        });

        btnSignAccout.setOnClickListener((v) -> {

            auth.signOut();
            finish();
            Intent i = new Intent(this, SplashActivity.class);
            startActivity(i);

        });

    }

    public void btnSelectPhoto(View v) {

        alertDialog.setTitle("Selecione a opção desejada")
                .setPositiveButton("CÂMERA", (dialog, witch) -> {

                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (i.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(i, CAMERA);
                    }

                }).setNegativeButton("GALERIA", (dialog, witch) -> {

            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (i.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(i, GALERY);
            }

        }).create().show();

    }

    public void getDataUser() {

        DatabaseReference user = database.child("User").child("Profile").child(idUser);
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    User u = snapshot.getValue(User.class);

                    editName.setText(u.getName());
                    editNameUser.setText(u.getNameUser());
                    editSite.setText(u.getSite());
                    editBioDescription.setText(u.getBioDescription());
                    textEmail.setText(u.getEmail());

                    if (u.getUrlPhoto() != null) {

                        Uri url = Uri.parse(u.getUrlPhoto());
                        Glide.with(getApplicationContext()).load(url).into(imagePhoto);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", error.getMessage());
            }
        });

    }

    public void setDataUser() {

        String name = editName.getText().toString();
        String nameUser = editNameUser.getText().toString();
        String site = editSite.getText().toString();
        String bio = editBioDescription.getText().toString();

        if (!name.isEmpty()) {
            if (!nameUser.isEmpty()) {
                if (!nameUser.contains(" ")) {

                    User u = new User();
                    u.setIdUser(idUser);
                    u.setName(name);
                    u.setNameUser(nameUser);
                    u.setSite(site);
                    u.setBioDescription(bio);
                    u.updateDataUser();

                    finish();
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);

                } else {
                    editNameUser.setError("Digite um nome de usuário sem espaços.");
                }
            } else {
                editNameUser.setError("Ops! Digite seu nome de usuário.");
            }
        } else {
            editName.setError("Ops! Digite seu nome.");
        }

    }

    public void iniComponents() {

        imagePhoto = findViewById(R.id.imageFotoPerfil);
        editName = findViewById(R.id.editNomePerfil);
        editNameUser = findViewById(R.id.editNomeUsuario);
        editSite = findViewById(R.id.editSitePerfil);
        editBioDescription = findViewById(R.id.editBiografiaPerfil);
        textEmail = findViewById(R.id.textEmailPerfil);
        btnSave = findViewById(R.id.btnSalvarPerfil);
        btnSignAccout = findViewById(R.id.btnSairConta);
        Toolbar toolbar = findViewById(R.id.toobarEditarPerfil);
        alertDialog = new AlertDialog.Builder(this);

        idUser = UserFirebase.getIdUser();
        auth = ConfigFirebase.getAuth();
        database = ConfigFirebase.getDatabase();
        storage = ConfigFirebase.getStorage();

        toolbar.setTitle("Editar Perfil");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            Bitmap imageBitmap = null;

            try {

                switch (requestCode) {
                    case CAMERA:
                        imageBitmap = (Bitmap) data.getExtras().get("data");
                        break;
                    case GALERY:
                        Uri uri = data.getData();
                        imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        imagePhoto.setImageBitmap(imageBitmap);
                        break;
                }

                if (imageBitmap != null) {

                    imagePhoto.setImageBitmap(imageBitmap);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] imageBytes = baos.toByteArray();

                    StorageReference imageRef = storage.child("User").child("Profile").child(idUser)
                            .child(idUser + ".JPEG");

                    UploadTask upload = imageRef.putBytes(imageBytes);
                    upload.addOnSuccessListener((task) -> {

                        imageRef.getDownloadUrl().addOnCompleteListener((task2) -> {

                            String url = task2.getResult().toString();

                            User u = new User();
                            u.setIdUser(idUser);
                            u.setUrlPhoto(url);
                            u.saveUpdatePhoto();

                        });

                    });

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}