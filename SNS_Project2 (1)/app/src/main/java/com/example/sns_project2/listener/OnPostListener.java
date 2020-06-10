package com.example.sns_project2.listener;

import com.example.sns_project2.PostInfo;

public interface OnPostListener {
    void onDelete(PostInfo postInfo);
    void onModify();
}
