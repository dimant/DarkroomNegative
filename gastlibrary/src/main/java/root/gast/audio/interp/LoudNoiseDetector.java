/*
 * Copyright 2012 Greg Milette and Adam Stroud
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package root.gast.audio.interp;

import root.gast.audio.record.IAudioClipListener;
import android.util.Log;

/**
 * @author Greg Milette &#60;<a
 *         href="mailto:gregorym@gmail.com">gregorym@gmail.com</a>&#62;
 * 
 */
public class LoudNoiseDetector implements IAudioClipListener
{
    private static final String TAG = "LoudNoiseDetector";


    public static final int DEFAULT_LOUDNESS_THRESHOLD = 2000;
    private double volumeThreshold = DEFAULT_LOUDNESS_THRESHOLD;

    private static final boolean DEBUG = false;

    private ILoudNoiseListener _listener;

    public LoudNoiseDetector()
    {
    }

    public LoudNoiseDetector(ILoudNoiseListener listener) {
        _listener = listener;
    }

    public void setListener(ILoudNoiseListener listener) {
        _listener = listener;
    }

    public LoudNoiseDetector(double volumeThreshold)
    {
        this.volumeThreshold = volumeThreshold;
    }

    @Override
    public boolean heard(short[] data, int sampleRate)
    {
        if(data == null) {
            _listener.heard();
        }

        boolean heard = false;
        // use rms to take the entire audio signal into account
        // and discount any one single high amplitude
        double currentVolume = rootMeanSquared(data);
        if (DEBUG)
        {
            Log.d(TAG, "current: " + currentVolume + " threshold: "
                    + volumeThreshold);
        }

        if (currentVolume > volumeThreshold)
        {
            Log.d(TAG, "heard");
            heard = true;
            if(_listener != null) {
                _listener.heard();
            }
        }

        return heard;
    }

    private double rootMeanSquared(short[] nums)
    {
        double ms = 0;
        for (int i = 0; i < nums.length; i++)
        {
            ms += nums[i] * nums[i] / nums.length;
        }

        return Math.sqrt(ms);
    }
}
