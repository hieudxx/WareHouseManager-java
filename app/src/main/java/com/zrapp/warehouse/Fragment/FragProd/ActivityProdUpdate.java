package com.zrapp.warehouse.Fragment.FragProd;

import static com.zrapp.warehouse.SigninActivity.account;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.zrapp.warehouse.ChangePassActivity;
import com.zrapp.warehouse.DAO.ProductDAO;
import com.zrapp.warehouse.MainActivity;
import com.zrapp.warehouse.RequestActivity;
import com.zrapp.warehouse.SigninActivity;
import com.zrapp.warehouse.databinding.ActivityProdUpdateBinding;
import com.zrapp.warehouse.model.Product;
import com.zrapp.warehouse.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class ActivityProdUpdate extends AppCompatActivity {
    ActivityProdUpdateBinding binding;
    List<Product> listP;

    ProductDAO dao_prod;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    FirebaseStorage storage;
    StorageReference storageReference;
    String temp;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProdUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storage = FirebaseStorage.getInstance("gs://warehouse-a4e2b.appspot.com");
        storageReference = storage.getReference();

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        Bundle bundle = getIntent().getExtras();
        int index = bundle.getInt("pos");

        dao_prod = new ProductDAO();
        listP = dao_prod.getAll_Prod();

        binding.edNameUD.setText(listP.get(index).getName());
        binding.edViTriUD.setText(listP.get(index).getViTri());
        binding.edPriceUD.setText(listP.get(index).getPrice() + "");
        binding.edCostPriceUD.setText(listP.get(index).getCost_price() + "");
        Picasso.get().load(listP.get(index).getImg()).into(binding.imgUpdate);

        binding.btnAddImgUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeaphoto();
            }
        });

        binding.btnUpImgUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        binding.btnUpdateProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idSp = listP.get(index).getId();
                String nameSp = binding.edNameUD.getText().toString();
                String locationSp = binding.edViTriUD.getText().toString();
                int priceSp = Integer.valueOf(binding.edPriceUD.getText().toString());
                int costpriceSp = Integer.valueOf(binding.edCostPriceUD.getText().toString());
                String img = temp;

                Product prod_update = new Product();
                prod_update.setId(idSp);
                prod_update.setName(nameSp);
                prod_update.setViTri(locationSp);
                prod_update.setPrice(priceSp);
                prod_update.setCost_price(costpriceSp);
                prod_update.setImg(img);

                dao_prod.updateProd(prod_update);
                Toast.makeText(ActivityProdUpdate.this, "Cập nhật sản phẩm thành công!!!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ActivityProdUpdate.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityProdUpdate.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityProdUpdate.this);
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
                            temp = task.getResult().toString();// lấy url để lưu vào database
                        }
                    });
                    Toast.makeText(ActivityProdUpdate.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ActivityProdUpdate.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                binding.imgUpdate.setImageBitmap(bitmap);
                uploadImage(getImgUri(bitmap));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                binding.imgUpdate.setImageBitmap(bitmap);
                uploadImage(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Uri getImgUri(Bitmap bit){
        ByteArrayOutputStream bytew = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.PNG,100,bytew);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(),bit,"title",null);
        return  Uri.parse(path);
    }
}