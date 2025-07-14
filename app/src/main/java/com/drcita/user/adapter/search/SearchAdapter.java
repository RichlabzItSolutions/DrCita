package com.drcita.user.adapter.search;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.R;
import com.drcita.user.models.home.SectionItem;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SectionItem> originalList;  // Full data
    private List<SectionItem> filteredList;  // Filtered data

    public SearchAdapter(List<SectionItem> items) {
        this.originalList = new ArrayList<>(items);
        this.filteredList = new ArrayList<>(items);
    }

    @Override
    public int getItemViewType(int position) {
        return filteredList.get(position).getType();
    }
    public interface OnItemClickListener {
        void onItemClicked(SectionItem item);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SectionItem.TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
            return new NameViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SectionItem item = filteredList.get(position);
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).title.setText(item.getTitle());
        } else if (holder instanceof NameViewHolder) {
            ((NameViewHolder) holder).name.setText(
                    item.getArea() != null && !item.getArea().isEmpty()
                            ? item.getName() + " (" + item.getArea() + ")"
                            : item.getName()
            );


            if (item.isPlaceholder()) {
                ((NameViewHolder) holder).imgArrow.setVisibility(View.GONE);
                ((NameViewHolder) holder).imgProfile.setVisibility(View.GONE);
                ((NameViewHolder) holder).view_line.setVisibility(View.GONE);

                ((NameViewHolder) holder).name.setTextColor(Color.GRAY);
                ((NameViewHolder) holder).name.setTypeface(null, Typeface.ITALIC);
                holder.itemView.setOnClickListener(null); // disable click
            } else {
                ((NameViewHolder) holder).imgArrow.setVisibility(View.VISIBLE);
                ((NameViewHolder) holder).imgProfile.setVisibility(View.VISIBLE);
                ((NameViewHolder) holder).view_line.setVisibility(View.VISIBLE);

                ((NameViewHolder) holder).name.setTextColor(Color.BLACK);
                ((NameViewHolder) holder).name.setTypeface(null, Typeface.NORMAL);

                holder.itemView.setOnClickListener(v -> {
                    if (listener != null) listener.onItemClicked(item);
                });
            }
        }
    }

    // ViewHolder classes
    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        HeaderViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.txtHeader);
        }
    }

    static class NameViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView tvRelation;
        ImageView imgProfile,imgArrow;
        View view_line;

        NameViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tvName);
            tvRelation=view.findViewById(R.id.tvRelation);
            imgProfile=view.findViewById(R.id.imgProfile);
            view_line=view.findViewById(R.id.view_line);
            imgArrow=view.findViewById(R.id.imgArrow);
            tvRelation.setVisibility(View.GONE);
        }
    }


}
