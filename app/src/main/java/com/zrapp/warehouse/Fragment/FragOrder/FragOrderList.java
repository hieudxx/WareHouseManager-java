package com.zrapp.warehouse.Fragment.FragOrder;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.zrapp.warehouse.Adapter.OrderAdapter;
import com.zrapp.warehouse.DAO.OrderDao;
import com.zrapp.warehouse.MainActivity;
import com.zrapp.warehouse.databinding.FragOrderListBinding;
import com.zrapp.warehouse.model.Order;
import com.zrapp.warehouse.R;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class FragOrderList extends Fragment {
    FragOrderListBinding binding;
    public static List<Order> OrderList = new ArrayList();
    public static OrderAdapter adapter;
    OrderDao dao;

    public FragOrderList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragOrderListBinding.inflate(inflater, container, false);

        dao = new OrderDao();
        OrderList = dao.getAll();
        adapter = new OrderAdapter((Context) getActivity(), R.layout.item_order, (ArrayList<Order>) OrderList);
        binding.rcvOrder.setAdapter(adapter);

        binding.btnFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.loadFrag(new FragOrderAdd());
            }
        });

        MainActivity.binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        binding.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.binding.searchBar.setIconifiedByDefault(true);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        OrderList = dao.getAll();
        adapter = new OrderAdapter((Context) getActivity(), R.layout.item_order, (ArrayList<Order>) OrderList);
        binding.rcvOrder.setAdapter(adapter);
        if (OrderList.isEmpty()){
            MainActivity.loadFrag(new FragOrder());
        }
    }
}