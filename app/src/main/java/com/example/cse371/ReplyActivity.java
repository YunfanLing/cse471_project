package com.example.cse371;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse371.adapter.QaAdapter;
import com.example.cse371.adapter.ReplyAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class ReplyActivity extends AppCompatActivity {
    Qa qa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            qa = (Qa) b.getSerializable("qa");
        }

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("qa").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists() && document.get("qa").equals(qa.getQa())) {
                            Qa qa = new Qa();
                            qa.setDocid(String.valueOf(document.getReference().getId()));
                            qa.setName(String.valueOf(document.get("name")));
                            qa.setQa(String.valueOf(document.get("qa")));
                            qa.setQa_author(String.valueOf(document.get("qa_author")));
                            String replies = String.valueOf(document.get("replies"));
                            String[] s = replies.split("###");
                            ArrayList<QaReply> qaReplies = new ArrayList<>();
                            for (int i = 0; i < s.length; i++) {
                                String content = s[i];
                                String[] c = content.split("@@@");
                                if (c.length==2){
                                    QaReply qr = new QaReply();
                                    qr.setName(c[0]);
                                    qr.setContent(c[1]);
                                    qaReplies.add(qr);
                                }
                            }
                            qa.setReplies(qaReplies);
                            ReplyActivity.this.qa = qa;

                            TextView qatv = findViewById(R.id.qa);
                            qatv.setText(qa.getQa());


                            TextView authortv = findViewById(R.id.author);
                            authortv.setText(qa.getQa_author());

                            RecyclerView listview = findViewById(R.id.recyclerView);
                            listview.setLayoutManager(new LinearLayoutManager(ReplyActivity.this));
                            listview.addItemDecoration(new DividerItemDecoration(ReplyActivity.this,LinearLayoutManager.VERTICAL));
                            ReplyAdapter adapter = new ReplyAdapter(ReplyActivity.this,qa.getReplies());
                            listview.setAdapter(adapter);
                        }
                    }
                }
            }
        });

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ReplyActivity.this);
                final EditText edittext = new EditText(ReplyActivity.this);
                alert.setMessage("");
                alert.setTitle("Reply");

                alert.setView(edittext);
                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String content = edittext.getText().toString();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        ArrayList<QaReply> qaReplies = qa.getReplies();
                        String replybuffer = "";
                        for (int i = 0; i < qaReplies.size(); i++) {
                            QaReply qaReply = qaReplies.get(i);
                            if (replybuffer.length()<=0){
                                replybuffer += qaReply.getName()+"@@@"+qaReply.getContent();
                            }else{
                                replybuffer += "###"+qaReply.getName()+"@@@"+qaReply.getContent();
                            }
                        }
                        if (replybuffer.length()<=0){
                            replybuffer += LoginActivity.username+"@@@"+content;
                        }else{
                            replybuffer += "###"+LoginActivity.username+"@@@"+content;
                        }
                        db.collection("qa").document(qa.getDocid()).update("replies",replybuffer);
                        Toast.makeText(ReplyActivity.this, "Reply success!",
                                Toast.LENGTH_LONG).show();
                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                        firebaseFirestore.collection("qa").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.exists() && document.get("qa").equals(qa.getQa())) {
                                            Qa qa = new Qa();
                                            qa.setDocid(String.valueOf(document.getReference().getId()));
                                            qa.setName(String.valueOf(document.get("name")));
                                            qa.setQa(String.valueOf(document.get("qa")));
                                            qa.setQa_author(String.valueOf(document.get("qa_author")));
                                            String replies = String.valueOf(document.get("replies"));
                                            String[] s = replies.split("###");
                                            ArrayList<QaReply> qaReplies = new ArrayList<>();
                                            for (int i = 0; i < s.length; i++) {
                                                String content = s[i];
                                                String[] c = content.split("@@@");
                                                if (c.length==2){
                                                    QaReply qr = new QaReply();
                                                    qr.setName(c[0]);
                                                    qr.setContent(c[1]);
                                                    qaReplies.add(qr);
                                                }
                                            }
                                            qa.setReplies(qaReplies);
                                            ReplyActivity.this.qa = qa;

                                            TextView qatv = findViewById(R.id.qa);
                                            qatv.setText(qa.getQa());


                                            TextView authortv = findViewById(R.id.author);
                                            authortv.setText(qa.getQa_author());

                                            RecyclerView listview = findViewById(R.id.recyclerView);
                                            listview.setLayoutManager(new LinearLayoutManager(ReplyActivity.this));
                                            listview.addItemDecoration(new DividerItemDecoration(ReplyActivity.this,LinearLayoutManager.VERTICAL));
                                            ReplyAdapter adapter = new ReplyAdapter(ReplyActivity.this,qa.getReplies());
                                            listview.setAdapter(adapter);
                                        }
                                    }
                                }
                            }
                        });
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();
            }
        });
    }
}