package com.example.agendaily2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.agendaily2.activitysnotas.Notas;


public class Menus extends AppCompatActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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