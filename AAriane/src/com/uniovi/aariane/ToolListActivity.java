package com.uniovi.aariane;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import android.app.Activity;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ToolListActivity extends FragmentActivity {

	private Camera cam;
	private Camera.Parameters ekparam;

	private static String tools[];

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
	private SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;

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

		// Load tools
		tools = getResources().getStringArray(R.array.tools);

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

		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK && requestCode == ACTION_AUDIO) {

			if (data.getData() != null) {

				moveToArianeDir(getRealPath(data.getData()), ROOT_PATH
						+ AUDIO_DIRECTORY);
			}
		}
	}

	/**
	 * Lantern tool
	 * 
	 * @category tool
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
	 * Audio recorder tool
	 * 
	 * @category tool
	 */
	public void recordAudio() {
		Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
		startActivityForResult(intent, ACTION_AUDIO);
	}

	/**
	 * Video recorder tool
	 * 
	 * @category tool
	 */
	public void recordVideo() {

		createDirectory(ROOT_PATH + VIDEO_DIRECTORY);
		File f = new File(ROOT_PATH + VIDEO_DIRECTORY, "prueba" + VIDEO_EXT);

		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		startActivityForResult(takeVideoIntent, 1);
	}

	/**
	 * Camera picture
	 * 
	 * @category tool
	 */
	public void takePicture() {

		createDirectory(ROOT_PATH + PICTURE_DIRECTORY);
		File f = new File(ROOT_PATH + PICTURE_DIRECTORY, "prueba" + PICTURE_EXT);

		Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
		i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		startActivityForResult(i, 2);
	}

	/**
	 * Get real path of a picture make by intent
	 * 
	 * @param contentUri
	 *            uri povided
	 * @return Absolute path to image resource
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
	 * Move resource file from public directories to Ariane directory
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
	 * Create a new directroy
	 * 
	 * @param path
	 *            directory/ies to create
	 */
	private void createDirectory(String path) {
		File f = new File(path);
		f.mkdirs();
	}

	/**
	 * Release the camera for other applications
	 */
	private void releaseCamera() {
		if (cam != null) {
			cam.release();
			cam = null;
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return ToolFragment.newInstance(position);
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
	public static class ToolFragment extends Fragment implements
			OnClickListener {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_TOOL_NUMBER = "tool_number";
		private static final String RES_DRAWABLE_TYPE = "drawable";

		private String methods[];
		private String descriptions[];

		private int tool;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			if (getArguments().containsKey(ARG_TOOL_NUMBER)) {
				tool = getArguments().getInt(ARG_TOOL_NUMBER);
			}
		}

		public static ToolFragment newInstance(int tool) {
			ToolFragment f = new ToolFragment();

			// Supply index input as an argument.
			Bundle args = new Bundle();
			args.putInt(ARG_TOOL_NUMBER, tool);
			f.setArguments(args);

			return f;
		}

		public ToolFragment() {

		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);

			methods = getResources().getStringArray(R.array.methods);
			descriptions = getResources().getStringArray(R.array.descriptions);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			int resourceId = getResources().getIdentifier(
					tools[tool].toLowerCase(), RES_DRAWABLE_TYPE,
					this.getActivity().getPackageName());

			View rootView = inflater.inflate(R.layout.fragment_tool_list_dummy,
					container, false);

			((TextView) rootView.findViewById(R.id.tv_tool_description))
					.setText(descriptions[tool]);

			if (resourceId != 0) {
				((ImageView) rootView.findViewById(R.id.iv_tool_image))
						.setImageResource(resourceId);
			}

			rootView.findViewById(R.id.bt_tool).setOnClickListener(this);

			return rootView;
		}

		@Override
		public void onClick(View v) {
			java.lang.reflect.Method method;
			try {
				method = getActivity().getClass().getMethod(methods[tool]);

				method.invoke(getActivity());
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	}

}
