package com.drcita.user.adapter;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.drcita.user.R;
import java.util.List;
public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {

    private Context context;
    private List<String> docUrls;

    public DocumentAdapter(Context context, List<String> docUrls) {
        this.context = context;
        this.docUrls = docUrls;
    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_document, parent, false);
        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        String url = docUrls.get(position);
        holder.tvDocName.setText("Document " + (position + 1));
        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            context.startActivity(intent);
            new AlertDialog.Builder(context)
                    .setTitle("Choose Option")
                    .setMessage("Do you want to view or download this document?")
                    .setPositiveButton("View", (dialog, which) -> {
                        // View the document in browser or PDF viewer
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(intent);
                    })
                    .setNegativeButton("Download", (dialog, which) -> {
                        // Download using DownloadManager
                        downloadFile(url, "Document_" + (position + 1) + ".pdf");
                    })
                    .setNeutralButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return docUrls.size();
    }

    public static class DocumentViewHolder extends RecyclerView.ViewHolder {
        TextView tvDocName;

        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDocName = itemView.findViewById(R.id.tvDocName);
        }
    }
    private void downloadFile(String url, String fileName) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(fileName);
        request.setDescription("Downloading document...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        request.setMimeType("application/pdf");

        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager != null) {
            manager.enqueue(request);
        }
    }
}

