package com.uniovi.aariane;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockFragmentActivity {

	private static final String DIALOG_ABOUT = "description";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_about:
			showAboutDialog();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClickAdventure(View v) {
		Intent advIntent = new Intent(this, AdventureListActivity.class);
		startActivity(advIntent);
	}

	public void onClickResult(View v) {

	}

	public void onClickTool(View v) {
		Intent toolIntent = new Intent(this, ToolListActivity.class);
		startActivity(toolIntent);
	}

	private void showAboutDialog() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		Fragment prev = getSupportFragmentManager().findFragmentByTag(
				DIALOG_ABOUT);

		if (prev != null) {
			ft.remove(prev);
		}

		ft.addToBackStack(null);

		AboutDialog.newInstance().show(getSupportFragmentManager(),
				DIALOG_ABOUT);
	}

	/**
	 * A fragment representing an about dialog..
	 */
	public static class AboutDialog extends SherlockDialogFragment implements
			OnClickListener {

		static AboutDialog newInstance() {
			return new AboutDialog();
		}

		public AboutDialog() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			getDialog().setTitle(
					getResources().getString(R.string.developed_about));

			View v = inflater
					.inflate(R.layout.fragment_about, container, false);
			
			DevelopersPagerAdapter mSectionsPagerAdapter = ((MainActivity) getActivity()).new DevelopersPagerAdapter(
					getChildFragmentManager());
			// Set up the ViewPager with the sections adapter.
			ViewPager mViewPager = (ViewPager) v.findViewById(R.id.pager);
			mViewPager.setAdapter(mSectionsPagerAdapter);
	
			return v;
		}

		@Override
		public void onClick(View v) {
			dismiss();
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class DevelopersPagerAdapter extends FragmentPagerAdapter {
		private String developers[] = { "Developer", "Doctor", "Director",
				"CoDirector", "Uniovi" };

		public DevelopersPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return DeveloperFragment.newInstance(position);
		}

		@Override
		public int getCount() {
			return developers.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			return developers[position].toUpperCase(l);
		}
	}

	/**
	 * A fragment representing a participant of the app, but that simply
	 * displays a short description.
	 */
	public static class DeveloperFragment extends Fragment {
		/**
		 * The fragment argument representing the developer number for this
		 * fragment.
		 */
		private static final String ARG_DEVELOPER_NUMBER = "developer_number";

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			if (getArguments().containsKey(ARG_DEVELOPER_NUMBER)) {

			}
		}

		public static DeveloperFragment newInstance(int developer) {
			DeveloperFragment f = new DeveloperFragment();

			// Supply index input as an argument.
			Bundle args = new Bundle();
			args.putInt(ARG_DEVELOPER_NUMBER, developer);
			f.setArguments(args);

			return f;
		}

		public DeveloperFragment() {

		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = inflater.inflate(R.layout.fragment_about_detail,
					container, false);

			return rootView;
		}

	}
}
