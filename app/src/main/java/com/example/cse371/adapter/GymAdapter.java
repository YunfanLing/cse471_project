package com.example.cse371.adapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cse371.Gym;
import com.example.cse371.LoginActivity;
import com.example.cse371.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class GymAdapter extends RecyclerView.Adapter<GymAdapter.ViewHolder> {

    private ArrayList<Gym> mGyms;
    private LayoutInflater mInflater;
    private Context mContext;

    public GymAdapter(Context context, ArrayList<Gym> gyms) {
        this.mInflater = LayoutInflater.from(context);
        this.mGyms = gyms;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.gym_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Gym gym = mGyms.get(position);
        holder.name.setText(gym.getName());
        holder.time.setText(gym.getTime());
        holder.place.setText(gym.getPlace());
        holder.join.setText("");
        holder.gym = gym;
        if (gym.isJoined(LoginActivity.username)){
            holder.join.setText("Joined");
        }
    }

    @Override
    public int getItemCount() {
        return mGyms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView time;
        TextView place;
        TextView join;
        Gym gym;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            place = itemView.findViewById(R.id.place);
            join = itemView.findViewById(R.id.join);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    String message = "Unjoin the gym?";
                    if (!gym.isJoined(LoginActivity.username)){
                        message = "Join the gym?";
                    }
                    ArrayList<String> joinedlist = gym.getJoinedList();
                    String joinedBuffer = "";
                    for (int i = 0; i < joinedlist.size(); i++) {
                        String name = joinedlist.get(i);
                        joinedBuffer += name+"\n";
                    }
                    new AlertDialog.Builder(mContext)
                            .setTitle(message)
                            .setMessage(joinedBuffer)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    if (!gym.isJoined(LoginActivity.username)){
                                        String joinbuf = gym.getJoined()+";"+LoginActivity.username;
                                        db.collection("gym").document(gym.getDocid()).update("joined",joinbuf);
                                        join.setText("Joined");
                                        gym.setJoined(joinbuf);
                                    }else{
                                        String buffer = gym.removeJoined(LoginActivity.username);
                                        db.collection("gym").document(gym.getDocid()).update("joined",buffer);
                                        join.setText("");
                                        gym.setJoined(buffer);
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                }
            });
        }
    }
}