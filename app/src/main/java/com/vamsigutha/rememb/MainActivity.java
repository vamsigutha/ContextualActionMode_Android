package com.vamsigutha.rememb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CreateTaskDialog.TaskNameListener{

    ListView listView;
    TextView option;
    static ArrayList<String> taskList;
    //String[] versions;
    //ArrayAdapter<String> adapter;

    TextView completedTasksNumber;



    String optionButton;
    String circle;
    MovieAdapter movieAdapter;

    Button clear_all;
    Button notification_bar_style;

    ConstraintLayout innerLayout;

    //List data_provider= new ArrayList();

    List selections = new ArrayList();
    int count =0;

    public MainActivity() throws UnsupportedEncodingException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clear_all =  (Button) findViewById(R.id.clear_all);
        notification_bar_style = (Button) findViewById(R.id.notification_bar_style);
        innerLayout = (ConstraintLayout) findViewById(R.id.inner_layout);

        loadData();

        if(taskList.size()==0){
            clear_all.setVisibility(View.INVISIBLE);
            innerLayout.setVisibility(View.VISIBLE);
        }


        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        completedTasksNumber = (TextView) findViewById(R.id.completed_tasks_number);

        View viewInflater = getLayoutInflater().inflate(R.layout.row_layout,null);
        option = (TextView) viewInflater.findViewById(R.id.options_menu);

        listView = (ListView) findViewById(R.id.list_view);


        optionButton = getResources().getString(R.string.optionsMenu);
        circle=getString(R.string.circle);
        movieAdapter = new MovieAdapter(clear_all, innerLayout, completedTasksNumber,progressBar,getApplicationContext(), R.layout.row_layout);
        listView.setAdapter(movieAdapter);

        //versions = getResources().getStringArray(R.array.versions);
//        for(String item:versions){
//            data_provider.add(item);
//        }
//        adapter = new ArrayAdapter<String>(this,R.layout.row_layout,R.id.row_item, data_provider);
//        listView.setAdapter(adapter);



        for(int i=0; i<taskList.size();i++){
            MovieDataProvider movieDataProvider = new MovieDataProvider(circle,taskList.get(i),optionButton);
            movieAdapter.add(movieDataProvider);

        }






//        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
//        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
//            @Override
//            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
//                if(checked){
//                    selections.add(movieAdapter.getItem(position));
//                    count++;
//                    mode.setTitle(count+ " Selected");
//                    movieAdapter.isItemSelected(true);
//                    View child = listView.getChildAt(position);
//                    child.setBackgroundColor(Color.parseColor("#0C064D"));
//
//                }else{
//                    selections.remove(movieAdapter.getItem(position));
//                    count--;
//                    mode.setTitle(count+ " Selected");
//                    if(count==0){
//                        movieAdapter.isItemSelected(false);
//                    }
//
//                }
//
//            }
//
//            @Override
//            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                MenuInflater menuInflater = getMenuInflater();
//                menuInflater.inflate(R.menu.context_menu,menu);
//                return true;
//            }
//
//            @Override
//            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            @Override
//            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                if(item.getItemId()==R.id.delete){
//                    for(Object i:selections){
//
//                        movieAdapter.remove(i);
//
//                    }
//                    movieAdapter.notifyDataSetChanged();
//                    movieAdapter.isItemSelected(false);
//                    mode.finish();
//                }
//                return false;
//            }
//
//            @Override
//            public void onDestroyActionMode(ActionMode mode) {
//                count=0;
//                selections.clear();
//                movieAdapter.isItemSelected(false);
//            }
//        });
    }

    public void createTask(View view){
        CreateTaskDialog createTaskDialog = new CreateTaskDialog();
        createTaskDialog.show(getSupportFragmentManager(), "task_dialog");
    }

    @Override
    public void onTaskNameProvided(String taskName) {
        StringBuilder taskNameBuilder = new StringBuilder(taskName);
        for(int i = 0; i<5; i++){
            taskNameBuilder.insert(0, " ");
        }
        taskName = taskNameBuilder.toString();

        taskList.add(taskName);
        setData();
        MovieDataProvider movieDataProvider = new MovieDataProvider(circle,taskName,optionButton);
        movieAdapter.add(movieDataProvider);
        clear_all.setVisibility(View.VISIBLE);
        innerLayout.setVisibility(View.INVISIBLE);


    }

    public void setData(){

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(taskList);
        editor.putString(getString(R.string.TASK_LIST),json);
        editor.apply();
    }

    public void loadData(){
        //Load data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(getString(R.string.TASK_LIST),null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        taskList = gson.fromJson(json,type);
        if(taskList==null){
            taskList= new ArrayList<>();
        }
    }

    public void clearAll(View view){
        taskList.clear();
        movieAdapter.removeAll();
        setData();
        Toast.makeText(getApplicationContext(), "cleared all tasks",Toast.LENGTH_LONG).show();
        view.setVisibility(View.INVISIBLE);
        innerLayout.setVisibility(View.VISIBLE);
    }

    public void notificationBarStyle(View view){
            Intent i = new Intent(this,NotificationBanner.class);
            startActivity(i);
    }



}