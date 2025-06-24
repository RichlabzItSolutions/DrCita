package com.drcita.user.adapter.medical;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.R;

import java.io.File;
import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.ViewHolder> {
    private List<File> pdfs;
    private ImageAdapter.OnRemoveListener onRemoveListener;

    public PdfAdapter(List<File> pdfs, ImageAdapter.OnRemoveListener onRemoveListener) {
        this.pdfs = pdfs;
        this.onRemoveListener = onRemoveListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPdfName;
        ImageView btnRemove;

        public ViewHolder(View view) {
            super(view);
            tvPdfName = view.findViewById(R.id.tvPdfName);
            btnRemove = view.findViewById(R.id.btnRemovePdf);
        }
    }

    @NonNull
    @Override
    public PdfAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvPdfName.setText(pdfs.get(position).getName());
        holder.btnRemove.setOnClickListener(v -> onRemoveListener.onRemove(pdfs.get(position)));
    }

    @Override
    public int getItemCount() {
        return pdfs.size();
    }
}
