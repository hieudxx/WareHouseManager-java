package com.zrapp.warehouse;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zrapp.warehouse.DAO.StaffDAO;
import com.zrapp.warehouse.databinding.ActivityRegisterBinding;
import com.zrapp.warehouse.model.Staff;

import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityRegisterBinding binding;
    StaffDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        statusTrans();

        dao = new StaffDAO();

        binding.passToggle1.setOnClickListener(this);
        binding.passToggle2.setOnClickListener(this);

        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, SigninActivity.class);
                startActivity(i);
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.edName.getText().toString();
                String username = binding.edUsername.getText().toString();
                String pass = binding.edPassword.getText().toString();
                String confirm = binding.edConfirm.getText().toString();
                if (name.isEmpty() || username.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    Staff staff = dao.getStaff(username);
                    if (staff.getUsername() != null) {
                        Toast.makeText(RegisterActivity.this, "Username đã được sử dụng!!", Toast.LENGTH_SHORT).show();
                    }
                    if (!pass.equals(confirm)) {
                        Toast.makeText(RegisterActivity.this, "Nhập lại mật khẩu chưa đúng!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Staff account = new Staff();
                        account.setName(name);
                        account.setUsername(username);
                        account.setPass(pass);
                        account.setPost("Nhân viên");
                        dao.insertStaff(account, 0);
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công!!", Toast.LENGTH_SHORT).show();
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Intent i = new Intent(RegisterActivity.this, SigninActivity.class);
                                startActivity(i);
                            }
                        }, 2300);
                    }
                }
            }
        });
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
                passToggle(binding.passToggle1, binding.edPassword);
                break;
            case R.id.pass_toggle2:
                passToggle(binding.passToggle2, binding.edConfirm);
                break;
        }
    }

    public void statusTrans() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}