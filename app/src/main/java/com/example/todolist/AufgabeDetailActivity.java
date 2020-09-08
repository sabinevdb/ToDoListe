package com.example.todolist;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AufgabeDetailActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private Button buttonErledigt;
    private Button buttonDatum;
    private Button buttonAnhang;
    private EditText editTextDatum;
    private ImageView imageView;
    private EditText editTextName;
    private EditText editTextKommentar;

    private long id;
    static final int DIALOG_ID = 0;
    public static final int PICK_IMAGE = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private Date date;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aufgabe_detail);

        editTextName = findViewById(R.id.editTextName);
        editTextKommentar = findViewById(R.id.editTextKommentar);
        editTextDatum = findViewById(R.id.editTextDatum);
        buttonAnhang = findViewById(R.id.buttonAnhang);
        imageView = findViewById(R.id.imageView);
        buttonErledigt = findViewById(R.id.buttonErledigt);

        buttonAnhang.setOnClickListener(this);
        buttonAnhang.setOnLongClickListener(this);
        buttonErledigt.setOnClickListener(this);
        imageView.setOnClickListener(this);

        AufgabeAccess aufgabeAccess = new AufgabeAccess(this);

        // Id auslesen
        Bundle b = getIntent().getExtras();
        id = b.getLong("id");
        AufgabeModell aufgabe = aufgabeAccess.Read(id);
        editTextName.setText(aufgabe.getTitle());
        editTextKommentar.setText(aufgabe.getDescription());
        fileName = aufgabe.getFilename();

        LoadImage();

        date = aufgabe.getTimestamp();
        String dateStr = dateTimeToString(date);
        final Calendar cal = Calendar.getInstance();
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        int dayOfMonth = date.getDate();
        cal.set(year, month, dayOfMonth);
        showDialogOnButtonClick();
        editTextDatum.setText(dateStr);
    }

    private String dateTimeToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(date);
    }

    private void LoadImage() {
        try {
            File file = new File(fileName);
            if(file.exists()) {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(fileName,bmOptions);
                imageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (id == DIALOG_ID) {
            int year = date.getYear() + 1900;
            int month = date.getMonth();
            int dayOfMonth = date.getDate();
            return new DatePickerDialog(this, dpickerListener, year, month, dayOfMonth);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            date = new Date(year - 1900, month, dayOfMonth);
            String dateStr = dateTimeToString(date);
            editTextDatum.setText(dateStr);
        }
    };

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonErledigt:
                AufgabeAccess aufgabeAccess = new AufgabeAccess(this);
                aufgabeAccess.Delete(id);
                finish();
                break;
            case R.id.buttonAnhang:
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                pickIntent.setType("image/*");
                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {galleryIntent});
                startActivityForResult(chooserIntent, PICK_IMAGE);
                break;
            case R.id.imageView:
                if (fileName != null && !fileName.isEmpty()) {
                    Intent intent = new Intent(this, ZeigeAnhangActivity.class);
                    Bundle b = new Bundle();
                    b.putString("fileName", fileName);
                    intent.putExtras(b);
                    startActivity(intent);
                }

                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch(v.getId()){
            case R.id.buttonAnhang:
                fileName = "";
                imageView.setImageBitmap(null);
                break;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && data != null) {
            Uri uri = data.getData();
            if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
                fileName = FileTools.getPath(this, uri);
            }

            imageView.setImageURI(uri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        String name = editTextName.getText().toString();
        if (name == null || name.isEmpty()) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setCancelable(false);
            alertBuilder.setTitle("Fehler");
            alertBuilder.setMessage("Name der Aufgabe darf nicht leer sein!");
            alertBuilder.setPositiveButton(android.R.string.yes,null);
            AlertDialog alert = alertBuilder.create();
            alert.show();
        }
        else {
            AufgabeAccess aufgabeAccess = new AufgabeAccess(this);
            String kommentar = editTextKommentar.getText().toString();
            aufgabeAccess.Update(id, name, kommentar, date, fileName);
            super.onBackPressed();
        }
    }
}