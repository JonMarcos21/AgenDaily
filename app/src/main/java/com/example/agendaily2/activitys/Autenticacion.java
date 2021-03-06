package com.example.agendaily2.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agendaily2.R;
import com.example.agendaily2.componentBD.ComponentAgendaily;
import com.example.agendaily2.hash.Sha;
import com.example.agendaily2.pojos.User;

import java.util.ArrayList;
import java.util.Iterator;

public class Autenticacion extends AppCompatActivity {

    // creacion de variables para añadir datos
    private TextView textViewWrongPassword, textViewNoMatch, textViewEmpty,registrarse;
    private EditText editTextPassword, editTextPasswordRepeated, editTextNewPassword,editTextEmail,cadena;
    private Button buttonLogin, buttonChange, buttonBack;

//llamamiento a la base de datos y al pojo user

    private User user;
    private ComponentAgendaily componentAgendaily;

    private final String SHA = "SHA-1";         //Constante que guarda el tipo de hash






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacion);

        //Inicializamos los componentes
        componentAgendaily = new ComponentAgendaily(this);

        textViewWrongPassword = (TextView) findViewById(R.id.textViewWrongPassword);
        textViewEmpty = (TextView) findViewById(R.id.textViewEmpty);
        textViewNoMatch = (TextView) findViewById(R.id.textViewNoMatch);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPasswordRepeated = (EditText) findViewById(R.id.editTextPasswordRepeated);
        editTextNewPassword = (EditText) findViewById(R.id.editTextPasswordChange);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonChange = (Button) findViewById(R.id.buttonChange);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        registrarse = (TextView)findViewById(R.id.Registrarse);


        //hacemos la llamada de los métodos creados
        userLogin();

        checkActivity();


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
     * el visible pondra los valores a la vista y el invisible los ocultara
     */
    private void checkActivity() {
        if (SplashScreen.nameActivity.equals("SplashScreen")) {
            buttonLogin.setVisibility(View.VISIBLE);
            editTextPasswordRepeated.setVisibility(View.VISIBLE);
            editTextNewPassword.setVisibility(View.INVISIBLE);
            buttonChange.setVisibility(View.INVISIBLE);
            buttonBack.setVisibility(View.INVISIBLE);
        } else if (SplashScreen.nameActivity.equals("Usuario")) {
            editTextEmail.setText(user.getEmail());
            buttonLogin.setVisibility(View.INVISIBLE);
            editTextPasswordRepeated.setVisibility(View.INVISIBLE);
            editTextNewPassword.setVisibility(View.VISIBLE);
            buttonChange.setVisibility(View.VISIBLE);
            buttonBack.setVisibility(View.VISIBLE);
            registrarse.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * Insertamos un usuario con los datos recogidos de los campos del activity en la BDD
     */
    public void singIn(View view) {



        //Comprobamos que ningun de los EditText esté vacío
        //En caso de que alguno no esté mostramos un Toast que pone que "Se deben de completar todos los campos"
        if (editTextEmail.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty() ||
                editTextPasswordRepeated.getText().toString().isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
            textViewNoMatch.setVisibility(View.INVISIBLE);
        } else {
            textViewEmpty.setVisibility(View.INVISIBLE);
            //Comprobamos que las contraseñas coinciden
            //En caso de que no coincidan mostramos un Toast que diga "Las constraseñas no coinciden"
            if (editTextPassword.getText().toString().equals(editTextPasswordRepeated.getText().toString())) {
                textViewNoMatch.setVisibility(View.INVISIBLE);
                //Leemos los datos de la interfaz, hacemos un insert en la BDD y lanzamos al menu
                User user = new User(editTextEmail.getText().toString(), passwordConvertHash(editTextPassword));

                if (componentAgendaily.insertUser(user) != 0) {

                    goTOMenu();
                } else {
                    Toast.makeText(this, "Fallo al registrar usuario", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * Actualizamos la contraseña del usuario en la BDD
     */
    public void changePassword(View view) {
        //Comprobamos que ningun de los EditText esté vacío
        //En caso de que alguno esté mostramos un TextView que pone que "Se deben de completar todos los campos"
        if (editTextNewPassword.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Se deben completar los campos", Toast.LENGTH_SHORT).show();
            textViewWrongPassword.setVisibility(View.INVISIBLE);
        } else {
            textViewEmpty.setVisibility(View.INVISIBLE);
            //Comprobamos que la contraseña sea la correcta
            //En caso de que no lo sea mostramos un TexteView que pone "Contraseña incorrecta"
            if (user.getPassword().equals(passwordConvertHash(editTextPassword))) {
                textViewWrongPassword.setVisibility(View.INVISIBLE);
                //Hacemos un update con la nueva contraseña
                componentAgendaily.updateUser(user.getEmail(), new User(editTextEmail.getText().toString(),
                        passwordConvertHash(editTextNewPassword)));

                Toast.makeText(this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                menu(new View(this));
            } else {
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Convierte el contenido del EditText en un Hash tipo SHA-1
     */
    private String passwordConvertHash(EditText editText) {
        return Sha.stringToHash(editText.getText().toString(), SHA);
    }


   // Intents wque nos llevan a la activity menus

    public void goTOMenu(){



        Intent menu = new Intent(this, Menus.class);
        startActivity(menu);
        finish();

    }

    /**
     * Se lanza SettingsActivity
     */
    public void menu(View view) {
        Intent intent = new Intent(Autenticacion.this, Menus.class);
        startActivity(intent);
        finish();
    }
}