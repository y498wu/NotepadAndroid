package com.cs349.a4.notepad;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.View;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // floating action button
        FloatingActionButton fab = findViewById(R.id.plus);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchEditActivity();
            }
        });

        // Lookup the recyclerview in activity layout
        RecyclerView myRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        int orientation = getResources().getConfiguration().orientation;
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(myRecyclerView.getContext(),
                (orientation == 1) ? RecyclerView.VERTICAL : RecyclerView.HORIZONTAL);
        myRecyclerView.addItemDecoration(dividerItemDecoration);

        // Initialize contacts
        Context context = getApplicationContext();
        notes = Note.createNotesList(context);
        // Create adapter passing in the sample user data
        NoteAdapter adapter = new NoteAdapter(notes, context, this);
        // Attach the adapter to the recyclerview to populate items
        myRecyclerView.setAdapter(adapter);
        // Set layout manager to position the items
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void launchEditActivity(){
        launchEditActivity("");
    }

    public void launchEditActivity(String filename){
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("title", filename);
        startActivity(intent);
    }
}

class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        public class ViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
            public LinearLayout editBox;
            public TextView titleTextView;
            public TextView contentTextView;
            public ImageButton deleteButton;

            public NoteAdapter parent;
            public int position;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                titleTextView = (TextView) itemView.findViewById(R.id.note_title);
                contentTextView = (TextView) itemView.findViewById(R.id.note_content);

                deleteButton = (ImageButton) itemView.findViewById(R.id.note_delete_button);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String myTitle = titleTextView.getText().toString();
                        File dir = myContext.getFilesDir();
                        File file = new File(dir, myTitle);
                        file.delete();

                        // more stuff here
                        parent.myNotes.remove(position);
                        parent.notifyItemRemoved(position);
                        parent.notifyItemRangeChanged(position, parent.getItemCount());
                        parent.notifyDataSetChanged();
                    }
                });

                editBox = (LinearLayout) itemView.findViewById(R.id.editBox);
                editBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String myTitle = titleTextView.getText().toString();
                        myActivity.launchEditActivity(myTitle);
                    }
                });
            }
        }

    // Store a member variable for the contacts
        private List<Note> myNotes;
        private Context myContext;
        private MainActivity myActivity;

    // Pass in the contact array into the constructor
    public NoteAdapter(List<Note> notes, Context context, MainActivity activity) {
        myNotes = notes;
        myContext = context;
        myActivity = activity;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View noteView = inflater.inflate(R.layout.item_note, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(noteView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(NoteAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Note notes = myNotes.get(position);

        // Set item views based on your views and data model
        TextView titleTextView = holder.titleTextView;
        titleTextView.setText(notes.getMyTitle());
        TextView contentTextView = holder.contentTextView;
        contentTextView.setText(notes.getMyContent());

        holder.parent = this;
        holder.position = position;
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return myNotes.size();
    }
}