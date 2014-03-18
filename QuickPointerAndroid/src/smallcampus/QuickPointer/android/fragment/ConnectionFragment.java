package smallcampus.QuickPointer.android.fragment;

import java.io.IOException;
import java.net.SocketException;

import smallcampus.QuickPointer.Config;
import smallcampus.QuickPointer.android.ConnectionManager;
import smallcampus.QuickPointer.android.MainActivity;
import smallcampus.QuickPointer.android.QPBluetoothClient;
import smallcampus.QuickPointer.android.R;
import smallcampus.QuickPointer.android.Setting;
import smallcampus.QuickPointer.android.viewManager.EditTextManager;
import smallcampus.QuickPointer.net.BaseClient;
import smallcampus.QuickPointer.net.DummyClient;
import smallcampus.QuickPointer.net.TCP.QPTcpUdpClient;
import smallcampus.QuickPointer.util.EventListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class ConnectionFragment extends AbstractFragment {
	public static final int id = 2;
	public static final String Tag = "ConnectionFragment";
	
	@Override
	protected void setUI() {
		//get the setting information
		final String btHistory = Setting.getBtHistory(getActivity());
		final String tcpHistory = Setting.getTcpHistory(getActivity());
		
		final Spinner connectionType = (Spinner) mView.findViewById(R.id.sp_connection_type);		
		final EditText hostname = (EditText) mView.findViewById(R.id.et_hostname);
		final EditTextManager etManager= new EditTextManager(mView,R.id.et_hostname);
		
		//show the history if exist
		if(tcpHistory!=null){
			hostname.setText(tcpHistory);
		}
		
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
				
				if(type.equals("TCP")){
					//show the history if exist
					if(tcpHistory!=null){
						etManager.changeText(tcpHistory);
					}
					etManager.changeHint("IP address");
				}else if(type.equals("Bluetooth")){
					//show the history if exist
					if(btHistory!=null){
						etManager.changeText(btHistory);
					}
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
				
				//debug mode
				if(hostname.getText().toString().equals("Debug")){
					//create a dummy client
					ConnectionManager.getInstance().setConnection(new DummyClient());
					
					MainActivity.getChangeFragmentHandler().changeFragment(ControllerFragment.id);
					
					return;
				}
				
				//Set up Connection
				BaseClient client = null;
				
				String type = connectionType.getSelectedItem().toString();
				if(type.equals("TCP")){
					try {
						client = new QPTcpUdpClient(hostname.getText().toString(),Setting.DEFAULT_TCP_SERVER_PORT,Setting.DEFAULT_UDP_SERVER_PORT);
					} catch (SocketException e) {
						Toast.makeText(getActivity(), "Socket Exception", Toast.LENGTH_SHORT).show();
					}
					
					//save the history
					Setting.setTcpHistory(getActivity(), hostname.getText().toString());
				}else if(type.equals("Bluetooth")){
					client = new QPBluetoothClient(hostname.getText().toString());
					
					//save the history
					Setting.setBtHistory(getActivity(), hostname.getText().toString());
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

	public static void quickConnect(Context context, EventListener success, EventListener failure){
		
		//Set up Connection
		BaseClient client = null;
		
		switch(Setting.getLastHistoryType(context)){
		case Setting.TYPE_TCP:
			try {
				client = new QPTcpUdpClient(Setting.getTcpHistory(context),Setting.DEFAULT_TCP_SERVER_PORT,Setting.DEFAULT_UDP_SERVER_PORT);
			} catch (SocketException e) {
				failure.perform(null);
				e.printStackTrace();
			}
			break;
		case Setting.TYPE_BT:
			client = new QPBluetoothClient(Setting.getBtHistory(context));
			break;
		default:
			failure.perform(null);
			break;
		}
				
		//set action when server is connected
		client.setOnServerConnectedListener(success);
		
		client.setOnServerConnectFailureListener(failure);
		
		client.connect();
		
		//save the connection
		ConnectionManager.getInstance().setConnection(client);
	}

}
