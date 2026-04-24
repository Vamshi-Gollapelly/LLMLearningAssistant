package com.vamshigollapelly.llmlearningassistant.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.vamshigollapelly.llmlearningassistant.R;

import java.util.ArrayList;
import java.util.List;

public class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.InterestViewHolder> {

    private final List<String> interestsList;
    private final List<String> selectedItems = new ArrayList<>();
    private final int maxSelection = 10;

    public InterestsAdapter(List<String> interestsList) {
        this.interestsList = interestsList;
    }

    public List<String> getSelectedItems() {
        return selectedItems;
    }

    @NonNull
    @Override
    public InterestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interest, parent, false);
        return new InterestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestViewHolder holder, int position) {
        String interest = interestsList.get(position);
        holder.tvInterestName.setText(interest);

        boolean isSelected = selectedItems.contains(interest);
        updateCardStyle(holder.cardInterest, holder.tvInterestName, isSelected);

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in));

        holder.itemView.setOnClickListener(v -> {
            if (selectedItems.contains(interest)) {
                selectedItems.remove(interest);
            } else {
                if (selectedItems.size() < maxSelection) {
                    selectedItems.add(interest);
                }
            }
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return interestsList.size();
    }

    private void updateCardStyle(MaterialCardView cardView, TextView textView, boolean isSelected) {
        if (isSelected) {
            cardView.setCardBackgroundColor(Color.parseColor("#27F35A"));
            cardView.setStrokeColor(Color.parseColor("#0B3D91"));
            textView.setTextColor(Color.BLACK);
        } else {
            cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            cardView.setStrokeColor(Color.parseColor("#0B3D91"));
            textView.setTextColor(Color.BLACK);
        }
    }

    static class InterestViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardInterest;
        TextView tvInterestName;

        public InterestViewHolder(@NonNull View itemView) {
            super(itemView);
            cardInterest = (MaterialCardView) itemView;
            tvInterestName = itemView.findViewById(R.id.tvInterestName);
        }
    }
}