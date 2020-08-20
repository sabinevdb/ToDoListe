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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText itemET;
    private Button btn;
    private ListView itemsList;

    private ArrayList<String> items;
    private ArrayAdapter<String> adapter;


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

        items = FileHelper.readData(this); // TODO read from DB

        AufgabeAccess aufgabeAccess = new AufgabeAccess(this);
        long id1 = aufgabeAccess.Create("Aufgabe1", "Kommentar1", new Date(), "Datei1");
        long id2 = aufgabeAccess.Create("Aufgabe2", "Kommentar2", new Date(), "Datei2");
        long id3 = aufgabeAccess.Create("Aufgabe3", "Kommentar3", new Date(), "Datei3");

        aufgabeAccess.Delete(id2);

        AufgabeModell data3 = aufgabeAccess.Read(id3);

        AufgabeModell data1Alt = aufgabeAccess.Read(id1);
        aufgabeAccess.Update(id1, "Aufgabe1 neu", "Kommentar1 neu", new Date(), "Datei1 neu");
        AufgabeModell data1Neu = aufgabeAccess.Read(id1);

        List<AufgabeModell> alleAufgaben = aufgabeAccess.ReadAll();

        adapter = new ArrayAdapter<String>(  this, android.R.layout.simple_list_item_1, items);
        itemsList.setAdapter(adapter);

        btn.setOnClickListener(this);
        itemsList.setOnItemClickListener((AdapterView.OnItemClickListener) this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.add_btn:
                String itemEntered = itemET.getText().toString();
                adapter.add(itemEntered);
                itemET.setText("");
                FileHelper.writeData(items, this); // TODO write to DB
                Toast.makeText(this, "Aufgabe hinzugefügt", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, Aufgabe.class);
        startActivity(intent);

    }

    public void onItemClickAlt(AdapterView<?> parent, View view, int position, long id) {
        items.remove(position);
        FileHelper.writeData(items, this);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Gelöscht", Toast.LENGTH_SHORT).show();
        }

    private void show() {
    }
}