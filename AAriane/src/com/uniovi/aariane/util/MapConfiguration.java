package com.uniovi.aariane.util;

import android.database.Cursor;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uniovi.aariane.persistence.MissionTable;

public class MapConfiguration {

	public static void addMarkers(Cursor data, GoogleMap map) {

		double lat, lng;
		String title, snippet;

		if (data != null) {

			if (data.moveToFirst())
				do {
					lat = data.getDouble(data
							.getColumnIndexOrThrow(MissionTable.COL_LAT));
					lng = data.getDouble(data
							.getColumnIndexOrThrow(MissionTable.COL_LNG));
					title = data.getString(data
							.getColumnIndexOrThrow(MissionTable.COL_NAM));
					snippet = data.getString(data
							.getColumnIndexOrThrow(MissionTable.COL_DES));

					map.addMarker(new MarkerOptions()
							.position(new LatLng(lat, lng))
							.title(title)
							.snippet(snippet)
							.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

				} while (data.moveToNext());
		}
	}

}
