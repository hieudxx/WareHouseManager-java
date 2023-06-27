package com.zrapp.warehouse.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zrapp.warehouse.R;
import com.zrapp.warehouse.databinding.ItemOrderDetailsBinding;
import com.zrapp.warehouse.databinding.ItemProdBinding;
import com.zrapp.warehouse.model.OrderDetails;
import com.zrapp.warehouse.model.Product;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderDetailsAdapter extends BaseAdapter {

    private Context context;
    private List<OrderDetails> list;
    private int layout;

    public OrderDetailsAdapter(Context context, List<OrderDetails> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
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
        ItemOrderDetailsBinding binding;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            binding = ItemOrderDetailsBinding.inflate(inflater, container, false);
            convertView = binding.getRoot();
            convertView.setTag(layout, binding);
        } else {
            binding = ((ItemOrderDetailsBinding) convertView.getTag(layout));
        }

        OrderDetails details = list.get(i);
        binding.nameProd.setText((i + 1) + ". " + details.getProd().getId() + " - " + details.getProd().getName());
        Locale locale = new Locale("vi", "VN");
        NumberFormat nf = NumberFormat.getInstance(locale);
        if (details.getOrder().getKindOfOrder().equals("Nhập")) {
            binding.tvCost.setText(nf.format(details.getProd().getCost_price()) + " x " + details.getQty());
            binding.tvTotal.setText(nf.format(details.getProd().getCost_price() * details.getQty()));
        } else {
            binding.tvCost.setText(nf.format(details.getProd().getPrice()) + " x " + details.getQty());
            binding.tvTotal.setText(nf.format(details.getProd().getPrice() * details.getQty()));
        }
        return convertView;
    }
}
