package com.example.agendaily2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendaily2.calendario.CalendarViewHolder;
import com.example.agendaily2.R;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{

    //Cremos un array list para guardar los dias del mes
    private final ArrayList<String> daysOffMonth;
    private final OnItemListener onItemListener ;

    //creamos un constructor de los elementos ceados
    public CalendarAdapter(ArrayList<String> daysOffMonth, OnItemListener onItemListener) {
        this.daysOffMonth = daysOffMonth;

        this.onItemListener = onItemListener;
    }


    @NonNull

    @Override
    //Creamos un soporte para la vista
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //declaramos una vista para pasarle al valor inflater con el layout de calendar cell
        View view = inflater.inflate(R.layout.calendar_cell,parent,false);
        //vemos los parametros de diseño  y obtenemos el layout para convertir los parametros en un int
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        //lo multiplicamos por 0,166 para obtener la altura total  y despues lo asignamos para cada celda para que sean del mismo tamaño
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {

        holder.dayOfMonth.setText(daysOffMonth.get(position));
    }

    @Override
    //creamos una variable para cuando clikemos sepa la posicion y el dia elegido
    public int getItemCount() {
        return daysOffMonth.size();
    }
    public interface  OnItemListener
    {
        void onItemClick(int position, String dayText);
    }
}
