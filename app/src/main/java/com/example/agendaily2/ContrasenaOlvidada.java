package com.example.agendaily2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agendaily2.activitys.Autenticacion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ContrasenaOlvidada extends AppCompatActivity {

    private EditText txtEmail;
    private Button btnReset;

    private String email= "";

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrasenaolvidada);

        txtEmail = (EditText) findViewById(R.id.txtRecordarContra);
        btnReset = (Button) findViewById(R.id.btnEnviar);

        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              email = txtEmail.getText().toString();

              if(!email.isEmpty()){
                  mDialog.setMessage(("Se esta enviado el correo.."));
                  mDialog.setCanceledOnTouchOutside(false);
                  mDialog.show();
                  resetPassword();

              }
              else{
                  Toast.makeText(ContrasenaOlvidada.this, "Debe introducir un email", Toast.LENGTH_SHORT).show();
              }

            }
        });
    }


    private void resetPassword(){

        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Toast.makeText(ContrasenaOlvidada.this, "Se ha enviado un correo a la direccion asignada", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ContrasenaOlvidada.this, "No se pudo enviar el correo para restablecer contraseñan ", Toast.LENGTH_SHORT).show();
                }

                mDialog.dismiss();
            }
        });
    }



    public void atras(View view){

        Intent atras = new Intent(this, Autenticacion.class);
        startActivity(atras);
        finish();

    }
}