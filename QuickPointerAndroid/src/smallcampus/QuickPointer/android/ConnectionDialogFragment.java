package smallcampus.QuickPointer.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;



public class ConnectionDialogFragment extends DialogFragment {
	
	private OnClickListener onClick;
	
	public interface OnClickListener{
		public void onConnectClick(DialogFragment dialog);
		public void onBTConnectClick(DialogFragment dialog);
	}
	
	public ConnectionDialogFragment(){
		onClick = null;
	}
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        
        builder.setView(inflater.inflate(R.layout.connection_dialog, null));
        builder.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				onClick.onConnectClick(ConnectionDialogFragment.this);
			}
		});
        builder.setNegativeButton("Bluetooth(default)", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				onClick.onBTConnectClick(ConnectionDialogFragment.this);
			}
		});

        // Create the AlertDialog object and return it
        return builder.create();
    }
    
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            onClick = (OnClickListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement DialogInterface.OnClickListener");
        }
    }
}