package com.zrapp.warehouse.Fragment.FragProd;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.zrapp.warehouse.DAO.ProductDAO;
import com.zrapp.warehouse.MainActivity;
import com.zrapp.warehouse.databinding.FragProdAddBinding;
import com.zrapp.warehouse.model.Product;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FragProdAdd extends Fragment {
    FragProdAddBinding binding;
    ProductDAO dao;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    FirebaseStorage storage;
    StorageReference storageReference;
    String temp;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragProdAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dao = new ProductDAO();

        storage = FirebaseStorage.getInstance("gs://warehouse-a4e2b.appspot.com");
        storageReference = storage.getReference();


        binding.btnUpImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        binding.btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeaphoto();
            }
        });

        binding.btnAddProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tenSP = binding.edNameProd.getText().toString();
                String viTri = binding.edViTriProd.getText().toString();
                String giaBan = binding.edPriceProd.getText().toString();
                String giaVon = binding.edCostPriceProd.getText().toString();

                if (tenSP.isEmpty()) {
                    binding.edNameProd.setHint("Vui lòng không để trống!");
                    binding.edNameProd.setHintTextColor(Color.parseColor("#E53935"));
                } else if (viTri.isEmpty()) {
                    binding.edViTriProd.setHint("Vui lòng không để trống!");
                    binding.edViTriProd.setHintTextColor(Color.parseColor("#E53935"));
                } else if (giaBan.isEmpty()) {
                    binding.edPriceProd.setHint("Vui lòng không để trống!");
                    binding.edPriceProd.setHintTextColor(Color.parseColor("#E53935"));
                } else {
                    Product model = new Product();
                    model.setName(tenSP);
                    model.setViTri(viTri);
                    model.setPrice(Integer.valueOf(giaBan));
                    if (giaVon.isEmpty()){
                        model.setCost_price(Integer.valueOf(0));
                    } else {
                        model.setCost_price(Integer.valueOf(giaVon));
                    }
                        model.setImg(temp);
                        dao.insertProd(model);
                        MainActivity.loadFrag(new FragProdList());
                    }

            }
        });

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void takeaphoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
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
                            temp = task.getResult().toString();// lấy url để lưu vào database
//                            Picasso.get().load(temp).into(binding.imgAdd); //demo load ảnh
                        }
                    });
                    Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                binding.imgAdd.setImageBitmap(bitmap);
                uploadImage(getImgUri(bitmap));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                binding.imgAdd.setImageBitmap(bitmap);
                uploadImage(filePath);
            } catch (IOException e) {
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
