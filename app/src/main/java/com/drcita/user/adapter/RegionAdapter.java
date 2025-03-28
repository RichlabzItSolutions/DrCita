package com.drcita.user.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.R;
import com.drcita.user.RegionActivity;
import com.drcita.user.models.region.DataItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.Viewholder> {
    private RegionActivity context;
    private List<DataItem> dataItems = new ArrayList<DataItem>();
    private String regionRG = "", selectedId;
    private int previousSelection = -1;
    boolean isEnabletype1=false;
    boolean isEnabletype2=false;

    public void setSelectionIndex(int index){
        this.previousSelection = index;
        isEnabletype2=false;
        isEnabletype1=false;


    }
    public RegionAdapter(RegionActivity regionActivity, List<DataItem> dataItems) {
        this.context = regionActivity;
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public RegionAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.region_list, parent, false);
        return new RegionAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholder holder, int position) {
        DataItem list = dataItems.get(position);
        holder.regionRB.setText(list.getName());
//        holder.regionRB.setChecked(list.isSelected());
//        holder.regionRB.setSelected(list.isSelected());

            if (list.getCurrencyType() == 1) {
                if ((isEnabletype1)) {
                    if(position==1){
                        holder.tv_currencytype1.setVisibility(View.VISIBLE);
                        holder.tv_currencytype2.setVisibility(View.GONE);
                    }
                    else {

                        holder.tv_currencytype1.setVisibility(View.GONE);
                        holder.tv_currencytype2.setVisibility(View.GONE);
                    }
                } else {
                    if (!list.isSelectedcurrency1()) {
                        isEnabletype1 = true;
                        list.setSelectedcurrency1(true);
                        holder.tv_currencytype1.setVisibility(View.VISIBLE);
                        holder.tv_currencytype2.setVisibility(View.GONE);
                    }

                    }

            } else {
                if ((isEnabletype2)){
                    holder.tv_currencytype1.setVisibility(View.GONE);
                    holder.tv_currencytype2.setVisibility(View.GONE);
                } else {
                    isEnabletype2 = true;
                    list.setSelectedcurrency2(true);
                    holder.tv_currencytype2.setVisibility(View.VISIBLE);
                    holder.tv_currencytype1.setVisibility(View.GONE);

                }
            }


        if (!list.isSelected()) {
            holder.regionRG.clearCheck();
        } else {
            holder.regionRB.setChecked(list.isSelected());
            holder.regionRB.setSelected(list.isSelected());
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
        TextView tv_currencytype1,tv_currencytype2;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            regionRB = itemView.findViewById(R.id.regionRB);
            regionRG = itemView.findViewById(R.id.regionRG);
            tv_currencytype1=itemView.findViewById(R.id.tv_currencytype1);
            tv_currencytype2=itemView.findViewById(R.id.currencytype2);
            regionRB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int adapterPosition = getAdapterPosition();
                    if (previousSelection != -1 && previousSelection != adapterPosition) {
                        DataItem dataItem1 = dataItems.get(previousSelection);
                        dataItem1.setSelected(false);
                        isEnabletype2=false;
                        dataItems.get(adapterPosition).setSelectedcurrency2(false);
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