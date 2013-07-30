package com.uniovi.aariane;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uniovi.aariane.persistence.DataProviderContract;
import com.uniovi.aariane.persistence.MissionTable;

public class MissionActivity extends SherlockFragmentActivity implements
		LoaderManager.LoaderCallbacks<Cursor>, ActionBar.OnNavigationListener {

	private GoogleMap map;
	private SimpleCursorAdapter adapter;
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	/**
	 * The fragment argument representing the adventure id for this fragment.
	 */
	static final String ARG_ADV_ID = "adventure_id";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mision);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		createAdapter();
		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				adapter, this);

		setUpMapIfNeeded();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	private void createAdapter() {

		String[] from = new String[] { MissionTable.COL_NAM,
				MissionTable.COL_DES };
		int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

		getSupportLoaderManager().initLoader(0, null, this);

		adapter = new SimpleCursorAdapter(this, R.layout.list_item_activated,
				null, from, to, 0);

	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (map == null) {
			map = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (map != null) {
				// The Map is verified. It is now safe to manipulate the map.
			}
		}
	}

	/**
	 * Backward-compatible version of {@link ActionBar#getThemedContext()} that
	 * simply returns the {@link android.app.Activity} if
	 * <code>getThemedContext</code> is unavailable.
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private Context getActionBarThemedContextCompat() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return getActionBar().getThemedContext();
		} else {
			return this;
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.mision, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_settings:
			/**
			 * @DEBUG
			 */
			saveState();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * @DEBUG
	 */
	private void saveState() {

		ContentValues values = new ContentValues();
		values.put(MissionTable.COL_NAM, "mision");
		values.put(MissionTable.COL_CAT, "categoria");
		values.put(MissionTable.COL_BEGD, "BEGD");
		values.put(MissionTable.COL_ENDD, "ENDD");
		values.put(MissionTable.COL_DES, "Descripcion");
		values.put(MissionTable.COL_LAT, "0.0");
		values.put(MissionTable.COL_LNG, "0.0");
		values.put(MissionTable.COL_AID, 1);
		getContentResolver().insert(DataProviderContract.MISSIONS_CONTENTURI,
				values);

	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		double lat, lng;

		Cursor data = (Cursor) adapter.getItem(position);
		lat = data.getDouble(data.getColumnIndexOrThrow(MissionTable.COL_LAT));
		lng = data.getDouble(data.getColumnIndexOrThrow(MissionTable.COL_LNG));

		map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));

		return true;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		String[] projection = { MissionTable.COL_ID, MissionTable.COL_NAM,
				MissionTable.COL_DES, MissionTable.COL_LAT,
				MissionTable.COL_LNG };
		
		//Always One argument
		String[] selectionArgs = new String[getIntent().getExtras().size()];

		if (getIntent().getExtras().containsKey(ARG_ADV_ID)) {
			selectionArgs[0] = getIntent().getExtras().getString(ARG_ADV_ID);
		}

		CursorLoader cursorLoader = new CursorLoader(this,
				DataProviderContract.MISSIONS_CONTENTURI, projection, null,
				selectionArgs, null);

		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.swapCursor(data);

		double lat, lng;
		if (data.moveToFirst())
			do {
				lat = data.getDouble(data
						.getColumnIndexOrThrow(MissionTable.COL_LAT));
				lng = data.getDouble(data
						.getColumnIndexOrThrow(MissionTable.COL_LNG));

				map.addMarker(new MarkerOptions()
						.position(new LatLng(lat, lng)));

			} while (data.moveToNext());
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// data is not available anymore, delete reference
		adapter.swapCursor(null);
	}

}
