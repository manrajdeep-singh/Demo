package android.course.project.sos.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.course.project.sos.domain.Contact;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private  static final String dbName="sosSettingDB";
	private  static final String contactTable="EmergencyContact";
	private  static final String colID="ContactID";
	private  static final String colName="ContactName";
	private  static final String colPhone="Phone";
	private  static final String colEmail="Email";
	private  static final String colisSms="isSms";
	private  static final String colisPhone="isPhone";
	private  static final String colisEmail="isEmail";
	private  static final int DATABASE_VERSION = 1;

	private String[] allColumns = { colID,
									colName,
									colPhone,
									colEmail,
									colisSms,
									colisPhone,
									colisEmail
								};

	public DatabaseHelper(Context context) {
		super(context, dbName, null,DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE "+contactTable+" ("+colID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
				colName+" TEXT, "+
				colPhone+" Integer, "+
				colEmail+" TEXT,  "+
				colisSms+" Integer DEFAULT 0,  "+
				colisPhone+" Integer DEFAULT 0,  "+
				colisEmail+" Integer DEFAULT 0	);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS "+contactTable);
		onCreate(db);
	}

	public void addContact(Contact data)
	{
		SQLiteDatabase db= this.getWritableDatabase();

		ContentValues cv=new ContentValues();

		cv.put(colName, data.getName());
		cv.put(colPhone, data.getPhoneNumber());
		cv.put(colEmail, data.getEmail());
		cv.put(colisEmail, data.isEmail());
		cv.put(colisPhone, data.isPhone());
		cv.put(colisSms, data.isSms());

		db.insert(contactTable, colName, cv);
		db.close();
	}

	int getContactCount()
	{
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cur= db.rawQuery("Select * from "+contactTable, null);
		int x= cur.getCount();
		cur.close();
		return x;
	}

	Cursor getAllPhoneCallContact()
	{
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cur= db.rawQuery("SELECT * FROM "+contactTable+" where "+colisPhone+" = 1",null);
		return cur;

	}

	Cursor getAllSMSContact()
	{
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cur= db.rawQuery("SELECT * FROM "+contactTable+" where "+colisSms+" = 1",null);
		return cur;

	}

	Cursor getAllEmailContact()
	{
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cur= db.rawQuery("SELECT * FROM "+contactTable+" where "+colisEmail+" = 1",null);
		return cur;

	}

	Cursor getAllConfiguredContact()
	{
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cur= db.rawQuery("SELECT * FROM "+contactTable,null);
		return cur;

	}


	public void DeleteContact(Contact contact)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		db.delete(contactTable,colID+"=?", new String [] {String.valueOf(contact.getID())});
		db.close();
	}

	
	public List<Contact> getAllContacts() {
	    List<Contact> contacts = new ArrayList<Contact>();
	    SQLiteDatabase db=this.getWritableDatabase();
	    
	    Cursor cursor = db.query(contactTable,allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	       Contact contact = cursorToContact(cursor);
	      contacts.add(contact);
	      cursor.moveToNext();
	    }

	    cursor.close();
	    return contacts;
	  }

	  Contact cursorToContact(Cursor cursor) {
		Contact contact = new Contact();
		contact.setID(cursor.getString(0));
		contact.setName(cursor.getString(1));
		contact.setPhoneNumber(cursor.getString(2));
		contact.setSms(cursor.getInt(3) == 1 ? true : false); // is sms
		contact.setPhone(cursor.getInt(4) == 1 ? true : false); // is phone
		contact.setEmail(cursor.getInt(5) == 1 ? true : false); // is email
		
	    return contact;
	  }

}
