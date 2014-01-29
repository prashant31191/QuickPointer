package smallcampus.QuickPointer.android.dialog;

import android.app.Dialog;
import android.content.Context;

public class ConnectionDialog extends Dialog{

	public ConnectionDialog(Context context) {
		super(context);
	}
	
	public static void showDialog(Context context) {
		ConnectionDialog dialog = new ConnectionDialog(context);
		dialog.show();
	}
	
	private void setUI(){
		
	}

}
