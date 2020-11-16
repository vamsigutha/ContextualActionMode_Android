package com.example.contextualactionmode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView option;
    //String[] versions;
    //ArrayAdapter<String> adapter;

    int[] movie_poster_resource = {R.drawable.movie1, R.drawable.movie2, R.drawable.movie3,
            R.drawable.movie4, R.drawable.movie5, R.drawable.movie6, R.drawable.movie7,
            R.drawable.movie8, R.drawable.movie9, R.drawable.movie10};

    String[] movie_titles;
    String[] movie_ratings;
    String optionButton;
    MovieAdapter movieAdapter;

    //List data_provider= new ArrayList();

    List selections = new ArrayList();
    int count =0;

    public MainActivity() throws UnsupportedEncodingException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View viewInflater = getLayoutInflater().inflate(R.layout.row_layout,null);
        option = (TextView) viewInflater.findViewById(R.id.options_menu);

        listView = (ListView) findViewById(R.id.list_view);
        movie_titles = getResources().getStringArray(R.array.movie_titles);
        movie_ratings = getResources().getStringArray(R.array.movie_ratings);
        optionButton = getResources().getString(R.string.optionsMenu);
        movieAdapter = new MovieAdapter(getApplicationContext(), R.layout.row_layout);
        listView.setAdapter(movieAdapter);

        //versions = getResources().getStringArray(R.array.versions);
//        for(String item:versions){
//            data_provider.add(item);
//        }
//        adapter = new ArrayAdapter<String>(this,R.layout.row_layout,R.id.row_item, data_provider);
//        listView.setAdapter(adapter);


        int i=0;
        for(String titles:movie_titles){
            MovieDataProvider movieDataProvider = new MovieDataProvider(movie_poster_resource[i],titles,movie_ratings[i],optionButton);
            movieAdapter.add(movieDataProvider);
            i++;
        }

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if(checked){
                    selections.add(movieAdapter.getItem(position));
                    count++;
                    mode.setTitle(count+ " Selected");
                    movieAdapter.isItemSelected(true);

                }else{
                    selections.remove(movieAdapter.getItem(position));
                    count--;
                    mode.setTitle(count+ " Selected");
                    if(count==0){
                        movieAdapter.isItemSelected(false);
                    }

                }

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = getMenuInflater();
                menuInflater.inflate(R.menu.context_menu,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if(item.getItemId()==R.id.delete){
                    for(Object i:selections){

                        movieAdapter.remove(i);

                    }
                    movieAdapter.notifyDataSetChanged();
                    movieAdapter.isItemSelected(false);
                    mode.finish();
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                count=0;
                selections.clear();
                movieAdapter.isItemSelected(false);
            }
        });
    }
}