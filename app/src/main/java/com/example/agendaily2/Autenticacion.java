package com.example.agendaily2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Autenticacion extends AppCompatActivity {

    // creacion de variables para añadir datos

    Button inicio;

    private EditText contraseña1;
    private EditText correo1;

    private String textUsuario = "";
    private String textContraseña = "";

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacion);

        inicio =(Button) findViewById(R.id.btnInicio);

        contraseña1 = (EditText) findViewById(R.id.contraseñaInicio);
        correo1 = (EditText) findViewById(R.id.correoInicio);
        mAuth=FirebaseAuth.getInstance();
    }

    //boton para acceder al formulario

    public void formulario(View view){

        Intent invitado = new Intent(this, Formulario.class);
        startActivity(invitado);
        finish();

    }

    public void menu(View view){

        Intent menu = new Intent(this, Menu.class);
        startActivity(menu);
        finish();

    }
    //creo un metodo para el boton de login que en caso de que esten los campos bien pasara de pantalla
    public void acceso(View view) {

        textUsuario = correo1.getText().toString();
        textContraseña = contraseña1.getText().toString();

        if(!textUsuario.isEmpty() && !textContraseña.isEmpty()){

            loginuser();
        }
        else{
            Toast.makeText(Autenticacion.this, "Completa los campos", Toast.LENGTH_SHORT).show();
        }
    }

    //metodo para comprobar que se añaden los datos
    private void loginuser(){

        mAuth.signInWithEmailAndPassword(textUsuario, textContraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //si la tarea es buena decimos que el boton play se active
                    startActivity(new Intent(Autenticacion.this , Menu.class));

                }
                // un else que genera un toast en caso de error
                else {
                    Toast.makeText(Autenticacion.this, "No se pudo iniciar sesion, compruebe los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}