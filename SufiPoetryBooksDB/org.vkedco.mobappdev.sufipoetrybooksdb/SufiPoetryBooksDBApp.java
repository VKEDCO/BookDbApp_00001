package org.vkedco.mobappdev.sufipoetrybooksdb;

import android.app.Application;

public class SufiPoetryBooksDBApp extends Application {
	
	private SufiPoetryBooksDBHelper mDBHelper = null;
	
	public SufiPoetryBooksDBApp() {}
	
	public SufiPoetryBooksDBHelper getDBHelper() {
		return mDBHelper;
	}
	
	public void setDBHelper(SufiPoetryBooksDBHelper dbHelper) {
		mDBHelper = dbHelper;
	}

}
