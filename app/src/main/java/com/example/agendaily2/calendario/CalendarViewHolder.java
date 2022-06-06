package com.example.agendaily2.calendario;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendaily2.R;
import com.example.agendaily2.adapters.CalendarAdapter;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    //creamos elementos para asiganarlos a los elementos del layout
    public final TextView dayOfMonth;
    private final CalendarAdapter.OnItemListener onItemListener;
    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener)
    {
        //decimos que el dia del mes es igual a la vista del elemento cellday lel layout calendar cell
        super(itemView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        onItemListener.onItemClick(getAdapterPosition(), (String) dayOfMonth.getText());
    }
}
