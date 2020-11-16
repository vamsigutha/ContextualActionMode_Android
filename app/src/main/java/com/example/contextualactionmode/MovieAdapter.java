package com.example.contextualactionmode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends ArrayAdapter {

    List list = new ArrayList();
    Context context;
    boolean isItemSelected=false;

    public MovieAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = context;
    }

    static class DataHandler{
        ImageView Poster;
        TextView title;
        TextView rating;
        TextView option;
    }

    @Override
    public void add(@Nullable Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public void remove(@Nullable Object object) {
        super.remove(object);
        list.remove(object);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    public void isItemSelected(boolean isItemSelected){
        this.isItemSelected = isItemSelected;
        
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row ;
        row = convertView;
        DataHandler dataHandler;

        if(convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_layout, parent, false);

            dataHandler = new DataHandler();
            dataHandler.Poster = (ImageView) row.findViewById(R.id.movie_poster);
            dataHandler.title = (TextView) row.findViewById(R.id.movie_title);
            dataHandler.rating = (TextView) row.findViewById(R.id.movie_rating);
            dataHandler.option = (TextView) row.findViewById(R.id.options_menu);
            row.setTag(dataHandler);
        }else{
            dataHandler = (DataHandler) row.getTag();
        }

        MovieDataProvider movieDataProvider;
        movieDataProvider = (MovieDataProvider) this.getItem(position);

        dataHandler.Poster.setImageResource(movieDataProvider.getMovie_poster_resource());
        dataHandler.title.setText(movieDataProvider.getMovie_title());
        dataHandler.rating.setText(movieDataProvider.getMovie_rating());
        dataHandler.option.setText(movieDataProvider.getOptionButton());
        //popup menu for individual item


            dataHandler.option.setOnClickListener(v -> {
                if(!isItemSelected) {
                    PopupMenu popupMenu = new PopupMenu(context, v);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.popup_actions, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.delete) {
                                remove(getItem(position));
                                notifyDataSetChanged();
                            }

                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });





        return row;
    }
}
