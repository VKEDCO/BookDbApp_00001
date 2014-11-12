package org.vkedco.mobappdev.sufipoetrybooksdb;

import android.os.Environment;
import android.provider.BaseColumns;

public class SufiPoetryBooksDBContract {
	
	public static final String LOGTAG = SufiPoetryBooksDBContract.class.getSimpleName() + "_TAG";
	public static final String SUFI_POETRY_DB_NAME = "sufipoetry.db";
	public static final String FORWARD_SLASH = "/";
	public static final String APP_PACKAGE = "org.vkedco.mobappdev.sufipoetrybooksdb";
	public static String SUFI_POETRY_DB_PATH = null;
	
	static {
		SUFI_POETRY_DB_PATH = 
				Environment.getDataDirectory().toString() + FORWARD_SLASH +
						"data" + FORWARD_SLASH + APP_PACKAGE + FORWARD_SLASH + 
						"databases" + FORWARD_SLASH + SUFI_POETRY_DB_NAME;
	}
	
	public static abstract class DBColumns implements BaseColumns {
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
	}

}
