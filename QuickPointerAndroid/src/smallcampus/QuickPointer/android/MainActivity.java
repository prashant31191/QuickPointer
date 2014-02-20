package smallcampus.QuickPointer.android;

import smallcampus.QuickPointer.android.fragment.AbstractFragment;
import smallcampus.QuickPointer.android.fragment.ConnectionFragment;
import smallcampus.QuickPointer.android.fragment.ControllerFragment;
import smallcampus.QuickPointer.android.fragment.IntroductionFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class MainActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//set up Fragment manager
		changeFragmentHandler = new ChangeFragmentHandler(getSupportFragmentManager());
		
		changeFragmentHandler.changeFragment(IntroductionFragment.id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
		
	private static ChangeFragmentHandler changeFragmentHandler; 
	public static ChangeFragmentHandler getChangeFragmentHandler(){
		return changeFragmentHandler;
		
	}
	
	public class ChangeFragmentHandler{
		private FragmentManager manager;
		
		public ChangeFragmentHandler(FragmentManager fragmentManager) {
			manager = fragmentManager;
		}
		
		public void changeFragment(int fragmentId){
			AbstractFragment fragment = null;
			switch(fragmentId){
			case IntroductionFragment.id:
				fragment = new IntroductionFragment();
				break;
			case ConnectionFragment.id:
				fragment = new ConnectionFragment();
				break;
			case ControllerFragment.id:
				fragment = new ControllerFragment();
				break;
			default:
				return;
			}
						
			FragmentTransaction transaction = manager.beginTransaction();

			// Replace whatever is in the fragment_container view with this fragment,
			// and add the transaction to the back stack so the user can navigate back
			transaction.replace(R.id.fragment_container, fragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
		}
	}

}
