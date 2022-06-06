package com.example.agendaily2.componentBD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.agendaily2.pojos.Diario;
import com.example.agendaily2.pojos.Note;
import com.example.agendaily2.pojos.User;

import java.util.ArrayList;

public class ComponentAgendaily {
    private SQLiteDatabase agendas;
    private AgendaOpenHelper agendaOpenHelper;

    /*
     *Método constructor donde se manda a crear la BDD
     */
    public ComponentAgendaily(Context context) {
        agendaOpenHelper = new AgendaOpenHelper(context, "agenda", null, 1);
    }

    /*
     *Abrimos la conexión con la BDD
     */
    public void openForWrite() {
        agendas = agendaOpenHelper.getWritableDatabase();
    }

    /*
     *Cerramos la conexión con la BDD
     */
    public void close() {
        agendas.close();
    }

    /*
     *Insertamos un usuario en la BDD
     */
    public long insertUser(User user) {
        openForWrite();
        long registers = 0;
        ContentValues content = new ContentValues();
        content.put("EMAIL", user.getEmail());
        content.put("PASSWORD", user.getPassword());
        registers = agendas.insert("USER", null, content);
        close();
        return registers;
    }

    /*
     *Eliminamos un usuario de la BDD a partir del email
     */
    public long deleteUser(String email) {
        openForWrite();
        long registers = 0;
        registers = agendas.delete("USER", "EMAIL = '" + email + "'", null);
        close();
        return registers;
    }

    /*
     *Actualizamos un usuario de la BDD según el email
     */
    public long updateUser(String email, User user) {
        openForWrite();
        long registers = 0;
        ContentValues content = new ContentValues();
        content.put("EMAIL", user.getEmail());
        content.put("PASSWORD", user.getPassword());
        registers = agendas.update("USER", content, "EMAIL = '" + email + "'", null);
        close();
        return registers;
    }

