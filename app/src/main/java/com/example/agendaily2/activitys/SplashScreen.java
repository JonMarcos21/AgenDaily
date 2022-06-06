package com.example.agendaily2.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.agendaily2.R;
import com.example.agendaily2.componentBD.ComponentAgendaily;

public class SplashScreen extends AppCompatActivity {

    //Establecemos duracion y nombre de la activity para el splashScreen
    private static int SPLASH_TIME_OUT = 2000;
    public static String nameActivity = "SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        getSupportActionBar().hide();


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