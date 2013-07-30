package com.uniovi.aariane;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.uniovi.aariane.persistence.AdventureTable;
import com.uniovi.aariane.persistence.DataProviderContract;
import com.uniovi.aariane.persistence.MissionTable;
import com.uniovi.aariane.util.MapConfiguration;

/**
 * A fragment representing a single Adventure detail screen. This fragment is
 * either contained in a {@link AdventureListActivity} in two-pane mode (on
 * tablets) or a {@link AdventureDetailActivity} on handsets.
 */
public class AdventureDetailFragment extends Fragment implements
		OnClickListener {

	private GoogleMap map;
	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onClick(String id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onClick(String id) {
		}
	};
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Uri mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public AdventureDetailFragment() {
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			String id = getArguments().getString(ARG_ITEM_ID);

			mItem = Uri.parse(DataProviderContract.ADVENTURES_CONTENTURI + "/"
					+ id);
		}
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (map == null) {
			FragmentManager fm = getActivity().getSupportFragmentManager();
			SupportMapFragment smf = ((SupportMapFragment) fm
					.findFragmentById(R.id.map));
			map = smf.getMap();
			// Check if we were successful in obtaining the map.
			if (map != null) {

				String[] projection = { MissionTable.COL_NAM,
						MissionTable.COL_DES, MissionTable.COL_LAT,
						MissionTable.COL_LNG };

				Cursor data = getActivity().getContentResolver().query(mItem,
						projection, null, null, null);

				// add markers to map
				MapConfiguration.addMarkers(data, map);
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_adventure_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			String[] projection = { AdventureTable.COL_NAM,
					AdventureTable.COL_DES };

			Cursor cursor = getActivity().getContentResolver().query(mItem,
					projection, null, null, null);

			if (cursor != null) {

				cursor.moveToFirst();

				((TextView) rootView.findViewById(R.id.tv_adventure))
						.setText(cursor.getString(cursor
								.getColumnIndexOrThrow(projection[0])));

				((TextView) rootView.findViewById(R.id.tv_description))
						.setText(cursor.getString(cursor
								.getColumnIndexOrThrow(projection[1])));

			}

		}

		setUpMapIfNeeded();
		rootView.findViewById(R.id.bt_start).setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onClick(View v) {
		mCallbacks.onClick(getArguments().getString(ARG_ITEM_ID));
	}

}
