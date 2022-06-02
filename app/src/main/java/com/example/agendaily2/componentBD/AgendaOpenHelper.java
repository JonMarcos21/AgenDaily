package com.example.agendaily2.componentBD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AgendaOpenHelper extends SQLiteOpenHelper {
    /*
     *Método constructor de la clase
     */
    public AgendaOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /*
     *Se crean las tablas de la BDD
     */
    @Override
    public void onCreate(SQLiteDatabase agenda) {

        agenda.execSQL("create table USER(USER_ID Integer primary key autoincrement, EMAIL text not null UNIQUE," +
                " PASSWORD text not null)");

        agenda.execSQL("create table NOTE(NOTE_ID Integer primary key autoincrement, TITLE text, DESCRIPTION text," +
                " ENCODE Integer DEFAULT 0 , USER_ID Integer, FOREIGN KEY(USER_ID) REFERENCES USER(USER_ID))");
        agenda.execSQL("create table DIARIO(DIARIO_ID Integer primary key autoincrement, FECHA text, DESCRIPTION text," +
                "  USER_ID Integer, FOREIGN KEY(USER_ID) REFERENCES USER(USER_ID))");
        agenda.execSQL("create table RECORDATORIO(RECORDATORIO_ID Integer primary key autoincrement, TITLE text, DESCRIPTION text," +
                " HORA Integer default 0, USER_ID Integer, FOREIGN KEY(USER_ID) REFERENCES USER(USER_ID))");

    }

    /*
     *En caso de que existan las tablas, se borrán y se crean de nuevo
     */
    @Override
    public void onUpgrade(SQLiteDatabase agenda, int i, int i1) {

        agenda.execSQL("drop table USER");
        agenda.execSQL("drop table NOTE");
        agenda.execSQL("drop table DIARIO");
        agenda.execSQL("drop table RECORDATORIO");

        onCreate(agenda);
    }
}

