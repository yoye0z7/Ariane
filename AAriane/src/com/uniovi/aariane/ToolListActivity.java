package com.uniovi.aariane;

import java.io.File;
import java.util.Locale;

import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ToolListActivity extends FragmentActivity {
	private Camera cam;
	private Camera.Parameters ekparam;

	private static final int ACTION_AUDIO = 0;
	private static final int ACTION_VIDEO = 1;
	private static final int ACTION_PICTURE = 2;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tool_list);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tool_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		ekparam = cam.getParameters();
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
		case R.id.action_on:
			// ekparam.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			// cam.setParameters(ekparam);
			// recordAudio();
			 recordVideo(null);
//			takePhoto(null);
			return true;
		case R.id.action_off:
			ekparam.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			cam.setParameters(ekparam);
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			String path = null;
			String directory = Environment.getExternalStorageDirectory()
					.getAbsolutePath();

			switch (requestCode) {
			case ACTION_AUDIO:
				path = getRealPath(data.getData(), MediaStore.Audio.Media.DATA);
				directory += "/ARIANE/Grabaciones/";
				break;
//			case ACTION_VIDEO:
//				path = getRealPath(data.getData(), MediaStore.Video.Media.DATA);
//				directory += "/ARIANE/Videos/";
//				break;
//			case ACTION_PICTURE:
//				path = getRealPath(data.getData(), MediaStore.Images.Media.DATA);
//				directory += "/ARIANE/Imagenes/";
//				break;
			default:
				break;
			}

			moveToArianeDir(path, directory);

			// // Obtener la ruta real
			// String[] proj = { MediaStore.Audio.Media.DATA };
			// Cursor cursor = getContentResolver().query(audioFileUri, proj,
			// null, null, null);
			// int column_index = cursor
			// .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
			// cursor.moveToFirst();
			//
			// // Mover el ficehro
			// try {
			//
			// File afile = new File(cursor.getString(column_index));
			//
			// if (afile.renameTo(new File(Environment
			// .getExternalStorageDirectory().getAbsolutePath()
			// + "/ARIANE/Grabaciones/" + afile.getName()))) {
			// System.out.println("File is moved successful!");
			// } else {
			// System.out.println("File is failed to move!");
			// }
			//
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
		}
	}

	private void recordAudio() {
		Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
		startActivityForResult(intent, ACTION_AUDIO);
	}

	public void recordVideo(View v) {
		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		String nombre = "video";
		File f = new File(Environment.getExternalStorageDirectory()+ "/ARIANE/Videos/", nombre
				+ ".mp4");
		takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		startActivityForResult(takeVideoIntent, 1);
	}

	public void takePhoto(View v) {
		Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
		String nombre = "foto";
		File f = new File(Environment.getExternalStorageDirectory()+ "/ARIANE/Imagenes/", nombre
				+ ".jpg");
		i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		startActivityForResult(i, 2);

	}

	// MediaStore.Audio.Media.DATA
	private String getRealPath(Uri contentUri, String source) {

		Cursor cursor = getContentResolver().query(contentUri,
				new String[] { source }, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private boolean moveToArianeDir(String from, String to) {
		try {
			File f = new File(to);
			f.mkdirs();
			// Create a file with the passed path
			File afile = new File(from);

			return afile.renameTo(new File(to + afile.getName()));

		} catch (Exception e) {

			return false;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseCamera(); // release the camera immediately on pause event
	}

	@Override
	protected void onResume() {
		super.onResume();
		cam = Camera.open();

	}

	private void releaseCamera() {
		if (cam != null) {
			cam.release(); // release the camera for other applications
			cam = null;
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		String tools[];

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			tools = getResources().getStringArray(R.array.tools);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return tools.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			return tools[position].toUpperCase(l);
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_tool_list_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

}
