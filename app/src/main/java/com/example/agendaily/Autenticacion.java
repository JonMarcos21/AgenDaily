package com.example.agendaily;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Autenticacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacion);
    }

    //boton para acceder al formulario

    public void formulario(View view){

        Intent invitado = new Intent(this, Formulario.class);
        startActivity(invitado);
        finish();

    }
}