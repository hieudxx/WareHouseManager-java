package com.zrapp.warehouse.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.zrapp.warehouse.databinding.ItemProdBinding;
import com.zrapp.warehouse.model.Product;
import com.zrapp.warehouse.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProdAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<Product> list;

    private List<Product> listFilter;

    public ProdAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
        this.listFilter = list;
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
        ItemProdBinding binding;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            binding = ItemProdBinding.inflate(inflater, container, false);
            convertView = binding.getRoot();
            convertView.setTag(R.layout.item_prod, binding);
        } else {
            binding = ((ItemProdBinding) convertView.getTag(R.layout.item_prod));
        }

        binding.tvnameProd.setText(list.get(i).getName());
        Locale locale = new Locale("vi", "VN");
        NumberFormat NF = NumberFormat.getInstance(locale);
        binding.tvPriceProd.setText(NF.format(list.get(i).getPrice()) + " vnd");
        Log.i("TAG info", "onBindViewHolder: " + i);
        if (list.get(i).getImg().equals("null")) {
            binding.imgItem.setImageResource(R.drawable.img_prod_default);
        } else {
            Picasso.get().load(list.get(i).getImg()).into(binding.imgItem, new Callback() {
                @Override
                public void onSuccess() {
                    if (i == list.size() - 1) {
                    }
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    list = listFilter;
                } else {
                    List<Product> listProd = new ArrayList<>();
                    for (Product prod : listFilter) {
                        if (prod.getName().toLowerCase().contains(strSearch.toLowerCase())) {
                            listProd.add(prod);
                        }
                    }

                    list = listProd;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<Product>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
