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

    private EditText itemET;
    private Button btn;
    private ListView itemsList;

    private ArrayAdapter<String> adapter;
    private List<AufgabeModell> aufgaben;

    public MainActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemET = findViewById(R.id.item_edit_text);
        btn = findViewById(R.id.add_btn);
        itemsList = findViewById(R.id.items_list);


        AufgabeAccess aufgabeAccess = new AufgabeAccess(this);
//        long id1 = aufgabeAccess.Create("Aufgabe1", "Kommentar1", new Date(), "Datei1");
//        long id2 = aufgabeAccess.Create("Aufgabe2", "Kommentar2", new Date(), "Datei2");
//        long id3 = aufgabeAccess.Create("Aufgabe3", "Kommentar3", new Date(), "Datei3");
//
//        aufgabeAccess.Delete(id2);
//
//        AufgabeModell data3 = aufgabeAccess.Read(id3);
//
//        AufgabeModell data1Alt = aufgabeAccess.Read(id1);
//        aufgabeAccess.Update(id1, "Aufgabe1 neu", "Kommentar1 neu", new Date(), "Datei1 neu");
//        AufgabeModell data1Neu = aufgabeAccess.Read(id1);

//        aufgaben = aufgabeAccess.ReadAll();
//        ArrayList<String> items = new ArrayList<String>();
//
//        for( AufgabeModell m: aufgaben )
//        {
//           items.add(m.getTitle());
//        }
//
//
//        adapter = new ArrayAdapter<String>(  this, android.R.layout.simple_list_item_1, items);
//        itemsList.setAdapter(adapter);

        btn.setOnClickListener(this);
        itemsList.setOnItemClickListener((AdapterView.OnItemClickListener) this);

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

        adapter = new ArrayAdapter<String>(  this, android.R.layout.simple_list_item_1, items);
        itemsList.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.add_btn:
                String itemEntered = itemET.getText().toString();
                adapter.add(itemEntered);
                itemET.setText("");
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