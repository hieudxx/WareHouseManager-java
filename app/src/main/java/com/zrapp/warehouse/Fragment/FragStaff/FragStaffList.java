package com.zrapp.warehouse.Fragment.FragStaff;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.zrapp.warehouse.Adapter.StaffAdapter;
import com.zrapp.warehouse.DAO.StaffDAO;
import com.zrapp.warehouse.MainActivity;
import com.zrapp.warehouse.R;
import com.zrapp.warehouse.databinding.BottomsheetStaffBinding;
import com.zrapp.warehouse.databinding.FragStaffListBinding;
import com.zrapp.warehouse.model.Staff;

import java.util.ArrayList;

public class FragStaffList extends Fragment {

    FragStaffListBinding binding;
    StaffDAO dao;
    ArrayList<Staff> listStaff;
    StaffAdapter staffAdapter;
    public static int stt;
    public static boolean flag;

    public FragStaffList() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragStaffListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fabAddNv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFrag(new FragStaffAdd());
            }
        });

        //Hiển thi danh sách nhân viên
        dao = new StaffDAO();
        changeListStaff();
        binding.lvNv.setDivider(null);
        binding.lvNv.setDividerHeight(0);

        binding.lvNv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showBottomSheeetStaff(i);
            }
        });

        findStaff();
    }

    private void findStaff() {
        MainActivity.binding.searchBar.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                staffAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                staffAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void changeListStaff() {
        listStaff = dao.getAll();
        staffAdapter = new StaffAdapter(getActivity(), R.layout.item_staff, listStaff);
        binding.lvNv.setAdapter(staffAdapter);
    }

    public void loadFrag(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void deleteStaff(String maNv, String nameNv) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setMessage("Bạn muốn xoá nhân viên " + nameNv + "?");
        mBuilder.setNegativeButton("Xác Nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dao.deleteRow(maNv);
                changeListStaff();
                Toast.makeText(getActivity(), "Xoá thành công", Toast.LENGTH_LONG).show();
                dialogInterface.cancel();
            }
        });


        mBuilder.setPositiveButton("Huỷ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        mBuilder.show();
    }

    private void showBottomSheeetStaff(int i) {
        BottomsheetStaffBinding bindingbts;
        bindingbts = BottomsheetStaffBinding.inflate(getLayoutInflater());

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(bindingbts.getRoot());
        bottomSheetDialog.show();

        bindingbts.btsNameStaff.setText(listStaff.get(i).getName());
        bindingbts.btsIdStaff.setText(listStaff.get(i).getId());
        bindingbts.btsUsernameStaff.setText(listStaff.get(i).getUsername());
        bindingbts.btsPhoneStaff.setText(listStaff.get(i).getTel());
        bindingbts.btsPositionStaff.setText(listStaff.get(i).getPost());

        bindingbts.btsBtnDeleteStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Staff staff = listStaff.get(i);
                deleteStaff(staff.getId(), staff.getName());
                bottomSheetDialog.dismiss();
            }
        });

        bindingbts.btsBtnUpdateStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = true;
                stt = i;
                Intent i = new Intent(getActivity(), ActivityStaffUpdate.class);
                startActivity(i);
                bottomSheetDialog.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        changeListStaff();
    }
}
