package root.gast.audio.record;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;
import android.util.Pair;

/**
 * Created by diman on 3/31/2016.
 */
public class AudioClipRecorder implements IAudioClipRecorder {
    private final String TAG = "AudioClipRecorder";
    private int _sampleRate;

    private Boolean _isRunning;
    private AudioRecord _recorder;
    private IAudioClipListener _listener;

    public AudioClipRecorder(IAudioClipListener listener) {
        _listener = listener;
        _isRunning = false;
    }

    private Pair<AudioRecord, short[]> bruteForceFormat() {
        int sampleRate =  44100;
        short format= AudioFormat.ENCODING_PCM_16BIT;
        short channel = AudioFormat.CHANNEL_IN_MONO;

        try {
            int bufferSize = AudioRecord.getMinBufferSize(sampleRate, channel, format);
            if(bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                AudioRecord record = new AudioRecord(
                        MediaRecorder.AudioSource.DEFAULT,
                        sampleRate,
                        channel,
                        format,
                        bufferSize);

                if(record.getState() == AudioRecord.STATE_INITIALIZED) {
                    _sampleRate = sampleRate;
                    short[] buffer = new short[bufferSize * 3];
                    return new Pair<AudioRecord, short[]>(record, buffer);
                }
            }
        } catch(Exception e) {
            // continue trying
        }

        return null;
    }

    @Override
    public void start() {
        if(_isRunning)
            return;

        Pair<AudioRecord, short[]> recordAllocation = bruteForceFormat();
        short[] readBuffer;

        if(recordAllocation != null) {
            _recorder = recordAllocation.first;
            readBuffer = recordAllocation.second;

            _recorder.startRecording();
            _isRunning = true;

            while(tryToHear(readBuffer) == false);
        } else {
            _listener.heard(null, _sampleRate);
        }

        stop();
    }

    private synchronized boolean tryToHear(short[] readBuffer) {
        boolean heard = false;
        if(_isRunning) {
            int result = _recorder.read(readBuffer, 0, readBuffer.length / 3);

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
