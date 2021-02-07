package com.italo.instagram.helper;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permission {

    public static String[] PERMISSIONS_USER = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    public static boolean validatePermissions(String[] permissions, Activity context, int requestCode) {

        // -> Validando se a versão da api é 29 ou superior
        if (Build.VERSION.SDK_INT >= 23) {

            // -> Passanto lista de permissões
            List<String> listPermissions = new ArrayList<>();

            // -> Percorrento permissoes e testando uma a uma para saber se já foram aceitas
            for (String permissao : permissions) {

                // -> Verificando se já foi concedida
                boolean hasPermission = ContextCompat.checkSelfPermission(context, permissao) == PackageManager.PERMISSION_GRANTED;

                // -> Caso nã tenha permissão
                if (!hasPermission) listPermissions.add(permissao);
            }

            /*
             * Verificando se a lista de permissões está vazia.
             * caso esteja vai retorna "true" e não vai precisar mais pedir permissões
             */
            if (listPermissions.isEmpty()) return true;

            // -> Convertendo novas permissões
            String[] newPermissions = new String[listPermissions.size()];
            listPermissions.toArray(newPermissions);

            // -> Caso a lista não esteja vazia vai cair no requestCode
            ActivityCompat.requestPermissions(context, newPermissions, requestCode);
        }

        return true;
    }

}
