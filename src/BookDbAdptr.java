package org.vkedco.mobappdev.simple_db_app_01;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;


public class BookDbAdptr {
	
	public static final String LOGTAG = BookDbAdptr.class.getSimpleName() + "_TAG";
	
	public static final String DB_NAME      = "book_info.db";
	public static final int    DB_VERSION   = 1;
	public static final String BOOK_TABLE   = "book";
	public static final String AUTHOR_TABLE = "author";

	// publish constants for table column names
	public static final String BOOK_ID_COL_NAME         = "ID";
	public static final String BOOK_TITLE_COL_NAME      = "Title";
	public static final String BOOK_AUTHOR_COL_NAME 	= "Author";
	public static final String BOOK_TRANSLATOR_COL_NAME = "Translator";
	public static final String BOOK_ISBN_COL_NAME		= "ISBN";
	public static final String BOOK_PRICE_COL_NAME	    = "Price";
	
	// publish constants for table column numbers
	public static final int BOOK_ID_COL_NUM         = 0;
	public static final int BOOK_TITLE_COL_NUM      = 1;
	public static final int BOOK_AUTHOR_COL_NUM     = 2;
	public static final int BOOK_TRANSLATOR_COL_NUM	= 3;
	public static final int BOOK_ISBN_COL_NUM		= 4;
	public static final int BOOK_PRICE_COL_NUM		= 5;
	
	public static final String AUTHOR_ID_COL_NAME 	  	  = "ID";
	public static final String AUTHOR_NAME_COL_NAME   	  = "Name";
	public static final String AUTHOR_BIRTH_YEAR_COL_NAME = "BirthYear";
	public static final String AUTHOR_DEATH_YEAR_COL_NAME = "DeathYear";
	public static final String AUTHOR_COUNTRY_COL_NAME    = "Country";
	
	public static final int AUTHOR_ID_COL_NUM 		  = 0;
	public static final int AUTHOR_NAME_COL_NUM       = 1;
	public static final int AUTHOR_BIRTH_YEAR_COL_NUM = 2;
	public static final int AUTHOR_DEATH_YEAR_COL_NUM = 3;
	public static final int AUTHOR_COUNTRY_COL_NUM    = 4;
	
	private SQLiteDatabase   	 mDb = null;
	private Context    	         mContext = null;
	private bookInfoDBOpenHelper mDbHelper = null;
	
	// bookInfoDBOpenHelper class creates the table in the database
	private static class bookInfoDBOpenHelper extends SQLiteOpenHelper {
		
		public bookInfoDBOpenHelper(Context context, String name, 
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}
		
		// table creation string constant
		static final String BOOK_TABLE_CREATE =
			"create table " + BOOK_TABLE + 
			" (" + 
			BOOK_ID_COL_NAME         + " integer primary key autoincrement, " + 
			BOOK_TITLE_COL_NAME      + " text not null, " + 
			BOOK_AUTHOR_COL_NAME     + " text not null, " +
			BOOK_TRANSLATOR_COL_NAME + " text not null, " + 
			BOOK_ISBN_COL_NAME		 + " text not null, " +
			BOOK_PRICE_COL_NAME      + " float not null " + 
			");";
		
		static final String AUTHOR_TABLE_CREATE =
			"create table " + AUTHOR_TABLE + 
			" (" + 
			AUTHOR_ID_COL_NAME    + " integer primary key autoincrement, " + 
			AUTHOR_NAME_COL_NAME  + " text not null, " + 
			AUTHOR_BIRTH_YEAR_COL_NAME + " integer not null, " +
			AUTHOR_DEATH_YEAR_COL_NAME + " integer not null, " +
			AUTHOR_COUNTRY_COL_NAME + " text not null " + 
			");"; 

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(BOOK_TABLE_CREATE);
			db.execSQL(AUTHOR_TABLE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, 
				int newVersion) {
			Log.d(LOGTAG, "Upgrading from version " +
					oldVersion + " to " +
					newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + BOOK_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + AUTHOR_TABLE);
			onCreate(db);
		}
	}
	
