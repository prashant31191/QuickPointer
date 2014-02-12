package smallcampus.QuickPointer.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbstractFragment extends Fragment {
	protected View mView;
	private ChangeFragmentHandler changeFragmentHandler;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(getFragmentLayoutId(), container, false);
        setUI();
        return mView;
    }
	
	protected abstract void setUI();
	
	public abstract int getFragmentLayoutId();
	
	public ChangeFragmentHandler getChangeFragmentHandler() {
		return changeFragmentHandler;
	}

	public void setChangeFragmentHandler(ChangeFragmentHandler changeFragmentHandler) {
		this.changeFragmentHandler = changeFragmentHandler;
	}

	public interface ChangeFragmentHandler{
		public void changeFragment(int fragmentId);
	}
}
