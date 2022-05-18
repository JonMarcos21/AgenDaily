package com.example.agendaily2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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