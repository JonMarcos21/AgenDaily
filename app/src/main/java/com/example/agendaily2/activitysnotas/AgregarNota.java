package com.example.agendaily2.activitysnotas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agendaily2.R;
import com.example.agendaily2.componentBD.ComponentAgendaily;
import com.example.agendaily2.pojos.Note;
import com.example.agendaily2.pojos.User;

public class AgregarNota extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription;
    private TextView textViewId, textViewEncode, textViewUserId;
    private ImageView imageViewAttached, imageViewDialog;
    private ImageButton imageButtonVolume;
    private ComponentAgendaily componentAgendaily;          //Objeto que nos permite realizar las operaciones con la BDD
    private ProgressDialog progressDialog;          //Objeto que nos muestra la ventana de carga

    private int stopTtsManager = 0;                 //Variable de apoyo para parar la lectura del texto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_nota);
        getSupportActionBar().setTitle("Editor de Notas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        componentAgendaily = new ComponentAgendaily(this);
        progressDialog = new ProgressDialog(AgregarNota.this);

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        textViewId = (TextView) findViewById(R.id.textViewId);
        textViewEncode = (TextView) findViewById(R.id.textViewEncode);
        textViewUserId = (TextView) findViewById(R.id.textViewUserId);
        imageViewAttached = (ImageView) findViewById(R.id.imageView);

        catchNote();


    }

    /*
     *Captura una nota, si se ha mandado desde las notas, y se meten los valores de la nota
     * en la pantalla
     */
    private void catchNote() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            //Se obtiene el la nota del objeto Bundle y se ñadden los valores a la interfaz
            Note note = (Note) bundle.getSerializable("note");
            Note notes = componentAgendaily.readNote(note.getNoteId());
            editTextTitle.setText(note.getTitle());
            editTextDescription.setText(note.getDescription());
            textViewId.setText(notes.getNoteId().toString());
            textViewEncode.setText(notes.getEncode().toString());
            textViewUserId.setText(notes.getUserId().getUserId().toString());

        }
    }


    /*
     *Se crean los botones del menú del ActionBar
     */
    public boolean onCreateOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.menu_edit_text_share, menu);
            return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.item_share:
                shareNote();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    /*
     *Leemos el titulo y la descripcion de la nota para mandarla como texto plano a la aplicacion
     * que elija el usuario
     */
    private void shareNote() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, editTextTitle.getText().toString()
                + "\n\n" + editTextDescription.getText().toString());
        intent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(intent, "Elige la aplicación");
        startActivity(shareIntent);
    }
    /*
     *Se crea una nota con los datos que se han metido en la interfaz, dependiendo de la variable isUpdate
     * hacemos un update de la nota o un insert
     */
    public void confirmNote(View view) {

        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog_save);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Note note = new Note(Integer.parseInt(textViewId.getText().toString()), editTextTitle.getText().toString(),
                editTextDescription.getText().toString(), Integer.parseInt(textViewEncode.getText().toString())
                , new User(Integer.parseInt(textViewUserId.getText().toString())));

        if (Notas.isUpdate) {
            componentAgendaily.updateNote(note.getNoteId(), note);
        } else {
            componentAgendaily.insertNote(note);
        }

        goMain();
    }
    /*
     *Nos lleva al MainActivity
     */
    private void goMain() {
        Intent intent = new Intent(AgregarNota.this, Notas.class);
        startActivity(intent);
        finish();
    }



}