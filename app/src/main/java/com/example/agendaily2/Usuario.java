package com.example.agendaily2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Usuario extends AppCompatActivity {

    private TextView usuario;
    private TextView email;

    private Button borrarCuenta;
    private Button Salir;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);


        email = (TextView) findViewById(R.id.txtCorreoUsuario);
        //Instancia Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();



        //método para pasar una variable de la base de datos a la activity


    }
    /*
     *Se crea el menu para acceder al usuario o monstrar información en el ActionBar
     */
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnBorrarCuenta:
                break;

            case R.id.mnPremium:
                Toast.makeText(this, "No disponible en estos momentos", Toast.LENGTH_SHORT).show();
                break;
            case R.id.volverMenu:
                Intent intentvolver = new Intent(Usuario.this, Menus.class);
                startActivity(intentvolver);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    public void contraseñanueva(View view){
        Intent avisos = new Intent(this, Autenticacion.class);
        startActivity(avisos);
        finish();
    }



}