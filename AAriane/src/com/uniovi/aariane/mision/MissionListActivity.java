package com.uniovi.aariane.mision;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.uniovi.aariane.R;
import com.uniovi.aariane.R.id;
import com.uniovi.aariane.R.layout;
import com.uniovi.aariane.R.menu;
import com.uniovi.aariane.persistence.AdventureTable;
import com.uniovi.aariane.persistence.DataProviderContract;

/**
 * An activity representing a list of Missions. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link MissionDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link MissionListFragment} and the item details (if present) is a
 * {@link MissionDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link MissionListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class MissionListActivity extends SherlockFragmentActivity implements
		MissionListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mission_list);

		if (findViewById(R.id.mission_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((MissionListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.mission_list))
					.setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.adventure_list, menu);
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
		case R.id.action_create:
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
		values.put(AdventureTable.COL_NAM, "mision");
		values.put(AdventureTable.COL_CAT, "categoria");
		values.put(AdventureTable.COL_BEGD, "BEGD");
		values.put(AdventureTable.COL_ENDD, "ENDD");
		values.put(AdventureTable.COL_DES, "Descripcion");
		values.put(AdventureTable.COL_LAT, "0.0");
		values.put(AdventureTable.COL_LNG, "0.0");

		getContentResolver().insert(DataProviderContract.MISSIONS_CONTENTURI,
				values);

	}

	/**
	 * Callback method from {@link MissionListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(MissionDetailFragment.ARG_ITEM_ID, id);
			MissionDetailFragment fragment = new MissionDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.mission_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, MissionDetailActivity.class);
			detailIntent.putExtra(MissionDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}
