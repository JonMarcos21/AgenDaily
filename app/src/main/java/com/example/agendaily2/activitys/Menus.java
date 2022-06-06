package com.example.agendaily2.activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.agendaily2.R;
import com.example.agendaily2.activitysnotas.Notas;

//imports para anuncios admob

import com.example.agendaily2.avisos.Avisos;
import com.example.agendaily2.calendario.Calendario;
import com.example.agendaily2.diario.Diarios;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class Menus extends AppCompatActivity  {

    //creamos un adview para monstar los anuncios
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //le damos un nombre a la activity
        SplashScreen.nameActivity = "Usuario";
        //Implementación de anuncios AdMob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        //Creación de los anuncios
        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    //creamos un menu superior
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }
    //Hacemos un llamamiento a los items del menu
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //Opcion de información
            case R.id.mnInfo:
                //se prepara la alerta creando nueva instancia
                AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
                //seleccionamos la cadena a mostrar
                alertbox.setMessage(getString(R.string.ayuda_menu));
                //elegimos un positivo SI y creamos un Listener
                alertbox.setPositiveButton(getString(R.string.Entendido), new DialogInterface.OnClickListener() {
                    //Funcion llamada cuando se pulsa el boton Si
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                //mostramos el alertbox
                alertbox.show();
                break;

                //Opción  de cambiar contraseña y ver perfil
            case R.id.mCambiar:

                //Monstramos un dialogo para cambiar la contraseña y ver el perfil
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Quiere cambiar la contraseña y ver el usuario?")
                        .setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Menus.this, Autenticacion.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.show();

                break;

                //Opcion con la función de premium
            case R.id.mnPremium:
                Toast.makeText(this, "No disponible en estos momentos", Toast.LENGTH_SHORT).show();
                break;

        }


        return super.onOptionsItemSelected(item);
    }



    //creacion de intent para ir a las activitys

    //intent para activity diario
    public void diario(View view){

        Intent diario = new Intent(this, Diarios.class);
        startActivity(diario);
        finish();

    }
    //intent para activity notas
    public void notas(View view){

        Intent notas = new Intent(this, Notas.class);
        startActivity(notas);
        finish();

    }
    //intent para activity avisos
    public void avisos(View view){

        Intent avisos = new Intent(this, Avisos.class);
        startActivity(avisos);
        finish();

    }
    //intent para activity calendario
    public void calendario(View view){

        Intent calendario = new Intent(this, Calendario.class);
        startActivity(calendario);
        finish();

    }



}