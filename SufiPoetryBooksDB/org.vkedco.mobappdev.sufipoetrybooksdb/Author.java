package org.vkedco.mobappdev.sufipoetrybooksdb;

public class Author {

	protected String mName;
	protected int mBirthYear;
	protected int mDeathYear;
	protected String mCountry;
	
	public Author(String name, int by, int dy, String country) {
		mName = name;
		mBirthYear = by;
		mDeathYear = dy;
		mCountry = country;
	}
	
	public String getName() {
		return mName;
	}
	
	public int getBirthYear() {
		return mBirthYear;
	}
	
	public int getDeathYear() {
		return mDeathYear;
	}
	
	public String getCountry() {
		return mCountry;
	}
}
