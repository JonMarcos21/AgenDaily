package com.example.agendaily2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.agendaily2.componentBD.ComponentAgendaily;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    public static String nameActivity = "SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


        ComponentAgendaily componentAgendaily = new ComponentAgendaily(this);

        //Leemos el usuario de la BDD
        //Si no hay un usuario lanzamos RegistryActivity
        //En caso contrario lanzamos MainActivity
        if (componentAgendaily.readUsers() == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, Autenticacion.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, Menus.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }
}