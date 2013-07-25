package com.uniovi.aariane.persistence;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.SparseArray;

public class ArianeContentProvider extends ContentProvider {
	// database
	private ArianeDatabaseHelper database;

	// Indicates that the incoming query is for an adventure
	public static final int ADVENTURES_QUERY = 1;
	// Indicates that the incoming query is for an adventure
	public static final int ADVENTURE_ID_QUERY = 2;
	// Indicates that the incoming query is for an adventure
	public static final int MISSIONS_QUERY = 3;
	// Indicates that the incoming query is for an adventure
	public static final int MISSION_ID_QUERY = 4;

	// Indicates an invalid content URI
	public static final int INVALID_URI = -1;
	// Defines a helper object that matches content URIs to table-specific
	// parameters
	private static final UriMatcher sUriMatcher;

	// Stores the MIME types served by this provider
	private static final SparseArray<String> sMimeTypes;

	/*
	 * Initializes meta-data used by the content provider: - UriMatcher that
	 * maps content URIs to codes - MimeType array that returns the custom MIME
	 * type of a table
	 */
	static {

		// Creates an object that associates content URIs with numeric codes
		sUriMatcher = new UriMatcher(0);

		/*
		 * Sets up an array that maps content URIs to MIME types, via a mapping
		 * between the URIs and an integer code. These are custom MIME types
		 * that apply to tables and rows in this particular provider.
		 */
		sMimeTypes = new SparseArray<String>();

		// Adds a URI "match" entry that maps adventures URLs content URIs to a
		// numeric code
		sUriMatcher.addURI(DataProviderContract.AUTHORITY,
				DataProviderContract.ADVENTURES_PATH, ADVENTURES_QUERY);

		// Adds a URI "match" entry that maps adventure URLs content URIs to a
		// numeric code
		sUriMatcher
				.addURI(DataProviderContract.AUTHORITY,
						DataProviderContract.ADVENTURES_PATH + "/#",
						ADVENTURE_ID_QUERY);

		// Adds a URI "match" entry that maps missions URLs content URIs to a
		// numeric code
		sUriMatcher.addURI(DataProviderContract.AUTHORITY,
				DataProviderContract.MISSIONS_PATH, MISSIONS_QUERY);

		// Adds a URI "match" entry that maps missions URLs content URIs to a
		// numeric code
		sUriMatcher.addURI(DataProviderContract.AUTHORITY,
				DataProviderContract.MISSIONS_PATH + "/#", MISSION_ID_QUERY);

		// Specifies a custom MIME type for the Adventures table (MULTIPLE)
		sMimeTypes.put(ADVENTURES_QUERY, "vnd.android.cursor.dir/vnd."
				+ DataProviderContract.AUTHORITY + "." + AdventureTable.TABLE);

		// Specifies a custom MIME type for the Adventures table (SINGLE)
		sMimeTypes.put(ADVENTURE_ID_QUERY, "vnd.android.cursor.item/vnd."
				+ DataProviderContract.AUTHORITY + "." + AdventureTable.TABLE);

		// Specifies a custom MIME type for the Adventures table (MULTIPLE)
		sMimeTypes.put(MISSIONS_QUERY, "vnd.android.cursor.dir/vnd."
				+ DataProviderContract.AUTHORITY + "." + MissionTable.TABLE);

		// Specifies a custom MIME type for the Adventures table (SINGLE)
		sMimeTypes.put(MISSION_ID_QUERY, "vnd.android.cursor.item/vnd."
				+ DataProviderContract.AUTHORITY + "." + MissionTable.TABLE);
	}

	public ArianeContentProvider() {
	}

	/**
	 * Returns an UnsupportedOperationException if delete is called
	 * 
	 * @see android.content.ContentProvider#delete(Uri, String, String[])
	 * @param uri
	 *            The content URI
	 * @param selection
	 *            The SQL WHERE string. Use "?" to mark places that should be
	 *            substituted by values in selectionArgs.
	 * @param selectionArgs
	 *            An array of values that are mapped to each "?" in selection.
	 *            If no "?" are used, set this to NULL.
	 * 
	 * @return the number of rows deleted
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// Implement this to handle requests to delete one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns the mimeType associated with the Uri (query).
	 * 
	 * @see android.content.ContentProvider#getType(Uri)
	 * @param uri
	 *            the content URI to be checked
	 * @return the corresponding MIMEtype
	 */
	@Override
	public String getType(Uri uri) {
		// return sMimeTypes.get(sUriMatcher.match(uri));
		return null;
	}

