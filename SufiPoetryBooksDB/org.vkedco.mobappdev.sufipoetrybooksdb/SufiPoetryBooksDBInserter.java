package org.vkedco.mobappdev.sufipoetrybooksdb;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SufiPoetryBooksDBInserter extends ListActivity {
	
	static final String LOGTAG = SufiPoetryBooksDBInserter.class.getSimpleName() + "_LOG";
	static final String XML_ENTRY_SEPARATOR = ";";
	//static final String SUFI_POETRY_DB_NAME = "sufipoetry.db";
	
	static Resources mRes = null;
	static String[] mBookTitles = null;
	static String[] mBookEntries = null;
	static ArrayAdapter<String> mAdapter = null;
	SufiPoetryBooksDBInserter mThisAct = null;
	SufiPoetryBooksDBHelper mDBHelper = null;
	SufiPoetryBooksDBApp mApp = null;
	
	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mRes = this.getResources();
		mBookTitles = mRes.getStringArray(R.array.book_titles);
		mBookEntries = mRes.getStringArray(R.array.book_table);
		mAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_expandable_list_item_1,
				mBookTitles);
		mAdapter.notifyDataSetChanged();
		
		mThisAct = this;
		mApp = ((SufiPoetryBooksDBApp)this.getApplication());
		mDBHelper = mApp.getDBHelper();
		
		getListView().setAdapter(mAdapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				try {
					//String title = ((TextView) view).getText().toString();
					mThisAct.insertBook(mBookEntries[pos]);
				} catch (Exception e) {
					//System.out.println(e.toString());
					e.printStackTrace();
				}
			}
		});
	}
	
	private void insertBook(String book_entry) {
		String book_entry_parts[] = book_entry.trim().split(XML_ENTRY_SEPARATOR);
		Log.d(LOGTAG, book_entry);
		
		Book book = new Book(book_entry_parts[0], // title
				  book_entry_parts[1], // author
				  book_entry_parts[2], // translator
				  book_entry_parts[3], // isbn
				  Float.parseFloat(book_entry_parts[4]) // price
				  );
		
		Log.d(LOGTAG, book.toString());
		
		long rowIndex = mDBHelper.insertUniqueBook(book);
		
		if ( rowIndex == -1 ) {
			Toast.makeText(this, book.toString() + "\n" + "is already in the database", 
					Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(this, book.toString() + "\n" + "has been inserted", 
					Toast.LENGTH_LONG).show();
		}
	}

}
