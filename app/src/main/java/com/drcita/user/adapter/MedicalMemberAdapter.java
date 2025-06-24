package com.drcita.user.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.MedicalMembersListActivity;
import com.drcita.user.MedicalRecordsActivity;
import com.drcita.user.MembersListActivity;
import com.drcita.user.ProfileUpdateActivity;
import com.drcita.user.R;
import com.drcita.user.models.userprofile.Member;

import java.util.List;

public class MedicalMemberAdapter extends RecyclerView.Adapter<MedicalMemberAdapter.MemberViewHolder> {

    private List<Member> members;
    private MedicalMembersListActivity context;


    public MedicalMemberAdapter(MedicalMembersListActivity membersListActivity, List<Member> members) {
        this.members = members;
        this.context = membersListActivity;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Member member = members.get(position);
        holder.tvName.setText(member.getFullName());
        if(member.getSelf()==1) {
            holder.tvRelation.setText("Self");
        }
        else {

            holder.tvRelation.setText(member.getRelation());
        }

       holder. ll_child.setOnClickListener(v -> {
           Intent i = new Intent(context, MedicalRecordsActivity.class);
           i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           i.putExtra("subuserId", String.valueOf(members.get(position).getSubUserId()));
           context.startActivity(i);

       });
        // You can load avatar with Glide/Picasso if needed
    }


    @Override
    public int getItemCount() {
        return members.size();
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRelation;
        ImageView imgProfile;
        LinearLayout ll_child;

        @SuppressLint("WrongViewCast")
        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvRelation = itemView.findViewById(R.id.tvRelation);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            ll_child = itemView.findViewById(R.id.ll_child);

        }
    }
}

