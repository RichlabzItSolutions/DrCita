package com.drcita.user.adapter.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.R;
import com.drcita.user.models.fliter.FilterOption;
import com.drcita.user.utitlities.SimpleTextWatcher;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;

public class FilterOptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    public interface OnSelectionChangedListener {
        void onSelectionChanged(int selectedCount);
    }

    private final Context context;
    private final List<FilterOption> fullOptionList;
    private List<FilterOption> filteredList;
    private final boolean isSingleChoice;
    private int selectedRadioPosition = -1;
    private final SharedPreferences sharedPreferences;
    private final String PREF_KEY;
    private final OnSelectionChangedListener selectionChangedListener;
    private final String categoryKey;

    public FilterOptionAdapter(Context context, List<FilterOption> optionList,
                               SharedPreferences sharedPreferences, boolean isSingleChoice,
                               OnSelectionChangedListener selectionChangedListener,
                               String categoryKey) {
        this.context = context;
        this.fullOptionList = new ArrayList<>(optionList);
        this.filteredList = new ArrayList<>(optionList);
        this.sharedPreferences = sharedPreferences;
        this.isSingleChoice = isSingleChoice;
        this.selectionChangedListener = selectionChangedListener;
        this.categoryKey = categoryKey;
        this.PREF_KEY = "filter_" + categoryKey;
        loadSelectionsFromPrefs();
        notifySelectionChanged();
    }

    @Override
    public int getItemViewType(int position) {
        FilterOption option = filteredList.get(position);
        if ("experience_range".equalsIgnoreCase(option.getId())) {
            return 2;
        }
        return isSingleChoice ? 1 : 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 2) {
            view = LayoutInflater.from(context).inflate(R.layout.item_filter_range, parent, false);
            return new RangeViewHolder(view);
        } else if (viewType == 1) {
            view = LayoutInflater.from(context).inflate(R.layout.item_filter_radio, parent, false);
            return new RadioViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_filter_checkbox, parent, false);
            return new CheckboxViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FilterOption option = filteredList.get(position);

        if (holder instanceof RangeViewHolder) {
            RangeViewHolder vh = (RangeViewHolder) holder;

            float minVal = 0;
            float maxVal = 50;

            try {
                minVal = Float.parseFloat(option.getMin());
                maxVal = Float.parseFloat(option.getMax());
            } catch (NumberFormatException e) {
                minVal = 0;
                maxVal = 50;
            }

            // Prepopulate slider and EditTexts
            vh.sliderExperience.setValues(minVal, maxVal);
            vh.etMin.setText(String.valueOf(Math.round(minVal)));
            vh.etMax.setText(String.valueOf(Math.round(maxVal)));

            // Slider listener
            vh.sliderExperience.addOnChangeListener((slider, value, fromUser) -> {
                List<Float> values = slider.getValues();
                String min = String.valueOf(Math.round(values.get(0)));
                String max = String.valueOf(Math.round(values.get(1)));

                vh.etMin.setText(min);
                vh.etMax.setText(max);

                option.setMin(min);
                option.setMax(max);
                saveSelectionsToPrefs();
            });
        }

        else if (holder instanceof CheckboxViewHolder) {
            CheckboxViewHolder vh = (CheckboxViewHolder) holder;
            vh.checkBox.setOnCheckedChangeListener(null); // detach listener
            vh.checkBox.setText(option.getLabel());
            vh.checkBox.setChecked(option.isSelected()); // bind state
            vh.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> { // reattach
                option.setSelected(isChecked);
                saveSelectionsToPrefs();
                notifySelectionChanged();
            });

        } else if (holder instanceof RadioViewHolder) {
            RadioViewHolder vh = (RadioViewHolder) holder;
            vh.radioButton.setText(option.getLabel());
            vh.radioButton.setChecked(position == selectedRadioPosition);
            vh.radioButton.setOnClickListener(v -> {
                selectedRadioPosition = position;
                for (int i = 0; i < fullOptionList.size(); i++) {
                    fullOptionList.get(i).setSelected(i == fullOptionList.indexOf(filteredList.get(position)));
                }
                saveSelectionsToPrefs();
                notifySelectionChanged();
                notifyDataSetChanged();
            });
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String query) {
        getFilter().filter(query);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<FilterOption> filtered = new ArrayList<>();
                if (constraint == null || constraint.toString().isEmpty()) {
                    filtered = new ArrayList<>(fullOptionList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (FilterOption option : fullOptionList) {
                        if (option.getLabel().toLowerCase().contains(filterPattern)) {
                            filtered.add(option);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (List<FilterOption>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private void saveSelectionsToPrefs() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (isSingleChoice) {
            editor.putInt(PREF_KEY + "_selected_position", selectedRadioPosition);
        }

        for (FilterOption option : fullOptionList) {
            String key = PREF_KEY + "_" + option.getId();
            editor.putBoolean(key, option.isSelected());

            if ("experience_range".equalsIgnoreCase(option.getId())) {
                editor.putString(key + "_min", option.getMin());
                editor.putString(key + "_max", option.getMax());
            }
        }

        editor.apply();
    }

    private void loadSelectionsFromPrefs() {
        if (isSingleChoice) {
            selectedRadioPosition = sharedPreferences.getInt(PREF_KEY + "_selected_position", -1);
        }

        for (int i = 0; i < fullOptionList.size(); i++) {
            FilterOption option = fullOptionList.get(i);
            String key = PREF_KEY + "_" + option.getId();

            if (isSingleChoice) {
                option.setSelected(i == selectedRadioPosition);
            } else {
                option.setSelected(sharedPreferences.getBoolean(key, false));
            }


            if ("experience_range".equalsIgnoreCase(option.getId())) {
                option.setMin(sharedPreferences.getString(key + "_min", ""));
                option.setMax(sharedPreferences.getString(key + "_max", ""));
            }
        }
    }

    private void notifySelectionChanged() {
        if (selectionChangedListener != null) {
            int count = 0;
            for (FilterOption option : fullOptionList) {
                if (option.isSelected()) count++;
            }
            selectionChangedListener.onSelectionChanged(count);
        }
    }

    static class CheckboxViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public CheckboxViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    static class RadioViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;

        public RadioViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radioButton);
        }
    }

    static class RangeViewHolder extends RecyclerView.ViewHolder {
        EditText etMin, etMax;
        RangeSlider sliderExperience;

        public RangeViewHolder(@NonNull View itemView) {
            super(itemView);
            etMin = itemView.findViewById(R.id.etMin);
            etMax = itemView.findViewById(R.id.etMax);
            sliderExperience = itemView.findViewById(R.id.sliderExperience); // 👈 this line is missing in your code
        }
    }


}
