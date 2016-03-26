package com.dtodorov.darkroomnegative;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

public class MainActivity extends AppCompatActivity {
    private final int PICK_PHOTO_FOR_EXPOSURE = 1;
    private IToaster _toaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _toaster = new Toaster(getApplicationContext(), getResources());
    }

    public void pickImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_EXPOSURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_EXPOSURE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }


            //Uri uri = data.getData();
            Uri uri = Uri.parse("foo");
            ContentResolver contentResolver = getContentResolver();

            try {
                InputStream inputStream = contentResolver.openInputStream(uri);
            } catch (FileNotFoundException e) {
                _toaster.Toast(R.string.error_image_open);
            }

            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }
}
