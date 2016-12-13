package mappins.sreekesh.com.mappins.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sree on 12/12/16.
 */

public class PrefHelper {

    //Preference Variables
    public static final String MAP_PIN_COUNT = "pin_count";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    private static final String PREF_NAME = "MapPreferenceFile";

    int PRIVATE_MODE = 0;

    public PrefHelper(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public Integer getPropertyInteger(String dataSet){
        return pref.getInt(dataSet, 0);
    }

    public void setPropertyInt(String dataSet, int seqId){
        editor.putInt(dataSet, seqId);
        editor.apply();
    }

    public Integer getMapPinNameSerialNumber(){
        return pref.getInt(MAP_PIN_COUNT, 0);
    }

    public void setMapPinNameSerialNumber(int seqId){
        editor.putInt(MAP_PIN_COUNT, seqId);
        editor.apply();
    }
}
