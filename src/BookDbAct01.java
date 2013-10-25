package org.vkedco.mobappdev.simple_db_app_01;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.Menu;

public class BookDbAct01 extends Activity {
	
	static final String LOGTAG = BookDbAct01.class.getSimpleName() + "_LOG";
	Resources mRes = null;
	static final String XML_ENTRY_SEPARATOR = ";";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_db_act01);
        
        BookDbAdptr dbAdptr = new BookDbAdptr(this);
        mRes = getResources();
        populateBookList(dbAdptr);
    }
    
    private void populateBookList(BookDbAdptr dbAdptr) {
    	dbAdptr.open();
    	
    	String[] book_table  = mRes.getStringArray(R.array.book_table);
    	String[] author_table = mRes.getStringArray(R.array.author_table);
    	
    	String[] book_entry_parts;
    	for(String book_entry: book_table) {
    		book_entry_parts = book_entry.trim().split(XML_ENTRY_SEPARATOR);
    		Log.d(LOGTAG, book_entry);
    		
    		dbAdptr.insertUniqueBook(new Book(book_entry_parts[0], // title
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
    		dbAdptr.insertUniqueAuthor(
    				new Author(author_entry_parts[0], // author's name
    						   Integer.parseInt(author_entry_parts[1]), // author's birth year
    						   Integer.parseInt(author_entry_parts[2]), // author's death year
    						   author_entry_parts[3]) // author's country
    		); 									  
    	}

    	dbAdptr.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_simple_db_act01, menu);
        return true;
    }
}
