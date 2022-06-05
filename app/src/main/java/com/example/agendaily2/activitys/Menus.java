package com.example.agendaily2.activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

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

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Implementación de anuncios AdMob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnPerfil:
                Intent intent = new Intent(Menus.this, Usuario.class);
                startActivity(intent);
                break;
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
        }


        return super.onOptionsItemSelected(item);
    }



    public void diario(View view){

        Intent diario = new Intent(this, Diarios.class);
        startActivity(diario);
        finish();

    }
    public void notas(View view){

        Intent notas = new Intent(this, Notas.class);
        startActivity(notas);
        finish();

    }
    public void avisos(View view){

        Intent avisos = new Intent(this, Avisos.class);
        startActivity(avisos);
        finish();

    }
    public void calendario(View view){

        Intent calendario = new Intent(this, Calendario.class);
        startActivity(calendario);
        finish();

    }
    public void atrasabajo(View view){

        Intent atras = new Intent(this, Autenticacion.class);
        startActivity(atras);
        finish();

    }


}