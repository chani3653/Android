package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class PatientListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mLayoutLnflator;
    ArrayList<PatientListData> Patient;

    public PatientListAdapter(Context context, ArrayList<PatientListData> data) {
        mContext = context;
        Patient = data;
        mLayoutLnflator = LayoutInflater.from(mContext);

    }

    @Override
    public int getCount() {
        return Patient.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public PatientListData getItem(int position) {
        return Patient.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = mLayoutLnflator.inflate(R.layout.listview_layout, null);

        TextView Pname = (TextView)view.findViewById(R.id.LLPatientName);
        TextView Pnum = (TextView)view.findViewById(R.id.LLPatientNum);
        TextView Cname = (TextView)view.findViewById(R.id.LLPatientDoctor);
        TextView Date = (TextView)view.findViewById(R.id.LLPatientDate);
        TextView Lisk = (TextView)view.findViewById(R.id.LLRiskScore);

        Pname.setText(Patient.get(position).getPname());
        Pnum.setText(Patient.get(position).getPnum());
        Cname.setText(Patient.get(position).getCname());
        Date.setText(Patient.get(position).getDate());
        Lisk.setText(Patient.get(position).getScore());

        return view;
    }
}
