package com.example.agendaily2.activitysnotas;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.agendaily2.activitys.Menus;
import com.example.agendaily2.R;
import com.example.agendaily2.adapters.NotesListAdapter;
import com.example.agendaily2.componentBD.ComponentAgendaily;
import com.example.agendaily2.hash.Sha;
import com.example.agendaily2.pojos.Note;
import com.example.agendaily2.pojos.User;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class Notas extends AppCompatActivity {
    //Objetos de la interfaz
    private ListView listViewNotes;

    //Objeto para agregar anuncios
    private AdView mAdView;

    private ComponentAgendaily componentAgendaily;          //Objeto que nos permite realizar las operaciones con la BDD
    private ArrayList<Note> listNotes;              //ArrayList que contendrá todas las notas de la BDD


    private final String SHA = "SHA-1";             //Constante que guarda el tipo de hash
    public static boolean isPermission;             //Variable que controla los permisos
    public static boolean isUpdate;                 //Variable que controla si hacemos un update o insert en el AgregarNota


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        //Implementación de anuncios AdMob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        //Agregamos anuncios a la página

        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        isUpdate = false;

        //Llamamos a la base de datos y al item list view que sera donde se guarden los datos
        componentAgendaily = new ComponentAgendaily(this);
        listViewNotes = (ListView) findViewById(R.id.listViewNotes);

        fillListView();

        //Cuando se selecciona un item del ListView mostramos una ventana de dialogo
        listViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Note note = (Note) listViewNotes.getItemAtPosition(i);
                showAlertDialog(note);
            }
        });
        if (validatePermissions()) {
            isPermission = true;
        } else {
            isPermission = false;
        }


    }

    /**
     * Pedimos los permisos al usuario para acceder a la galeria del teléfono
     */
    private boolean validatePermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return true;
        if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
            loadRecommendationDialog();
        } else {
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 100);
        }
        return false;
    }

    /**
     * Comprobamos si se han concedido los permisos
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isPermission = true;
            } else {
                isPermission = false;
            }
        }
    }

    /**
     * Mostramos una ventana de advertencia en caso de que no se hayan concedido los permisos
     */
    private void loadRecommendationDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Permisos Desactivados");
        alertDialogBuilder.setMessage("Debe aceptar los permisos para el correcto funcionamiento" +
                " de la aplicación");
        alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 100);
            }
        });
        alertDialogBuilder.show();
    }


    /*
     *Se crea el menu  en el ActionBar
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ayuda, menu);
        return true;
    }


    /*
     *Añadimos los items del menu
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //item para vover al menu principal
            case R.id.volver:
                Intent intentvolver = new Intent(Notas.this, Menus.class);
                startActivity(intentvolver);
                break;

            //Item para monstar informacion
            case R.id.infoboton:
                //se prepara la alerta creando nueva instancia
                AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
                //seleccionamos la cadena a mostrar
                alertbox.setMessage(getString(R.string.menu_notas));
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
     *Según el atributo Encode de Note mostramos el la ventana con las opciones de la nota o pedimos la contraseña
     */
    private void showAlertDialog(final Note note) {
        switch (note.getEncode()) {
            case 0:
                CharSequence[] options = {"Ver o Modificar", "Ocultar contenido", "Eliminar"};
                defaultAlertDialog(note, options);
                break;
            case 1:
                passwordAlertDialog(note);
                break;
        }
    }

    /*
     *Ventana de dialogo con las opciones de las notas
     */
    private void defaultAlertDialog(final Note note, final CharSequence[] options) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Seleccione una opción");
        alertDialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            //Compramos cual opción selecciona el usuario y asignamos diferentes funciones para cada if
            public void onClick(DialogInterface dialogInterface, int i) {
                if (options[i].equals("Ver o Modificar")) {
                    isUpdate = true;
                    User user = componentAgendaily.readUser(note.getUserId().getUserId());
                    note.setUserId(user);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("note", note);
                    Intent intent = new Intent(Notas.this, AgregarNota.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    //ocultamos el contenido de la nota
                } else if (options[i].equals("Ocultar contenido")) {
                    Note noteUpdate = componentAgendaily.readNote(note.getNoteId());
                    noteUpdate.setEncode(1);
                    if (componentAgendaily.updateNote(note.getNoteId(), noteUpdate) != 0) {
                        fillListView();

                        Toast.makeText(Notas.this, "Contenido Oculto", Toast.LENGTH_SHORT).show();
                    }
                } else if (options[i].equals("Mostrar contenido")) {
                    Note noteUpdate = componentAgendaily.readNote(note.getNoteId());
                    noteUpdate.setEncode(0);
                    if (componentAgendaily.updateNote(note.getNoteId(), noteUpdate) != 0) {
                        fillListView();
                    }
                } else if (options[i].equals("Eliminar")) {
                    if (componentAgendaily.deleteNote(note.getNoteId()) != 0) {
                        fillListView();
                        Toast.makeText(Notas.this, "Nota Eliminada", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //Monstamos el alert builder
        alertDialogBuilder.show();
    }

    /*
     *Ventana de dialogo que pide la contraseña
     */
    private void passwordAlertDialog(final Note note) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Notas.this);
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_password, null);
        alertDialog.setView(customLayout);

        alertDialog.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editTextPassword = customLayout.findViewById(R.id.editTextPassword);
                        User user = componentAgendaily.readUser(note.getUserId().getUserId());
                        if (user.getPassword().equals(Sha.stringToHash(editTextPassword.getText().toString(), SHA))) {
                            CharSequence[] options = {"Ver o Modificar", "Mostrar contenido", "Eliminar"};
                            defaultAlertDialog(note, options);
                        }

                    }
                });

        alertDialog.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }
    /*
     *Consultamos todas las notas de la BDD y las añadimos al listViewNotes
     */
    private void fillListView() {
        listNotes = componentAgendaily.readNotes();
        if (listNotes != null) {
            NotesListAdapter notesListAdapter = new NotesListAdapter(this,
                    R.layout.listview_item, listNotes);
            listViewNotes.setAdapter(notesListAdapter);
        } else {
            listViewNotes.setAdapter(null);
        }
    }
    /*
     *Añadimos las notas que contenga el ArrayList al listViewNotes
     */
    private void fillListView(ArrayList<Note> notes) {
        NotesListAdapter notesListAdapter = new NotesListAdapter(this,
                R.layout.listview_item, notes);
        listViewNotes.setAdapter(notesListAdapter);
    }

    /*
     *Llamamos a AgregarNota
     */
    public void addNote(View view) {
        Intent intent = new Intent(Notas.this, AgregarNota.class);
        startActivity(intent);
    }

}