package com.italo.instagram.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.italo.instagram.R;
import com.italo.instagram.activity.EditProfileActivity;
import com.italo.instagram.config.ConfigFirebase;
import com.italo.instagram.model.User;

public class RegisterFragment extends Fragment {

    private FirebaseAuth auth;
    private EditText editEmail, editPassword;
    private Button btnContinue;
    private ProgressBar progressBar;

    public RegisterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        iniComponents(v);
        confiInterface();

        return v;
    }

    public void confiInterface() {

        btnContinue.setOnClickListener((v) -> {

            Snackbar.make(getView(), "Deseja continuar o cadastro?",
                    BaseTransientBottomBar.LENGTH_LONG)
                    .setBackgroundTint(getResources().getColor(R.color.white))
                    .setTextColor(getResources().getColor(R.color.colorText))
                    .setAction("CONTINUAR", (action) -> {

                        validate();

                    }).show();

        });

    }

    public void validate() {

        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (!email.isEmpty()) {
            if (email.contains("gmail.com") | email.contains("hotmail.com") | email.contains("outloo.com")) {
                if (!password.isEmpty()) {

                    btnContinue.setClickable(false);
                    btnContinue.setText("CARREGANDO");
                    progressBar.setVisibility(View.VISIBLE);

                    User u = new User();
                    u.setEmail(email);
                    u.setPassword(password);
                    u.setCountPosts(0);
                    u.setCountFollowers(0);
                    u.setCountFollowing(0);

                    registerUser(u);

                } else {
                    showSnackBar("Ops! Defina uma senha!");
                }
            } else {
                showSnackBar("Ops! Digite um endereço ce e-mail válido.");
            }
        } else {
            showSnackBar("Ops! Digite seu e-mail.");
        }

    }

    public void registerUser(final User u) {

        auth.createUserWithEmailAndPassword(u.getEmail(), u.getPassword())
                .addOnCompleteListener(getActivity(), (task) -> {

                    try {
                        String id = task.getResult().getUser().getUid();
                        u.setIdUser(id);
                        u.saveDataUser();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (task.isSuccessful()) {

                        Intent i = new Intent(getContext(), EditProfileActivity.class);
                        startActivity(i);

                    } else {

                        btnContinue.setClickable(true);
                        btnContinue.setText("CONTINUAR");
                        progressBar.setVisibility(View.INVISIBLE);
                        String message = "Message";

                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            message = "Ops! Por favor defina uma senha mais forte.";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            message = "Ops! Por favor defina um e-mail válido.";
                        } catch (FirebaseAuthUserCollisionException e) {
                            message = "Ops! Este endereço de e-mail já está cadastrado.";
                        } catch (Exception e) {
                            message = "Ops! Algo saiu errado. Tente novamente mais tarde.";
                            e.printStackTrace();
                        }
                        showSnackBar(message);

                    }

                });

    }


    public void showSnackBar(String message) {

        Snackbar.make(getView(), message, BaseTransientBottomBar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.white))
                .setTextColor(getResources().getColor(R.color.colorText))
                .show();

    }

    public void iniComponents(View v) {

        editEmail = v.findViewById(R.id.editEmailCadastro);
        editPassword = v.findViewById(R.id.editSenhaCadastro);
        btnContinue = v.findViewById(R.id.btnContinuarCadastro);
        progressBar = v.findViewById(R.id.progressBarCadastro);

        auth = ConfigFirebase.getAuth();

    }

}