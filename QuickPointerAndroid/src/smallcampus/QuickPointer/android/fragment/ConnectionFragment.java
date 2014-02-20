package smallcampus.QuickPointer.android.fragment;

import java.net.SocketException;
import java.net.UnknownHostException;

import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import smallcampus.QuickPointer.Config;
import smallcampus.QuickPointer.android.ConnectionManager;
import smallcampus.QuickPointer.android.MainActivity;
import smallcampus.QuickPointer.android.QPBluetoothClient;
import smallcampus.QuickPointer.android.R;
import smallcampus.QuickPointer.android.viewManager.EditTextManager;
import smallcampus.QuickPointer.net.BaseClient;
import smallcampus.QuickPointer.net.TCP.QPTcpUdpClient;
import smallcampus.QuickPointer.util.EventListener;


public class ConnectionFragment extends AbstractFragment {
	public static final int id = 2;
	@Override
	protected void setUI() {
		final Spinner connectionType = (Spinner) mView.findViewById(R.id.sp_connection_type);		
		final EditText hostname = (EditText) mView.findViewById(R.id.et_hostname);
		final EditTextManager etManager= new EditTextManager(mView,R.id.et_hostname);
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.connection_type, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		connectionType.setAdapter(adapter);
		
		connectionType.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String type = connectionType.getItemAtPosition(arg2).toString();
				//TODO restore last input
				if(type.equals("TCP")){
					etManager.changeHint("IP address");
				}else if(type.equals("Bluetooth")){
					etManager.changeHint("Bluetooth MAC address");
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		((Button) mView.findViewById(R.id.btn_connect)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//TODO check format and connect
				//TODO save input
				//Set up Connection
				BaseClient client = null;
				
				String type = connectionType.getSelectedItem().toString();
				if(type.equals("TCP")){
					try {
						client = new QPTcpUdpClient(hostname.getText().toString(),Config.DEFAULT_TCP_SERVER_PORT,Config.DEFAULT_UDP_SERVER_PORT);
					} catch (UnknownHostException e) {
						Toast.makeText(getActivity(), "Unknown host", Toast.LENGTH_SHORT).show();
					} catch (SocketException e) {
						Toast.makeText(getActivity(), "Socket Exception", Toast.LENGTH_SHORT).show();
					}
				}else if(type.equals("Bluetooth")){
					client = new QPBluetoothClient(hostname.getText().toString());
				}
				
				//set action when server is connected
				client.setOnServerConnectedListener(new EventListener(){
					@Override
					public void perform(Object args) {
						//Change to controller UI
						MainActivity.getChangeFragmentHandler().changeFragment(ControllerFragment.id);
					}
				});
				client.setOnServerConnectFailureListener(new EventListener(){
					@Override
					public void perform(Object args) {
						getActivity().runOnUiThread(new Runnable() {
							  public void run() {
							    Toast.makeText(getActivity(), "Connection Failure", Toast.LENGTH_SHORT).show();
							  }
							});
					}
				});
				
				client.connect();
				
				//save the connection
				ConnectionManager.getInstance().setConnection(client);
			}
		});
	}
	
	@Override
	public int getFragmentLayoutId() {
		return R.layout.fragment_connection;
	}

}
