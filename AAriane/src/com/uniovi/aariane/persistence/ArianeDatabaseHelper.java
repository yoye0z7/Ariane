package com.uniovi.aariane.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ArianeDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "ariane.db";
	private static final int DATABASE_VERSION = 1;

	public ArianeDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		AdventureTable.onCreate(db);
		MissionTable.onCreate(db);
	}

	/**
	 * Activate foreign keys
	 */
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		// Enable foreign key constraints
		db.execSQL("PRAGMA foreign_keys=ON;");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		AdventureTable.onUpgrade(database, oldVersion, newVersion);
		MissionTable.onUpgrade(database, oldVersion, newVersion);
	}

}
