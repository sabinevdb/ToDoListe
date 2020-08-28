package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AufgabeDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonErledigt;
    private Button buttonDatum;
    int year_x,month_x,day_x;
    private long id;
    static final int DIALOG_ID = 0;
    private EditText datumTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aufgabe_detail);

        datumTextView = findViewById(R.id.editTextDatum);
        EditText nameTextView = findViewById(R.id.editTextName);
        EditText kommentarTextView = findViewById(R.id.editTextKommentar);
        buttonErledigt = findViewById(R.id.buttonErledigt);

        AufgabeAccess aufgabeAccess = new AufgabeAccess(this);

        // Id auslesen
        Bundle b = getIntent().getExtras();
        id = b.getLong("id");
        AufgabeModell aufgabe = aufgabeAccess.Read(id);
        nameTextView.setText(aufgabe.getTitle());
        kommentarTextView.setText(aufgabe.getDescription());
        buttonErledigt.setOnClickListener(this);

        // Kalender
        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        showDialogOnButtonClick();
        datumTextView.setText(year_x + "/" + month_x + "/" + day_x);
    }

    public void showDialogOnButtonClick() {
        buttonDatum = (Button)findViewById(R.id.buttonDatum);
        buttonDatum.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DIALOG_ID) ;
                    }
                }
        );
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID)
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month +1;
            day_x = dayOfMonth;
            datumTextView.setText(year_x + "/" + month_x + "/" + day_x);

        }
    };

    @Override
    public void onClick(View v) {
        AufgabeAccess aufgabeAccess = new AufgabeAccess(this);
        aufgabeAccess.Delete(id);
        finish();
    }
}