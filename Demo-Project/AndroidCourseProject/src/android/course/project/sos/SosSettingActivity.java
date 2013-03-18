package android.course.project.sos;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.course.project.sos.domain.Contact;
import android.course.project.sos.utility.CustomContactArrayAdapter;
import android.course.project.sos.utility.DatabaseHelper;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public final class SosSettingActivity extends Activity {

	private HashMap<String,Contact> selectedContact;
	static final int PICK_CONTACT_REQUEST = 1;  // The request code
	private DatabaseHelper dbHelper;
	private ListView emailListview;
	private ListView phoneSMSListview;
	
	private ArrayAdapter<Contact> phoneSMSAdapter;
	private ArrayAdapter<Contact> emailAdapter;

	// TODO Find better way to handle boolean settings
	private boolean sendEmail = false;
	private boolean phoneCall = false;
	private boolean phoneSms = false;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos_setting);
        selectedContact = new HashMap<String,Contact>();
        
        dbHelper=new DatabaseHelper(this);

//        List<Contact> values = dbHelper.getAllContacts();

        // handles Phone Call Selected contact UI
        phoneSMSListview = (ListView)findViewById(R.id.selectedContactList); 
     // Set our custom array adapter as the ListView's adapter.
        phoneSMSAdapter = new CustomContactArrayAdapter(this,R.layout.selected_phone_contact,R.id.selectedPhoneContactEntryText, dbHelper.getAllPhoneSMSContacts());
        phoneSMSListview.setAdapter(phoneSMSAdapter);				
      
        // handles Phone Call Selected contact UI
        emailListview = (ListView)findViewById(R.id.selectedSMSContactList); 
     // Set our custom array adapter as the ListView's adapter.
        emailAdapter = new CustomContactArrayAdapter(this,R.layout.selected_phone_contact,R.id.selectedPhoneContactEntryText, dbHelper.getAllEmailContacts());
        emailListview.setAdapter(emailAdapter);				
      
        
        Button pickPhoneCallButton = (Button) findViewById(R.id.btnPickSMSContact);       
        pickPhoneCallButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view){
				phoneCall = false;
				phoneSms  = true;
				sendEmail = false;
				initiatePickContact();
        	}
        });
        
        Button pickSmsButton = (Button) findViewById(R.id.btnPickEmailContact);       
        pickSmsButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				phoneCall = false;
				phoneSms  = false;
				sendEmail = true;
				initiatePickContact();
        	}
        });
        
    }
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		switch (reqCode) {
		case (PICK_CONTACT_REQUEST):
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
		        String[] projection = new String[] {
		                ContactsContract.Contacts._ID,
		                ContactsContract.Contacts.DISPLAY_NAME,
		                ContactsContract.Contacts.HAS_PHONE_NUMBER
		        };
		        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
				Cursor c = getContentResolver().query(contactData, projection, null, null, sortOrder);
				if (c.moveToFirst()) {
					String phoneNumber  = "";
					String emailAddress = "";
					Contact selected = new Contact();
					selected.setID(c.getString(c.getColumnIndexOrThrow(ContactsContract.Data._ID)));
					selected.setName(c.getString(c.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME)));
		            if (Integer.parseInt(c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) 
		            {
		                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ selected.getID(),null, null); 
		                while (phones.moveToNext()) { 
		                	phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		                	if (phoneNumber.length() > 0)
		                    	break;
		                } 
		                phones.close(); 
		                
		                // Do same to get email
		                Cursor email = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID +" = "+ selected.getID(),null, null); 
		                while (email.moveToNext()) { 
		                    emailAddress = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
		                    if (emailAddress.length() > 0)
		                    	break;
		                } 
		                email.close(); 

		            }
		            selected.setPhoneNumber(phoneNumber);
		            selected.setEmail(emailAddress);
		            
		            // TODO Find better way to set boolean flags (based on button click)
		            selected.setPhone(phoneCall);
		            selected.setSms(phoneSms);
		            selected.setEmail(sendEmail);
		            
		            dbHelper.addContact(selected);
					selectedContact.put(selected.getID(), selected);
				}
			}
			
			break;
		}
		phoneSMSAdapter.notifyDataSetChanged();
		emailAdapter.notifyDataSetChanged();
	}
	
	private void initiatePickContact(){
		Intent intent = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult(intent, PICK_CONTACT_REQUEST);

	}

}
