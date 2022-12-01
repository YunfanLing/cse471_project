package com.example.cse371;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse371.adapter.UserAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText editText = findViewById(R.id.et);

        UserAdapter userAdapter = new UserAdapter(new ArrayList<>());
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        recyclerView.setAdapter(userAdapter);


        findViewById(R.id.bt_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 查询
                String searchKey = editText.getText().toString().trim();
                if(TextUtils.isEmpty(searchKey)) {
                    ToastUtils.showShortToast(getApplicationContext(),"content can not be empty");
                } else{
                    // 请求接口，获取所有的用户信息
                    ProgressDialog progressDialog = ProgressDialog.show(SearchActivity.this,"","Loading");
                    firebaseFirestore.collection("Users").whereNotEqualTo("email", "").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            // 如果有数据
                            if (queryDocumentSnapshots.size() > 0) {
                                List<User> userList = new ArrayList<>();
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    String name = documentSnapshot.getString("name");
                                    String email = documentSnapshot.getString("email");
                                    if(null != name && name.contains(searchKey)) {
                                        userList.add(new User(email,name));
                                    }
                                }
                                // 把数据塞到列表中
                                if(Utils.isNotEmpty(userList)) {
                                    userAdapter.setNewInstance(userList);
                                } else {
                                    userAdapter.setNewInstance(new ArrayList<>());
                                    ToastUtils.showShortToast(getApplicationContext(),"no data");
                                }
                                Log.e("why", "queryDocumentSnapshots.size()" + queryDocumentSnapshots.size());
                            } else {
                                Log.e("why", "queryDocumentSnapshots.size()" + 0);
                            }
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("why", "Failed to get data" + e.getMessage());
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }
}