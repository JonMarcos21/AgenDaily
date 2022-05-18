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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Formulario extends AppCompatActivity {

    //creamos variables de tipo EditText
    private EditText mEditTextNombre;
    private EditText mEditTextEmail;
    private EditText mEditTextContraseña;

    //Creamos una variable de tipo button
    private Button registrar;


    //varibles de datos a registrar
    private String nombre ="";
    private String email ="";
    private String contraseña ="";

    //hacemos una llamada del metodo firebase para poder logear usuarios
    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);


        //asignamos las variables a los id de las variables del layout
        mEditTextNombre = (EditText) findViewById(R.id.txtNombre);
        mEditTextEmail =(EditText) findViewById(R.id.txtEmailFormulario);
        mEditTextContraseña = (EditText) findViewById(R.id.txtContraseñaFormulario);

        registrar = (Button) findViewById(R.id.btnRegistrarseFormulario);

        //Instanciamos las variables creadas
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Comprobación  que los campos estan bien
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // asiganamos las variables para que sean rellenadas
                nombre = mEditTextNombre.getText().toString();
                email = mEditTextEmail.getText().toString();
                contraseña = mEditTextContraseña.getText().toString();

                //hacemos un if para que en caso de que los campos esten completos se lanze el metodo registeruser
                if (!nombre.isEmpty() && !email.isEmpty() && !contraseña.isEmpty()){

                    if(contraseña.length()>=6){
                        registerUser();

                    }
                    //un else para que cuando la contraseña no tenga mas de 6 caracteres salte un toast
                    else{
                        Toast.makeText(Formulario.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }


                }
                // salta un toast si los campos no estan completos
                else {
                    Toast.makeText(Formulario.this, "Debe completar los campos ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void atras(View view){

        Intent atras = new Intent(this, Autenticacion.class);
        startActivity(atras);
        finish();

    }

    //metodo de regitrar usuario

    private void registerUser(){

        mAuth.createUserWithEmailAndPassword(email,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    //si la tarea es buena se lanza
                    startActivity(new Intent(Formulario.this , Autenticacion.class));
                    finish();
                    // creamos un hash map para guardar los campos
                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre",nombre);
                    map.put("contraseña",contraseña);
                    map.put("email",email);

                    String id= mAuth.getCurrentUser().getUid();

                    mDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                startActivity(new Intent(Formulario.this , Autenticacion.class));
                                finish();
                            }
                            else{
                                Toast.makeText(Formulario.this, "No se han podido crear los datos correctamente", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                else{
                    Toast.makeText(Formulario.this, "No se pudo registrar este usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}