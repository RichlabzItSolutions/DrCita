package com.drcita.user.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.DoctorsListActivity;
import com.drcita.user.HospitalReviewActivity;
import com.drcita.user.HospitalsListActivity;
import com.drcita.user.R;
import com.drcita.user.WriteReview;
import com.drcita.user.common.Constants;
import com.drcita.user.models.hospitals.DataItem;
import com.drcita.user.models.newProviderlist.NewProviderList;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class HospitalsAdapter extends RecyclerView.Adapter<HospitalsAdapter.Viewholder> implements Filterable {
    private HospitalsListActivity context;
    private List<NewProviderList> dataItems = new ArrayList<NewProviderList>();

    private List<NewProviderList> mFilteredList = new ArrayList<NewProviderList>();
    private boolean isfromdental;
    private int providerId;
    private String hospitalName,rating,time,location,ratingcount,ratingStar,free;


    public HospitalsAdapter(HospitalsListActivity hospitalsListActivity, List<NewProviderList> hospitalsResponses, boolean isfromdental) {
        this.context = hospitalsListActivity;
        this.dataItems = hospitalsResponses;
        this.mFilteredList=hospitalsResponses;
        this.isfromdental = isfromdental;
    }

    @NonNull
    @Override
    public HospitalsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospitals_list, parent, false);
        return new HospitalsAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalsAdapter.Viewholder holder, int position) {
        NewProviderList list = mFilteredList.get(position);
        providerId = list.getProviderId();
        hospitalName = list.getHospitalName();
//        rating = ""+list.ge();
//        time = ""+list.getOpeningHours();
//        location = ""+list.getRegion();
//        ratingcount =""+list.getRating();
//        free = ""+list.getFree();
        holder.hospitalname.setText(list.getHospitalName());
//        holder.ratingTV.setText(""+list.getRating());
//        holder.time.setText(list.getOpeningHours());
//        holder.location.setText(""+list.getRegion());
//        holder.ratingcount.setText(""+list.getRatedCount());

        if (free.equals("Yes")){
            holder.free.setText(R.string.free);
            holder.free.setVisibility(View.VISIBLE);

        }else if(free.equals("No")){
            holder.free.setVisibility(View.GONE);

        }

        if (!list.getPicture().isEmpty()) {
            Picasso.with(context).load(list.getPicture())
                    .into(holder.hospitallogo);
        }
    }
    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = dataItems;
                } else {
                    ArrayList<NewProviderList> filteredList = new ArrayList<>();
                    for (NewProviderList receipt : dataItems) {

                        if (receipt.getHospitalName().toLowerCase().startsWith(charString.toLowerCase()))
                        {
                            filteredList.add(receipt);
                        } else {
                            filteredList.remove(receipt);

                        }
                    }

                    mFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                notifyDataSetChanged();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<NewProviderList>) filterResults.values;
                notifyDataSetChanged();
            }

        };
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView hospitallogo;
        ImageView rating;
        LinearLayout ratingedit;
        TextView hospitalname,ratingTV,ratingcount,time,location,free;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            rating = itemView.findViewById(R.id.rating);
            hospitalname = itemView.findViewById(R.id.hospitalname);
            ratingTV = itemView.findViewById(R.id.ratingTV);
            ratingcount = itemView.findViewById(R.id.ratingcount);
            time = itemView.findViewById(R.id.time);
            location = itemView.findViewById(R.id.location);
            ratingedit = itemView.findViewById(R.id.ratingedit);
            hospitallogo = itemView.findViewById(R.id.hospitallogo);
            free = itemView.findViewById(R.id.free);

            ratingedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, WriteReview.class);
                    intent.putExtra("providerId",providerId);
                    context.startActivity(intent);
                }
            });
            ratingcount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NewProviderList dataItem = mFilteredList.get(getAdapterPosition());
                    Intent intent = new Intent(context, HospitalReviewActivity.class);
                    intent.putExtra("dataItem", Parcels.wrap(dataItem));
                    context.startActivity(intent);
                }
            } );
            hospitallogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NewProviderList dataItem = mFilteredList.get(getAdapterPosition());
                    Intent intent = new Intent(context, DoctorsListActivity.class);
                    intent.putExtra("dataItem", Parcels.wrap(dataItem));
                    intent.putExtra(Constants.isfromdental,isfromdental);
                    context.startActivity(intent);
                }
            });
            hospitalname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NewProviderList dataItem = mFilteredList.get(getAdapterPosition());
                    Intent intent = new Intent(context, DoctorsListActivity.class);
                    intent.putExtra("dataItem", Parcels.wrap(dataItem));
                    intent.putExtra(Constants.isfromdental,isfromdental);
                    context.startActivity(intent);
                }
            });

        }
    }
}
