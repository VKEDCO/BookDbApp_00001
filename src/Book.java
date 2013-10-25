package org.vkedco.mobappdev.simple_db_app_01;

public class Book {
	protected String mTitle;
	protected String mAuthor;
	protected String mTranslator;
	protected String mISBN;
	protected float mPrice;
	
	public Book(String title, String author, String translator, String isbn, float price) {
		mTitle = title;
		mAuthor = author;
		mTranslator = translator;
		mISBN = isbn;
		mPrice = price;
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public String getAuthor() {
		return mAuthor;
	}
	
	public String getISBN() {
		return mISBN;
	}
	
	public String getTranslator() {
		return mTranslator;
	}
	
	public float getPrice() {
		return mPrice;
	}
}
