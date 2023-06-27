package com.zrapp.warehouse.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zrapp.warehouse.Custom.NF;
import com.zrapp.warehouse.R;
import com.zrapp.warehouse.databinding.ItemStatisticBinding;
import com.zrapp.warehouse.model.Product;

import java.util.List;

public class StatisticAdapter extends RecyclerView.Adapter<StatisticAdapter.StatisticViewHolder> {
    private List<Product> list;
    private int value;

    public StatisticAdapter(List<Product> list, int value) {
        this.list = list;
        this.value = value;
    }

    @NonNull
    @Override
    public StatisticViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ItemStatisticBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_statistic, parent, false);
        return new StatisticViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticViewHolder holder, int position) {
        Product prod = list.get(position);
        holder.binding.tvNameProd.setText(prod.getName());
        holder.binding.tvQty.setText(prod.getPrice() + "");
        holder.binding.tvMoney.setText(NF.format(prod.getPrice()));
        if (value == 0) holder.binding.tvMoney.setVisibility(View.GONE);
        else holder.binding.cardView.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class StatisticViewHolder extends RecyclerView.ViewHolder {

        private final ItemStatisticBinding binding;

        public StatisticViewHolder(@NonNull ItemStatisticBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
