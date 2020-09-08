package com.example.todolist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class ZeigeAnhangActivity extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zeige_anhang);

        // Image file name auslesen
        Bundle b = getIntent().getExtras();
        String fileName = b.getString("fileName");
        imageView = findViewById(R.id.imageViewLarge);
        File file = new File(fileName);
        if(file.exists()) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(fileName,bmOptions);
            imageView.setImageBitmap(bitmap);
        }
    }
}