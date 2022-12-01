package com.example.cse371;


import java.io.Serializable;
import java.util.ArrayList;

public class Gym implements Serializable {
    private String docid;
    private String name;
    private String time;
    private String place;
    private String joined;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    public String removeJoined(String name){
        String[] names = joined.split(";");
        String buffer = "";
        for (int i = 0; i < names.length; i++) {
            String n = names[i];
            if (n.equals(name)){
                continue;
            }
            if (buffer.length()<=0){
                buffer += n;
            }else {
                buffer += ";"+n;
            }
        }
        return buffer;
    }

    public ArrayList<String> getJoinedList(){
        ArrayList<String> joinedlist = new ArrayList<>();
        String[] names = joined.split(";");
        String buffer = "";
        for (int i = 0; i < names.length; i++) {
            String n = names[i];
            if (n.indexOf("@")<0){
                continue;
            }
            joinedlist.add(n);
        }
        return joinedlist;
    }

    public boolean isJoined(String name){
        String[] names = joined.split(";");
        for (int i = 0; i < names.length; i++) {
            String n = names[i];
            if (n.equals(name)){
                return true;
            }
        }
        return false;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }
}
