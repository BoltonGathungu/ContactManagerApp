package com.example.workmanagerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //initializing the widgets
    TextView tv;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);

        //Data
        //it is used to pass input to a work request or a worker
        //allowing you to provide the necessary information for the background task to execute
        Data data = new Data.Builder()
                .putInt("max_limit", 8888).build();
        //Constraints
        //These are requirements that can be applied to the work request
        //to specify when and how the associated background task should be executed
        //they help you control the circumstances under which a task should run
        Constraints constraints = new Constraints
                .Builder()
                .setRequiresCharging(true)
                .build();



        //Making use of worker
        // by creating a worker instance
        WorkRequest w = new OneTimeWorkRequest
                .Builder(MyWorker.class)
                .setConstraints(constraints)
                .setInputData(data)
                .build();

        //Enqueue the request with WorkManager
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkManager.getInstance(
                  getApplicationContext()
                ).enqueue(w);
            }
        });

        //Monitoring the status of work manager
        WorkManager.getInstance(getApplicationContext())
                .getWorkInfoByIdLiveData(w.getId())
                .observe(this,
                        new Observer<WorkInfo>() {
                            @Override
                            public void onChanged(WorkInfo workInfo) {
                               if(workInfo != null){
                                   Toast.makeText(
                                           MainActivity.this,
                                           "Status:" +workInfo.getState().name(),
                                           Toast.LENGTH_SHORT
                                   ).show();

                                 if(workInfo.getState().isFinished()){
                                    Data data1 = workInfo.getOutputData();
                                    Toast.makeText(MainActivity.this,""+data1.getString("msg"),
                                            Toast.LENGTH_SHORT).show();
                                 }
                               }
                            }
                        });




    }
}