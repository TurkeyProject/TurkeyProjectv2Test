package com.example.turkeyprojectv2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText editTextPriority;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextTags;
//    private Button save,load,update,deleteNote,deleteDescription,addnotes,loadnotes;
    private Button addnotes;
    private Button loadnotes;
    private TextView textViewData;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference notebookRef=db.collection("Notebook");

//    private ListenerRegistration noteListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findObject();
        buttonClickEvent();

    }



    public void findObject(){
        editTextTitle=findViewById(R.id.edit_text_title);
        editTextDescription=findViewById(R.id.edit_text_description);
        addnotes=findViewById(R.id.btn_adds);
        loadnotes=findViewById(R.id.btn_loads);
        textViewData=findViewById(R.id.text_view_data);
        editTextPriority=findViewById(R.id.edit_text_priority);
        editTextTags=findViewById(R.id.edit_text_tags);

    }
    public void buttonClickEvent(){
        addnotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=editTextTitle.getText().toString();
                String description=editTextDescription.getText().toString();
                if(editTextPriority.length()==0){
                    editTextPriority.setText("0");
                }
                int priority=Integer.parseInt(editTextPriority.getText().toString());
                String tagInput=editTextTags.getText().toString();
                String[] tagArray=tagInput.split("\\s*,\\s*");
                Map<String,Boolean> tags= new HashMap<>();
                for(String tag:tagArray){
                    tags.put(tag,true);
                }
                Note note=new Note(title,description,priority,tags);
                notebookRef.document("vLqZee8HtvR7oc31lEAN").collection("child Notes").add(note);
                notebookRef.add(note);
            }
        });
        loadnotes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                notebookRef.document("vLqZee8HtvR7oc31lEAN").collection("child Notes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            note.setDocumentId(documentSnapshot.getId());

                            String documentId = note.getDocumentId();
                            data += "ID: " + documentId;
                            for(String tag: note.getTags().keySet()){
                                data+="\n-"+tag;

                            }
                            data+="\n\n";
//                            String title = note.getTitle();
//                            String descritpion = note.getDescription();
//                            int priority = note.getPriority();
//
//                            data += "ID: " + documentId + "\nTitle:" + title + "\nDescription" + descritpion + "\nPriority" + priority + "\n\n";

                        }
                        textViewData.setText(data);
                    }
                });




            }
        });
    }

}