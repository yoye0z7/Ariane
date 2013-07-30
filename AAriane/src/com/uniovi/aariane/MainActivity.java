package com.uniovi.aariane;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class MainActivity extends Activity {

	private static final String DIALOG_ABOUT = "description";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//	    Logger LOG = LoggerFactory.getLogger(MainActivity.class);
//	    LOG.info("hello world");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
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
		FragmentTransaction ft = getFragmentManager().beginTransaction();

		Fragment prev = getFragmentManager().findFragmentByTag(DIALOG_ABOUT);

		if (prev != null) {
			ft.remove(prev);
		}

		ft.addToBackStack(null);

		AboutDialog.newInstance().show(ft, DIALOG_ABOUT);
	}

	private static class AboutDialog extends DialogFragment implements
			OnClickListener {

		static AboutDialog newInstance() {
			return new AboutDialog();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			getDialog().setTitle(
					getResources().getString(R.string.developed_about));

			View v = inflater
					.inflate(R.layout.fragment_about, container, false);

			v.findViewById(R.id.bt_close).setOnClickListener(this);

			return v;
		}

		@Override
		public void onClick(View v) {
			dismiss();
		}
	}
}
