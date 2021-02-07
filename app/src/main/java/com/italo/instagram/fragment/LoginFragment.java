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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.italo.instagram.R;
import com.italo.instagram.activity.MainActivity;
import com.italo.instagram.config.ConfigFirebase;
import com.italo.instagram.model.User;

public class LoginFragment extends Fragment {

    private FirebaseAuth auth;
    private EditText editEmail, editPassword;
    private Button btnLogin;
    private ProgressBar progressBar;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        iniComponents(v);
        configInterface();

        return v;
    }

    public void configInterface() {

        btnLogin.setOnClickListener((v) -> {

            validate();

        });

    }

    public void validate() {

        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (!email.isEmpty()) {
            if (!password.isEmpty()) {

                btnLogin.setClickable(false);
                btnLogin.setText("CARREGANDO");
                progressBar.setVisibility(View.VISIBLE);

                User u = new User();
                u.setEmail(email);
                u.setPassword(password);

                loginUser(u);

            } else {
                showSnackBar("Ops! Digite sua senha.");
            }
        } else {
            showSnackBar("Ops! Digite sue e-mail.");
        }

    }

    public void loginUser(User u) {

        auth.signInWithEmailAndPassword(u.getEmail(), u.getPassword())
                .addOnCompleteListener(getActivity(), (task) -> {

                    if (task.isSuccessful()) {

                        Intent i = new Intent(getContext(), MainActivity.class);
                        startActivity(i);

                    } else {

                        btnLogin.setClickable(true);
                        btnLogin.setText("ENTRAR");
                        progressBar.setVisibility(View.VISIBLE);
                        String message = "Message";

                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            message = "Ops! Você ainda não está cadastrado.";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            message = "Ops! Endereço de e-mail ou senha estão incorretos.";
                        } catch (Exception e) {
                            message = "Ops! Algo saiu errado. Tente Novamente mais tarde.";
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

        editEmail = v.findViewById(R.id.editEmailLogin);
        editPassword = v.findViewById(R.id.editSenhaLogin);
        btnLogin = v.findViewById(R.id.btnEntrarLogin);
        progressBar = v.findViewById(R.id.progressBarLogin);

        auth = ConfigFirebase.getAuth();

    }

}