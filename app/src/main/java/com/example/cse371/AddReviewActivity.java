package com.example.cse371;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class AddReviewActivity extends AppCompatActivity {
    Uri myuri = null;
    String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            name = b.getString("name");
        }

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadMethod(myuri);
            }
        });
        findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(cameraIntent, 1111);
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1111);//one can be replaced with any action code
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111 && resultCode == Activity.RESULT_OK) {
            System.out.println(data.getData());
            // uploadMethod(data.getData());
            //Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            myuri = data.getData();
            /**
            imageview.setImageURI(selectedImage);
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            Random r = new Random();
            int i = r.nextInt(100000);
            File futureStudioIconFile = new File(path, i+".jpg");
            if (futureStudioIconFile.exists())
                futureStudioIconFile.delete();
            try {
                FileOutputStream fos = null;
                fos = new FileOutputStream(futureStudioIconFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            myuri = Uri.fromFile(futureStudioIconFile);
             **/
            //uploadMethod(myuri);
            ImageView image = findViewById(R.id.image);
            image.setImageURI(myuri);
        }
    }

    private void uploadMethod(Uri imageUri) {
        ProgressDialog progressDialog = ProgressDialog.show(AddReviewActivity.this,"","Loading",true,true);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReferenceProfilePic = firebaseStorage.getReference();
        Random r = new Random();
        int i = r.nextInt(100000);
        StorageReference imageRef = storageReferenceProfilePic.child(i+".jpg");

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        EditText editText = findViewById(R.id.reviewcontent);

                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                        Random r = new Random();
                        int i2 = r.nextInt(100000);
                        DocumentReference documentReference = firebaseFirestore.collection("review").document(String.valueOf(i2));
                        Map<String, Object> hm = new HashMap<>();
                        hm.put("content",editText.getText().toString());
                        hm.put("name",name);
                        hm.put("image",i+".jpg");
                        documentReference.set(hm).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                //Toast.makeText(AddReviewActivity.this, "Success", Toast.LENGTH_LONG).show();
                                new AlertDialog.Builder(AddReviewActivity.this)
                                        .setTitle("Tip")
                                        .setMessage("Submit review success!")
                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                AddReviewActivity.this.finish();
                                                Intent intent = new Intent();
                                                Review review = new Review();
                                                review.setImagePath(String.valueOf(i+".jpg"));
                                                review.setName(name);
                                                review.setContent(editText.getText().toString());
                                                intent.putExtra("review",review);
                                                //设置返回数据
                                                setResult(RESULT_OK, intent);
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        Toast.makeText(AddReviewActivity.this, "Fail", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                });

    }
}