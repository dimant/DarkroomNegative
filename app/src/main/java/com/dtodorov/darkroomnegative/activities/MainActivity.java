package com.dtodorov.darkroomnegative.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.dtodorov.darkroomnegative.services.IClapDetector;
import com.dtodorov.darkroomnegative.services.Toaster;


public class MainActivity extends AppCompatActivity {
    private final int PICK_PHOTO_FOR_EXPOSURE = 1;

    private MainController _mainController;
    private IEventDispatcher _eventDispatcher;
    private IClapDetector _clapDetector;
    private Bitmap _imageViewCache;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("image", _imageViewCache);

        final SeekBar exposureTimeControl = (SeekBar) findViewById(R.id.exposureTimeSeekBar);
        outState.putInt("exposureTime", exposureTimeControl.getProgress());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy()
    {
        _clapDetector.stop();
        super.onDestroy();
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

        _clapDetector = new ClapDetector();

        _mainController = new MainController(
                _eventDispatcher,
                new Toaster(context, resources),
                new FullScreen(this, contentControl),
                new Exposer(imageView, getContentResolver()),
                new BitmapLoader(context),
                new AsyncFilterTask(new Grayscale(renderScriptContextFactory)),
                _clapDetector
        );

        int exposureTime = 5;

        if(savedInstanceState != null) {
            _imageViewCache = savedInstanceState.getParcelable("image");
            _mainController.setImage(_imageViewCache);
            _mainController.fire(MainController.Trigger.Home);
            _eventDispatcher.emit("enableView", R.id.beginExposureButton);

            exposureTime = savedInstanceState.getInt("exposureTime");
        } else {
            _eventDispatcher.emit("disableView", R.id.beginExposureButton);

        }

        _mainController.setExposureTime(exposureTime);
        exposureTimeControl.setProgress(exposureTime);
        _clapDetector.setClapListener(_mainController);
        _clapDetector.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void pickImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_EXPOSURE);
    }

    public void enterExposeImage(View view) {
        _mainController.fire(MainController.Trigger.Expose);
    }

    public void exitExposeImage(View view) {
        _mainController.fire(MainController.Trigger.Home);
    }

    public void setupExposureTime(View view) {
        _mainController.fire(MainController.Trigger.SetupExposure);
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
