package com.uniovi.aariane.persistence;

import android.database.sqlite.SQLiteDatabase;

public class MissionTable {
	// Database table
	public static final String TABLE = "t_mission";

	public static final String COL_ID = "_id";
	public static final String COL_NAM = "name";
	public static final String COL_DES = "description";
	public static final String COL_CAT = "category";
	/* Begin and end dates */
	public static final String COL_BEGD = "begin";
	public static final String COL_ENDD = "end";
	public static final String COL_LAT = "lat";
	public static final String COL_LNG = "lng";

	/* many to one relationship */
	public static final String COL_AID = "adventure_id";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(createTable());
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {

		database.execSQL("DROP TABLE IF EXISTS " + TABLE);
		onCreate(database);

	}

	// Database creation SQL statement
	private static String createTable() {

		StringBuilder sb = new StringBuilder();
		sb.append("create table ");
		sb.append(TABLE);
		sb.append("(");
		sb.append(COL_ID);
		sb.append(" integer primary key autoincrement, ");
		sb.append(COL_NAM);
		sb.append(" text not null, ");
		sb.append(COL_DES);
		sb.append(" text not null, ");
		sb.append(COL_CAT);
		sb.append(" text not null, ");
		sb.append(COL_BEGD);
		sb.append(" text not null, ");
		sb.append(COL_ENDD);
		sb.append(" text not null, ");
		sb.append(COL_LAT);
		sb.append(" double not null, ");
		sb.append(COL_LNG);
		sb.append(" double not null);");

		// TODO Include foreign key
		return sb.toString();

	}
}
