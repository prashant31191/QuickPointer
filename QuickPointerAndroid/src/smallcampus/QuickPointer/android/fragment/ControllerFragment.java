package smallcampus.QuickPointer.android.fragment;

import smallcampus.QuickPointer.android.R;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ControllerFragment extends AbstractFragment{
    
    @Override
	protected void setUI(){
		((Button) mView.findViewById(R.id.btn_pointer))
			.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), 
							"btn_pointer", Toast.LENGTH_SHORT).show();;
				}});
		((Button) mView.findViewById(R.id.btn_pageup))
			.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), 
							"btn_page_up", Toast.LENGTH_SHORT).show();;
				}});
		((Button) mView.findViewById(R.id.btn_pagedown))
			.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), 
							"btn_page_down", Toast.LENGTH_SHORT).show();;
				}});
	}

    @Override
	public int getFragmentLayoutId() {
		return R.layout.fragment_controller;
	}
}
