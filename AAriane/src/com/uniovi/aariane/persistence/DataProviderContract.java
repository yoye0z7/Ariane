package com.uniovi.aariane.persistence;

import android.net.Uri;
import android.provider.BaseColumns;

public final class DataProviderContract implements BaseColumns {

	// The URI scheme used for content URIs
	public static final String SCHEME = "content";

	// The provider's authority
	public static final String AUTHORITY = "es.uniovi.android.ariane.contentprovider";

	/**
	 * The DataProvider content URI
	 */
	public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);

	/**
	 * The DataProvider content base path for one ADVENTURE_ID
	 */
	public static final String ADVENTURE_ID_PATH = "adventure";
	/**
	 * The DataProvider base path for ADVENTURE_IDs
	 */
	public static final String ADVENTURES_PATH = "adventures";
	
	/**
	 * The DataProvider content base path for one ADVENTURE_ID
	 */
	public static final String MISSION_ID_PATH = "mission";
	/**
	 * The DataProvider base path for ADVENTURE_IDs
	 */
	public static final String MISSIONS_PATH = "missions";

	/**
	 * The MIME type for a content URI that would return multiple rows
	 * <P>
	 * Type: TEXT
	 * </P>
	 */
	public static final String MIME_TYPE_ROWS = "vnd.android.cursor.dir/vnd.es.uniovi.android.ariane.contentprovider";

	/**
	 * The MIME type for a content URI that would return a single row
	 * <P>
	 * Type: TEXT
	 * </P>
	 * 
	 */
	public static final String MIME_TYPE_SINGLE_ROW = "vnd.android.cursor.item/vnd.es.uniovi.android.ariane.contentprovider";

	/**
	 * ADVENTURE table content URI (multiple rows)
	 */
	public static final Uri ADVENTURES_CONTENTURI = Uri.withAppendedPath(
			CONTENT_URI, ADVENTURES_PATH);
	
	/**
	 * Mission table content URI (multiple rows)
	 */
	public static final Uri MISSIONS_CONTENTURI = Uri.withAppendedPath(
			CONTENT_URI, MISSIONS_PATH);
}
