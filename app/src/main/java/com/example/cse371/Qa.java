package com.example.cse371;


import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.ArrayList;

public class Qa implements Serializable {
    private String name;
    private String qa;
    private String qa_author;
    private String docid;
    private ArrayList<QaReply> replies;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQa() {
        return qa;
    }

    public void setQa(String qa) {
        this.qa = qa;
    }

    public String getQa_author() {
        return qa_author;
    }

    public void setQa_author(String qa_author) {
        this.qa_author = qa_author;
    }

    public ArrayList<QaReply> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<QaReply> replies) {
        this.replies = replies;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }
}
