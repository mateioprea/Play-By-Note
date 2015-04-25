package app.playbynote;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.graphics.*;
import android.widget.TextView;
import android.view.View.OnClickListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordActivity extends ActionBarActivity implements OnClickListener{
    private static final String LOG_TAG = "AudioRecord" ;
    private Button start;
    private Button stop;
    private TextView analysing;
    private AudioRecord audioRecord = null;
    int frequency = 8000;
    int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    int blockSize = 256;
    private String mFileName;
    public boolean started = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        start=(Button)findViewById(R.id.button_record);
        stop=(Button)findViewById(R.id.stop_button);
        analysing = (TextView)findViewById(R.id.analysing);
        analysing.setVisibility(View.GONE);
        start.setOnClickListener(this);
    }

    private void buildFileName(){
        String date = this.getDateTime();
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName +=  "/" + date.replace('/', '_').replace(':', '_').replace(' ', '_') + "_recording";
    }

    private void startRecording() {

        int bufferSize = AudioRecord.getMinBufferSize(frequency,
                channelConfiguration, audioEncoding);
        audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC, frequency,
                channelConfiguration, audioEncoding, bufferSize);

        audioRecord.startRecording();

        System.out.println(audioRecord.getState());
        analysing.setVisibility(View.VISIBLE);
        //System.out.println(started);
            while(started){
                short[] buffer = new short[blockSize];
                double[] toTransform = new double[blockSize];
                int bufferReadResult = audioRecord.read(buffer, 0,
                        blockSize);
                for (int i = 0; i < blockSize && i < bufferReadResult; i++) {
                    toTransform[i] = (double) buffer[i] / 32768.0; // signed
                    // 16
                    //System.out.println(toTransform[i]);
                }
            }

    }
    private void stopRecording(){
        analysing.setVisibility(View.GONE);
        audioRecord.stop();
        System.out.println(audioRecord.getState());
    }
    private void readAudio(){

    }
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String s = dateFormat.format(date);
        return s;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View arg0){
        System.out.println(started);
        if(started){
            started = false;
            startRecording();
        }else{
            started = true;
            stopRecording();
        }
    }
}
