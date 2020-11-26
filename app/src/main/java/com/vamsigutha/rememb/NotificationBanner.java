package com.vamsigutha.rememb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class NotificationBanner extends AppCompatActivity {

    ListView listView;
    BannerAdapter bannerAdapter;

    int[] banners = {R.drawable.flower,R.drawable.rose,R.drawable.flower_design,R.drawable.flower_border,
                        R.drawable.easter_border,R.drawable.easter_design,R.drawable.pluto,R.drawable.doraemon,
                    R.drawable.doraemon_flying,R.drawable.doraemon_eating,R.drawable.kristoff};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_banner);

        Log.e("flower",""+banners[0]);

        listView = (ListView) findViewById(R.id.banner_list_view);

        bannerAdapter = new BannerAdapter(getApplicationContext(),R.layout.banner_row_layout);
        listView.setAdapter(bannerAdapter);

        for(int i=0;i<banners.length;i++){
            BannerDataProvider bannerDataProvider = new BannerDataProvider(banners[i]);
            bannerAdapter.add(bannerDataProvider);
        }


    }

    public void clearSelection(View view){
        bannerAdapter.clearSelection();
    }
}