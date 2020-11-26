package com.vamsigutha.rememb;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BannerAdapter extends ArrayAdapter {

    List list = new ArrayList();
    Context context;
    View previous=null;

    int storedSelectedId;


    public BannerAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.COMPLETED_LIST_FILE),Context.MODE_PRIVATE);
        storedSelectedId = sharedPreferences.getInt("selectedBannerId",-1);

    }

    public void clearSelection() {
        storedSelectedId=-1;
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.COMPLETED_LIST_FILE),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("selected banner",27);
        editor.putInt("selectedBannerId",-1);
        editor.apply();
        notifyDataSetChanged();
    }

    static class BannerDataHandler{
        ImageView banner;
     }

    @Override
    public void add(@Nullable Object object) {
        super.add(object);
        this.list.add(object);
//        if(storedSelectedId == getItemId())
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        BannerDataHandler bannerDataHandler;

        if(convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.banner_row_layout,parent,false);

            bannerDataHandler = new BannerDataHandler();
            bannerDataHandler.banner = (ImageView) row.findViewById(R.id.banner_row_item);
            row.setTag(bannerDataHandler);
        }else{
            bannerDataHandler = (BannerDataHandler) row.getTag();
        }

        BannerDataProvider bannerDataProvider = (BannerDataProvider) this.getItem(position);
        bannerDataHandler.banner.setImageResource(bannerDataProvider.getBanner());

        if(storedSelectedId != -1 && storedSelectedId == this.getItemId(position)){
            row.setBackgroundColor(Color.parseColor("#DCDCDC"));
            previous=row;
        }else{
            row.setBackgroundColor(Color.WHITE);
        }


        View finalRow = row;
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.COMPLETED_LIST_FILE),Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("selected banner",bannerDataProvider.banner);
                editor.putInt("selectedBannerId",(int)getItemId(position));
                editor.apply();
                storedSelectedId = (int)getItemId(position);
                if(previous != null){

                    previous.setBackgroundColor(Color.WHITE);
                    finalRow.setBackgroundColor(Color.parseColor("#DCDCDC"));
                    previous = finalRow;
                }else{
                    previous=finalRow;
                    finalRow.setBackgroundColor(Color.parseColor("#DCDCDC"));
                }
            }
        });





        return row;
    }
}
