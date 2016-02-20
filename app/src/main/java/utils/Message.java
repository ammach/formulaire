package utils;

import android.content.Context;
import android.widget.Toast;

public class Message {

	public static void show(Context c,String msg){
		Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
	}
}
