package com.dtodorov.darkroomnegative.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dtodorov.darkroomnegative.R;
import com.dtodorov.darkroomnegative.controllers.IBitmapListener;
import com.dtodorov.darkroomnegative.controllers.MainController;
import com.dtodorov.darkroomnegative.services.BitmapLoader;
import com.dtodorov.darkroomnegative.services.IBitmapLoader;
import com.dtodorov.darkroomnegative.services.IToaster;
import com.dtodorov.darkroomnegative.services.Toaster;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements IBitmapListener{
    private final int PICK_PHOTO_FOR_EXPOSURE = 1;

    private MainController _mainController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();

        _mainController = new MainController(
                new Toaster(context, getResources()),
                new BitmapLoader(context),
                this
        );
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

            Uri uri = data.getData();
            _mainController.onImagePicked(uri);
        }
    }

    @Override
    public void onBitmapChanged(Bitmap bitmap) {
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
    }
}
