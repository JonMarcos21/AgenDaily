package com.example.agendaily2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agendaily2.componentBD.ComponentAgendaily;
import com.example.agendaily2.hash.Sha;
import com.example.agendaily2.pojos.User;

import java.util.ArrayList;
import java.util.Iterator;

public class Autenticacion extends AppCompatActivity {

    // creacion de variables para añadir datos
    private TextView textViewWrongPassword, textViewNoMatch, textViewEmpty, editTextEmail;
    private EditText editTextPassword, editTextPasswordRepeated, editTextNewPassword;
    private Button buttonLogin, buttonChange, buttonBack;


    private User user;                          //Creamos un POJO de apoyo
    private ComponentAgendaily componentAgendaily;      //Objeto que nos permite realizar las operaciones con la BDD

    private final String SHA = "SHA-1";         //Constante que guarda el tipo de hash
    Button inicio;


    private String email ="";
    private String contraseña ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacion);

        //Inicializamos los componentes
        componentAgendaily = new ComponentAgendaily(this);


        textViewEmpty = (TextView) findViewById(R.id.textViewEmpty);
        textViewNoMatch = (TextView) findViewById(R.id.textViewNoMatch);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPasswordRepeated = (EditText) findViewById(R.id.editTextPasswordRepeated);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        userLogin();

        checkActivity();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = editTextEmail.getText().toString();
                contraseña= editTextPassword.getText().toString();

                //hacemos un if para que en caso de que los campos esten completos se lanze el metodo registeruser
                if (!email.isEmpty() && !contraseña.isEmpty()){

                    if(contraseña.length()>=6){
                        singIn();


                    }
                    //un else para que cuando la contraseña no tenga mas de 6 caracteres salte un toast
                    else{
                        Toast.makeText(Autenticacion.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }


                }
                // salta un toast si los campos no estan completos
                else {
                    Toast.makeText(Autenticacion.this, "Debe completar los campos ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Leemos el usuario de la BDD
     */
    private void userLogin() {
        ArrayList<User> users = componentAgendaily.readUsers();
        if (users != null) {
            Iterator itr = users.iterator();
            while (itr.hasNext()) {
                user = (User) itr.next();
            }
        }
    }


    /**
     * Comprobamos si el usuario viene del SplashActivity o del SettingsActivity
     * Dependiendo de que pantalla venga mostramos unos botones u otros
     */
    private void checkActivity() {
        if (SplashScreen.nameActivity.equals("SplashScreen")) {
            buttonLogin.setVisibility(View.VISIBLE);
            editTextPasswordRepeated.setVisibility(View.VISIBLE);

        }
    }


    /**
     * Insertamos un usuario con los datos recogidos de los campos del activity en la BDD
     */
    public void singIn() {

        //Comprobamos que ningun de los EditText esté vacío
        //En caso de que alguno esté mostramos un TextView que pone que "Se deben de completar todos los campos"
        if (editTextEmail.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty() ||
                editTextPasswordRepeated.getText().toString().isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
            textViewNoMatch.setVisibility(View.INVISIBLE);
        } else {
            textViewEmpty.setVisibility(View.INVISIBLE);
            //Comprobamos que las contraseñas coinciden
            //En caso de que no coincidan mostramos un TextView que pone "Las constraseñas no coinciden"
            if (editTextPassword.getText().toString().equals(editTextPasswordRepeated.getText().toString())) {
                textViewNoMatch.setVisibility(View.INVISIBLE);
                //Leemos los datos de la interfaz, hacemos un insert en la BDD y lanzamos MainActivity
                User user = new User(editTextEmail.getText().toString(), passwordConvertHash(editTextPassword));

                if (componentAgendaily.insertUser(user) != 0) {
                    goTOMenu();
                } else {
                    Toast.makeText(this, "Fallo al registrar usuario", Toast.LENGTH_SHORT).show();
                }
            } else {
                textViewNoMatch.setVisibility(View.VISIBLE);
            }
        }
    }
    /**
     * Convierte el contenido del EditText en un Hash tipo SHA-1
     */
    private String passwordConvertHash(EditText editText) {
        return Sha.stringToHash(editText.getText().toString(), SHA);
    }


    //boton para acceder al formulario

    public void formulario(View view){

        Intent invitado = new Intent(this, Formulario.class);
        startActivity(invitado);
        finish();

    }

    //boton para acceder al reseteo de la contraseña

    public void contrasena(View view){

        Intent contrasena = new Intent(this, ContrasenaOlvidada.class);
        startActivity(contrasena);
        finish();

    }

    public void goTOMenu(){

        Intent menu = new Intent(this, Menus.class);
        startActivity(menu);
        finish();

    }
}