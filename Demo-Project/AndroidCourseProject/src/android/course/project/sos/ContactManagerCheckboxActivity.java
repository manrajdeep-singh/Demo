package android.course.project.sos;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.course.project.sos.domain.Contact;
import android.course.project.sos.domain.ContactView;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public final class ContactManagerCheckboxActivity extends Activity
{

	public static final String TAG = "ContactManager";

	private ListView mainContactList;
	private ArrayAdapter<Contact> listAdapter ;
	/**
	 * Called when the activity is first created. Responsible for initializing the UI.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.v(TAG, "Activity State: onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_manager);

		// Obtain handles to UI objects
		mainContactList = (ListView) findViewById(R.id.contactList);


		mainContactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick( AdapterView<?> parent, View item, 
					int position, long id) {
				Contact contact = listAdapter.getItem( position );
				contact.toggleChecked();
				ContactView viewHolder = (ContactView) item.getTag();
				viewHolder.getCheckBox().setChecked( contact.isChecked() );
			}
		});


		// Populate the contact list
		populateContactList();
	}

	/**
	 * Populate the contact list based on account currently selected in the account spinner.
	 */
	private void populateContactList() {
		// Build adapter with contact entries
		Cursor cursor = getContacts();
		String[] fields = new String[] {
				ContactsContract.Data.DISPLAY_NAME
		};
		//        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.contact_entry, cursor,
		//                fields, new int[] {R.id.contactEntryText},0);


		ArrayList<Contact> mArrayList = new ArrayList<Contact>();

		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Contact data = new Contact();
			data.setName(cursor.getString(cursor.getColumnIndexOrThrow(People.DISPLAY_NAME)));
			//            data.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(Phone.NUMBER)));
			data.setPhone(true);
			mArrayList.addAll( Arrays.asList(data) );    
			cursor.moveToNext();
		}


		// Set our custom array adapter as the ListView's adapter.
		listAdapter = new ContactArrayAdapter(this, mArrayList);

		mainContactList.setAdapter(listAdapter);
	}

	/**
	 * Obtains the contact list for the currently selected account.
	 *
	 * @return A cursor for for accessing the contact list.
	 */
	private Cursor getContacts()
	{
		// Run query
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] {
				ContactsContract.Contacts.DISPLAY_NAME
		};
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
		return getContentResolver().query(uri, projection, null, null, sortOrder);
	}

	private static class ContactArrayAdapter extends ArrayAdapter<Contact> {

		private LayoutInflater inflater;

		public ContactArrayAdapter( Context context, List<Contact> contactList ) {
			super( context, R.layout.simplerow, R.id.rowTextView, contactList );
			// Cache the LayoutInflate to avoid asking for a new one each time.
			inflater = LayoutInflater.from(context) ;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// contact to display
			Contact contact = (Contact) this.getItem( position ); 

			// The child views in each row.
			CheckBox checkBox ; 
			TextView textView ; 

			// Create a new row view
			if ( convertView == null ) {
				convertView = inflater.inflate(R.layout.simplerow, null);

				// Find the child views.
				textView = (TextView) convertView.findViewById( R.id.rowTextView );
				checkBox = (CheckBox) convertView.findViewById( R.id.CheckBox01 );

				// Optimization: Tag the row with it's child views, so we don't have to 
				// call findViewById() later when we reuse the row.
				convertView.setTag( new ContactView(textView,checkBox) );

				// If CheckBox is toggled, update the contact it is tagged with.
				checkBox.setOnClickListener( new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v ;
						Contact contact = (Contact) cb.getTag();
						contact.setChecked( cb.isChecked() );
					}
				});        
			}
			// Reuse existing row view
			else {
				// Because we use a ViewHolder, we avoid having to call findViewById().
				ContactView viewHolder = (ContactView) convertView.getTag();
				checkBox = viewHolder.getCheckBox() ;
				textView = viewHolder.getTextView() ;
			}

			// Tag the CheckBox with the Planet it is displaying, so that we can
			// access the contact in onClick() when the CheckBox is toggled.
			checkBox.setTag( contact ); 

			// Display planet data
			checkBox.setChecked( contact.isChecked() );
			textView.setText( contact.getName() );      

			return convertView;
		}

	}


}
