package com.example.cse371;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText user,email, pwd, pwd2;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private String getStr(EditText textInputLayout) {
        if (null == textInputLayout) {
            return "";
        }
        return textInputLayout.getText().toString().trim();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user = findViewById(R.id.et_user);
        email = findViewById(R.id.et_email);
        pwd = findViewById(R.id.et_password);
        pwd2 = findViewById(R.id.et_password_2);


        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resAccount = getStr(user);
                String emailStr = getStr(email);
                String resPassword = getStr(pwd);
                String resPassword2 = getStr(pwd2);
                if (TextUtils.isEmpty(resAccount)
                        || TextUtils.isEmpty(emailStr)
                        || TextUtils.isEmpty(resPassword)
                        || TextUtils.isEmpty(resPassword2)) {
                    ToastUtils.showShortToast(getApplicationContext(), "username/password can not be empty");
                } else if (!TextUtils.equals(resPassword, resPassword2)) {
                    ToastUtils.showShortToast(getApplicationContext(), "password and confirm password must be same");
                } else if (resPassword.length() < 6 ||  resPassword2.length() <6) {
                    ToastUtils.showShortToast(getApplicationContext(), "Password should be at least 6 characters");
                } else {
                    doRegister(resAccount,emailStr, resPassword);
                }
            }
        });
    }

    public void doRegister(String resAccount,String emailStr, String password) {
        ProgressDialog progressDialog = ProgressDialog.show(RegisterActivity.this, "", "Loading",true,true);
        Log.d("why", "fetchSignInMethodsForEmail ");
        firebaseAuth.fetchSignInMethodsForEmail(emailStr).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task2) {
                if (task2.getResult().getSignInMethods().size() != 0) {
                    progressDialog.dismiss();
                    ToastUtils.showShortToast(getApplicationContext(), "account name already exist");
                } else {
                    Log.d("why", "fetchSignInMethodsForEmail 2222222");
                    // 不存在该账户，可以继续进行注册
                    System.out.println(emailStr);
                    System.out.println(password);
                    firebaseAuth.createUserWithEmailAndPassword(emailStr,
                            password).addOnCompleteListener((task) -> {
                        Log.d("why", "fetchSignInMethodsForEmail 333333333");
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            String userId = firebaseAuth.getCurrentUser().getUid();
                            // 根据userId去保存用户信息到新建的Users表里
                            DocumentReference documentReference = firebaseFirestore.collection("Users").document(userId);
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("email", emailStr);
                            userInfo.put("name", resAccount);
                            documentReference.set(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("why", "User successfully created | UserId: " + userId);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("why", "Fails to create user | " + e.toString());
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d("why", "onComplete | ");
                                }
                            });
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            ToastUtils.showShortToast(getApplicationContext(), "register success");
                            finish();
                        } else {
                            Log.d("why", "fetchSignInMethodsForEmail 4444444");
                            progressDialog.dismiss();
                            try {
                                task.getResult();
                            }catch (Exception e) {
                                ToastUtils.showShortToast(getApplicationContext(), "Failed to create account " + e.getMessage());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("why", "onFailure e =" + e.getMessage());
                        }
                    }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("why", "onComplete");
                        }
                    });
                }
            }
        });
    }
}