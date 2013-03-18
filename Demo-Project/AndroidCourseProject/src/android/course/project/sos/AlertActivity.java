package android.course.project.sos;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.course.project.sos.domain.Contact;
import android.course.project.sos.utility.DatabaseHelper;
import android.course.project.sos.utility.MorseCodeConverter;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;


public class AlertActivity extends Activity implements LocationListener{

	// TODO: Create background process for 
	// 		 sending SMS, Phone Call and Email
	//       Flash Light as main activity and put phone on vibrate 
	
	private GridLayout main_view;
	private ToneGenerator audioToPlay;
	private DatabaseHelper dbHelper;
	private LocationManager locationManager;
	private String provider = null;
	private int lat;
	private int lng;
	private StringBuilder strloc;
	private Location geoloc;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flash_sos);
		
		// get location
		Location loc = this.getLocation();
		strloc = new StringBuilder();
		if (loc != null)
			strloc.append("geo: "+loc.getLatitude() + ","+ loc.getLongitude());
		
		dbHelper=new DatabaseHelper(this);
		
		sendEmailAlert();
		sendSMSAlert();
		
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
	
	 public Location getLocation() {
	        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	      
	        if(provider  == null) {
	        	 Criteria criteria = createCriteria();
	             provider = locationManager.getBestProvider(criteria, true);
	        }
	        locationManager.requestLocationUpdates(provider, 0, 0, this);
	        geoloc = locationManager.getLastKnownLocation(provider);
	        
	        return geoloc;
	    }
	    
	    private Criteria createCriteria() {
	        Criteria criteria = new Criteria();
	        criteria.setAccuracy(Criteria.ACCURACY_FINE);
	        criteria.setPowerRequirement(Criteria.POWER_LOW);
	        criteria.setAltitudeRequired(false);
	        criteria.setBearingRequired(false);
	        criteria.setSpeedRequired(false);
	        criteria.setCostAllowed(false);
	        return criteria;
	    }
	
	private void sendSMSAlert(){
		List<Contact> contacts = dbHelper.getAllPhoneSMSContacts();
		StringBuffer str = new StringBuffer();
		for (Contact contact: contacts){
			str.append(contact.getPhoneNumber());
			str.append(";");
		}	
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(str.toString(), null, getResources().getString(R.string.alert_message) + strloc.toString(), null, null);   
	}
	
//	private void sendPhoneCallAlert(){
//		List<Contact> contacts = dbHelper.getAllPhoneCallContacts();
//		for (Contact contact: contacts){
//			Intent intent = new Intent(Intent.ACTION_CALL);
//	    	intent.setData(Uri.parse("tel:"+contact.getPhoneNumber()));
//	    	startActivity(intent);
//
//		}
//	}
	
	private void sendEmailAlert(){
		List<Contact> contacts = dbHelper.getAllEmailContacts();
		StringBuffer str = new StringBuffer();
		for (Contact contact: contacts){
			str.append(contact.getEmail());
			str.append(";");
		}
		try {
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            String emailRecipient[] = new String[] {"manrajdeep@gmail.com"};
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailRecipient);
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.alert_subject));
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.alert_message) + strloc.toString());
//            startActivity(emailIntent);
            startActivity(Intent.createChooser(emailIntent, "Send your email in:"));  
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
    
    /* Request updates at startup */
    @Override
    protected void onResume() {
      super.onResume();
      locationManager.requestLocationUpdates(provider, 2000, 1, this);
    }
    
    @Override
    protected void onPause() {
      super.onPause();
      locationManager.removeUpdates(this); 
    }
    
	public void onLocationChanged(Location location) {
		geoloc = location;
		lat = (int) (location.getLatitude());
	    lng = (int) (location.getLongitude());

	}

	public void onProviderDisabled(String provider) {
		Toast.makeText( this, "Gps Disabled", Toast.LENGTH_SHORT ).show();
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}