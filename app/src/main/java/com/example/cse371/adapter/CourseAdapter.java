package com.example.cse371.adapter;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cse371.CourseActivity;
import com.example.cse371.LoginActivity;
import com.example.cse371.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private ArrayList<String> mTitiles;
    private LayoutInflater mInflater;
    private Context mContext;
    String filepath;
    long downloadID;
    ProgressDialog progressDialog;

    public CourseAdapter(Context context, ArrayList<String> titles) {
        this.mInflater = LayoutInflater.from(context);
        this.mTitiles = titles;
        this.mContext = context;
        mContext.registerReceiver(onDownloadComplete, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.course_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = mTitiles.get(position);
        holder.title.setText(title);
        holder.titlestr = title;
    }

    @Override
    public int getItemCount() {
        return mTitiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        String titlestr;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    downloadPdf(titlestr+".pdf");
                }
            });
        }
    }

    private void downloadPdf(String filename){
        progressDialog = ProgressDialog.show(mContext,"","Loading",true,true);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReferenceProfilePic = firebaseStorage.getReference();
        storageReferenceProfilePic.child(filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                DownloadManager downloadmanager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setTitle(filename);
                request.setDescription("downloading");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setVisibleInDownloadsUi(false);
                //uri2 = Uri.parse("file://Downloads/"+filename);
                Random r = new Random();
                String name = r.nextInt(100000)+".pdf";
                File file = new File(Environment.DIRECTORY_DOWNLOADS,name);
                filepath = file.getAbsolutePath();
                System.out.println(filepath);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,name);
                downloadID = downloadmanager.enqueue(request);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressDialog.dismiss();
                exception.printStackTrace();
            }
        });
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            progressDialog.dismiss();
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadID == id) {
                Intent target = new Intent(Intent.ACTION_VIEW);
                System.out.println(Uri.parse(filepath));
                target.setDataAndType(Uri.parse(filepath),"application/pdf");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                intent = Intent.createChooser(target, "Open File");
                try {
                    mContext.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                }
            }
        }
    };
}