package smallcampus.QuickPointer.android.fragment;

import smallcampus.QuickPointer.android.MainActivity;
import smallcampus.QuickPointer.android.R;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class IntroductionFragment extends AbstractFragment{
	public static final int id =1;
	
	@Override
	protected void setUI(){
		//Connect by QR code button
		((Button) mView.findViewById(R.id.btn_connect_qr))
			.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), 
							"Not implemented yet", Toast.LENGTH_SHORT).show();
				}});
		
		//Connect by manual input button
		((Button) mView.findViewById(R.id.btn_connect_manuel))
			.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					//Change Fragment to dialog_connection
					MainActivity.getChangeFragmentHandler().changeFragment(ConnectionFragment.id);
				}});
	}

	@Override
	public int getFragmentLayoutId() {
		return R.layout.fragment_introduction;
	}
	
}