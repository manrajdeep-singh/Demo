package android.course.project.sos;

import android.app.Activity;
import android.course.project.sos.utility.MorseCodeConverter;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

public class AlertActivity extends Activity{

	// TODO: Create background process for 
	// 		 sending SMS, Phone Call and Email
	//       Flash Light as main activity and put phone on vibrate 
	
	private GridLayout main_view;
	private ToneGenerator audioToPlay;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flash_sos);
		
		  main_view = (GridLayout)findViewById(R.id.sos_flash_background);
		  
		  Handler myHandler = new Handler();
	        long[] morseCode = MorseCodeConverter.pattern("SOS");
	        long total = 0;
	        for (int i = 0; i < morseCode.length; i++) {
	            total = total + morseCode[i];
	            long MULTIPLIER = 5;
	            if (i % 2 == 0) {
	                myHandler.postDelayed(new ColorSwitch(total, Color.WHITE), total * MULTIPLIER);
	            } else {
	                myHandler.postDelayed(new ColorSwitch(total, Color.BLACK), total * MULTIPLIER);
	            }
	        }

	        audioToPlay = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
	        audioToPlay.startTone(ToneGenerator.TONE_SUP_RINGTONE);

	        
	        Button stopRingButton = (Button) findViewById(R.id.stopTone);       
	        stopRingButton.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View view){
	        		audioToPlay.stopTone();
	        	}
	        });
	        
	        
	}
	
    class ColorSwitch implements Runnable {

        final int color;
        long delay;

        ColorSwitch(long delay, int color) {
            this.delay = delay;
            this.color = color;
        }

        public void run() {
        	main_view.setBackgroundColor(color);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
