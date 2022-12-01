package com.example.cse371;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Random;


public class RestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        Bundle b = getIntent().getExtras();
        String name = "";
        if(b != null) {
            name = b.getString("name");
        }
        String finalName = name;
        findViewById(R.id.open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1001);
                 **/
                ProgressDialog progressDialog = ProgressDialog.show(RestaurantActivity.this,"","Loading",true,true);
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("restaurant").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists() && document.get("name").equals(finalName)) {
                                    boolean isOpen = isInTime(String.valueOf(document.get("open_hour")));
                                    String message = "";
                                    if (isOpen){
                                        message = "Now is Open hour";
                                    }else{
                                        message = "Closed. Open hour is "+String.valueOf(document.get("open_hour"));
                                    }
                                    new AlertDialog.Builder(RestaurantActivity.this)
                                            .setTitle("Tip")
                                            .setMessage(message)
                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Continue with delete operation
                                                }
                                            })
                                            .show();
                                }
                            }
                        }
                    }
                });
            }
        });

        findViewById(R.id.review).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ReviewActivity.class);
                Bundle b = new Bundle();
                b.putString("name", finalName);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = ProgressDialog.show(RestaurantActivity.this,"","Loading",true,true);
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("restaurant").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists() && document.get("name").equals(finalName)) {
                                    String menuid = String.valueOf(document.get("menu_id"));
                                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                    StorageReference storageReferenceProfilePic = firebaseStorage.getReference();

                                    storageReferenceProfilePic.child(menuid+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            progressDialog.dismiss();
                                            Intent intent = new Intent();
                                            intent.setAction(android.content.Intent.ACTION_VIEW);
                                            intent.setDataAndType(uri,"image/*");
                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            progressDialog.dismiss();
                                        }
                                    });
                                    break;
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    public static boolean isInTime(String aTime){
        String atime = aTime;
        int index = atime.indexOf(" ");
        String week = atime.substring(0,index);
        int minweek = 0;
        System.out.println(week);
        if (week.indexOf("Sunday")==0){
            minweek = 1;
        }
        if (week.indexOf("Monday")==0){
            minweek = 2;
        }
        if (week.indexOf("Tuesday")==0){
            minweek = 3;
        }
        if (week.indexOf("Wednesday")==0){
            minweek = 4;
        }
        if (week.indexOf("Thursday")==0){
            minweek = 5;
        }
        if (week.indexOf("Friday")==0){
            minweek = 6;
        }
        if (week.indexOf("Saturday")==0){
            minweek = 7;
        }

        int maxweek = 0;
        if (week.indexOf("Sunday")>0){
            maxweek = 1;
        }
        if (week.indexOf("Monday")>0){
            maxweek = 2;
        }
        if (week.indexOf("Tuesday")>0){
            maxweek = 3;
        }
        if (week.indexOf("Wednesday")>0){
            maxweek = 4;
        }
        if (week.indexOf("Thursday")>0){
            maxweek = 5;
        }
        if (week.indexOf("Friday")>0){
            maxweek = 6;
        }
        if (week.indexOf("Saturday")>0){
            maxweek = 7;
        }
        System.out.println(minweek);
        System.out.println(maxweek);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day<minweek || day>maxweek){
            return false;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            //uploadMethod(data.getData());
        }
    }


}