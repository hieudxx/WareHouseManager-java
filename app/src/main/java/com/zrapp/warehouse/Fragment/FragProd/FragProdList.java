package com.zrapp.warehouse.Fragment.FragProd;

import static com.zrapp.warehouse.SigninActivity.account;

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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.zrapp.warehouse.Adapter.ProdAdapter;
import com.zrapp.warehouse.DAO.ProductDAO;
import com.zrapp.warehouse.MainActivity;
import com.zrapp.warehouse.databinding.FragProdListBinding;
import com.zrapp.warehouse.databinding.LayoutBottomsheetProdBinding;
import com.zrapp.warehouse.model.Product;
import com.zrapp.warehouse.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FragProdList extends Fragment {
    FragProdListBinding binding;
    List<Product> list = new ArrayList<>();
    ProductDAO dao_prod;
    ProdAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragProdListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dao_prod = new ProductDAO();
        list = dao_prod.getAll_Prod();

        adapter = new ProdAdapter(getContext(),list);
        binding.lvSp.setAdapter(adapter);

        binding.btnFloatAddProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.loadFrag(new FragProdAdd());
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
    }

    public void showBottomSheetDialog(int position) {
        LayoutBottomsheetProdBinding Binding;
        Binding = LayoutBottomsheetProdBinding.inflate(getLayoutInflater());

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(Binding.getRoot());
        bottomSheetDialog.show();
        if (account.getPost().equals("Nhân viên")) {
            Binding.tvcostpricesp.setVisibility(View.GONE);
            Binding.tvCostpriceProd.setVisibility(View.GONE);
            Binding.btnXoaProd.setVisibility(View.GONE);
            Binding.btnCapNhatProd.setVisibility(View.GONE);
        }

        Binding.tvNameProd.setText(list.get(position).getName());
        Binding.tvLocationProd.setText(list.get(position).getViTri());

        Locale locale = new Locale("vi", "VN");
        NumberFormat NF = NumberFormat.getInstance(locale);

        Binding.tvPriceProd.setText(NF.format(list.get(position).getPrice()) + " VND");
        Binding.tvCostpriceProd.setText(NF.format(list.get(position).getCost_price()) + " VND");

        Binding.btnXoaProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Xóa Sản Phẩm?");
                builder.setMessage(
                        "Sản phẩm này sẽ được xóa khỏi danh sách các sản phẩm trong cửa hàng của bạn." +
                                "Bạn có chắc rằng muốn thực hiện?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dao_prod.deleteProd(list.get(position).getId());
                        Toast.makeText(getContext(), "Xóa sản phẩm thành công!!!", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                        MainActivity.loadFrag(new FragProdList());
                    }
                });
                builder.setNegativeButton("Hủy Xóa", null);
                builder.show();
            }
        });

        Binding.btnCapNhatProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ActivityProdUpdate.class);
                Bundle b = new Bundle();
                b.putInt("pos", position);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }
}