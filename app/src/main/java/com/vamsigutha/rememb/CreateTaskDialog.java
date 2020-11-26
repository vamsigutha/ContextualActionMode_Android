package com.vamsigutha.rememb;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CreateTaskDialog extends DialogFragment {
    EditText task_name;

    public interface TaskNameListener{
        public void onTaskNameProvided(String taskName);
    }

    TaskNameListener taskNameListener;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.task_dialog_layout,null);
        builder.setView(view);
        task_name = view.findViewById(R.id.task_name);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                taskNameListener.onTaskNameProvided(task_name.getText().toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Activity activity = (Activity) context;
        try {
            taskNameListener = (TaskNameListener) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString()+" Must implement TaskNameListener methods");
        }
    }
}