	/**
	 * 
	 * Insert a single row into a table
	 * 
	 * @see android.content.ContentProvider#insert(Uri, ContentValues)
	 * @param uri
	 *            the content URI of the table
	 * @param values
	 *            a {@link android.content.ContentValues} object containing the
	 *            row to insert
	 * @return the content URI of the new row
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// Creates a writable database or gets one from cache
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		// id of the new row
		long id = 0;

		// Decode the URI to choose which action to take
		switch (sUriMatcher.match(uri)) {

		// For the modification date table
		case ADVENTURES_QUERY:
			// Inserts the row into the table and returns the new row's _id
			// value
			id = sqlDB.insert(AdventureTable.TABLE, null, values);
			break;

		case MISSIONS_QUERY:
			// Inserts the row into the table and returns the new row's _id
			// value
			id = sqlDB.insert(MissionTable.TABLE, null, values);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		// If the insert succeeded, notify a change and return the new row's
		// content URI.
		if (-1 != id) {
			getContext().getContentResolver().notifyChange(uri, null);
			return Uri.withAppendedPath(uri, Long.toString(id));
		} else {

			throw new SQLiteException("Insert error:" + uri);
		}

	}

	@Override
	public boolean onCreate() {
		database = new ArianeDatabaseHelper(getContext());
		return true;
	}

	/**
	 * Returns the result of querying the chosen table.
	 * 
	 * @see android.content.ContentProvider#query(Uri, String[], String,
	 *      String[], String)
	 * @param uri
	 *            The content URI of the table
	 * @param projection
	 *            The names of the columns to return in the cursor
	 * @param selection
	 *            The selection clause for the query
	 * @param selectionArgs
	 *            An array of Strings containing search criteria
	 * @param sortOrder
	 *            A clause defining the order in which the retrieved rows should
	 *            be sorted
	 * @return The query results, as a {@link android.database.Cursor} of rows
	 *         and columns
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		// Using SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// Check if the caller has requested a column which does not exists
		checkColumns(projection);

		// Decodes the content URI and maps it to a code
		switch (sUriMatcher.match(uri)) {

		// If the query is for a picture URL
		case ADVENTURES_QUERY:
			// Set the table
			queryBuilder.setTables(AdventureTable.TABLE);
			break;
		case ADVENTURE_ID_QUERY:
			// Set the table
			queryBuilder.setTables(AdventureTable.TABLE);
			// Adding the ID to the original query
			queryBuilder.appendWhere(AdventureTable.COL_ID + "="
					+ uri.getLastPathSegment());
			break;
		// If the query is for a picture URL
		case MISSIONS_QUERY:
			// Set the table
			queryBuilder.setTables(MissionTable.TABLE);
			break;
		case MISSION_ID_QUERY:
			// Set the table
			queryBuilder.setTables(MissionTable.TABLE);
			// Adding the ID to the original query
			queryBuilder.appendWhere(MissionTable.COL_ID + "="
					+ uri.getLastPathSegment());
			break;
		case INVALID_URI:
			throw new IllegalArgumentException("Query -- Invalid URI:" + uri);
		}

		SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		// Make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	/**
	 * Updates one or more rows in a table.
	 * 
	 * @see android.content.ContentProvider#update(Uri, ContentValues, String,
	 *      String[])
	 * @param uri
	 *            The content URI for the table
	 * @param values
	 *            The values to use to update the row or rows. You only need to
	 *            specify column names for the columns you want to change. To
	 *            clear the contents of a column, specify the column name and
	 *            NULL for its value.
	 * @param selection
	 *            An SQL WHERE clause (without the WHERE keyword) specifying the
	 *            rows to update. Use "?" to mark places that should be
	 *            substituted by values in selectionArgs.
	 * @param selectionArgs
	 *            An array of values that are mapped in order to each "?" in
	 *            selection. If no "?" are used, set this to NULL.
	 * 
	 * @return int The number of rows updated.
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO: Implement this to handle requests to update one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private void checkColumns(String[] projection) {
		String[] available = { AdventureTable.COL_ID, AdventureTable.COL_NAM,
				AdventureTable.COL_DES, AdventureTable.COL_CAT,
				AdventureTable.COL_BEGD, AdventureTable.COL_ENDD,
				AdventureTable.COL_LAT, AdventureTable.COL_LNG };

		if (projection != null) {

			HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(available));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException(
						"Unknown columns in projection");
			}
		}
	}
}
