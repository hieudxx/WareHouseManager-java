package com.zrapp.warehouse.Adapter;

import static com.zrapp.warehouse.RequestActivity.adapter;
import static com.zrapp.warehouse.RequestActivity.binding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.squareup.picasso.Picasso;
import com.zrapp.warehouse.DAO.StaffDAO;
import com.zrapp.warehouse.R;
import com.zrapp.warehouse.RequestActivity;
import com.zrapp.warehouse.model.Staff;

import java.util.ArrayList;

public class StaffAdapter extends BaseAdapter implements Filterable {

    private final Context context;
    private final int resource;
    private ArrayList<Staff> list;
    private final LayoutInflater inflater;
    public ArrayList<Staff> listFilter;
    StaffDAO dao;

    public StaffAdapter(@NonNull Context context, int resource, ArrayList<Staff> list) {
        this.context = context;
        this.resource = resource;
        this.list = list;
        this.listFilter = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dao = new StaffDAO();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        StaffViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(resource, null);

            holder = new StaffViewHolder();
            holder.tv_name = convertView.findViewById(R.id.tvname_nhanvien);
            holder.tv_sdt = convertView.findViewById(R.id.tvsdt_nhanvien);
            holder.img_avt = convertView.findViewById(R.id.img_item_avt);
            holder.imgAccept = convertView.findViewById(R.id.imgAccept);
            holder.imgDefine = convertView.findViewById(R.id.imgDefine);
            convertView.setTag(holder);
        } else {
            holder = (StaffViewHolder) convertView.getTag();
        }


        //gán dữ liệu
        Staff nv = list.get(position);
        holder.tv_name.setText(nv.getName());
        holder.tv_sdt.setText(nv.getTel().equals("null") ? "Chưa cung cấp" : nv.getTel());
        if (list.get(position).getImg().equals("null")) {
            holder.img_avt.setImageResource(R.drawable.avatar_default);
        } else {
            Picasso.get().load(list.get(position).getImg()).into(holder.img_avt);
        }
        if (resource == R.layout.item_staff_request) {
            holder.imgAccept.setOnClickListener(view -> {
                nv.setStatus(true);
                dao.updateRow(nv);
                RequestActivity.list = dao.getRequest();
                adapter = new StaffAdapter(context, R.layout.item_staff_request, RequestActivity.list);
                binding.lv.setAdapter(adapter);
            });

            holder.imgDefine.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có chắc chắn muốn huỷ yêu cầu?");
                builder.setNegativeButton("Có", (dialog, which) -> {
                    dao.deleteRow(nv.getId());
                    RequestActivity.list = dao.getRequest();
                    adapter = new StaffAdapter(context, R.layout.item_staff_request, RequestActivity.list);
                    binding.lv.setAdapter(adapter);
                });
                builder.setPositiveButton("Không", null);
                builder.show();
            });
        }
        return convertView;
    }

    public class StaffViewHolder {
        TextView tv_name, tv_sdt;
        ImageView imgAccept, imgDefine, img_avt;
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
                    ArrayList<Staff> listStaff = new ArrayList<>();
                    for (Staff staff : listFilter) {
                        if (staff.getName().toLowerCase().contains(strSearch.toLowerCase())) {
                            listStaff.add(staff);
                        }
                    }
                    list = listStaff;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (ArrayList<Staff>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
