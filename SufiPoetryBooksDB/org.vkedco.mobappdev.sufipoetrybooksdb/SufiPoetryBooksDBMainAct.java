package org.vkedco.mobappdev.sufipoetrybooksdb;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SufiPoetryBooksDBMainAct extends ListActivity {

	static final String LOGTAG = SufiPoetryBooksDBMainAct.class.getSimpleName() + "_LOG";
	static final String XML_ENTRY_SEPARATOR = ";";
	//static final String SUFI_POETRY_DB_NAME = "sufipoetry.db";
	
	static Resources mRes = null;
	static String[] mBookTitles = null;
	static ArrayAdapter<String> mAdapter = null;
	
	SufiPoetryBooksDBMainAct mThisAct = null;
	SufiPoetryBooksDBHelper mDBHelper = null;
	SufiPoetryBooksDBApp mApp = null;
	
	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mRes = this.getResources();
		mBookTitles = mRes.getStringArray(R.array.book_titles);
		mAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_expandable_list_item_1,
				mBookTitles);
		mAdapter.notifyDataSetChanged();
		
		mThisAct = this;
		mApp = ((SufiPoetryBooksDBApp)this.getApplication());
		mApp.setDBHelper(new SufiPoetryBooksDBHelper(this, SufiPoetryBooksDBContract.SUFI_POETRY_DB_NAME, null, 1));
		mDBHelper = mApp.getDBHelper();
		if ( !SufiPoetryBooksDBHelper.doesDatabaseExist2(this.getApplicationContext()) )
			this.populateBookList();
		
		Log.d(LOGTAG, SufiPoetryBooksDBContract.SUFI_POETRY_DB_PATH);


		getListView().setAdapter(mAdapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				try {
					String title = ((TextView) view).getText().toString();
					String info = mDBHelper.retrieveBookInfoFromTitle(title);
					if ( "".equals(info) ) {
						info = "This book is not in the database";
					}
					Toast.makeText(mThisAct, info, Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					//System.out.println(e.toString());
					e.printStackTrace();
				}
			}
		});
	}
	
	private void populateBookList() {
	    	mDBHelper.open();
	    	
	    	String[] book_table  = mRes.getStringArray(R.array.book_table);
	    	String[] author_table = mRes.getStringArray(R.array.author_table);
	    	
	    	String[] book_entry_parts;
	    	for(String book_entry: book_table) {
	    		book_entry_parts = book_entry.trim().split(XML_ENTRY_SEPARATOR);
	    		Log.d(LOGTAG, book_entry);
	    		
	    		mDBHelper.insertUniqueBook(new Book(book_entry_parts[0], // title
	    								  book_entry_parts[1], // author
	    								  book_entry_parts[2], // translator
	    								  book_entry_parts[3], // isbn
	    								  Float.parseFloat(book_entry_parts[4]) // price
	    				));
	    	}
	    	
	    	String[] author_entry_parts;
	    	for(String author_entry: author_table) {
	    		author_entry_parts = author_entry.trim().split(XML_ENTRY_SEPARATOR);
	    		Log.d(LOGTAG, author_entry);
	    		mDBHelper.insertUniqueAuthor(
	    				new Author(author_entry_parts[0], // author's name
	    						   Integer.parseInt(author_entry_parts[1]), // author's birth year
	    						   Integer.parseInt(author_entry_parts[2]), // author's death year
	    						   author_entry_parts[3]) // author's country
	    		); 									  
	    	}

	    	mDBHelper.close();
	    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sufi_poetry_books_dbmain, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		else if ( id == R.id.insert_book) {
			Intent i = new Intent(getApplicationContext(), SufiPoetryBooksDBInserter.class);
			startActivity(i);
		}
		else if ( id == R.id.delete_book) {
			Intent i = new Intent(getApplicationContext(), SufiPoetryBooksDBDeleter.class);
			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}

	

}
