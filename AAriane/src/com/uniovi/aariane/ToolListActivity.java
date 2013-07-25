package com.uniovi.aariane;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ToolListActivity extends FragmentActivity {

	private Camera cam;
	private Camera.Parameters ekparam;

	private static final int ACTION_AUDIO = 0;

	private static final String ROOT_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	private static final String AUDIO_DIRECTORY = "/ARIANE/Grabaciones/";
	private static final String VIDEO_DIRECTORY = "/ARIANE/Videos/";
	private static final String PICTURE_DIRECTORY = "/ARIANE/Imagenes/";

	private static final String VIDEO_EXT = ".mp4";
	private static final String PICTURE_EXT = ".jpg";

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
	protected void onPause() {
		super.onPause();
		releaseCamera(); // release the camera immediately on pause event
	}

	@Override
	protected void onResume() {
		super.onResume();
		cam = Camera.open();

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

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK && requestCode == ACTION_AUDIO) {

			if (data.getData() != null) {

				String from = getRealPath(data.getData());
				String to = ROOT_PATH + AUDIO_DIRECTORY;
				moveToArianeDir(from, to);
			}
		}
	}

	/**
	 * 
	 */
	public void turnOnLantern() {

		ekparam = cam.getParameters();

		if (ekparam.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH))
			ekparam.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
		else
			ekparam.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

		cam.setParameters(ekparam);
	}

	/**
	 * 
	 * @param name
	 */
	public void recordAudio() {
		Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
		startActivityForResult(intent, ACTION_AUDIO);
	}

	/**
	 * 
	 * @param name
	 */
	public void recordVideo() {

		createDirectory(ROOT_PATH + VIDEO_DIRECTORY);
		File f = new File(ROOT_PATH + VIDEO_DIRECTORY, "prueba" + VIDEO_EXT);

		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		startActivityForResult(takeVideoIntent, 1);
	}

	/**
	 * 
	 * @param name
	 */
	public void takePicture() {

		createDirectory(ROOT_PATH + PICTURE_DIRECTORY);
		File f = new File(ROOT_PATH + PICTURE_DIRECTORY, "prueba" + PICTURE_EXT);

		Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
		i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		startActivityForResult(i, 2);
	}

	/**
	 * 
	 * @param contentUri
	 * @return
	 */
	private String getRealPath(Uri contentUri) {

		Cursor cursor = getContentResolver().query(contentUri,
				new String[] { MediaStore.Audio.Media.DATA }, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	/**
	 * 
	 * @param path
	 * @param to
	 * @return
	 */
	private boolean moveToArianeDir(String path, String to) {
		try {
			// create directory
			createDirectory(to);
			// get file to move
			File from = new File(path);
			// move file
			return from.renameTo(new File(to + from.getName()));

		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * @param path
	 */
	private void createDirectory(String path) {
		File f = new File(path);
		f.mkdirs();
	}

	/**
	 * 
	 */
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
			return SectionFragment.newInstance(position);
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
	public static class SectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		private String tools[];
		private String methods[];
		private String descriptions[];

		public static SectionFragment newInstance(int index) {
			SectionFragment f = new SectionFragment();

			// Supply index input as an argument.
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, index);
			f.setArguments(args);

			return f;
		}

		public SectionFragment() {

		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			Resources res = getResources();
			tools = res.getStringArray(R.array.tools);
			methods = res.getStringArray(R.array.methods);
			descriptions = res.getStringArray(R.array.descriptions);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_tool_list_dummy,
					container, false);

			((TextView) rootView.findViewById(R.id.section_label))
					.setText(tools[getArguments().getInt(ARG_SECTION_NUMBER)]);
			
			((TextView) rootView.findViewById(R.id.tv_tool_description))
			.setText(descriptions[getArguments().getInt(ARG_SECTION_NUMBER)]);

//			ImageView image = (ImageView) rootView
//					.findViewById(R.id.iv_tool_image);
			Button bt_tool = (Button) rootView.findViewById(R.id.bt_tool);
			bt_tool.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					java.lang.reflect.Method method;
					try {
						method = getActivity().getClass().getMethod(
								methods[getArguments().getInt(
										ARG_SECTION_NUMBER)]);

						method.invoke(getActivity());
					} catch (SecurityException e) {
						// ...
					} catch (NoSuchMethodException e) {
						// ...
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			});

			return rootView;
		}
	}

}
