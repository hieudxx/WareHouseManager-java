package com.zrapp.warehouse.Fragment.FragStaff;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.zrapp.warehouse.DAO.StaffDAO;
import com.zrapp.warehouse.R;
import com.zrapp.warehouse.databinding.FragStaffAddBinding;
import com.zrapp.warehouse.model.Staff;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


public class FragStaffAdd extends Fragment implements PopupMenu.OnMenuItemClickListener {
    FragStaffAddBinding binding;
    StaffDAO dao;
    List<Staff> listnv;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 10;
    public static final int CAMERA_PIC_REQUEST = 22;
    public static FirebaseStorage storage;
    public static StorageReference storageReference;
    String temp;

    public FragStaffAdd() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragStaffAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dao = new StaffDAO();
        listnv = dao.getAll();

        storage = FirebaseStorage.getInstance("gs://warehouse-a4e2b.appspot.com");
        storageReference = storage.getReference();

        binding.dropdownPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        //Thêm nhân viên
        binding.btnXacnhanNv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNhanVien();
            }
        });

        binding.btnAddImgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        binding.btnCaptureImgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, CAMERA_PIC_REQUEST);
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage(Uri filePath) {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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
                            temp = task.getResult().toString();// lấy url để lưu vào
                            // database
                        }
                    });
                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(),
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

    //    Thêm nhân viên
    private void addNhanVien() {
                Log.d("TAG Kiem Tra: ", "onComplete: " + temp);
                Log.d("TAG Kiem Tra: ", "addNhanVien: " + FragStaffList.stt);
                String edNameNV = binding.edNameFragthemnv.getText().toString();
                String edUsernameNV = binding.edUsernameFragthemnv.getText().toString();
                String edPasswordNV = binding.edPasswordFragthemnv.getText().toString();
                String edSdtNV = binding.edSdtFragthemnv.getText().toString();
                String edPostNV = binding.edPosition.getText().toString();
                String regexSdt = "^\\d{10}$";

                if (edNameNV.equals("")
                        || edUsernameNV.equals("")
                        || edPasswordNV.equals("")) {
                    binding.edNameFragthemnv.setHint("Vui lòng không để trống!");
                    binding.edUsernameFragthemnv.setHint("Vui lòng không để trống!");
                    binding.edPasswordFragthemnv.setHint("Vui lòng không để trống!");
                    binding.edPosition.setHint("Vui lòng không để trống!");
                    return;
                }

                if (edSdtNV.length() >= 1 && edSdtNV.length() < 10) {
                    binding.edSdtFragthemnv.setText("");
                    binding.edSdtFragthemnv.setHint("Vui lòng nhập đầy đủ 10 số!");
                    return;
                }

                if (edSdtNV.length() > 0 && edSdtNV.matches(regexSdt) == false) {
                    binding.edSdtFragthemnv.setText("");
                    binding.edSdtFragthemnv.setHint("Không đúng định dạng số điện thoại!");
                    return;
                }

                Staff staff = new Staff();
                staff.setName(edNameNV);
                staff.setUsername(edUsernameNV);
                staff.setPass(edPasswordNV);
                staff.setTel(edSdtNV);
                staff.setPost(edPostNV);
                staff.setImg(temp);

                for (int i = 0; i < listnv.size(); i++) {
                    if (edUsernameNV.equals(listnv.get(i).getUsername())) {

                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                        mBuilder.setMessage("Tên tài khoản đã tồn tại!");
                        mBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        mBuilder.show();
                        return;
                    }
                }
                dao.insertStaff(staff, 1);
                loadFrag(new FragStaffList());
                Toast.makeText(getActivity(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
                FragStaffList.flag = false;
    }

    public void loadFrag(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popup.setGravity(Gravity.RIGHT);
        }
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_position);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.quan_ly:
                binding.edPosition.setText("Quản lý");
                return true;
            case R.id.nhan_vien:
                binding.edPosition.setText("Nhân viên");
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                binding.imgAddAvt.setImageBitmap(bitmap);
                uploadImage(getImgUri(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (resultCode == RESULT_OK && requestCode == CAMERA_PIC_REQUEST && data != null) {
            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                binding.imgAddAvt.setImageBitmap(bitmap);
                uploadImage(getImgUri(bitmap));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Uri getImgUri(Bitmap bit){
        ByteArrayOutputStream bytew = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.PNG,100,bytew);
        String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),bit,"title",null);
        return  Uri.parse(path);
    }
}
