package com.zrapp.warehouse.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.zrapp.warehouse.DAO.OrderDetailsDao;
import com.zrapp.warehouse.R;
import com.zrapp.warehouse.databinding.ItemDetailsProdBinding;
import com.zrapp.warehouse.model.OrderDetails;

import java.util.List;

public class ListDetailsAdapter extends BaseAdapter {

    private Context context;
    private List<OrderDetails> list;
    OrderDetailsDao detailsDao;

    public ListDetailsAdapter(Context context, List<OrderDetails> list) {
        this.context = context;
        this.list    = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup container) {
        ItemDetailsProdBinding binding;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            binding     = ItemDetailsProdBinding.inflate(inflater, container, false);
            convertView = binding.getRoot();
            convertView.setTag(R.layout.item_details_prod, binding);
        } else {
            binding = (ItemDetailsProdBinding) convertView.getTag(R.layout.item_details_prod);
        }

        detailsDao = new OrderDetailsDao();
        OrderDetails details = list.get(i);
        binding.tvNameProd.setText(details.getProd().getName());
        binding.edQty.setText(details.getQty() + "");
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.edQty.setText((details.getQty() + 1) + "");
                details.setQty(details.getQty() + 1);
                list.set(i, details);
                notifyDataSetChanged();
            }
        });

        binding.btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (details.getQty() == 1) {
                    Toast.makeText(context, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
                } else {
                    binding.edQty.setText((details.getQty() - 1) + "");
                    details.setQty(details.getQty() - 1);
                    list.set(i, details);
                    notifyDataSetChanged();
                }
            }
        });

        binding.imgViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(i);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void changeDataset(List<OrderDetails> items) {
        this.list = items;
        notifyDataSetChanged();
    }
}
