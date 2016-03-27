package com.dtodorov.darkroomnegative.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dtodorov.darkroomnegative.ImageProcessing.AsyncFilterTask;
import com.dtodorov.darkroomnegative.ImageProcessing.RenderScriptContextFactory;
import com.dtodorov.darkroomnegative.ImageProcessing.filters.Grayscale;
import com.dtodorov.darkroomnegative.R;
import com.dtodorov.darkroomnegative.controllers.MainController;
import com.dtodorov.darkroomnegative.helpers.EventDispatcher;
import com.dtodorov.darkroomnegative.helpers.IConverter;
import com.dtodorov.darkroomnegative.helpers.IEventDispatcher;
import com.dtodorov.darkroomnegative.helpers.IEventListener;
import com.dtodorov.darkroomnegative.helpers.UnitDisplayConverter;
import com.dtodorov.darkroomnegative.services.BitmapLoader;
import com.dtodorov.darkroomnegative.services.ClapDetector;
import com.dtodorov.darkroomnegative.services.Exposer;
import com.dtodorov.darkroomnegative.services.FullScreen;
import com.dtodorov.darkroomnegative.services.IFullScreen;
import com.dtodorov.darkroomnegative.services.Toaster;


public class MainActivity extends AppCompatActivity {
    private final int PICK_PHOTO_FOR_EXPOSURE = 1;

    private MainController _mainController;
    private IEventDispatcher _eventDispatcher;
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

        final Context context = getApplicationContext();
        final Resources resources = getResources();

        final RenderScriptContextFactory renderScriptContextFactory = new RenderScriptContextFactory(context);

        final SeekBar exposureTimeControl = (SeekBar) findViewById(R.id.exposureTimeSeekBar);
        exposureTimeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                _mainController.setExposureTime(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final View contentControl = findViewById(R.id.contentPanel);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);

        _eventDispatcher = new EventDispatcher();

        _eventDispatcher.register("exposureTimeChanged", new IEventListener() {
            private IConverter<Integer, String, String> _converter = new UnitDisplayConverter();
            @Override
            public void callback(Object param) {
                Integer progress = (Integer) param;
                String value = _converter.convert((Integer) progress, resources.getString(R.string.seconds));
                TextView exposureTimeDisplay = (TextView) findViewById(R.id.exposureTimeDisplay);
                exposureTimeDisplay.setText(value);
            }
        });

        _eventDispatcher.register("hideView", new IEventListener() {
            @Override
            public void callback(Object param) {
                Integer id = (Integer) param;
                View view = findViewById(id.intValue());
                view.setVisibility(View.GONE);
            }
        });

        _eventDispatcher.register("showView", new IEventListener() {
            @Override
            public void callback(Object param) {
                Integer id = (Integer) param;
                View view = findViewById(id.intValue());
                view.setVisibility(View.VISIBLE);
            }
        });

        _eventDispatcher.register("enableView", new IEventListener() {
            @Override
            public void callback(Object param) {
                Integer id = (Integer) param;
                View view = findViewById(id.intValue());
                view.setEnabled(true);
            }
        });

        _eventDispatcher.register("disableView", new IEventListener() {
            @Override
            public void callback(Object param) {
                Integer id = (Integer) param;
                View view = findViewById(id.intValue());
                view.setEnabled(false);
            }
        });

        _eventDispatcher.register("imageSet", new IEventListener() {
            @Override
            public void callback(Object param) {
                Bitmap bitmap = (Bitmap) param;
                _imageViewCache = bitmap;
                imageView.setImageBitmap(bitmap);
            }
        });

        _mainController = new MainController(
                _eventDispatcher,
                new Toaster(context, resources),
                new FullScreen(this, contentControl),
                new Exposer(imageView),
                new BitmapLoader(context),
                new AsyncFilterTask(new Grayscale(renderScriptContextFactory)),
                new ClapDetector()
        );

        if(savedInstanceState != null)
        {
            _imageViewCache = savedInstanceState.getParcelable("image");
            _mainController.setImage(_imageViewCache);
        }

        int defaultExposureTime = 5;
        _mainController.setExposureTime(defaultExposureTime);
        exposureTimeControl.setProgress(defaultExposureTime);
        _eventDispatcher.emit("disableView", R.id.beginExposureButton);
    }

    public void pickImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_EXPOSURE);
    }

    public void enterExposeImage(View view) {
        _mainController.enterExposeImage();
    }

    public void exitExposeImage(View view) {
        _mainController.exitExposeImage();
    }

    public void setupExposureTime(View view) {
        _mainController.setupExposureTime();
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
}
