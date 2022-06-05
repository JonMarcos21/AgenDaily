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

    private final ArrayList<String> daysOffMonth;
    private final OnItemListener onItemListener ;

    public CalendarAdapter(ArrayList<String> daysOffMonth, OnItemListener onItemListener) {
        this.daysOffMonth = daysOffMonth;

        this.onItemListener = onItemListener;
    }


    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell,parent,false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {

        holder.dayOfMonth.setText(daysOffMonth.get(position));
    }

    @Override
    public int getItemCount() {
        return daysOffMonth.size();
    }
    public interface  OnItemListener
    {
        void onItemClick(int position, String dayText);
    }
}
