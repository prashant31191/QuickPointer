package smallcampus.QuickPointer.android.fragment;

import smallcampus.QuickPointer.android.MainActivity.OnChangeFragmentListener;
import smallcampus.QuickPointer.android.R;
import smallcampus.QuickPointer.android.dialog.ConnectionDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class IntroductionFragment extends Fragment{
	
	private View mView;
	private OnChangeFragmentListener mOnChangeFragmentListener;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_introduction, container, false);
        setUI();
        return mView;
    }
	
	private void setUI(){
		((TextView) mView.findViewById(R.id.tv_introduction)).setText("test");
		((Button) mView.findViewById(R.id.btn_connect_qr))
			.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					ConnectionDialog.showDialog(getActivity());
					Toast.makeText(getActivity(), 
							"connect qr", Toast.LENGTH_SHORT).show();;
				}});
		((Button) mView.findViewById(R.id.btn_connect_manuel))
			.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), 
							"connect manuel", Toast.LENGTH_SHORT).show();;
				}});
	}

	public OnChangeFragmentListener getmOnChangeFragmentListener() {
		return mOnChangeFragmentListener;
	}

	public void setmOnChangeFragmentListener(
			OnChangeFragmentListener mOnChangeFragmentListener) {
		this.mOnChangeFragmentListener = mOnChangeFragmentListener;
	}

}
