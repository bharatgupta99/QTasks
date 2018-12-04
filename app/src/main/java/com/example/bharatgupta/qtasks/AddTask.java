package com.example.bharatgupta.qtasks;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddTask extends AppCompatActivity {

    private EditText task;
    private EditText taskDetails;
    private Button p1Button;
    private Button p2Button;
    private Button p3Button;
    private Button addButton;
    private int priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        priority = -1;
        task = findViewById(R.id.new_task);
        taskDetails = findViewById(R.id.new_task_details);
        p1Button = findViewById(R.id.priority_1);
        p2Button = findViewById(R.id.priority_2);
        p3Button = findViewById(R.id.priority_3);
        addButton = findViewById(R.id.new_add_button);

        p1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priority = 1;
                changeStyling(priority);
            }
        });

        p2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priority = 2;
                changeStyling(priority);
            }
        });

        p3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priority = 3;
                changeStyling(priority);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTask();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(AddTask.this, MainActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeStyling(int priority) {
        switch (priority) {
            case 1:
                selectPriority(1);
                deselectPriority(2);
                deselectPriority(3);
                break;
            case 2:
                selectPriority(2);
                deselectPriority(1);
                deselectPriority(3);
                break;
            case 3:
                selectPriority(3);
                deselectPriority(1);
                deselectPriority(2);
                break;
            default:
                break;
        }
    }
    private void selectPriority(int priority) {
        switch (priority) {
            case 1:
                p1Button.setBackgroundResource(R.drawable.task_display_background);
                p1Button.setTextColor(Color.parseColor("#03A9F4"));
                break;
            case 2:
                p2Button.setBackgroundResource(R.drawable.task_display_background);
                p2Button.setTextColor(Color.parseColor("#03A9F4"));
                break;
            case 3:
                p3Button.setBackgroundResource(R.drawable.task_display_background);
                p3Button.setTextColor(Color.parseColor("#03A9F4"));
                break;
            default:
                break;
        }
    }
    private void deselectPriority(int priority) {
        switch (priority) {
            case 1:
                p1Button.setBackgroundColor(Color.parseColor("#03A9F4"));
                p1Button.setTextColor(Color.parseColor("#ffffff"));
                break;
            case 2:
                p2Button.setBackgroundColor(Color.parseColor("#03A9F4"));
                p2Button.setTextColor(Color.parseColor("#ffffff"));
                break;
            case 3:
                p3Button.setBackgroundColor(Color.parseColor("#03A9F4"));
                p3Button.setTextColor(Color.parseColor("#ffffff"));
                break;
            default:
                break;
        }
    }

    private void saveTask() {
        final String taskString = task.getText().toString().trim();
        final String taskDetailsString = taskDetails.getText().toString().trim();

        if(taskString.isEmpty()) {
            Toast.makeText(AddTask.this, "task is needed!", Toast.LENGTH_SHORT ).show();
            return;
        }
        if(taskDetailsString.isEmpty()) {
            Toast.makeText(AddTask.this, "task details are needed", Toast.LENGTH_SHORT ).show();
            return;
        }
        if(priority == -1) {
            Toast.makeText(AddTask.this, "please set a priority..", Toast.LENGTH_SHORT ).show();
            return;
        }

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                Task task = new Task();
                task.setTask(taskString);
                task.setTaskDetails(taskDetailsString);
                task.setPriority(priority);

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .insert(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                Toast.makeText(AddTask.this, "Saved!", Toast.LENGTH_SHORT ).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();

    }
}
