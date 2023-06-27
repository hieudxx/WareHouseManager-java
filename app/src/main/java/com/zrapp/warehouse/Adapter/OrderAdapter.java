package com.zrapp.warehouse.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zrapp.warehouse.DAO.OrderDao;
import com.zrapp.warehouse.DAO.OrderDetailsDao;
import com.zrapp.warehouse.DAO.StaffDAO;
import com.zrapp.warehouse.Fragment.FragOrder.OrderDetailsActivity;
import com.zrapp.warehouse.databinding.ItemOrderBinding;
import com.zrapp.warehouse.model.Order;
import com.zrapp.warehouse.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> implements Filterable {

    private Context context;
    private int layout;
    private ArrayList<Order> OrderList = new ArrayList<>();
    private StaffDAO dao;
    private ArrayList<Order> listFilter;
    OrderDetailsDao detailsDao;

    public OrderAdapter(Context context, int layout, ArrayList<Order> orderList) {
        this.context = context;
        this.layout = layout;
        this.OrderList = orderList;
        this.listFilter = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemOrderBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_order, parent, false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = OrderList.get(position);
        dao = new StaffDAO();
        detailsDao = new OrderDetailsDao();
        holder.binding.tvIdOrder.setText(order.getId_order());
        holder.binding.tvNameStaff.setText(dao.getStaffByID(order.getId_staff()).getName());
        SimpleDateFormat sDF = new SimpleDateFormat("dd/MM/yy");
        try {
            holder.binding.tvTypeDate.setText(sDF.format(new SimpleDateFormat("yyyy-MM-dd").parse(order.getDate())) + " - DH " + order.getKindOfOrder());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.binding.tvTotal.setText(detailsDao.getTotal(order));
        holder.binding.cvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, OrderDetailsActivity.class);
                Bundle b = new Bundle();
                b.putString("idOrder", order.getId_order());
                b.putString("idStaff", order.getId_staff());
                i.putExtras(b);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return OrderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        private final ItemOrderBinding binding;

        public OrderViewHolder(@NonNull ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    OrderList = listFilter;
                } else {
                    ArrayList<Order> listO = new ArrayList<>();
                    for (Order order : listFilter) {
                        if (order.getId_order().toLowerCase().contains(strSearch.toLowerCase())) {
                            listO.add(order);
                        }
                    }

                    OrderList = listO;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = OrderList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                OrderList = (ArrayList<Order>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
