package com.vamsigutha.rememb;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends ArrayAdapter {

    List list = new ArrayList();
    List completed = new ArrayList();
    List modifiedCompletedList = new ArrayList();
    List modifiedPinnedList = new ArrayList();
    int banner_id;
    Context context;
    boolean isItemSelected=false;
    boolean isPinnedToTaskBar = false;
   List pinnedToTaskBar = new ArrayList();
    ProgressBar progressBar;
    TextView completedTasksNumber;
    ConstraintLayout innerLayout;
    Button clearAll;
    //For notification
    private final String CHANNEL_ID = "remainder_notification";
    private final int NOTIFICATION_ID = 001;

    public MovieAdapter(Button clearAll, ConstraintLayout innerLayout, TextView completedTasksNumber, ProgressBar progressBar, @NonNull Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.progressBar = progressBar;
        this.completedTasksNumber = completedTasksNumber;
        this.innerLayout = innerLayout;
        this.clearAll = clearAll;
        loadCompletedData();
        loadPinnedList();
        setProgressBar();
        if(MainActivity.taskList.size()!=0){
            clearAll.setVisibility(View.VISIBLE);
            innerLayout.setVisibility(View.INVISIBLE);
        }
    }


    static class DataHandler{

        TextView title;
        TextView option;
        TextView circle;
    }

    public void setCompletedData(){

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.COMPLETED_LIST_FILE), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(completed);
        editor.putString(context.getString(R.string.COMPLETED_LIST),json);
        editor.apply();
    }

    public void loadCompletedData(){
        //Load data from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.COMPLETED_LIST_FILE),Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(context.getString(R.string.COMPLETED_LIST),null);
        Type type = new TypeToken<ArrayList<Object>>() {}.getType();
        completed = gson.fromJson(json,type);
        banner_id = sharedPreferences.getInt("selected banner",27);

        if(completed == null){

            completed = new ArrayList();
        }else{
            if(modifiedCompletedList.size()==0){
                for(int i=0;i<completed.size();i++){
                    modifiedCompletedList.add(gson.toJson(completed.get(i)));

                }
            }else{
                for(int i=0;i<completed.size();i++){
                    modifiedCompletedList.set(i,gson.toJson(completed.get(i)));
                }
            }


        }
    }

    public void setPinnedList(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.PINNED_LIST_FILE), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(pinnedToTaskBar);
        editor.putString(context.getString(R.string.PINNED_LIST),json);
        editor.apply();
    }

    public void loadPinnedList(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.PINNED_LIST_FILE),Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(context.getString(R.string.PINNED_LIST),null);
        Type type = new TypeToken<ArrayList<Object>>() {}.getType();
        pinnedToTaskBar= gson.fromJson(json,type);
        if(pinnedToTaskBar ==null){
            pinnedToTaskBar = new ArrayList();
        }else{
            if(pinnedToTaskBar.size() !=0){
                if(modifiedPinnedList.size()==0){
                    modifiedPinnedList.add(gson.toJson(pinnedToTaskBar.get(0)));
                }else{
                    modifiedPinnedList.set(0,gson.toJson(pinnedToTaskBar.get(0)));
                }
            }

        }
    }




    public void displayNotification(String title){

        createNotificationChannel(title);

        Intent serviceIntent = new Intent(context, NotificationService.class);
        serviceIntent.putExtra("title", title);
        serviceIntent.putExtra("CHANNEL_ID ",CHANNEL_ID );
        serviceIntent.putExtra("BANNER_ID",banner_id);
        context.startService(serviceIntent);





//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
//        notificationManagerCompat.notify(NOTIFICATION_ID,);

    }

    public void removeNotification(){
        Intent serviceIntent = new Intent(context, NotificationService.class);
        context.stopService(serviceIntent);
    }

    private void createNotificationChannel(String title){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name = title;

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name,importance);



            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


    @Override
    public void add(@Nullable Object object) {
        super.add(object);
        list.add(object);
        setProgressBar();
    }

    @Override
    public void remove(@Nullable Object object) {
        super.remove(object);
        list.remove(object);
        completed.remove(object);
    }

    public void removeAll(){
        removeNotification();
        list.clear();
        completed.clear();
        setCompletedData();
        pinnedToTaskBar.clear();
        setPinnedList();
        modifiedCompletedList.clear();
        modifiedPinnedList.clear();
        setProgressBar();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        Gson gson = new Gson();

        return gson.toJson(this.list.get(position));
    }

    public Object getItem1(int position){
        return this.list.get(position);
    }

    public void isItemSelected(boolean isItemSelected){
        this.isItemSelected = isItemSelected;
        
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setProgressBar(){
        int progress =  (int)((this.completed.size()/(float)this.getCount())*100);
        this.progressBar.setProgress(progress);
        this.completedTasksNumber.setText(this.completed.size()+" of "+this.getCount()+" tasks");
    }

    public String getText(int position){
        MovieDataProvider movieDataProvider;
        movieDataProvider = (MovieDataProvider) this.getItem1(position);
        return movieDataProvider.getTitle();
    }






    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row ;
        row = convertView;
        DataHandler dataHandler;
        dataHandler = new DataHandler();
        if(convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_layout, parent, false);



            dataHandler.title = (TextView) row.findViewById(R.id.task_title);

            dataHandler.option = (TextView) row.findViewById(R.id.options_menu);
            dataHandler.circle = (TextView) row.findViewById(R.id.circle);
            row.setTag(dataHandler);
        }else{
            dataHandler = (DataHandler) row.getTag();
        }

        MovieDataProvider movieDataProvider;
        movieDataProvider = (MovieDataProvider) this.getItem1(position);

        dataHandler.title.setText(movieDataProvider.getTitle());


        //we check if the current object is present in completed list then we apply some changes
        if(modifiedCompletedList.contains(this.getItem(position))){

//            Log.e("mod",""+modifiedCompletedList.get(0));
//            Log.e("this", ""+this.getItem(position));

            row.setBackgroundColor(Color.parseColor("#FFFFFF"));
            dataHandler.circle.setText("");
            dataHandler.title.setTextColor(Color.parseColor("#BE0909"));
            dataHandler.title.setPaintFlags(dataHandler.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }else{
            dataHandler.circle.setText(movieDataProvider.getCircle());
            row.setBackgroundColor(Color.WHITE);
            dataHandler.title.setPaintFlags(0);
            dataHandler.title.setTextColor(Color.BLACK);
        }


        dataHandler.option.setText(movieDataProvider.getOptionButton());
        //popup menu for individual item


        dataHandler.option.setOnClickListener(v -> {
                if(!isItemSelected) {
                    PopupMenu popupMenu = new PopupMenu(context, v);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.popup_actions, popupMenu.getMenu());
                    MenuItem pinItem = popupMenu.getMenu().getItem(1);
                    if(modifiedPinnedList.contains(getItem(position))){
                        pinItem.setTitle(context.getString(R.string.unpin));
                    }
                    if(modifiedCompletedList.contains(getItem(position))){
                        popupMenu.getMenu().getItem(1).setVisible(false);
                        popupMenu.getMenu().getItem(0).setTitle(context.getString(R.string.unmark));
                    }

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.delete) {

                                //here we don't need to remove item from completed because I already
                                //wrote that in remove method.

                                Log.e("mod",""+modifiedCompletedList);
                                Log.e("currentItem", ""+getItem(position));

                                if(modifiedPinnedList.contains(getItem(position))){
                                    pinnedToTaskBar.clear();
                                    modifiedPinnedList.clear();
                                    setPinnedList();
                                    removeNotification();
                                }

                                MainActivity.taskList.remove(movieDataProvider.getTitle());
                                if(MainActivity.taskList.size()==0){
                                    clearAll.setVisibility(View.INVISIBLE);
                                    innerLayout.setVisibility(View.VISIBLE);
                                }
                                SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.PREF_FILE),Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(MainActivity.taskList);
                                editor.putString(context.getString(R.string.TASK_LIST),json);
                                editor.apply();

                                modifiedCompletedList.remove(getItem(position));

                                //modified current object check below for explanation


                                    List l = new ArrayList();
                                    l.add(getItem1(position));
                                    String s = gson.toJson(l);
                                    Type type = new TypeToken<ArrayList<Object>>() {}.getType();
                                    l = gson.fromJson(s,type);

                                    Log.e("modifiedCurrentObject",""+l);

                                    completed.remove(getItem1(position));
                                    completed.remove(l.get(0));
                                    l.clear();

                                //end of block for removing modified object

                                //In below function we already defined to remove item in completed list
                                //It delets the object if user does not destroy the activity
                                //and above modified version makes sure to delete if activity restarted and object format changed

                                remove(getItem1(position));

                                setCompletedData();
                                setProgressBar();
                                notifyDataSetChanged();



                            }
                            if(item.getItemId()==R.id.completed){
                                if(!modifiedCompletedList.contains(getItem(position))){
                                    completed.add(getItem1(position));
                                    modifiedCompletedList.add(getItem(position));
                                    Log.e("mod",""+modifiedCompletedList);
                                    Log.e("this", ""+completed);


                                    setCompletedData();
                                    setProgressBar();
                                    if(modifiedPinnedList.contains(getItem(position))){
                                        pinnedToTaskBar.remove(getItem1(position));
                                        modifiedPinnedList.remove(getItem(position));
                                        setPinnedList();
                                        removeNotification();
                                    }
                                    notifyDataSetChanged();
                                }else{

                                    /*

                                    Here the thing is that when user un marks the task we want to remove it
                                    but this.getItem1(position) provides us with "com.example.rememb.MovieDataProvider@7944db5" like type
                                    but it may happen that user closed activity and when he reopens the activity the items present in completed list
                                    will change into [{circle=●, movie_rating=Rating: 7.4/10, movie_title=        krishna, optionButton=⋮}] format
                                    so when we check current object is stored in it and remove it if it is
                                    but current object format looks different from what is stored and retrieved
                                    if the activity is not destroyed both look same
                                    so we convert current object into the format that completed retrieved after restart of activity
                                    we remove both formats of current object because one will be present in completed list

                                    */

                                    Gson gson = new Gson();

                                    Log.e("moddedList",""+modifiedCompletedList);
                                    Log.e("completedList", ""+completed);

                                    List l = new ArrayList();
                                    l.add(getItem1(position));
                                    String s = gson.toJson(l);
                                    Type type = new TypeToken<ArrayList<Object>>() {}.getType();
                                    l = gson.fromJson(s,type);

                                    Log.e("modifiedCurrentObject",""+l);

                                    completed.remove(getItem1(position));
                                    completed.remove(l.get(0));
                                    l.clear();
                                    modifiedCompletedList.remove(getItem(position));


                                    setCompletedData();
                                    setProgressBar();
                                    notifyDataSetChanged();
                                }

                            }
                            if(item.getItemId()==R.id.pin_to_notification_bar){
                                if(!modifiedPinnedList.contains(getItem(position))){
                                    if(pinnedToTaskBar.size()==1){
                                        pinnedToTaskBar.clear();
                                        modifiedPinnedList.clear();
                                        setPinnedList();
                                    }
                                    loadCompletedData();
                                    displayNotification(getText(position));
                                    item.setTitle(context.getString(R.string.unpin));
                                    pinnedToTaskBar.add(getItem1(position));
                                    modifiedPinnedList.add(getItem(position));
                                    setPinnedList();
                                    notifyDataSetChanged();
                                }else{
                                    removeNotification();
                                    item.setTitle(context.getString(R.string.pin));
                                    pinnedToTaskBar.remove(getItem1((position)));
                                    modifiedPinnedList.remove(getItem(position));
                                    setPinnedList();
                                    notifyDataSetChanged();
                                }

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
