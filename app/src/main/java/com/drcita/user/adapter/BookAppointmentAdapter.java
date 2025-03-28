package com.drcita.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.BookanAppotimentActivity;
import com.drcita.user.R;
import com.drcita.user.models.doctorslots.DataItem;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class BookAppointmentAdapter extends RecyclerView.Adapter<BookAppointmentAdapter.Viewholder> {
    private BookanAppotimentActivity context;
    private List<DataItem> responses = new ArrayList<DataItem>();


    public BookAppointmentAdapter(BookanAppotimentActivity bookanAppotimentActivity, List<DataItem> dataItems) {
        this.context = bookanAppotimentActivity;
        this.responses = dataItems;
    }

    @NonNull
    @Override
    public BookAppointmentAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slots_list, parent, false);
        return new BookAppointmentAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAppointmentAdapter.Viewholder holder, int position) {
        com.drcita.user.models.doctorslots.DataItem list = responses.get(position);
        Chip chip = new Chip(holder.timeslots.getContext());
        chip.setText(list.getFromTime()+"-"+list.getToTime());
        holder.timeslots.addView(chip);
    }
    @Override
    public int getItemCount() {
        return responses.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        ChipGroup timeslots;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            timeslots = itemView.findViewById(R.id.timeslot);

        }
    }
}

