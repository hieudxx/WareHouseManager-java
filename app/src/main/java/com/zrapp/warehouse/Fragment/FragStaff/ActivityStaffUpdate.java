package com.zrapp.warehouse.Fragment.FragStaff;


import static com.zrapp.warehouse.Fragment.FragStaff.FragStaffAdd.storage;
import static com.zrapp.warehouse.Fragment.FragStaff.FragStaffAdd.storageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zrapp.warehouse.DAO.StaffDAO;
import com.zrapp.warehouse.R;
import com.zrapp.warehouse.databinding.ActivityStaffUpdateBinding;
import com.zrapp.warehouse.model.Staff;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class ActivityStaffUpdate extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    ActivityStaffUpdateBinding bindingActivityStaff;
    List<Staff> listUpdate;
    StaffDAO dao;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 10;
    String temp2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingActivityStaff = ActivityStaffUpdateBinding.inflate(getLayoutInflater());
        setContentView(bindingActivityStaff.getRoot());

        storage = FirebaseStorage.getInstance("gs://warehouse-a4e2b.appspot.com");
        storageReference = storage.getReference();

        dao = new StaffDAO();
        listUpdate = dao.getAll();

        bindingActivityStaff.toolbarUpdateStaff.setTitle("Cập nhật nhân viên");
        bindingActivityStaff.toolbarUpdateStaff.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        bindingActivityStaff.edNameUpdatenv.setText(listUpdate.get(FragStaffList.stt).getName());
        bindingActivityStaff.edUsernameUpdatenv.setText(listUpdate.get(FragStaffList.stt).getUsername());
        bindingActivityStaff.edSdtUpdatenv.setText(listUpdate.get(FragStaffList.stt).getTel());
        bindingActivityStaff.edPositionUpdate.setText(listUpdate.get(FragStaffList.stt).getPost());

        bindingActivityStaff.btnUpdateImgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        bindingActivityStaff.btnUpdateNv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStaff();
            }
        });

        bindingActivityStaff.dropdownPositionUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });
    }

    private void updateStaff() {
        String edNameNV = bindingActivityStaff.edNameUpdatenv.getText().toString();
        String edUsernameNV = bindingActivityStaff.edUsernameUpdatenv.getText().toString();
        String edSdtNV = bindingActivityStaff.edSdtUpdatenv.getText().toString();
        String edPostNV = bindingActivityStaff.edPositionUpdate.getText().toString();
        String maNv = listUpdate.get(FragStaffList.stt).getId();

        String regexSdt = "^\\d{10}$";

        if (edNameNV.equals("")|| edUsernameNV.equals("")) {
            bindingActivityStaff.edNameUpdatenv.setHint("Vui lòng không để trống!");
            bindingActivityStaff.edUsernameUpdatenv.setHint("Vui lòng không để trống!");
            bindingActivityStaff.edPositionUpdate.setHint("Vui lòng không để trống!");
            return;
        }

        if (edSdtNV.length() >= 1 && edSdtNV.length() < 10) {
            bindingActivityStaff.edSdtUpdatenv.setText("");
            bindingActivityStaff.edSdtUpdatenv.setHint("Vui lòng nhập đầy đủ 10 số!");
            return;
        }

        if (edSdtNV.length() > 0 && edSdtNV.matches(regexSdt) == false) {
            bindingActivityStaff.edSdtUpdatenv.setText("");
            bindingActivityStaff.edSdtUpdatenv.setHint("Không đúng định dạng số điện thoại!");
            return;
        }

        Staff staffUpdate = new Staff();
        staffUpdate.setName(edNameNV);
        staffUpdate.setUsername(edUsernameNV);
        staffUpdate.setTel(edSdtNV);
        staffUpdate.setId(maNv);
        staffUpdate.setPost(edPostNV);

        if (temp2.equals("")){
            temp2 = listUpdate.get(FragStaffList.stt).getImg();
            staffUpdate.setImg(temp2);
            temp2 = "";
        }else {
            staffUpdate.setImg(temp2);
            temp2 = "";
        }

        dao.updateRow(staffUpdate);
        Toast.makeText(getApplicationContext(), "Sửa thành công!", Toast.LENGTH_SHORT).show();
        FragStaffList.flag = false;
        finish();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage(){
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            temp2 = task.getResult().toString();// lấy url để lưu vào
                            // database
                            Log.d("TAG Kiem Tra: ", "temp2: "+temp2);
                        }
                    });
                    Toast.makeText(ActivityStaffUpdate.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ActivityStaffUpdate.this,
                            "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() /
                            taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bindingActivityStaff.imgUpdateAvt.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popup.setGravity(Gravity.RIGHT);
        }
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.menu_position);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.quan_ly:
                bindingActivityStaff.edPositionUpdate.setText("Quản lý");
                return true;
            case R.id.nhan_vien:
                bindingActivityStaff.edPositionUpdate.setText("Nhân viên");
                return true;
            default:
                return false;
        }
    }
}