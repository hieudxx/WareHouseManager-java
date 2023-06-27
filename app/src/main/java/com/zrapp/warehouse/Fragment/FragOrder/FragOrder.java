package com.zrapp.warehouse.Fragment.FragOrder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zrapp.warehouse.DAO.OrderDao;
import com.zrapp.warehouse.MainActivity;
import com.zrapp.warehouse.databinding.FragOrderBinding;
import com.zrapp.warehouse.model.Order;

import java.util.List;

public class FragOrder extends Fragment {
    FragOrderBinding binding;
    OrderDao dao;
    List<Order> listdh;

    public FragOrder() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dao = new OrderDao();
        listdh = dao.getAll();
        if (!listdh.isEmpty()) {
            MainActivity.loadFrag(new FragOrderList());
        }

        binding.btnAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment;
                fragment = new FragOrderAdd();
                MainActivity.loadFrag(fragment);
            }
        });
    }
}