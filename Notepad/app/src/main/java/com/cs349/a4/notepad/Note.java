package com.cs349.a4.notepad;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Note {
    private String myTitle;
    private String myContent;

    public Note(String title, String content){
        myTitle = title;
        myContent = content;
    }

    public String getMyTitle(){
        return myTitle;
    }

    public String getMyContent(){
        return myContent;
    }

    private static int lastContactId = 0;

    public static ArrayList<Note> createNotesList(Context context) {
        ArrayList<Note> contacts = new ArrayList<Note>();
        // try to get file content
        String ret = "";
        File[] files = context.getFilesDir().listFiles();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File file, File t1) {
                if (file.lastModified() == t1.lastModified()) return 0;
                else return (file.lastModified() > t1.lastModified()) ? -1 : 1;
            }
        });
        for (File f : files) {
            try{
                InputStream inputStream = context.openFileInput(f.getName());
                byte[] b = new byte[15];
                inputStream.read(b, 0, 15);
                ret = new String(b, StandardCharsets.UTF_8);
            } catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }

            contacts.add(new Note(f.getName(), ret));
        }

        return contacts;
    }
}