	// initialize the mContext and the helper objects
	public BookDbAdptr(Context context) {
		mContext = context;
		mDbHelper = new bookInfoDBOpenHelper(context, DB_NAME, null, DB_VERSION);
	}
	
	// open either writeable or, if that is impossible,
	// readable database
	public void open() throws SQLiteException {
		try {
			mDb = mDbHelper.getWritableDatabase();
			Log.d(LOGTAG, "WRITEABLE DB CREATED");
		}
		catch ( SQLiteException ex ) {
			Log.d(LOGTAG, "READABLE DB CREATED");
			mDb = mDbHelper.getReadableDatabase();
		}
	}
	
	public void close() {
		mDb.close();
	}
	
	// Strongly typed insertion method but inserts multiple books with the same ISBN.
	public long insertBook(Book book) {
		ContentValues newBook = new ContentValues();
		newBook.put(BOOK_TITLE_COL_NAME, book.getTitle());
		newBook.put(BOOK_AUTHOR_COL_NAME, book.getAuthor());
		newBook.put(BOOK_ISBN_COL_NAME, book.getISBN());
		newBook.put(BOOK_TRANSLATOR_COL_NAME, book.getTranslator());
		newBook.put(BOOK_PRICE_COL_NAME, book.getPrice());
		long insertedRowIndex = mDb.insertWithOnConflict(BOOK_TABLE, null, newBook, SQLiteDatabase.CONFLICT_REPLACE);
		Log.d(LOGTAG, "Inserted book record " + insertedRowIndex);
		return insertedRowIndex;
	}
	
	// Strongly typed insertion method. Insert the book if and only if it is not already
	// in the database.
	public long insertUniqueBook(Book book) {
		ContentValues newBook = new ContentValues();
		newBook.put(BOOK_TITLE_COL_NAME, book.getTitle());
		newBook.put(BOOK_AUTHOR_COL_NAME, book.getAuthor());
		newBook.put(BOOK_ISBN_COL_NAME, book.getISBN());
		newBook.put(BOOK_TRANSLATOR_COL_NAME, book.getTranslator());
		newBook.put(BOOK_PRICE_COL_NAME, book.getPrice());
		//long insertedRowIndex = mDb.insert(BOOK_TABLE, null, newBook);
		Cursor rslt = mDb.query(BOOK_TABLE, new String[] { BOOK_ISBN_COL_NAME }, BOOK_ISBN_COL_NAME + "=" + book.getISBN(), 
				null, null, null, null);
		long insertedRowIndex = -1;
		if ((rslt.getCount() == 0 || !rslt.moveToFirst()) ) {
			insertedRowIndex =  mDb.insertWithOnConflict(BOOK_TABLE, null, newBook, SQLiteDatabase.CONFLICT_REPLACE);	
		}		
		
		// close the curstor and make it completely invalid
		rslt.close();
		Log.d(LOGTAG, "Inserted book record " + insertedRowIndex);
		return insertedRowIndex;
	}
	
	public long insertUniqueAuthor(Author author) {
		ContentValues newBook = new ContentValues();
		newBook.put(this.AUTHOR_NAME_COL_NAME, author.getName());
		newBook.put(this.AUTHOR_BIRTH_YEAR_COL_NAME, author.getBirthYear());
		newBook.put(this.AUTHOR_DEATH_YEAR_COL_NAME, author.getDeathYear());
		newBook.put(this.AUTHOR_COUNTRY_COL_NAME, author.getCountry());
		Cursor rslt = mDb.query(AUTHOR_TABLE, new String[] { AUTHOR_NAME_COL_NAME }, 
				AUTHOR_NAME_COL_NAME + "=" + "\"" + author.getName() + "\"", 
				null, null, null, null);
		long insertedRowIndex = -1;
		if ((rslt.getCount() == 0 || !rslt.moveToFirst()) ) {
			insertedRowIndex =  mDb.insertWithOnConflict(AUTHOR_TABLE, null, newBook, SQLiteDatabase.CONFLICT_REPLACE);	
		}		
		
		// close the cursor and make it completely invalid
		rslt.close();
		Log.d(LOGTAG, "Inserted author record " + insertedRowIndex);
		return insertedRowIndex;
	}
}
