package com.example.agendaily2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

        usuario = (TextView) findViewById(R.id.txtNombreUsuario);
        email = (TextView) findViewById(R.id.txtCorreoUsuario);
        //Instancia Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();



        //método para pasar una variable de la base de datos a la activity


    }

    private void getUserInfo(){
        String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    String nombre = dataSnapshot.child("nombre").getValue().toString();

                    String correo = dataSnapshot.child("email").getValue().toString();
                    usuario.setText( nombre);
                    email.setText(correo);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}