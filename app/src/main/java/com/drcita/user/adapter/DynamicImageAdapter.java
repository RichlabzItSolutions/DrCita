package com.drcita.user.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.drcita.user.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

public class DynamicImageAdapter extends RecyclerView.Adapter<DynamicImageAdapter.ImageViewHolder> {

    private Context context;
    private List<String> imageUrls;

    public DynamicImageAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(context);
        int size = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 80, context.getResources().getDisplayMetrics());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(8, 8, 8, 8);
        return new ImageViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Glide.with(context).load(imageUrls.get(position)).into((ImageView) holder.itemView);

        holder.itemView.setOnClickListener(v -> showZoomDialog(imageUrls.get(position)));
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private void showZoomDialog(String imageUrl) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_fullscreen_image);

        PhotoView photoView = dialog.findViewById(R.id.photoView);
        Glide.with(context).load(imageUrl).into(photoView);

        // Dismiss on tap
        photoView.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
