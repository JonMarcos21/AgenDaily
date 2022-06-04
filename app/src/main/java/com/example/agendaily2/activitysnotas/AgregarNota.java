package com.example.agendaily2.activitysnotas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agendaily2.R;
import com.example.agendaily2.componentBD.ComponentAgendaily;
import com.example.agendaily2.pojos.Note;
import com.example.agendaily2.pojos.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

public class AgregarNota extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription;
    private TextView textViewId, textViewEncode, textViewUserId;
    private ImageView imageViewAttachedCamara, imageViewDialog;


    private Dialog dialogShowImage;                 //Objeto que nos muestra un dialogo con la imagen adjuntada
    private ComponentAgendaily componentAgendaily;          //Objeto que nos permite realizar las operaciones con la BDD
    private ProgressDialog progressDialog;          //Objeto que nos muestra la ventana de carga

  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_nota);
        getSupportActionBar().setTitle("Editor de Notas");



        init();

        catchNote();



        //Espera que presionen la imagen para lanzar el método showImage()
        imageViewAttachedCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImage();
            }
        });
    }

    /*
     *Se crea una ventana diálogo y  añade la imagen que se ha pinchado
     */
    private void showImage() {
        dialogShowImage = new Dialog(AgregarNota.this);
        dialogShowImage.setContentView(R.layout.dialog_show_image);
        imageViewDialog = dialogShowImage.findViewById(R.id.imageViewDialog);
        imageViewDialog.setImageBitmap(((BitmapDrawable) imageViewAttachedCamara.getDrawable()).getBitmap());
        dialogShowImage.show();
    }



    /*
     *Captura una nota, si se ha mandado desde el MainActivity, y se meten los valores de la nota
     * en la pantalla
     */
    private void catchNote() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            //Se obtiene el la nota del objeto Bundle y se ñadden los valores a la interfaz
            Note note = (Note) bundle.getSerializable("note");
            Note noteImage = componentAgendaily.readNote(note.getNoteId());
            editTextTitle.setText(note.getTitle());
            editTextDescription.setText(note.getDescription());
            textViewId.setText(noteImage.getNoteId().toString());
            textViewEncode.setText(noteImage.getEncode().toString());
            textViewUserId.setText(noteImage.getUserId().getUserId().toString());

            //Se comprueba que la nota tiene una imagen
            if (noteImage.getImage() != null) {
                if (!Arrays.equals(noteImage.getImage(), imageViewToByte())) {
                    //Se convierte el byte[] a Bitmap y se añade a la imagen del Activity
                    Bitmap bitmap = BitmapFactory.decodeByteArray(noteImage.getImage(),
                            0, noteImage.getImage().length);
                    imageViewAttachedCamara.setImageBitmap(bitmap);
                }
            }
        }
    }

    /*
     *Se inicializan todos los obejetos de la interfaz, el objeto componentNotes y el progressDialog
     */
    private void init() {
        componentAgendaily = new ComponentAgendaily(this);
        progressDialog = new ProgressDialog(AgregarNota.this);

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        textViewId = (TextView) findViewById(R.id.textViewId);
        textViewEncode = (TextView) findViewById(R.id.textViewEncode);
        textViewUserId = (TextView) findViewById(R.id.textViewUserId);
        imageViewAttachedCamara = (ImageView) findViewById(R.id.imageViewCamaraNota);
    }


    /*
     *Se crean los botones del menú del ActionBar
     */
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_agregarnota, menu);
        return true;

    }

    /*
     *Comprobamos cual de los botones del ActionBar ha sido selecionado y la lanzamos la funcion correspondiente
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.item_clip:
                openGalery();
                break;

            case R.id.item_share:
                shareNote();
                break;
            case R.id.InfoAgregarNOtas:
                //se prepara la alerta creando nueva instancia
                AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
                //seleccionamos la cadena a mostrar
                alertbox.setMessage(getString(R.string.menu_AgregarNota));
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

    /*
     *Abrimos la galería del dispositivo
     */
    private void openGalery() {
        if (Notas.isPermission) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/");
            startActivityForResult(intent.createChooser(intent, "Selecione la Aplicación"), 10);
        } else {

            Toast.makeText(this, "La aplicación no tiene permisos " +
                    "para abrir la galería", Toast.LENGTH_SHORT).show();
        }
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
     *Capturamos la imagen que elieg el usaurio y la mostramos en el ImageView de la pantalla
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri path = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(path);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageViewAttachedCamara.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
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
                editTextDescription.getText().toString(), Integer.parseInt(textViewEncode.getText().toString()),
                imageViewToByte(), new User(Integer.parseInt(textViewUserId.getText().toString())));

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

    /*
     *Convertimos un ImageView en un byte[]
     */
    private byte[] imageViewToByte() {
        Bitmap bitmap = ((BitmapDrawable) imageViewAttachedCamara.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream(20480);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }


}