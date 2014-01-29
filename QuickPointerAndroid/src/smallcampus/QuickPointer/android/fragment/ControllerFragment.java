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

public class ControllerFragment extends Fragment{
	
	private View mView;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_controller, container, false);
        setUI();
        return mView;
    }
	
	private void setUI(){
		((Button) mView.findViewById(R.id.btn_controller_pointer))
			.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), 
							"btn_controller_pointer", Toast.LENGTH_SHORT).show();;
				}});
		((Button) mView.findViewById(R.id.btn_controller_page_up))
			.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), 
							"btn_controller_page_up", Toast.LENGTH_SHORT).show();;
				}});
		((Button) mView.findViewById(R.id.btn_controller_page_down))
			.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), 
							"btn_controller_page_down", Toast.LENGTH_SHORT).show();;
				}});
	}
}
