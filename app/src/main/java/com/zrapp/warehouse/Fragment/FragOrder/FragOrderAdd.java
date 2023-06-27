package com.zrapp.warehouse.Fragment.FragOrder;

import static com.zrapp.warehouse.MainActivity.loadFrag;
import static com.zrapp.warehouse.SigninActivity.account;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.zrapp.warehouse.Adapter.ListDetailsAdapter;
import com.zrapp.warehouse.DAO.OrderDao;
import com.zrapp.warehouse.DAO.OrderDetailsDao;
import com.zrapp.warehouse.DAO.ProductDAO;
import com.zrapp.warehouse.MainActivity;
import com.zrapp.warehouse.R;
import com.zrapp.warehouse.databinding.FragOrderAddBinding;
import com.zrapp.warehouse.model.Order;
import com.zrapp.warehouse.model.OrderDetails;
import com.zrapp.warehouse.model.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragOrderAdd extends Fragment {
    FragOrderAddBinding binding;
    OrderDao orderDao;
    OrderDetailsDao detailsDao;
    ProductDAO prodDAO;
    List<OrderDetails> list = new ArrayList<>();
    ListDetailsAdapter adapter;
    String[] arr;

    public FragOrderAdd() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragOrderAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderDao = new OrderDao();
        detailsDao = new OrderDetailsDao();
        prodDAO = new ProductDAO();
        adapter = new ListDetailsAdapter(getActivity(), list);
        binding.lv.setAdapter(adapter);

        binding.tvOrderID.setText(orderDao.getOrderID(binding.actvType.getText().toString().equals("Nhập") ? 0 : 1));
        binding.tvStaffID.setText(account.getId());

        //Filter loại DH
        arr = new String[]{"Nhập", "Xuất"};
        ArrayAdapter typeAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arr);
        binding.actvType.setAdapter(typeAdapter);
        binding.actvType.setThreshold(1);
        binding.actvType.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                binding.tvOrderID.setText(orderDao.getOrderID(binding.actvType.getText().toString().equals("Nhập") ? 0 : 1));
            }
        });

        //Filter sản phẩm
        ArrayList<String> listProd = new ArrayList<>();
        for (Product prod : prodDAO.getAll_Prod()) {
            listProd.add(prod.getName());
        }
        ArrayAdapter prodAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listProd);
        binding.actvProd.setAdapter(prodAdapter);
        binding.actvProd.setThreshold(1);

        //Thêm sản phẩm vào giỏ hàng
        binding.actvProd.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                addProd();
                binding.actvProd.setText("");
            }
        });

        //Hoàn tất đơn hàng
        binding.btnCofirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrder();
                loadFrag(new FragOrderList());
            }
        });
    }

    public void addProd() {
        try {
            if (validation() < 0) {
                Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                int i = 0;
                for (; i < arr.length; i++) {
                    if (arr[i].equals(binding.actvType.getText().toString())) break;
                }
                if (i == arr.length) {
                    Toast.makeText(getContext(), "Loại DH chỉ có thể \"Nhập/Xuất\"", Toast.LENGTH_SHORT).show();
                } else {
                    Product prod = prodDAO.getProdByName(binding.actvProd.getText().toString());
                    int pos = checkProd(list, binding.actvProd.getText().toString());
                    Order order = new Order(binding.tvOrderID.getText().toString(), binding.actvType.getText().toString());
                    OrderDetails details = new OrderDetails(prod, order, 1);
                    if (pos >= 0) {
                        int soluong = list.get(pos).getQty();
                        details.setQty(soluong + 1);
                        list.set(pos, details);
                    } else {
                        list.add(details);
                    }
                    adapter.changeDataset(list);
                }
            }

        } catch (Exception ex) {
            Log.e("Error", ex.toString());
        }
    }

    public void addOrder() {
        try {
            Order order = new Order();
            order.setId_staff(account.getId());
            order.setKindOfOrder(binding.actvType.getText().toString());
            orderDao.insertRow(order);
            for (OrderDetails details : list) {
                detailsDao.insertOrderDetail(details);
            }
        } catch (Exception ex) {
            Log.e("Error", ex.toString());
        }
    }

    public int checkProd(List<OrderDetails> lsHD, String prodName) {
        int pos = -1;
        for (int i = 0; i < lsHD.size(); i++) {
            OrderDetails details = lsHD.get(i);
            if (details.getProd().getName().equalsIgnoreCase(prodName)) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    public int validation() {
        if (binding.actvType.getText().toString().isEmpty() ||
                binding.actvProd.getText().toString().isEmpty()) {
            return -1;
        }
        return 1;
    }
}