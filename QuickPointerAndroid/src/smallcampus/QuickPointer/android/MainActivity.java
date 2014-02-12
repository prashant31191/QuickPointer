package smallcampus.QuickPointer.android;

import smallcampus.QuickPointer.android.fragment.AbstractFragment;
import smallcampus.QuickPointer.android.fragment.AbstractFragment.ChangeFragmentHandler;
import smallcampus.QuickPointer.android.fragment.ConnectionFragment;
import smallcampus.QuickPointer.android.fragment.ControllerFragment;
import smallcampus.QuickPointer.android.fragment.IntroductionFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		AbstractFragment fragment = new IntroductionFragment();
		fragment.setChangeFragmentHandler(changeFragmentHandler);
		getSupportFragmentManager().beginTransaction()
        .add(R.id.fragment_container, fragment).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private ChangeFragmentHandler changeFragmentHandler = new ChangeFragmentHandler(){

		@Override
		public void changeFragment(int fragmentId) {
			AbstractFragment fragment = null;
			switch(fragmentId){
			case R.integer.introduction_fragment:
				fragment = new IntroductionFragment();
				break;
			case R.integer.connection_fragment:
				fragment = new ConnectionFragment();
				break;
			case R.integer.controller_fragment:
				fragment = new ControllerFragment();
				break;
			default:
				return;
			}
			
			fragment.setChangeFragmentHandler(changeFragmentHandler);
			
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

			// Replace whatever is in the fragment_container view with this fragment,
			// and add the transaction to the back stack so the user can navigate back
			transaction.replace(R.id.fragment_container, fragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
		}};
		

}
