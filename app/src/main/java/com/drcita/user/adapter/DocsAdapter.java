package com.drcita.user.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.ViewMedicalRecordsActivity;
import com.drcita.user.databinding.DocuemntsListBinding;
import com.drcita.user.models.viewmedicalrecords.DocsItem;

import java.util.ArrayList;
import java.util.List;

public class DocsAdapter extends RecyclerView.Adapter<DocsAdapter.ViewHolder> {
    private ViewMedicalRecordsActivity viewMedicalRecordsActivity;
    private List<DocsItem> responses = new ArrayList<DocsItem>();


    public DocsAdapter(ViewMedicalRecordsActivity viewMedicalRecordsActivity, List<DocsItem> documentsLists) {
        this.viewMedicalRecordsActivity = viewMedicalRecordsActivity;
        this.responses = documentsLists;
    }

    @NonNull
    @Override
    public DocsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DocsAdapter.ViewHolder(DocuemntsListBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull DocsAdapter.ViewHolder holder, int position) {
//        holder.itemView.documentTV.setText(responses.get(position).getRecordDoc());

        /*for (int i =0; i<responses.size(); i++){
           holder.itemView.documentTV.setText(responses.get(i).getDocs().get(i).getRecordDoc());
        }*/


    }

    @Override
    public int getItemCount() {
        return responses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private com.drcita.user.databinding.DocuemntsListBinding itemView;


        public ViewHolder(@NonNull DocuemntsListBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
            itemView.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(responses.get(getAdapterPosition()).getRecordDoc()));
                    view.getContext().startActivity(browserIntent);
                }
            });
        }
    }
}
