package android.course.project.sos;

import android.app.Activity;
import android.course.project.sos.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class TimerActivity extends Activity {
	
    TextView mButtonLabel;
    
    // Counter of time since app started ,a background task
    private long mStartTime = 0L;
    private TextView mTimerLabel;
    
    // in seconds
    private int TIME_TO_WAIT = 5;
    // Handler to handle the message to the timer task
    private Handler mHandler = new Handler();
           
    String timerStop1;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
                        		     
        mTimerLabel = (TextView) findViewById(R.id.textTimer);
        
        if(mStartTime == 0L){
        	mStartTime = SystemClock.uptimeMillis();
        	mHandler.removeCallbacks(mUpdateTimeTask);
        	mHandler.postDelayed(mUpdateTimeTask, 100);
        }
        
        Button timerCancelButton = (Button) findViewById(R.id.btnCancel);       
        timerCancelButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view){
        		       	
        		stopTimer();	
        	}
        });
        
        Button configurationButton = (Button) findViewById(R.id.btnSetting);       
        configurationButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view){
        		stopTimer();       	
        		launchConfigurationManager();	
        	}
        });
                     
    } // OnCreate
    
    private Runnable mUpdateTimeTask = new Runnable(){

		public void run() {
			
			final long start = mStartTime;
			long millis = SystemClock.uptimeMillis()- start;
			       	
			int seconds = (int) (millis / 1000);
			int minutes = seconds / 60;
			seconds = seconds % 60;
			
			mTimerLabel.setText("" + minutes + ":" + String.format("%02d", seconds));					
			
			timerStop1 = minutes + ":" + String.format("%02d", seconds);
								
			mHandler.postDelayed(this, 200);	
			//check if button was clicked in 10 secs
			if (seconds > TIME_TO_WAIT ) {
				stopTimer();
				Toast.makeText(TimerActivity.this, "Time to wait exceeded the given limit",
	                    Toast.LENGTH_SHORT).show();
//				launchContactManager();
//				launchEmergencyCallManager();
				launchAlertActivity();
				
			}
			
		}

    }; 
    
    private void stopTimer(){
    	mHandler.removeCallbacks(mUpdateTimeTask);
		mTimerLabel.setText(timerStop1);
		mStartTime = 0L;
    }
    
    /**
     * Launches the ContactManager activity .
     */
    protected void launchContactManager() {
    	Intent intent = new Intent(this, ContactManagerActivity.class);
    	startActivity(intent);		 
    }
    
    /**
     * Launches the emergency call activity.
     */
    protected void launchEmergencyCallManager() {
    	Intent intent = new Intent(Intent.ACTION_CALL);
    	intent.setData(Uri.parse("tel:4255559622"));
    	startActivity(intent);
    }
    
    /**
     * Launches the Setting activity .
     */
    protected void launchConfigurationManager() {
    	Intent intent = new Intent(this, SosSettingActivity.class);
    	startActivity(intent);		 
    }

    /**
     * Launches the Alert activity .
     */
    private void launchAlertActivity() {
    	Intent intent = new Intent(this, AlertActivity.class);
    	startActivity(intent);		 
		
	} 	
    
} 