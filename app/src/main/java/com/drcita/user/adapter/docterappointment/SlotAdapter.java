//package com.drcita.user.adapter.docterappointment;
//
//import android.view.View;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.drcita.user.R;
//
//public class SlotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private final List<SlotItem> items;
//    private int selectedPosition = -1;
//
//    public SlotAdapter(List<SlotItem> items) {
//        this.items = items;
//    }
//
//    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
//        TextView txtHeader;
//
//        public HeaderViewHolder(@NonNull View itemView) {
//            super(itemView);
//            txtHeader = itemView.findViewById(R.id.txtHeader);
//        }
//    }
//
//    public static class SlotViewHolder extends RecyclerView.ViewHolder {
//        TextView txtSlot;
//
//        public SlotViewHolder(@NonNull View itemView) {
//            super(itemView);
//            txtSlot = itemView.findViewById(R.id.txtSlot);
//        }
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return items.get(position).getType();
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType == SlotItem.TYPE_HEADER) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
//            return new HeaderViewHolder(view);
//        } else {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slot, parent, false);
//            return new SlotViewHolder(view);
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        SlotItem item = items.get(position);
//
//        if (holder instanceof HeaderViewHolder) {
//            ((HeaderViewHolder) holder).txtHeader.setText(item.getText());
//        } else if (holder instanceof SlotViewHolder) {
//            SlotViewHolder vh = (SlotViewHolder) holder;
//            vh.txtSlot.setText(item.getText());
//
//            if (item.isSelected()) {
//                vh.txtSlot.setBackgroundResource(R.drawable.slot_pill_green);
//                vh.txtSlot.setTextColor(Color.WHITE);
//            } else {
//                vh.txtSlot.setBackgroundResource(R.drawable.slot_pill_gray);
//                vh.txtSlot.setTextColor(Color.parseColor("#555555"));
//            }
//
//            vh.txtSlot.setOnClickListener(v -> {
//                // Deselect previously selected
//                if (selectedPosition != -1) {
//                    items.get(selectedPosition).setSelected(false);
//                    notifyItemChanged(selectedPosition);
//                }
//
//                // Select current
//                item.setSelected(true);
//                selectedPosition = holder.getAdapterPosition();
//                notifyItemChanged(selectedPosition);
//            });
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    public String getSelectedSlot() {
//        return (selectedPosition != -1) ? items.get(selectedPosition).getText() : null;
//    }
//}
//
