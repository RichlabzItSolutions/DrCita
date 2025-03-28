package com.drcita.user.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.NotificationViewImage;
import com.drcita.user.ViewMedicalRecordsActivity;
import com.drcita.user.databinding.PhotosListBinding;
import com.drcita.user.models.viewmedicalrecords.PhotosItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
    private ViewMedicalRecordsActivity viewMedicalsRecordsAdapter;
    private List<PhotosItem> photosItems = new ArrayList<PhotosItem>();



    public PhotosAdapter(ViewMedicalRecordsActivity viewMedicalsRecordsAdapter, List<PhotosItem> photosItems) {
        this.viewMedicalsRecordsAdapter = viewMedicalsRecordsAdapter;
        this.photosItems = photosItems;
    }

    @NonNull
    @Override
    public PhotosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotosAdapter.ViewHolder(PhotosListBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosAdapter.ViewHolder holder, int position) {
        Picasso.with(holder.itemView.getRoot().getContext()).load(photosItems.get(position).getRecordPhoto()).into(holder.itemView.photosIv);

        /*if (responses.get(position).getRecordPhoto() != null) {
        }*/
        /* for (int i =0; i<responses.size(); i++){
             if (responses.get(position).getPhotos() != null) {
                 Picasso.with(holder.itemView.getRoot().getContext()).load(responses.get(position).getPhotos().get(i).getRecordPhoto()).into(holder.itemView.photosIv);
             }
         }*/



    }

    @Override
    public int getItemCount() {
        return photosItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private com.drcita.user.databinding.PhotosListBinding itemView;


        public ViewHolder(@NonNull PhotosListBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
            itemView.photosIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(viewMedicalsRecordsAdapter, NotificationViewImage.class);
                    intent.putExtra("image",photosItems.get(getAdapterPosition()).getRecordPhoto());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    viewMedicalsRecordsAdapter.startActivity(intent);
                }
            });
            itemView.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
