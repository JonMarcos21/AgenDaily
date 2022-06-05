package com.example.agendaily2.diario;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.agendaily2.activitys.Menus;
import com.example.agendaily2.R;
import com.example.agendaily2.adapters.DiarioListAdapter;

import com.example.agendaily2.componentBD.ComponentAgendaily;
import com.example.agendaily2.hash.Sha;
import com.example.agendaily2.pojos.Diario;

import com.example.agendaily2.pojos.User;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Iterator;

public class Diarios extends AppCompatActivity {

    private ListView listViewDiario;
    private EditText editTextSearch;

    private ComponentAgendaily componentAgendaily;          //Objeto que nos permite realizar las operaciones con la BDD
    private ArrayList<Diario> listDiarios;              //ArrayList que contendrá todas las notas de la BDD

    private AdView mAdView;


    private final String SHA = "SHA-1";             //Constante que guarda el tipo de hash
    public static boolean isPermission;             //Variable que controla los permisos
    public static boolean isUpdate;                 //Variable que controla si hacemos un update o insert en el EditTextActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diario);


        //Implementación de anuncios AdMob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        isUpdate = false;

        componentAgendaily = new ComponentAgendaily(this);
        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        listViewDiario = (ListView) findViewById(R.id.listViewDiario);

        fillListView();
        //Indicamos que el editTextSearch este pendiente del boton ENTER del teclado del usuario
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });


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
        getMenuInflater().inflate(R.menu.menu_ayuda, menu);
        return true;
    }


    /*
     *Comprobamos si han seleccionado el boton de Ajuste y llamamos a SettingsActivity
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.volver:
                Intent intentvolver = new Intent(Diarios.this, Menus.class);
                startActivity(intentvolver);
                break;

            case R.id.infoboton:
                //se prepara la alerta creando nueva instancia
                AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
                //seleccionamos la cadena a mostrar
                alertbox.setMessage(getString(R.string.ayuda_Diario));
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
    /**
     * Buscamos una nota por su titulo a partir del String que ingrese el usuario en el editTextSearch
     */
    private void performSearch() {
        if (listDiarios != null) {

            //Clonamos el ArrayList listNotes para no tener que hacer una consulta a la base de datos
            ArrayList<Diario> diarioCopy = (ArrayList<Diario>) listDiarios.clone();
            if (editTextSearch.getText().toString().isEmpty()) {
                //Si editTextSearch está vacío y se le da al ENTER con el teclado volvemos a mostrar todas las notas
                fillListView(diarioCopy);
            } else {
                //Buscamos todas las notas que coincidan con el String de editTextSearch
                ArrayList<Diario> diarios = new ArrayList<Diario>();
                Iterator itr = diarioCopy.iterator();
                while (itr.hasNext()) {
                    Diario diario = (Diario) itr.next();
                    if (diario.getFecha().toLowerCase().contains(editTextSearch.getText().toString().toLowerCase())) {
                        diarios.add(diario);
                    }
                }
                fillListView(diarios);
            }
        }
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
                        Toast.makeText(Diarios.this, "Diario Eliminada", Toast.LENGTH_SHORT).show();
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