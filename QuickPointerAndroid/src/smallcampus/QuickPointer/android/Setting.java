package smallcampus.QuickPointer.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.SensorManager;

public final class Setting {
	public final static int TYPE_BT = 2;
	public final static int TYPE_TCP = 1;
	public final static int TYPE_NULL = 0;
	
    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences("QPPref", 0);
    }
    
    public static void setBtHistory(Context context, String history){
    	getPrefs(context).edit().putString("btHistory", history).commit();
    	//set last history type = 2
    	setLastHistoryType(context,2);
    }
    public static String getBtHistory(Context context){
    	return getPrefs(context).getString("btHistory", null);
    }
    
    public static void setTcpHistory(Context context, String history){
    	getPrefs(context).edit().putString("tcpHistory", history).commit();
    	//set last history type = 1
    	setLastHistoryType(context,1);
    }
    public static String getTcpHistory(Context context){
    	return getPrefs(context).getString("tcpHistory", null);
    }
    
    private static void setLastHistoryType(Context context, int type){
    	getPrefs(context).edit().putInt("lastHistoryType", type).commit();
    }
    public static int getLastHistoryType(Context context){
    	return getPrefs(context).getInt("lastHistoryType", 0);
    }
    
    //---------------- 
    //TODO make an setting page for them
	//Time delay interval of sending coordinate msg 
	public static final int SEND_DELAY = 80;
	public static final int SENSOR_DELAY = SensorManager.SENSOR_DELAY_FASTEST;
	
	//Max ~= 10 for SEND_DELAY = 200ms, DELAY_NORMAL
	//Max = 5 for SEND_DELAY =100ms, DELAY_GAME
	public static final int SENSOR_NUM_FRAMES = 20; 
	
	public static final int POINTER_THRESHOLD_X = 40;
	public static final int POINTER_THRESHOLD_Y = 20;
	//---------------------
	public static final int DEFAULT_TCP_SERVER_PORT = 0;
	public static final int DEFAULT_UDP_SERVER_PORT = 0;
}
