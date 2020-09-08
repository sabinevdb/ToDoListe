package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText editTextName;
    private Button buttonHinzufuegen;
    private ListView listViewAufgaben;

    private ArrayAdapter<String> adapter;
    private List<AufgabeModell> aufgaben;

    public MainActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextName = findViewById(R.id.item_edit_text);
        buttonHinzufuegen = findViewById(R.id.add_btn);
        listViewAufgaben = findViewById(R.id.items_list);
        buttonHinzufuegen.setOnClickListener(this);
        listViewAufgaben.setOnItemClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        AufgabeAccess aufgabeAccess = new AufgabeAccess(this);
        aufgaben = aufgabeAccess.ReadAll();
        ArrayList<String> items = new ArrayList<String>();
        for( AufgabeModell m: aufgaben )
        {
            items.add(m.getTitle());
        }

        https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
        adapter = new ArrayAdapter<String>(  this, android.R.layout.simple_list_item_1, items);
        listViewAufgaben.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.add_btn:
                String itemEntered = editTextName.getText().toString();
                adapter.add(itemEntered);
                editTextName.setText("");
                AufgabeAccess aufgabeAccess = new AufgabeAccess(this);
                long id = aufgabeAccess.Create(itemEntered, "", new Date(), "");
                Intent intent = new Intent(this, AufgabeDetailActivity.class);
                // Id übergeben
                Bundle b = new Bundle();
                b.putLong("id", id);
                intent.putExtras(b);
                startActivity(intent);
                break;
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, AufgabeDetailActivity.class);
        AufgabeModell m = aufgaben.get(position);
        // Id übergeben
        Bundle b = new Bundle();
        b.putLong("id", m.getId());
        intent.putExtras(b);
        startActivity(intent);
    }
}