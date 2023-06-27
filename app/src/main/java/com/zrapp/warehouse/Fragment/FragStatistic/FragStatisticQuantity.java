package com.zrapp.warehouse.Fragment.FragStatistic;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.zrapp.warehouse.Adapter.StatisticAdapter;
import com.zrapp.warehouse.Custom.NF;
import com.zrapp.warehouse.DAO.OrderDetailsDao;
import com.zrapp.warehouse.databinding.FragStatisticsMoneyBinding;
import com.zrapp.warehouse.databinding.FragStatisticsQtyBinding;
import com.zrapp.warehouse.databinding.LayoutBottomsheetTimeBinding;
import com.zrapp.warehouse.model.Product;

import java.util.ArrayList;
import java.util.List;

public class FragStatisticQuantity extends Fragment {
    FragStatisticsQtyBinding binding;
    OrderDetailsDao dao;
    StatisticAdapter adapter;
    List<Product> list;

    LayoutBottomsheetTimeBinding bindingTime;
    BottomSheetDialog bottomSheetDialog;

    MaterialDatePicker picker;
    int type = 0, time = 0;
    Object selection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragStatisticsQtyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dao = new OrderDetailsDao();
        list = new ArrayList<>();
        show();

        binding.dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });

        binding.tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.indicator1.setVisibility(View.VISIBLE);
                binding.tvIncome.setTextColor(Color.parseColor("#600A0A"));
                binding.indicator2.setVisibility(View.INVISIBLE);
                binding.tvExpense.setTextColor(Color.BLACK);
                type = 0;
                if (time == 5) showDiff();
                else show();
            }
        });

        binding.tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.indicator2.setVisibility(View.VISIBLE);
                binding.tvExpense.setTextColor(Color.parseColor("#67982F"));
                binding.indicator1.setVisibility(View.INVISIBLE);
                binding.tvIncome.setTextColor(Color.BLACK);
                type = 1;
                if (time == 5) showDiff();
                else show();
            }
        });
    }

    public void show() {
        int s = 0;
        for (Product prod : dao.getStatistic(0, time, 0)) {
            s += prod.getPrice();
        }
        binding.tvIncome.setText(s + "");
        s = 0;
        for (Product prod : dao.getStatistic(1, time, 0)) {
            s += prod.getPrice();
        }
        binding.tvExpense.setText(s + "");
        //Xử lý list view
        list = dao.getStatistic(type, time, 0);
        adapter = new StatisticAdapter(list, 0);
        binding.rcv.setAdapter(adapter);
    }

    public void showDiff() {
        int s = 0;
        for (Product prod : dao.getStatisticDiff(0, selection, 0)) {
            s += prod.getPrice();
        }
        binding.tvIncome.setText(s + "");
        s = 0;
        for (Product prod : dao.getStatisticDiff(1, selection, 0)) {
            s += prod.getPrice();
        }
        binding.tvExpense.setText(s + "");
        //Xử lý list view
        list = dao.getStatisticDiff(type, selection, 0);
        adapter = new StatisticAdapter( list, 0);
        binding.rcv.setAdapter(adapter);
    }

    private void showBottomSheetDialog() {
        bindingTime = LayoutBottomsheetTimeBinding.inflate(getLayoutInflater());
        bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(bindingTime.getRoot());
        bottomSheetDialog.show();

        RadioGroup rdoG = bindingTime.radioGr;
        ((RadioButton) rdoG.getChildAt(time * 2)).setChecked(true);
        rdoG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                time = rdoG.indexOfChild(rdoG.findViewById(i)) / 2;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheetDialog.dismiss();
                    }
                }, 500);
            }
        });

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                switch (time) {
                    case 0:
                        binding.tvTime.setText("Hôm nay");
                        show();
                        break;

                    case 1:
                        binding.tvTime.setText("Tuần này");
                        show();
                        break;

                    case 2:
                        binding.tvTime.setText("Tháng này");
                        show();
                        break;

                    case 3:
                        binding.tvTime.setText("Tuần trước");
                        show();
                        break;

                    case 4:
                        binding.tvTime.setText("Tháng trước");
                        show();
                        break;

                    case 5:
                        binding.tvTime.setText("Khoảng thời gian");
                        dateSelect();
                        break;
                }
            }
        });
    }

    public void dateSelect() {
        Pair pair = new Pair(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds());
        picker = MaterialDatePicker.Builder.dateRangePicker().setSelection(pair).build();
        picker.show(getParentFragmentManager(), picker.toString());

        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object sel) {
                binding.tvTime.setText(picker.getHeaderText());
                selection = sel;
                showDiff();
            }
        });
    }
}