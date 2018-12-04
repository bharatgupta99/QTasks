package com.example.bharatgupta.qtasks;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private TextView task;
    private TextView taskDetails;
    private ImageButton doneButton;
    private Task currTask;
    private Animation fadeIn;
    private Animation fadeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.add_task_floating);
        task = findViewById(R.id.display_task);
        taskDetails = findViewById(R.id.display_details);
        doneButton = findViewById(R.id.done_button);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTaskIntent = new Intent(MainActivity.this, AddTask.class);
                startActivity(addTaskIntent);
                }
        });

        getTask();

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doneTask(currTask);
            }
        });

    }


    private void doneTask(final Task currTask) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .delete(currTask);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                getTask();
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();
    }

    private void getTask() {
        class GetTask extends AsyncTask<Void, Void, List<Task>> {

            @Override
            protected List<Task> doInBackground(Void... voids) {
                List<Task> list = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao().getAll();
                return list;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    PriorityQueue<Task> pq = new PriorityQueue<>(new PriorityComparator());
                    for(Task t : tasks) {
                        pq.add(t);
                    }
                    currTask = pq.peek();
                    if(currTask != null) {
                        task.setText(currTask.getTask());
                        taskDetails.setText(currTask.getTaskDetails());
                    }else{
                        task.setText("You are done for the day!");
                        findViewById(R.id.container_task).setBackground(null);
                        taskDetails.setVisibility(View.GONE);
                        doneButton.setVisibility(View.GONE);
                    }
                }else {
                    //display first task
                }
            }
        }
        GetTask gt = new GetTask();
        gt.execute();
    }



    class PriorityComparator implements Comparator<Task> {

        @Override
        public int compare(Task task, Task t1) {
            return t1.getPriority() - task.getPriority();
        }

        @Override
        public boolean equals(Object o) {
            return false;
        }
    }

}
