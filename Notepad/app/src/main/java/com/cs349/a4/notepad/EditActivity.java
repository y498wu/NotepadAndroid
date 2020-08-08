package com.cs349.a4.notepad;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.*;
import java.nio.charset.StandardCharsets;

import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    private String startTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        startTitle = intent.getStringExtra("title");
        if (!startTitle.equals("")){
            EditText title = findViewById(R.id.titleBox);
            title.setText(startTitle, TextView.BufferType.EDITABLE);

            EditText content = findViewById(R.id.contentBox);
            String file = "";
            try{
                File f = new File(getApplicationContext().getFilesDir(), startTitle);
                InputStream inputStream = getApplicationContext().openFileInput(startTitle);
                byte[] b = new byte[(int)f.length()];
                inputStream.read(b);
                file = new String(b, StandardCharsets.UTF_8);
            } catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }
            content.setText(file, TextView.BufferType.EDITABLE);
        }

        // floating action button
        FloatingActionButton fab = findViewById(R.id.save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                EditText title = findViewById(R.id.titleBox);
                EditText content = findViewById(R.id.contentBox);
                saveFile(title.getText().toString(), content.getText().toString(), context);
                launchMainActivity();
            }
        });
    }

    private void saveFile(String title, String content,Context context){
        if (!startTitle.equals(title)){
            File f = new File(getApplicationContext().getFilesDir(), startTitle);
            if (f.exists()) f.delete();
        }

        try {
            if(title.matches("")){
                title = content.substring(0, Math.min(content.length()-1, 9));
            }
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(title, Context.MODE_PRIVATE));
            outputStreamWriter.write(content);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void launchMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Context context = getApplicationContext();
        EditText title = findViewById(R.id.titleBox);
        EditText content = findViewById(R.id.contentBox);
        saveFile(title.getText().toString(), content.getText().toString(), context);
        launchMainActivity();
    }
}
