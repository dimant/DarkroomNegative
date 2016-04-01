package root.gast.audio.record;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * Created by diman on 3/31/2016.
 */
public class AudioClipRecorder implements IAudioClipRecorder {
    private final String TAG = "AudioClipRecorder";
    private int _sampleRate = 8000;
    private int _format = AudioFormat.ENCODING_PCM_16BIT;

    private Boolean _isRunning;
    private AudioRecord _recorder;
    private IAudioClipListener _listener;

    public AudioClipRecorder(IAudioClipListener listener) {
        _listener = listener;
        _isRunning = false;
    }

    private int determineMinimumBufferSize(final int sampleRate, int encoding)
    {
        int minBufferSize =
                AudioRecord.getMinBufferSize(sampleRate,
                        AudioFormat.CHANNEL_IN_MONO, encoding);
        return minBufferSize;
    }

    @Override
    public void start() {
        if(_isRunning)
            return;

        int bufferSize = determineMinimumBufferSize(_sampleRate, _format);

        _recorder = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                _sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                _format,
                bufferSize * 3 // give it extra space to prevent overflow
        );
        _recorder.startRecording();
        _isRunning = true;

        final short[] readBuffer = new short[bufferSize];
        while(tryToHear(readBuffer) == false);
        stop();
    }

    private synchronized boolean tryToHear(short[] readBuffer) {
        boolean heard = false;
        if(_isRunning) {
            int result = _recorder.read(readBuffer, 0, readBuffer.length);

            switch(result) {
                case AudioRecord.ERROR_INVALID_OPERATION:
                    Log.e(TAG, "ERROR_INVALID_OPERATION");
                    break;
                case AudioRecord.ERROR_BAD_VALUE:
                    Log.e(TAG, "ERROR_BAD_VALUE");
                    break;
                default:
                    heard = _listener.heard(readBuffer, _sampleRate);
            }
        }

        return heard;
    }

    @Override
    public synchronized void stop() {
        _isRunning = false;
        _recorder.stop();
        _recorder.release();
        _recorder = null;
    }
}
