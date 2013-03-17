package android.course.project.sos;

import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.course.project.sos.domain.Contact;
import android.course.project.sos.utility.DatabaseHelper;
import android.course.project.sos.utility.MorseCodeConverter;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

public class AlertActivity extends Activity{

	// TODO: Create background process for 
	// 		 sending SMS, Phone Call and Email
	//       Flash Light as main activity and put phone on vibrate 
	
	private GridLayout main_view;
	private ToneGenerator audioToPlay;
	private DatabaseHelper dbHelper;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flash_sos);
		dbHelper=new DatabaseHelper(this);
		
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

	        audioToPlay = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
	        audioToPlay.startTone(ToneGenerator.TONE_SUP_RINGTONE);

	        
	        Button stopRingButton = (Button) findViewById(R.id.stopTone);       
	        stopRingButton.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View view){
	        		audioToPlay.stopTone();
	        	}
	        });
	        
	        
	}
	
	private void sendSMSAlert(){
		List<Contact> contacts = dbHelper.getAllPhoneSMSContacts();
		StringBuffer str = new StringBuffer();
		for (Contact contact: contacts){
			str.append(contact.getPhoneNumber());
			str.append(";");
		}	
		PendingIntent pi = PendingIntent.getActivity(this, 0,new Intent(this, AlertActivity.class), 0);                
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(str.toString(), null, getResources().getString(R.string.alert_message), null, null);   
	}
	
	private void sendPhoneCallAlert(){
		List<Contact> contacts = dbHelper.getAllPhoneCallContacts();
		for (Contact contact: contacts){
			Intent intent = new Intent(Intent.ACTION_CALL);
	    	intent.setData(Uri.parse("tel:"+contact.getPhoneNumber()));
	    	startActivity(intent);

		}
	}
	
	private void sendEmailAlert(){
		List<Contact> contacts = dbHelper.getAllEmailContacts();
		StringBuffer str = new StringBuffer();
		for (Contact contact: contacts){
			str.append(contact.getEmail());
			str.append(";");
		}
		try {
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            String emailRecipient[] = new String[] {str.toString()};
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailRecipient);
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.alert_subject));
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.alert_message));
            startActivity(emailIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Error Sending Email", Toast.LENGTH_LONG).show();
        }
		
		Toast.makeText(this, "Sending Email....", Toast.LENGTH_LONG).show();

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
