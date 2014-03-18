package smallcampus.QuickPointer.android.fragment;

import smallcampus.QuickPointer.android.MainActivity;
import smallcampus.QuickPointer.android.R;
import smallcampus.QuickPointer.android.Setting;
import smallcampus.QuickPointer.util.EventListener;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class IntroductionFragment extends AbstractFragment{
	public static final int id =1;
	private static final String Tag = "IntroductionFragment";
	
	@Override
	protected void setUI(){
		//get the setting information
		final int historyType = Setting.getLastHistoryType(getActivity());
		final String btHistory = Setting.getBtHistory(getActivity());
		final String tcpHistory = Setting.getTcpHistory(getActivity());
		
		//Quick Start by history button
		final Button qsButton = (Button) mView.findViewById(R.id.btn_connect_qr);
		
		qsButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String host = (btHistory!=null&&btHistory.length()>0)?btHistory:tcpHistory;
				
				final EventListener success = new EventListener(){
					@Override
					public void perform(Object args) {
						// TODO Auto-generated method stub
						MainActivity.getChangeFragmentHandler().changeFragment(ControllerFragment.id);
					}
				};
				
				final EventListener failure = new EventListener(){
					@Override
					public void perform(Object args) {
						// TODO Auto-generated method stub
						getActivity().runOnUiThread(new Runnable() {
							  public void run() {
							    Toast.makeText(getActivity(), "Connection Failure", Toast.LENGTH_SHORT).show();
							  }
							});
					}
				};
				
				ConnectionFragment.quickConnect(getActivity(),success,failure);
				
		}});
		
		//enable the button if there is any history
		qsButton.setEnabled((historyType!=Setting.TYPE_NULL));
		
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
