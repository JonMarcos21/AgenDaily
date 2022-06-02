package com.example.agendaily2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.example.agendaily2.R;
import com.example.agendaily2.pojos.Note;

import java.util.List;

public class NotesListAdapter extends ArrayAdapter<Note> {
    private Context context;

    /**
     * Método constructor de la clase.
     */
    public NotesListAdapter(@NonNull Context context, int resource, @NonNull List<Note> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    /**
     * Método en el que se obtiene un Objeto de la clase Note, se leen sus atributos y se añaden a la
     * vista del ListView
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        Note note = (Note) getItem(i);
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.listview_item, null);
        }

        //Creamos los objetos de la interfaz
        TextView textViewId = (TextView) v.findViewById(R.id.textViewId);
        TextView textViewUserId = (TextView) v.findViewById(R.id.textViewUserId);
        TextView textViewEncode = (TextView) v.findViewById(R.id.textViewEncode);
        TextView textViewTitle = (TextView) v.findViewById(R.id.textViewTitle);
        TextView textViewDescription = (TextView) v.findViewById(R.id.textViewDescription);

        //Dependiendo del valor de encode mostramos la descripcion de la nota
        switch (note.getEncode()) {
            case 0:
                textViewId.setText(note.getNoteId().toString());
                textViewUserId.setText(note.getUserId().getUserId().toString());
                textViewEncode.setText(note.getEncode().toString());
                textViewTitle.setText(note.getTitle());
                textViewDescription.setText(note.getDescription());
                break;

            case 1:
                textViewId.setText(note.getNoteId().toString());
                textViewUserId.setText(note.getUserId().getUserId().toString());
                textViewEncode.setText(note.getEncode().toString());
                textViewTitle.setText(note.getTitle());
                break;
        }
        return v;
    }
}