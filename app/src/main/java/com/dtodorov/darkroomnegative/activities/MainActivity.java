package com.dtodorov.darkroomnegative.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dtodorov.darkroomnegative.ImageProcessing.AsyncFilterTask;
import com.dtodorov.darkroomnegative.ImageProcessing.RenderScriptContextFactory;
import com.dtodorov.darkroomnegative.ImageProcessing.filters.CompositeFilter;
import com.dtodorov.darkroomnegative.ImageProcessing.filters.Grayscale;
import com.dtodorov.darkroomnegative.ImageProcessing.filters.Invert;
import com.dtodorov.darkroomnegative.ImageProcessing.filters.Rotate;
import com.dtodorov.darkroomnegative.R;
import com.dtodorov.darkroomnegative.controllers.MainController;
import com.dtodorov.darkroomnegative.fragments.MainFragment;
import com.dtodorov.darkroomnegative.helpers.EventDispatcher;
import com.dtodorov.darkroomnegative.helpers.IConverter;
import com.dtodorov.darkroomnegative.helpers.IEventDispatcher;
import com.dtodorov.darkroomnegative.helpers.IEventListener;
import com.dtodorov.darkroomnegative.helpers.UnitDisplayConverter;
import com.dtodorov.darkroomnegative.services.BitmapLoader;
import com.dtodorov.darkroomnegative.services.Brightness;
import com.dtodorov.darkroomnegative.services.ClapDetector;
import com.dtodorov.darkroomnegative.services.DialogPresenter;
import com.dtodorov.darkroomnegative.services.Exposer;
import com.dtodorov.darkroomnegative.services.FullScreen;
import com.dtodorov.darkroomnegative.services.IBrightness;
import com.dtodorov.darkroomnegative.services.IClapDetector;
import com.dtodorov.darkroomnegative.services.IPermissionService;
import com.dtodorov.darkroomnegative.services.IStringResolver;
import com.dtodorov.darkroomnegative.services.PermissionRequester;
import com.dtodorov.darkroomnegative.services.PermissionService;
import com.dtodorov.darkroomnegative.services.StringResolver;
import com.dtodorov.darkroomnegative.services.Toaster;

import java.util.Arrays;

import root.gast.audio.interp.LoudNoiseDetectorAboveNormal;
import root.gast.audio.record.AudioClipRecorder;


public class MainActivity extends AppCompatActivity {
    private final int PICK_PHOTO_FOR_EXPOSURE = 1;

    private MainController _mainController;
    private MainFragment _mainFragment;
    private IEventDispatcher _eventDispatcher;

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
                imageView.setImageBitmap(bitmap);
            }
        });

        IStringResolver stringResolver = new StringResolver(getResources());
        IPermissionService permissionService = new PermissionService(
                new PermissionRequester(this),
                new DialogPresenter(getFragmentManager()));
        IBrightness brightness = new Brightness(getContentResolver(), permissionService);

        _mainController = new MainController(
                _eventDispatcher,
                new Toaster(context, stringResolver),
                new FullScreen(this, contentControl),
                new Exposer(imageView, brightness),
                new BitmapLoader(context),
                new AsyncFilterTask(new Grayscale(renderScriptContextFactory)),
                new AsyncFilterTask(
                        new CompositeFilter(
                                Arrays.asList(
                                        new Invert(renderScriptContextFactory),
                                        new Rotate(180.0f)))),
                new ClapDetector(),
                permissionService,
                stringResolver,
                brightness
        );


        FragmentManager fm = getFragmentManager();
        _mainFragment = (MainFragment) fm.findFragmentByTag("mainFragment");

        if(_mainFragment == null) {
            _mainFragment = new MainFragment();
            fm.beginTransaction().add(_mainFragment, "mainFragment").commit();

            _eventDispatcher.emit("disableView", R.id.beginExposureButton);
            _mainController.setExposureTime(5);
        } else {
            Bitmap positiveImage = _mainFragment.getObject(MainFragment.POSITIVE_IMAGE);
            _mainFragment.putObject(MainFragment.POSITIVE_IMAGE, null);

            Bitmap negativeImage = _mainFragment.getObject(MainFragment.NEGATIVE_IMAGE);
            _mainFragment.putObject(MainFragment.NEGATIVE_IMAGE, null);

            Integer exposureTime = _mainFragment.getObject(MainFragment.EXPOSURE_TIME);
            _mainFragment.putObject(MainFragment.EXPOSURE_TIME, null);

            _mainController.restoreImages(positiveImage, negativeImage);
            _mainController.setExposureTime(exposureTime);

            _mainController.fire(MainController.Trigger.Home);
        }

        exposureTimeControl.setProgress(_mainController.getExposureTime());
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        _mainFragment.putObject(MainFragment.POSITIVE_IMAGE, _mainController.getPositiveBitmap());
        _mainFragment.putObject(MainFragment.NEGATIVE_IMAGE, _mainController.getNegativeBitmap());
        _mainFragment.putObject(MainFragment.EXPOSURE_TIME, _mainController.getExposureTime());
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
