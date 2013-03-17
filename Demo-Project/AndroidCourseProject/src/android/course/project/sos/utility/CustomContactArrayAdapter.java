package android.course.project.sos.utility;

import java.util.List;

import android.content.Context;
import android.course.project.sos.R;
import android.course.project.sos.domain.Contact;
import android.course.project.sos.domain.ContactView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomContactArrayAdapter extends ArrayAdapter<Contact>{

	private LayoutInflater inflater;
	private static int layoutId = R.layout.simplerow;
	private static int textViewId = R.id.rowTextView;

	public CustomContactArrayAdapter( Context context, List<Contact> contactList ) {
		super( context, R.layout.simplerow, R.id.rowTextView, contactList );
		// Cache the LayoutInflate to avoid asking for a new one each time.
		inflater = LayoutInflater.from(context) ;
	}
	public CustomContactArrayAdapter( Context context, int resource, int textViewResourceId, List<Contact> contactList ) {
		super( context, resource, textViewResourceId, contactList );
		// Cache the LayoutInflate to avoid asking for a new one each time.
		inflater = LayoutInflater.from(context) ;
		layoutId = resource;
		textViewId = textViewResourceId;
	}
	

	public View getView(int position, View convertView, ViewGroup parent) {
		// contact to display
		Contact contact = (Contact) this.getItem( position ); 

		// The child views in each row.
		TextView textView ; 

		// Create a new row view
		if ( convertView == null ) {
			convertView = inflater.inflate(layoutId, null);

			// Find the child views.
			textView = (TextView) convertView.findViewById( textViewId );

			// Optimization: Tag the row with it's child views, so we don't have to 
			// call findViewById() later when we reuse the row.
			convertView.setTag( new ContactView(textView) );

		}
		// Reuse existing row view
		else {
			// Because we use a ViewHolder, we avoid having to call findViewById().
			ContactView viewHolder = (ContactView) convertView.getTag();
			textView = viewHolder.getTextView() ;
		}

		textView.setText( contact.getName() );      

		return convertView;
	}

}

