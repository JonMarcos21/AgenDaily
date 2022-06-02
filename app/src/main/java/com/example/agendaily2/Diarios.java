package com.example.agendaily2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.agendaily2.activitysnotas.AgregarNota;
import com.example.agendaily2.activitysnotas.Notas;
import com.example.agendaily2.adapters.DiarioListAdapter;
import com.example.agendaily2.adapters.NotesListAdapter;
import com.example.agendaily2.componentBD.ComponentAgendaily;
import com.example.agendaily2.hash.Sha;
import com.example.agendaily2.pojos.Diario;
import com.example.agendaily2.pojos.Note;
import com.example.agendaily2.pojos.User;

import java.util.ArrayList;

public class Diarios extends AppCompatActivity {

    private ListView listViewDiario;


    private ComponentAgendaily componentAgendaily;          //Objeto que nos permite realizar las operaciones con la BDD
    private ArrayList<Diario> listDiarios;              //ArrayList que contendrá todas las notas de la BDD


    private final String SHA = "SHA-1";             //Constante que guarda el tipo de hash
    public static boolean isPermission;             //Variable que controla los permisos
    public static boolean isUpdate;                 //Variable que controla si hacemos un update o insert en el EditTextActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diario);



        isUpdate = false;

        componentAgendaily = new ComponentAgendaily(this);
        listViewDiario = (ListView) findViewById(R.id.listViewDiario);

        fillListView();

        listViewDiario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Diario diario = (Diario) listViewDiario.getItemAtPosition(i);
                showAlertDialog(diario);
            }
        });
    }


    /*
     *Se crea el boton  en el ActionBar
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notas, menu);
        return true;
    }


    /*
     *Comprobamos si han seleccionado el boton de Ajuste y llamamos a SettingsActivity
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nvolver:
                Intent intentvolver = new Intent(Diarios.this, Menus.class);
                startActivity(intentvolver);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    /*
     *Según el atributo Encode de Note mostramos el la ventana con las opciones de la nota o pedimos la contraseña
     */
    private void showAlertDialog(final Diario diario) {
        switch (diario.getEncode()) {
            case 0:
                CharSequence[] options = {"Ver o Modificar", "Ocultar contenido", "Eliminar"};
                defaultAlertDialog(diario, options);
                break;
            case 1:
                passwordAlertDialog(diario);
                break;
        }
    }
    /*
     *Ventana de dialogo con las opciones de las notas
     */
    private void defaultAlertDialog(final Diario diario, final CharSequence[] options) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Seleccione una opción");
        alertDialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (options[i].equals("Ver o Modificar")) {
                    isUpdate = true;
                    User user = componentAgendaily.readUser(diario.getUserId().getUserId());
                    diario.setUserId(user);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("diario", diario);
                    Intent intent = new Intent(Diarios.this, AgregarDiario.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (options[i].equals("Ocultar contenido")) {
                    Diario diarioUpdate = componentAgendaily.readDiario(diario.getDiarioId());
                    diarioUpdate.setEncode(1);
                    if (componentAgendaily.updateDiario(diario.getDiarioId(), diarioUpdate) != 0) {
                        fillListView();

                        Toast.makeText(Diarios.this, "Contenido Oculto", Toast.LENGTH_SHORT).show();
                    }
                } else if (options[i].equals("Mostrar contenido")) {
                    Diario diarioUpdate = componentAgendaily.readDiario(diario.getDiarioId());
                    diarioUpdate.setEncode(0);
                    if (componentAgendaily.updateDiario(diario.getDiarioId(), diarioUpdate) != 0) {
                        fillListView();
                    }
                } else if (options[i].equals("Eliminar")) {
                    if (componentAgendaily.deleteDiario(diario.getDiarioId()) != 0) {
                        fillListView();
                        Toast.makeText(Diarios.this, "Nota Eliminada", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        alertDialogBuilder.show();
    }

    /*
     *Ventana de dialogo que pide la contraseña
     */
    private void passwordAlertDialog(final Diario diario) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Diarios.this);
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_password, null);
        alertDialog.setView(customLayout);

        alertDialog.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editTextPassword = customLayout.findViewById(R.id.editTextPassword);
                        User user = componentAgendaily.readUser(diario.getUserId().getUserId());
                        if (user.getPassword().equals(Sha.stringToHash(editTextPassword.getText().toString(), SHA))) {
                            CharSequence[] options = {"Ver o Modificar", "Mostrar contenido", "Eliminar"};
                            defaultAlertDialog(diario, options);
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
        listDiarios = componentAgendaily.readDiarios();
        if (listDiarios != null) {
            DiarioListAdapter diarioListAdapter = new DiarioListAdapter(this,
                    R.layout.listview_item_diario, listDiarios);
            listViewDiario.setAdapter(diarioListAdapter);
        } else {
            listViewDiario.setAdapter(null);
        }
    }
    /*
     *Añadimos las notas que contenga el ArrayList al listViewNotes
     */
    private void fillListView(ArrayList<Diario> diarios) {
        DiarioListAdapter diarioListAdapter = new DiarioListAdapter(this,
                R.layout.listview_item_diario, diarios);
        listViewDiario.setAdapter(diarioListAdapter);
    }

    /*
     *Llamamos a EditTextActivity
     */
    public void addDiario(View view) {
        Intent intent = new Intent(Diarios.this, AgregarDiario.class);
        startActivity(intent);
    }
}