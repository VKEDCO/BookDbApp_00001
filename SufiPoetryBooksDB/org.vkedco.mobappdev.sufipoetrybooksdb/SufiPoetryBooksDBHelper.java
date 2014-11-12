package org.vkedco.mobappdev.sufipoetrybooksdb;

import java.io.File;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SufiPoetryBooksDBHelper extends SQLiteOpenHelper {
	
	//public static final String DB_NAME      = "sufipoetry.db";
	public static final int    DB_VERSION   = 1;
	public static final String BOOK_TABLE   = "book";
	public static final String AUTHOR_TABLE = "author";
	
	
	private SQLiteDatabase mDb = null;
	// table creation string constant
	static final String BOOK_TABLE_CREATE =
		"create table " + BOOK_TABLE + 
		" (" + 
		SufiPoetryBooksDBContract.DBColumns.BOOK_ID_COL_NAME         + " integer primary key autoincrement, " + 
		SufiPoetryBooksDBContract.DBColumns.BOOK_TITLE_COL_NAME      + " text not null, " + 
		SufiPoetryBooksDBContract.DBColumns.BOOK_AUTHOR_COL_NAME     + " text not null, " +
		SufiPoetryBooksDBContract.DBColumns.BOOK_TRANSLATOR_COL_NAME + " text not null, " + 
		SufiPoetryBooksDBContract.DBColumns.BOOK_ISBN_COL_NAME		 + " text not null, " +
		SufiPoetryBooksDBContract.DBColumns.BOOK_PRICE_COL_NAME      + " float not null " + 
	");";
			
	static final String AUTHOR_TABLE_CREATE =
		"create table " + AUTHOR_TABLE + 
		" (" + 
		SufiPoetryBooksDBContract.DBColumns.AUTHOR_ID_COL_NAME    + " integer primary key autoincrement, " + 
		SufiPoetryBooksDBContract.DBColumns.AUTHOR_NAME_COL_NAME  + " text not null, " + 
		SufiPoetryBooksDBContract.DBColumns.AUTHOR_BIRTH_YEAR_COL_NAME + " integer not null, " +
		SufiPoetryBooksDBContract.DBColumns.AUTHOR_DEATH_YEAR_COL_NAME + " integer not null, " +
		SufiPoetryBooksDBContract.DBColumns.AUTHOR_COUNTRY_COL_NAME + " text not null " + 
	");"; 
	
	public SufiPoetryBooksDBHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(BOOK_TABLE_CREATE);
		db.execSQL(AUTHOR_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(SufiPoetryBooksDBContract.LOGTAG, "Upgrading from version " +
				oldVersion + " to " +
				newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + BOOK_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + AUTHOR_TABLE);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(SufiPoetryBooksDBContract.LOGTAG, "Downgrading from version " +
				oldVersion + " to " +
				newVersion + ", which will destroy all old data");
		onUpgrade(db, oldVersion, newVersion);
	}
	
	// open either writeable or, if that is impossible,
	// readable database
	public void open() throws SQLiteException {
		try {
			mDb = this.getWritableDatabase();
			Log.d(SufiPoetryBooksDBContract.LOGTAG, "WRITEABLE DB CREATED");
		}
		catch ( SQLiteException ex ) {
			Log.d(SufiPoetryBooksDBContract.LOGTAG, "READABLE DB CREATED");
			mDb = this.getReadableDatabase();
		}
	}
	
	public void close() {
		mDb.close();
	}
	
	// Strongly typed insertion method but inserts multiple books with the same ISBN.
	public long insertBook(Book book) {
		if ( ! mDb.isOpen() ) 
			this.open();
		ContentValues newBook = new ContentValues();
		newBook.put(SufiPoetryBooksDBContract.DBColumns.BOOK_TITLE_COL_NAME, book.getTitle());
		newBook.put(SufiPoetryBooksDBContract.DBColumns.BOOK_AUTHOR_COL_NAME, book.getAuthor());
		newBook.put(SufiPoetryBooksDBContract.DBColumns.BOOK_ISBN_COL_NAME, book.getISBN());
		newBook.put(SufiPoetryBooksDBContract.DBColumns.BOOK_TRANSLATOR_COL_NAME, book.getTranslator());
		newBook.put(SufiPoetryBooksDBContract.DBColumns.BOOK_PRICE_COL_NAME, book.getPrice());
		long insertedRowIndex = mDb.insertWithOnConflict(BOOK_TABLE, null, newBook, SQLiteDatabase.CONFLICT_REPLACE);
		Log.d(SufiPoetryBooksDBContract.LOGTAG, "Inserted book record " + insertedRowIndex);
		return insertedRowIndex;
	}
	
	// Strongly typed insertion method but inserts multiple books with the same ISBN.
	public long deleteBook(Book book) {
			if ( ! mDb.isOpen() ) 
				this.open();
			ContentValues newBook = new ContentValues();
			newBook.put(SufiPoetryBooksDBContract.DBColumns.BOOK_TITLE_COL_NAME, book.getTitle());
			newBook.put(SufiPoetryBooksDBContract.DBColumns.BOOK_AUTHOR_COL_NAME, book.getAuthor());
			newBook.put(SufiPoetryBooksDBContract.DBColumns.BOOK_ISBN_COL_NAME, book.getISBN());
			newBook.put(SufiPoetryBooksDBContract.DBColumns.BOOK_TRANSLATOR_COL_NAME, book.getTranslator());
			newBook.put(SufiPoetryBooksDBContract.DBColumns.BOOK_PRICE_COL_NAME, book.getPrice());
			
			final String QUERY_WHERE_CLAUSE = 
					SufiPoetryBooksDBContract.DBColumns.BOOK_TITLE_COL_NAME  + " = ? AND " +
					SufiPoetryBooksDBContract.DBColumns.BOOK_AUTHOR_COL_NAME + " = ? AND " + 
					SufiPoetryBooksDBContract.DBColumns.BOOK_ISBN_COL_NAME   + " = ? AND " +
					SufiPoetryBooksDBContract.DBColumns.BOOK_TRANSLATOR_COL_NAME + " = ?";
			final String[] QUERY_SELECTION_ARGS = new String[]{book.getTitle(), book.getAuthor(),
					book.getISBN(), book.getTranslator()};
			
			long rowsAffected = mDb.delete(SufiPoetryBooksDBHelper.BOOK_TABLE, QUERY_WHERE_CLAUSE, QUERY_SELECTION_ARGS);
			Log.d(SufiPoetryBooksDBContract.LOGTAG, "Row deletion result " + rowsAffected);
			return rowsAffected;
	}
	
	// Strongly typed insertion method. Insert the book if and only if it is not already
	// in the database.
	public long insertUniqueBook(Book book) {
		Log.d(SufiPoetryBooksDBContract.LOGTAG, "Inserting " + book.toString());
		if ( ! mDb.isOpen() ) 
			this.open();
		ContentValues newBook = new ContentValues();
		newBook.put(SufiPoetryBooksDBContract.DBColumns.BOOK_TITLE_COL_NAME, book.getTitle());
		newBook.put(SufiPoetryBooksDBContract.DBColumns.BOOK_AUTHOR_COL_NAME, book.getAuthor());
		newBook.put(SufiPoetryBooksDBContract.DBColumns.BOOK_ISBN_COL_NAME, book.getISBN());
		newBook.put(SufiPoetryBooksDBContract.DBColumns.BOOK_TRANSLATOR_COL_NAME, book.getTranslator());
		newBook.put(SufiPoetryBooksDBContract.DBColumns.BOOK_PRICE_COL_NAME, book.getPrice());
		//long insertedRowIndex = mDb.insert(BOOK_TABLE, null, newBook);
		Cursor rslt = mDb.query(BOOK_TABLE, new String[] 
				{ SufiPoetryBooksDBContract.DBColumns.BOOK_ISBN_COL_NAME }, 
				SufiPoetryBooksDBContract.DBColumns.BOOK_ISBN_COL_NAME + "=" + book.getISBN(), 
				null, null, null, null);
		long insertedRowIndex = -1;
		if ((rslt.getCount() == 0 || !rslt.moveToFirst()) ) {
			insertedRowIndex =  mDb.insertWithOnConflict(BOOK_TABLE, null, newBook, SQLiteDatabase.CONFLICT_REPLACE);	
		}		
		
		// close the curstor and make it completely invalid
		rslt.close();
		Log.d(SufiPoetryBooksDBContract.LOGTAG, "Inserted book record " + insertedRowIndex);
		return insertedRowIndex;
	}
	
	public long insertUniqueAuthor(Author author) {
		if ( ! mDb.isOpen() ) 
			this.open();
		ContentValues newBook = new ContentValues();
		newBook.put(SufiPoetryBooksDBContract.DBColumns.AUTHOR_NAME_COL_NAME, author.getName());
		newBook.put(SufiPoetryBooksDBContract.DBColumns.AUTHOR_BIRTH_YEAR_COL_NAME, author.getBirthYear());
		newBook.put(SufiPoetryBooksDBContract.DBColumns.AUTHOR_DEATH_YEAR_COL_NAME, author.getDeathYear());
		newBook.put(SufiPoetryBooksDBContract.DBColumns.AUTHOR_COUNTRY_COL_NAME, author.getCountry());
		Cursor rslt = mDb.query(AUTHOR_TABLE, new String[] 
				{ SufiPoetryBooksDBContract.DBColumns.AUTHOR_NAME_COL_NAME }, 
				SufiPoetryBooksDBContract.DBColumns.AUTHOR_NAME_COL_NAME + "=" + "\"" + author.getName() + "\"", 
				null, null, null, null);
		long insertedRowIndex = -1;
		if ((rslt.getCount() == 0 || !rslt.moveToFirst()) ) {
			insertedRowIndex =  mDb.insertWithOnConflict(AUTHOR_TABLE, null, newBook, SQLiteDatabase.CONFLICT_REPLACE);	
		}		
		
		// close the cursor and make it completely invalid
		rslt.close();
		Log.d(SufiPoetryBooksDBContract.LOGTAG, "Inserted author record " + insertedRowIndex);
		return insertedRowIndex;
	}
	
	// SELECT Title, Translator, Price FROM book_title WHERE Price < 15 or Translator='C. Barks' ORDER BY Price DESC;
	public String retrieveBookInfoFromTitle(String title) {
		if ( ! mDb.isOpen() ) 
			this.open();
		final String[] QUERY_COLNAMES = new String[]{
				SufiPoetryBooksDBContract.DBColumns.BOOK_TITLE_COL_NAME,
				SufiPoetryBooksDBContract.DBColumns.BOOK_AUTHOR_COL_NAME,
				SufiPoetryBooksDBContract.DBColumns.BOOK_TRANSLATOR_COL_NAME,
				SufiPoetryBooksDBContract.DBColumns.BOOK_PRICE_COL_NAME
		};
		final String QUERY_WHERE_CLAUSE = 
				SufiPoetryBooksDBContract.DBColumns.BOOK_TITLE_COL_NAME + " = ?";
		final String[] QUERY_SELECTION_ARGS = new String[]{title};
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor crsr = 
					db.query(SufiPoetryBooksDBHelper.BOOK_TABLE, 
							QUERY_COLNAMES, 
							QUERY_WHERE_CLAUSE, 
							QUERY_SELECTION_ARGS, 
							null, null, null);
			StringBuffer buffer = new StringBuffer("");
			
			if ( crsr.getCount() != 0 ) {
				crsr.moveToFirst();
				while ( crsr.isAfterLast() == false ) {
					String book_title = crsr.getString(crsr.getColumnIndex(SufiPoetryBooksDBContract.DBColumns.BOOK_TITLE_COL_NAME));
					String book_author = crsr.getString(crsr.getColumnIndex(SufiPoetryBooksDBContract.DBColumns.BOOK_AUTHOR_COL_NAME));
					String book_translator = crsr.getString(crsr.getColumnIndex(SufiPoetryBooksDBContract.DBColumns.BOOK_TRANSLATOR_COL_NAME));
					float book_price = crsr.getFloat(crsr.getColumnIndex(SufiPoetryBooksDBContract.DBColumns.BOOK_PRICE_COL_NAME));
					buffer.append("Title      = " + book_title + "\n");
					buffer.append("Author     = " + book_author + "\n");
					buffer.append("Translator = " + book_translator + "\n");
					buffer.append("Price      = " + book_price + "\n");
					buffer.append("=====================\n");
					crsr.moveToNext();
				}
			}
			
			crsr.close();
			db.close();
			Log.d(SufiPoetryBooksDBContract.LOGTAG, "QUERY_05 Readable DB closed...");
			return buffer.toString();
		}
		catch (SQLiteException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static boolean doesDatabaseExist() {
		boolean rslt = false;
		try{
			SQLiteDatabase dbe = SQLiteDatabase.openDatabase(SufiPoetryBooksDBContract.SUFI_POETRY_DB_PATH, null,0);
			dbe.close();
			rslt = true;
		}
		catch (SQLiteException ex) {
			rslt = false;
		}
	    Log.d(SufiPoetryBooksDBContract.LOGTAG, "DATABASE EXISTS = " + rslt);
	    return rslt;
	}
	
	public static boolean doesDatabaseExist2(Context context) {
		boolean rslt;
		File dbFile = context.getDatabasePath(SufiPoetryBooksDBContract.SUFI_POETRY_DB_NAME);
		rslt = dbFile.exists();
		Log.d(SufiPoetryBooksDBContract.LOGTAG, "DATABASE2 EXISTS = " + rslt);
		return rslt;
	}
}
