package com.dtodorov.darkroomnegative.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dtodorov.darkroomnegative.ImageProcessing.AsyncFilterTask;
import com.dtodorov.darkroomnegative.ImageProcessing.RenderScriptContextFactory;
import com.dtodorov.darkroomnegative.ImageProcessing.filters.Grayscale;
import com.dtodorov.darkroomnegative.R;
import com.dtodorov.darkroomnegative.controllers.IBitmapListener;
import com.dtodorov.darkroomnegative.controllers.MainController;
import com.dtodorov.darkroomnegative.services.BitmapLoader;
import com.dtodorov.darkroomnegative.services.ClapDetector;
import com.dtodorov.darkroomnegative.services.FullScreen;
import com.dtodorov.darkroomnegative.services.Toaster;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.OnsetHandler;
import be.tarsos.dsp.onsets.PercussionOnsetDetector;

public class MainActivity extends AppCompatActivity implements IBitmapListener{
    private final int PICK_PHOTO_FOR_EXPOSURE = 1;

    private MainController _mainController;
    private FullScreen _fullScreen;
    private Bitmap _imageViewCache;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("image", _imageViewCache);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();

        RenderScriptContextFactory renderScriptContextFactory = new RenderScriptContextFactory(context);

        _mainController = new MainController(
                new Toaster(context, getResources()),
                new BitmapLoader(context),
                new AsyncFilterTask(new Grayscale(renderScriptContextFactory)),
                this,
                new ClapDetector()
        );

        View contentControl = findViewById(R.id.contentPanel);
        View[] controlsToHide = new View[] { findViewById(R.id.controlPanel)};

        _fullScreen = new FullScreen(this, contentControl, controlsToHide);

        if(savedInstanceState != null)
        {
            _imageViewCache = savedInstanceState.getParcelable("image");
            _mainController.setImage(_imageViewCache);
        }
    }

    private Thread _thread;

    public void pickImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_EXPOSURE);
    }

    public void exposeImage(View view) {
        _fullScreen.enterFullScreen();
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
    public void onImageSet(Bitmap bitmap) {
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        _imageViewCache = bitmap;
        imageView.setImageBitmap(bitmap);
    }
}
