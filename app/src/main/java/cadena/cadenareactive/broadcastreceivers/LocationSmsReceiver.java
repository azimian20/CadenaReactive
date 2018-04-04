
package cadena.cadenareactive.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import cadena.cadenareactive.MapsActivity;
import cadena.cadenareactive.R;


public class LocationSmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();

        if (intentExtras != null) {

            Object[] sms = (Object[]) intentExtras.get("pdus"); //-- Reading text messages from extra data of the intent

            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                String phone = smsMessage.getOriginatingAddress();
                String message = smsMessage.getMessageBody().toString();
                if (phone.equals(context.getString(R.string.CADENA_PHONE_NUMBER))) {
                    System.out.println("*** A message from Cadena is received:[" + message + "]");
                    Intent showMapIntent = new Intent(context, MapsActivity.class);
                    showMapIntent.putExtra(context.getString(R.string.CADENA_LOCATION_SMS_CONTAINER), message);
                    showMapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(showMapIntent);
                    break;
                }
            }
        }

    }
}
