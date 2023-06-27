package com.zrapp.warehouse;

import static com.zrapp.warehouse.SigninActivity.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zrapp.warehouse.DAO.StaffDAO;
import com.zrapp.warehouse.databinding.ActivityChangePassBinding;

import java.util.Timer;
import java.util.TimerTask;

public class ChangePassActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityChangePassBinding binding;
    StaffDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        dao = new StaffDAO();

        binding.passToggle1.setOnClickListener(this);
        binding.passToggle2.setOnClickListener(this);

        binding.btnCofirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass = binding.edOldPass.getText().toString();
                String pass = binding.edPass.getText().toString();
                String confirm = binding.edConfirmPass.getText().toString();
                if (oldPass.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                    Toast.makeText(ChangePassActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (oldPass.equals(pass)) {
                    Toast.makeText(ChangePassActivity.this, "Mật khẩu mới cần khác mật khẩu cũ", Toast.LENGTH_SHORT).show();
                } else if (!pass.equals(confirm)) {
                    Toast.makeText(ChangePassActivity.this, "Nhập lại mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                } else {
                    account.setPass(pass);
                    dao.updateRow(account);
                    Toast.makeText(ChangePassActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(ChangePassActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }, 2200);
                }
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (account.getPost().equals("Nhân viên")) {
            getMenuInflater().inflate(R.menu.menu_option_staff, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_option_manager, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
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

    public void logOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassActivity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassActivity.this);
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

    public void passToggle(ImageView view, EditText ed) {
        if (view.getTag().equals("blind")) {
            view.setTag("eye");
            view.setImageResource(R.drawable.ic_eye);
            ed.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        } else {
            view.setTag("blind");
            view.setImageResource(R.drawable.ic_eye_blind);
            ed.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pass_toggle1:
                passToggle(binding.passToggle1, binding.edPass);
                break;
            case R.id.pass_toggle2:
                passToggle(binding.passToggle2, binding.edConfirmPass);
                break;
        }
    }
}