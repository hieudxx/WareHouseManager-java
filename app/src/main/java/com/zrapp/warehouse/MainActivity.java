package com.zrapp.warehouse;

import static com.zrapp.warehouse.SigninActivity.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.zrapp.warehouse.Fragment.FragOrder.FragOrder;
import com.zrapp.warehouse.Fragment.FragProd.FragProd;
import com.zrapp.warehouse.Fragment.FragStaff.FragStaff;
import com.zrapp.warehouse.Fragment.FragStatistic.FragStatistic;
import com.zrapp.warehouse.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public static ActivityMainBinding binding;
    static FragmentManager manager;
    static ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        manager = getSupportFragmentManager();
        loadFrag(new FragProd()); // Khi test app ofline thì thay đổi frag phù hợp với nhu cầu

//        binding.bottomNav.setItemHorizontalTranslationEnabled(true);
        binding.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_sp:
                        loadFrag(new FragProd());
                        binding.tvToolbar.setText("Sản phẩm");
                        break;
                    case R.id.nav_donhang:
                        loadFrag(new FragOrder());
                        binding.tvToolbar.setText("Đơn hàng");
                        break;
                    case R.id.nav_nv:
                        loadFrag(new FragStaff());
                        binding.tvToolbar.setText("Nhân viên");
                        break;
                    case R.id.nav_thongke:
                        loadFrag(new FragStatistic());
                        binding.tvToolbar.setText("Thống kê");
                        break;
                }
                return true;
            }
        });

        binding.frameContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                binding.searchBar.setIconifiedByDefault(true);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (account.getPost().equals("Nhân viên")) {
            binding.bottomNav.inflateMenu(R.menu.menu_bnav_staff);
            getMenuInflater().inflate(R.menu.menu_option_staff, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_option_manager, menu);
            binding.bottomNav.inflateMenu(R.menu.menu_bnav_manager);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                binding.searchBar.setVisibility(View.VISIBLE);
                actionBar.setDisplayHomeAsUpEnabled(false);
                break;

            case R.id.confirmRequest:
                Intent intentR = new Intent(getApplicationContext(), RequestActivity.class);
                startActivity(intentR);
                break;

            case R.id.changePass:
                Intent intentC = new Intent(getApplicationContext(), ChangePassActivity.class);
                startActivity(intentC);
                break;

            case R.id.logOut:
                logOut();
                break;

            case R.id.exit:
                exit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void loadFrag(Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frameContent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        switch (fragment.getClass().getSimpleName()) {
            case "FragProdAdd":
            case "FragOrderAdd":
            case "FragStaffAdd":
            case "FragStatistic":
                binding.searchBar.setVisibility(View.GONE);
                actionBar.setDisplayHomeAsUpEnabled(true);
                break;
            default:
                binding.searchBar.setVisibility(View.VISIBLE);
                actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    public void logOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Đăng xuất");
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất?");
        builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(intent);
            }
        });
        builder.setPositiveButton("Không", null);
        builder.show();
    }

    public void exit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Thoát");
        builder.setMessage("Bạn có chắc chắn muốn thoát chương trình?");
        builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
        });
        builder.setPositiveButton("Không", null);
        builder.show();
    }


    @Override
    public void onBackPressed() {
        if (!binding.searchBar.isIconified()) {
            binding.searchBar.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}