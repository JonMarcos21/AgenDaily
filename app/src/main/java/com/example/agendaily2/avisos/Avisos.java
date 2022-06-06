package com.example.agendaily2.avisos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.agendaily2.activitys.Menus;
import com.example.agendaily2.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;



import java.util.Calendar;

public class Avisos extends AppCompatActivity {

    private TextView notificationsTime;
    private int alarmID = 1;
    private SharedPreferences settings;

    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avisos);
        getSupportActionBar().setTitle("Avisos");

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


        notificationsTime = (TextView) findViewById(R.id.notifications_time);

        //creamos un on click para el boton de cambiar la hora
        findViewById(R.id.change_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            // hcreamos un objeto de tipo calendar y creamos las variable hora y minuto
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Avisos.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    //Creamos una variable time picker y dos ints para almacenar los valores seleccionados
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String finalHour, finalMinute;

                        finalHour = "" + selectedHour;
                        finalMinute = "" + selectedMinute;
                        if (selectedHour < 10) finalHour = "0" + selectedHour;
                        if (selectedMinute < 10) finalMinute = "0" + selectedMinute;
                        notificationsTime.setText(finalHour + ":" + finalMinute);

                        Calendar today = Calendar.getInstance();

                        today.set(Calendar.HOUR_OF_DAY, selectedHour);
                        today.set(Calendar.MINUTE, selectedMinute);
                        today.set(Calendar.SECOND, 0);

                        Toast.makeText(Avisos.this, getString(R.string.changed_to, finalHour + ":" + finalMinute), Toast.LENGTH_LONG).show();
                        setAlarm(alarmID,today.getTimeInMillis(),Avisos.this);

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle(getString(R.string.select_time));
                mTimePicker.show();

            }
        });
    }

    /*
    *Se crea el menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ayuda, menu);
        return true;
    }


    /*
     *Creamos los items del menu
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.volver:
                Intent intentvolver = new Intent(Avisos.this, Menus.class);
                startActivity(intentvolver);
                break;

            case R.id.infoboton:
                //se prepara la alerta creando nueva instancia
                AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
                //seleccionamos la cadena a mostrar
                alertbox.setMessage(getString(R.string.menu_ayudarecordatorio));
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




        //Le pasamos un identificador para la alarma y el contexto para que pueda llamar a la clase alarm manager
        private static void setAlarm(int i ,long timestamp,Context ctx){
            AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
            Intent alarmIntent = new Intent(ctx, AlarmReceiver.class);
            PendingIntent pendingIntent;
            pendingIntent = PendingIntent.getBroadcast(ctx,i,alarmIntent,PendingIntent.FLAG_ONE_SHOT);
            alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())) );
            alarmManager.set(AlarmManager.RTC_WAKEUP,timestamp,pendingIntent);

        }


//Intent para guardar la notificación y volver a los menus
    public void save(View view){

        Intent save = new Intent(this, Menus.class);
        startActivity(save);
        finish();

    }
}


