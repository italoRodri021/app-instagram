package com.italo.instagram.helper;

import java.text.SimpleDateFormat;

public class CurrentTimeDate {

    // -> Padrão de Formatação = EX: Dia 10/02/20 às 12:23


    // -> Recuperando data e hora atual do dispositivo
    public static String dateHour(){

       long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String dateStromg = simpleDateFormat.format( date );

        return dateStromg;
    }

}
