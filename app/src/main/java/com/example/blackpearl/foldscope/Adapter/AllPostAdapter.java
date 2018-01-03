package com.example.blackpearl.foldscope.Adapter;

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
import com.example.blackpearl.foldscope.Model.AllPostData;
import com.example.blackpearl.foldscope.R;
import com.example.blackpearl.foldscope.Utils.Utility;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class AllPostAdapter extends RecyclerView.Adapter<AllPostAdapter.MyViewHolder> {

    Context caller;
    public ArrayList<AllPostData> allPostDataArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView Tv_Title, Tv_Disc, Tv_Name, Tv_Time, Tv_View;
        public ImageView Img_Event, Img_Profile;

        public MyViewHolder(View view) {

            super(view);
            Tv_Title = (TextView) view.findViewById(R.id.tv_ad_title);
            Tv_Disc = (TextView) view.findViewById(R.id.tv_ad_disc);
            Tv_Name = (TextView) view.findViewById(R.id.tv_ad_name);
            Tv_Time = (TextView) view.findViewById(R.id.tv_ad_time);
            Tv_View = (TextView) view.findViewById(R.id.tv_ad_views);
            Img_Event = (ImageView) view.findViewById(R.id.img_ad_story);
            Img_Profile = (ImageView) view.findViewById(R.id.img_ad_profile);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
            }
        }
    }

    public AllPostAdapter(Context context, ArrayList<AllPostData> allPostDataArrayList) {

        this.caller = context;
        this.allPostDataArrayList = allPostDataArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_all_post, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (allPostDataArrayList.get(position).getDescription().trim().length()==0) {
            holder.Tv_Disc.setVisibility(View.GONE);
        } else {
            holder.Tv_Disc.setText(Html.fromHtml(allPostDataArrayList.get(position).getDescription()));
        }
        holder.Tv_Title.setText(allPostDataArrayList.get(position).getTitle());
        holder.Tv_View.setText("");
        holder.Tv_Time.setText(allPostDataArrayList.get(position).getTime());
        holder.Tv_Name.setText(allPostDataArrayList.get(position).getName());
        holder.Img_Event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(caller, ViewBlog.class);
                intent.putExtra("Link", allPostDataArrayList.get(position).getLink());
                intent.putExtra("Name", allPostDataArrayList.get(position).getName());
                intent.putExtra("Time", allPostDataArrayList.get(position).getTime());
                intent.putExtra("Title", allPostDataArrayList.get(position).getTitle());
                intent.putExtra("Pic", allPostDataArrayList.get(position).getProfilePic());
                intent.putExtra("Desc",allPostDataArrayList.get(position).getAllDescription());
                caller.startActivity(intent);
            }
        });
        holder.Tv_Title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(caller, ViewBlog.class);
                intent.putExtra("Link", allPostDataArrayList.get(position).getLink());
                intent.putExtra("Name", allPostDataArrayList.get(position).getName());
                intent.putExtra("Time", allPostDataArrayList.get(position).getTime());
                intent.putExtra("Title", allPostDataArrayList.get(position).getTitle());
                intent.putExtra("Pic", allPostDataArrayList.get(position).getProfilePic());
                intent.putExtra("Desc",allPostDataArrayList.get(position).getAllDescription());
                caller.startActivity(intent);
            }
        });
        holder.Tv_Disc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(caller, ViewBlog.class);
                intent.putExtra("Link", allPostDataArrayList.get(position).getLink());
                intent.putExtra("Name", allPostDataArrayList.get(position).getName());
                intent.putExtra("Time", allPostDataArrayList.get(position).getTime());
                intent.putExtra("Title", allPostDataArrayList.get(position).getTitle());
                intent.putExtra("Pic", allPostDataArrayList.get(position).getProfilePic());
                intent.putExtra("Desc",allPostDataArrayList.get(position).getAllDescription());
                caller.startActivity(intent);
            }
        });
        if (Utility.isOnline(caller)) {
            Picasso.with(caller)
                    .load(allPostDataArrayList.get(position).getEventPic()).centerCrop().fit()
                    .placeholder(R.drawable.test)
                    .error(R.drawable.test)
                    .into(holder.Img_Event);
            String Pic = "http:" + allPostDataArrayList.get(position).getProfilePic();
            Picasso.with(caller)
                    .load(Pic).centerCrop().fit()
                    .placeholder(R.drawable.test)
                    .error(R.drawable.test)
                    .into(holder.Img_Profile);
        } else {
            Uri uri = Uri.fromFile(new File(allPostDataArrayList.get(position).getEventPic()));
            Uri uri1 = Uri.fromFile(new File(allPostDataArrayList.get(position).getProfilePic()));
            Picasso.with(caller)
                    .load(uri)
                    .placeholder(R.drawable.test)
                    .error(R.drawable.test)
                    .into(holder.Img_Event);
            Picasso.with(caller)
                    .load(uri1)
                    .placeholder(R.drawable.test)
                    .error(R.drawable.test)
                    .into(holder.Img_Profile);
        }
    }

    @Override
    public int getItemCount() {

        return allPostDataArrayList.size();
    }
}


