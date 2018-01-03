package com.example.blackpearl.foldscope.Adapter;
/**
 * Created by BAPS on 21-03-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blackpearl.foldscope.Activity.ViewBlog;
import com.example.blackpearl.foldscope.Model.MyExperimentData;
import com.example.blackpearl.foldscope.R;
import com.example.blackpearl.foldscope.Utils.Const;
import com.example.blackpearl.foldscope.Utils.SharedPreference;
import com.example.blackpearl.foldscope.Utils.Utility;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class MyExperimentAdapter extends RecyclerView.Adapter<MyExperimentAdapter.MyViewHolder> {

    Context caller;
    ArrayList<MyExperimentData> myExperimentDataArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView Tv_Title, Tv_Desc, Tv_Date, Tv_Preview;
        private ImageView Img_Event;

        public MyViewHolder(View view) {

            super(view);
            Tv_Title = (TextView) view.findViewById(R.id.tv_ad_title);
            Tv_Desc = (TextView) view.findViewById(R.id.tv_ad_disc);
            Tv_Date = (TextView) view.findViewById(R.id.tv_date);
            Img_Event = (ImageView) view.findViewById(R.id.img_ad_story);
            Tv_Preview = (TextView) view.findViewById(R.id.txtv_preview);
        }
    }

    public MyExperimentAdapter(Context context, ArrayList<MyExperimentData> myExperimentDataArrayList) {

        this.caller = context;
        this.myExperimentDataArrayList = myExperimentDataArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_my_experiement, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if (myExperimentDataArrayList.get(position).getDescription().trim().length() == 0) {
            holder.Tv_Desc.setVisibility(View.GONE);
        } else {
            holder.Tv_Desc.setText(myExperimentDataArrayList.get(position).getDescription());
        }
        holder.Tv_Title.setText(myExperimentDataArrayList.get(position).getTitle());
//        holder.Tv_Desc.setText(myExperimentDataArrayList.get(position).getDescription());
        holder.Tv_Date.setText(myExperimentDataArrayList.get(position).getTime());
        holder.Tv_Preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(caller, ViewBlog.class);
                intent.putExtra("Link", myExperimentDataArrayList.get(position).getLink());
                intent.putExtra("Name", SharedPreference.getString(Const.PREF_USERNAME));
                intent.putExtra("Time", myExperimentDataArrayList.get(position).getTime());
                intent.putExtra("Title", myExperimentDataArrayList.get(position).getTitle());
                intent.putExtra("Pic", SharedPreference.getString(Const.PREF_PROFILE_PIC));
                intent.putExtra("Desc",myExperimentDataArrayList.get(position).getAllDescription());
                caller.startActivity(intent);
            }
        });

        if (Utility.isOnline(caller)) {
            Picasso.with(caller)
                    .load(myExperimentDataArrayList.get(position).getEventPic()).centerCrop().fit()
                    .placeholder(R.drawable.test)
                    .error(R.drawable.test)
                    .into(holder.Img_Event);
        } else {
            Uri uri = Uri.fromFile(new File(myExperimentDataArrayList.get(position).getEventPic()));
            Picasso.with(caller)
                    .load(uri).centerCrop().fit()
                    .placeholder(R.drawable.test)
                    .error(R.drawable.test)
                    .into(holder.Img_Event);
        }
    }

    @Override
    public int getItemCount() {

        return myExperimentDataArrayList.size();
    }
}


