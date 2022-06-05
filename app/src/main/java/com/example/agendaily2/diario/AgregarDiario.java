package com.example.agendaily2.diario;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agendaily2.R;
import com.example.agendaily2.componentBD.ComponentAgendaily;
import com.example.agendaily2.pojos.Diario;
import com.example.agendaily2.pojos.User;

import java.text.SimpleDateFormat;

public class AgregarDiario extends AppCompatActivity {

    private EditText  editTextDescriptionDiario ,editTextFechaDiario;
    private TextView textViewIdDiario, textViewEncodeDiario, textViewUserIdDiario;
    private ImageView imageViewAttachedDiario, imageViewDialogDiario;
    private ComponentAgendaily componentAgendaily;          //Objeto que nos permite realizar las operaciones con la BDD
    private ProgressDialog progressDialog;          //Objeto que nos muestra la ventana de carga

    private int stopTtsManager = 0;                 //Variable de apoyo para parar la lectura del texto
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_diario);



        getSupportActionBar().setTitle("Editor de diario");



        componentAgendaily = new ComponentAgendaily(this);
        progressDialog = new ProgressDialog(AgregarDiario.this);

        editTextFechaDiario= (EditText) findViewById(R.id.editTextFechaDiario);
        editTextDescriptionDiario = (EditText) findViewById(R.id.editTextDescriptionDiario);
        textViewIdDiario = (TextView) findViewById(R.id.textViewIdDiario);
        textViewEncodeDiario = (TextView) findViewById(R.id.textViewEncodeDiario);
        textViewUserIdDiario = (TextView) findViewById(R.id.textViewUserIdDiario);
        imageViewAttachedDiario = (ImageView) findViewById(R.id.imageView);

        //Hilo para que la fecha y la hora esten actualizadas

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EditText tdate = (EditText) findViewById(R.id.editTextFechaDiario);
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
                                String dateString = sdf.format(date);
                                tdate.setText(dateString);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();

         catchDiario();
    }

    /*
     *Captura un diario, si se ha mandado desde las notas, y se meten los valores de la nota
     * en la pantalla
     */
    private void catchDiario() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            //Se obtiene el la nota del objeto Bundle y se ñadden los valores a la interfaz
            Diario diario = (Diario) bundle.getSerializable("diario");
            Diario diarios = componentAgendaily.readDiario(diario.getDiarioId());
            editTextFechaDiario.setText(diario.getFecha());
            editTextDescriptionDiario.setText(diario.getDescription());
            textViewIdDiario.setText(diarios.getDiarioId().toString());
            textViewEncodeDiario.setText(diarios.getEncode().toString());
            textViewUserIdDiario.setText(diarios.getUserId().getUserId().toString());


        }

    }
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_agregardiario, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.item_shareDiario:
                shareNote();
                break;

            case R.id.InfoAgregardiario:
                //se prepara la alerta creando nueva instancia
                AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
                //seleccionamos la cadena a mostrar
                alertbox.setMessage(getString(R.string.menu_agregarDiario));
                //elegimos un positivo SI y creamos un Listener
                alertbox.setPositiveButton(getString(R.string.Entendido), new DialogInterface.OnClickListener() {
                    //Funcion llamada cuando se pulsa el boton Si
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                //mostramos el alertbox
                alertbox.show();
                break;
            case R.id.volverdiario:
                Intent intentvolver = new Intent(AgregarDiario.this, Diarios.class);
                startActivity(intentvolver);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    /*
     *Leemos la fecha  y la descripcion del diario para mandarla como texto plano a la aplicacion
     * que elija el usuario
     */
    private void shareNote() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, editTextFechaDiario.getText().toString()
                + "\n\n" + editTextDescriptionDiario.getText().toString());
        intent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(intent, "Elige la aplicación");
        startActivity(shareIntent);
    }
    /*
     *Se crea una diario con los datos que se han metido en la interfaz, dependiendo de la variable isUpdate
     * hacemos un update del diario o un insert
     */
    public void confirmDiario(View view) {

        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog_save);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Diario diario = new Diario(Integer.parseInt(textViewIdDiario.getText().toString()), editTextFechaDiario.getText().toString(),
                editTextDescriptionDiario.getText().toString(), Integer.parseInt(textViewEncodeDiario.getText().toString())
                , new User(Integer.parseInt(textViewUserIdDiario.getText().toString())));

        if (Diarios.isUpdate) {
            componentAgendaily.updateDiario(diario.getDiarioId(), diario);
        } else {
            componentAgendaily.insertDiario(diario);
        }

        goMain();
    }
    /*
     *Nos lleva al MainActivity
     */
    private void goMain() {
        Intent intent = new Intent(AgregarDiario.this, Diarios.class);
        startActivity(intent);
        finish();
    }
}