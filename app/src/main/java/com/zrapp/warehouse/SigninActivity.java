package com.zrapp.warehouse;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.zrapp.warehouse.DAO.StaffDAO;
import com.zrapp.warehouse.databinding.ActivitySigninBinding;
import com.zrapp.warehouse.model.Staff;

public class SigninActivity extends AppCompatActivity {
    ActivitySigninBinding binding;
    StaffDAO dao;
    public static Staff account = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        statusTrans();
        dao = new StaffDAO(); // Khi test app ofline thì "rào"

        binding.edPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    signIn();
                    return true;
                }
                return false;
            }
        });

        binding.passToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passToggle(binding.passToggle, binding.edPassword);
            }
        });

        binding.tvRegister.setOnClickListener(view -> {
            Intent i = new Intent(SigninActivity.this, RegisterActivity.class);
            startActivity(i);
        });

        binding.btnLogin.setOnClickListener(view -> {
            signIn();
        });
    }

    // Khi test app oflinethì "rào"
    public void signIn() {
        //Sử sụng khi test app ofline - Khi test app online thì "rào"
        {
            account = new Staff("NV0001", "admin", "admin", "admin", "null", "", "Quản lý", true);
            Intent i = new Intent(SigninActivity.this, MainActivity.class);
            startActivity(i);
        }
        //        {
        //            String username = binding.edUsername.getText().toString();
        //            String pass = binding.edPassword.getText().toString();
        //            if (username.isEmpty() || pass.isEmpty()) {
        //                Toast.makeText(SigninActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
        //            } else {
        //                account = dao.getStaff(username);
        //                if (!dao.isExistsStaff(username)) {
        //                    Toast.makeText(SigninActivity.this, "Tài khoản không tồn tại!!", Toast.LENGTH_SHORT).show();
        //                } else if (!pass.equals(account.getPass())) {
        //                    Toast.makeText(SigninActivity.this, "Mật khẩu không đúng!!", Toast.LENGTH_SHORT).show();
        //                } else if (!account.isStatus()) {
        //                    Toast.makeText(SigninActivity.this, "Vui lòng chờ xác nhận", Toast.LENGTH_SHORT).show();
        //                } else {
        //                    Intent i = new Intent(SigninActivity.this, MainActivity.class);
        //                    startActivity(i);
        //                }
        //            }
        //        }
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