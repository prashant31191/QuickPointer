package smallcampus.QuickPointer.android;

import smallcampus.QuickPointer.android.fragment.IntroductionFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getSupportFragmentManager().beginTransaction()
        .add(R.id.fragment_container, new IntroductionFragment()).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private OnChangeFragmentListener mOnChangeFragmentListener = new OnChangeFragmentListener(){

		@Override
		public void onChangeFragment(int fragmentId) {
			Fragment fragment = null;
			switch(fragmentId){
			//TODO
			default:
				fragment = new IntroductionFragment();
				((IntroductionFragment)fragment).setmOnChangeFragmentListener(mOnChangeFragmentListener);
				break;
			}
			
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

			// Replace whatever is in the fragment_container view with this fragment,
			// and add the transaction to the back stack so the user can navigate back
			transaction.replace(R.id.fragment_container, fragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
		}};
	
	public interface OnChangeFragmentListener{
		public void onChangeFragment(int fragmentId);
	}
}
