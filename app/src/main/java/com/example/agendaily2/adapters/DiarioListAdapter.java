package com.example.agendaily2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.agendaily2.R;
import com.example.agendaily2.pojos.Diario;
import com.example.agendaily2.pojos.Note;

import java.util.List;

public class DiarioListAdapter extends ArrayAdapter<Diario> {
    private Context context;

    /**
     * Método constructor de la clase.
     */
    public DiarioListAdapter(@NonNull Context context, int resource, @NonNull List<Diario> objects) {
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
        Diario diario = (Diario) getItem(i);
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.listview_item_diario, null);
        }

        //Creamos los objetos de la interfaz
        TextView textViewIdDiario = (TextView) v.findViewById(R.id.textViewIdDiario);
        TextView textViewUserIdDiario = (TextView) v.findViewById(R.id.textViewUserIdDiario);
        TextView textViewEncodeDiario = (TextView) v.findViewById(R.id.textViewEncodeDiario);
        TextView textViewFechaDiario = (TextView) v.findViewById(R.id.textViewFechaDiario);
        TextView textViewDescriptionDiario = (TextView) v.findViewById(R.id.textViewDescriptionDiario);

        //Dependiendo del valor de encode mostramos la descripcion de la nota
        switch (diario.getEncode()) {
            case 0:
                textViewIdDiario.setText(diario.getDiarioId().toString());
                textViewUserIdDiario.setText(diario.getUserId().getUserId().toString());
                textViewEncodeDiario.setText(diario.getEncode().toString());
                textViewFechaDiario.setText(diario.getFecha());
                textViewDescriptionDiario.setText(diario.getDescription());
                break;

            case 1:
                textViewIdDiario.setText(diario.getDiarioId().toString());
                textViewUserIdDiario.setText(diario.getUserId().getUserId().toString());
                textViewEncodeDiario.setText(diario.getEncode().toString());
                textViewFechaDiario.setText(diario.getFecha());
                break;
        }
        return v;
    }
}