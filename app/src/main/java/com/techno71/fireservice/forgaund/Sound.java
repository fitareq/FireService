package com.techno71.fireservice.forgaund;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class Sound {

    private static Ringtone r = null;
    public static void playSound(Context context){
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        // playing audio and vibration when user se reques
        if (r == null)
        r = RingtoneManager.getRingtone(context, soundUri);
        r.play();
    }
    public static void stopSound(){
        r.stop();
    }
    public static boolean isPlaying(){
        if (r != null)
            return r.isPlaying();
        return false;
    }
}
