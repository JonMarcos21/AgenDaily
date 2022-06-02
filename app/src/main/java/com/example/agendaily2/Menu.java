package com.example.agendaily2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.agendaily2.activitysnotas.Notas;


public class Menu extends AppCompatActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    /*
     *Se crea el menu para acceder al usuario o monstrar información en el ActionBar
     */
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnPerfil:
                Intent intent = new Intent(Menu.this, Usuario.class);
                startActivity(intent);
                break;
            case R.id.mnvolver:
                 Intent intent2 = new Intent(Menu.this, Autenticacion.class);
                startActivity(intent2);
                break;
            case R.id.mnInfo:
                break;

        }
        return super.onOptionsItemSelected(item);
    }



    public void diario(View view){

        Intent diario = new Intent(this, Usuario.class);
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