package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AufgabeDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aufgabe_detail);

        EditText nameTextView = findViewById(R.id.editTextName);
        EditText kommentarTextView = findViewById(R.id.editTextKommentar);
        btn = findViewById(R.id.buttonErledigt);

        AufgabeAccess aufgabeAccess = new AufgabeAccess(this);

        // Id auslesen
        Bundle b = getIntent().getExtras();
        id = b.getLong("id");
        AufgabeModell aufgabe = aufgabeAccess.Read(id);
        nameTextView.setText(aufgabe.getTitle());
        kommentarTextView.setText(aufgabe.getDescription());
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AufgabeAccess aufgabeAccess = new AufgabeAccess(this);
        aufgabeAccess.Delete(id);
        finish();
    }
}