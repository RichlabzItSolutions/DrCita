package com.drcita.user.adapter;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.R;
import com.drcita.user.RegionActivity;
import com.drcita.user.common.Constants;
import com.drcita.user.models.region.DataItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RegionChildAdapter extends RecyclerView.Adapter<RegionChildAdapter.Viewholder> {
    private RegionActivity context;
    private List<DataItem> dataItems = new ArrayList<DataItem>();
    private String regionRG = "", selectedId;
    private int previousSelection = -1;

    public void setSelectionIndex(int index){
        this.previousSelection = index;
    }
    public RegionChildAdapter(RegionActivity regionActivity, List<DataItem> dataItems, int index) {
        this.context = regionActivity;
        this.dataItems = dataItems;
        this.previousSelection=index;
    }

    @NonNull
    @Override
    public RegionChildAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.region_childlist, parent, false);
        return new RegionChildAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholder holder, int position) {
        DataItem list = dataItems.get(position);
        holder.regionRB.setText(list.getName());
//        holder.regionRB.setChecked(list.isSelected());
//        holder.regionRB.setSelected(list.isSelected());
        if (!list.isSelected()) {
            holder.regionRG.clearCheck();
        } else {
            holder.regionRB.setChecked(list.isSelected());
            holder.regionRB.setSelected(list.isSelected());
            SharedPreferences preferences =context.getSharedPreferences(Constants.PREFERENCE_NAME, 0);
            preferences.edit().putString(Constants.Currancy_Type, String.valueOf(list.getCurrencyType())).apply();
        }
        if (list.isSelected()) {
            Log.d("TAG", "onBindViewHolder: " + dataItems.toString());
        }
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        RadioButton regionRB;
        RadioGroup regionRG;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            regionRB = itemView.findViewById(R.id.regionRB);
            regionRG = itemView.findViewById(R.id.regionRG);
            regionRB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int adapterPosition = getAdapterPosition();
                    if (previousSelection != -1 && previousSelection != adapterPosition) {
                        DataItem dataItem1 = dataItems.get(previousSelection);
                        dataItem1.setSelected(false);
                        dataItems.set(previousSelection, dataItem1);
                        notifyItemChanged(previousSelection);
                    }
                    DataItem dataItem = dataItems.get(adapterPosition);
                    dataItem.setSelected(true);
                    dataItems.set(adapterPosition, dataItem);
                    notifyItemChanged(adapterPosition);
                    previousSelection = adapterPosition;
                }
            });
        }
    }
}