    /*
     *Leemos un usuario de la BDD con el id
     */
    public User readUser(Integer userId) {
        openForWrite();
        Cursor cursor = agendas.rawQuery("select USER_ID, EMAIL, PASSWORD from USER where USER_ID = " + userId,
                null);
        if (cursor.getCount() == 0) {
            cursor.close();
            close();
            return null;
        }
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        }
        cursor.close();
        close();
        return user;
    }

    /*
     *Leemos todos los usuarios de la BDD
     */
    public ArrayList<User> readUsers() {
        openForWrite();
        Cursor cursor = agendas.rawQuery("select USER_ID, EMAIL, PASSWORD from USER", null);
        if (cursor.getCount() == 0) {
            cursor.close();
            close();
            return null;
        }
        ArrayList<User> users = new ArrayList<>();
        while (cursor.moveToNext()) {
            users.add(new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
        }
        cursor.close();
        close();
        return users;
    }

    /*
     *Insertamos una nota en la BDD
     */
    public long insertNote(Note note) {
        openForWrite();
        long registers = 0;
        ContentValues content = new ContentValues();
        content.put("TITLE", note.getTitle());
        content.put("DESCRIPTION", note.getDescription());
        content.put("IMAGE", note.getImage());
        content.put("ENCODE", note.getEncode());
        content.put("USER_ID", note.getUserId().getUserId());
        registers = agendas.insert("NOTE", null, content);
        close();
        return registers;
    }

    /*
     *Eliminamos un nota de la BDD por el id
     */
    public long deleteNote(Integer noteId) {
        openForWrite();
        long registers = 0;
        registers = agendas.delete("NOTE", "NOTE_ID = " + noteId, null);
        close();
        return registers;
    }

    /*
     *Actualizamos una nota de la BDD según el id
     */
    public long updateNote(Integer noteId, Note note) {
        openForWrite();
        long registers = 0;
        ContentValues content = new ContentValues();
        content.put("TITLE", note.getTitle());
        content.put("DESCRIPTION", note.getDescription());
        content.put("IMAGE", note.getImage());
        content.put("ENCODE", note.getEncode());
        content.put("USER_ID", note.getUserId().getUserId());
        registers = agendas.update("NOTE", content, "NOTE_ID = " + noteId, null);
        close();
        return registers;
    }

    /*
     *Leemos una nota de la BDD por el id
     */
    public Note readNote(Integer noteId) {
        openForWrite();
        Cursor cursor = agendas.rawQuery("select NOTE_ID, TITLE, DESCRIPTION, ENCODE, IMAGE, USER_ID" +
                " from NOTE where NOTE_ID = " + noteId, new String[]{});
        if (cursor.getCount() == 0) {
            cursor.close();
            close();
            return null;
        }
        Note note = null;
        if (cursor.moveToFirst()) {
            note = new Note(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3), cursor.getBlob(4), new User(cursor.getInt(5)));
        }
        cursor.close();
        close();
        return note;
    }

    /*
     *Leemos todas las notas de la BDD
     */
    public ArrayList<Note> readNotes() {
        openForWrite();
        Cursor cursor = agendas.rawQuery("select NOTE_ID, TITLE, DESCRIPTION, ENCODE, USER_ID from NOTE", null);
        if (cursor.getCount() == 0) {
            cursor.close();
            close();
            return null;
        }
        ArrayList<Note> listNotes = new ArrayList<>();
        while (cursor.moveToNext()) {
            listNotes.add(new Note(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3), readUser(cursor.getInt(4))));
        }
        cursor.close();
        close();
        return listNotes;
    }



    /*
     *Insertamos un diario en la BDD
     */
    public long insertDiario(Diario diario) {
        openForWrite();
        long registers = 0;
        ContentValues content = new ContentValues();
        content.put("FECHA", diario.getFecha());
        content.put("DESCRIPTION", diario.getDescription());
        content.put("ENCODE", diario.getEncode());
        content.put("USER_ID", diario.getUserId().getUserId());
        registers = agendas.insert("DIARIO", null, content);
        close();
        return registers;
    }
    /*
     *Eliminamos un diario de la BDD por el id
     */
    public long deleteDiario(Integer diarioId) {
        openForWrite();
        long registers = 0;
        registers = agendas.delete("DIARIO", "DIARIO_ID = " + diarioId, null);
        close();
        return registers;
    }

    /*
     *Actualizamos un diario de la BDD según el id
     */
    public long updateDiario(Integer diarioId, Diario diario) {
        openForWrite();
        long registers = 0;
        ContentValues content = new ContentValues();
        content.put("FECHA", diario.getFecha());
        content.put("DESCRIPTION", diario.getDescription());
        content.put("ENCODE", diario.getEncode());
        content.put("USER_ID", diario.getUserId().getUserId());
        registers = agendas.update("DIARIO", content, "DIARIO_ID = " + diarioId, null);
        close();
        return registers;
    }

    /*
     *Leemos un diario de la BDD por el id
     */
    public Diario readDiario(Integer diarioId) {
        openForWrite();
        Cursor cursor = agendas.rawQuery("select DIARIO_ID, FECHA, DESCRIPTION,ENCODE, USER_ID" +
                " from DIARIO where DIARIO_ID = " + diarioId, new String[]{});
        if (cursor.getCount() == 0) {
            cursor.close();
            close();
            return null;
        }
        Diario diario = null;
        if (cursor.moveToFirst()) {
            diario = new Diario(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3), new User(cursor.getInt(4)));
        }
        cursor.close();
        close();
        return diario;
    }

    /*
     *Leemos todas las diarios de la BDD
     */
    public ArrayList<Diario> readDiarios() {
        openForWrite();
        Cursor cursor = agendas.rawQuery("select DIARIO_ID, FECHA, DESCRIPTION,ENCODE, USER_ID from DIARIO", null);
        if (cursor.getCount() == 0) {
            cursor.close();
            close();
            return null;
        }
        ArrayList<Diario> listDiario = new ArrayList<>();
        while (cursor.moveToNext()) {
            listDiario.add(new Diario(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3),readUser(cursor.getInt(4))));
        }
        cursor.close();
        close();
        return listDiario;
    }


}

