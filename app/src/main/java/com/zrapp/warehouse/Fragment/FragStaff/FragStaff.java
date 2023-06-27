package com.zrapp.warehouse.Fragment.FragStaff;

import static com.zrapp.warehouse.MainActivity.loadFrag;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zrapp.warehouse.DAO.StaffDAO;
import com.zrapp.warehouse.databinding.FragStaffBinding;
import com.zrapp.warehouse.model.Staff;

import java.util.ArrayList;

public class FragStaff extends Fragment {
    FragStaffBinding binding;
    ArrayList<Staff> list;
    StaffDAO dao;

    public FragStaff() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragStaffBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dao = new StaffDAO();
        list = dao.getAll();

        if (!list.isEmpty()) {
            loadFrag(new FragStaffList());
        }

        loadFrag(new FragStaffList());

        binding.btnAddnv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFrag(new FragStaffAdd());
            }
        });
    }

}