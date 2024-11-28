package project.group6.eams.activityUtils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import project.group6.eams.R;
import project.group6.eams.execptions.ExistingUserException;
import project.group6.eams.execptions.PendingUserException;
public class ActivityUtils {

    /**
     * Shows toast that appears for different User related Exceptions
     * <p>
     * Resource used to help learn how to make custom toasts:
     * <a href="https://additionalsheet.com/android-studio-custom-toast>...</a>
     *
     * @param e is the Exception type that determines the message shown.
     */
    public static void showToast (String e, Context context) {
        View layout = LayoutInflater.from(context).inflate(R.layout.custom_toast,
                (ViewGroup) ((Activity)context).findViewById(R.id.customizedToastLayout));

        TextView text = (TextView) layout.findViewById(R.id.customEmailToastText);

        text.setText(e);
        Toast emailToast = new Toast(context);
        emailToast.setGravity(Gravity.CENTER_VERTICAL, 0, -600);
        emailToast.setDuration(Toast.LENGTH_LONG);
        emailToast.setView(layout);
        emailToast.show();
    }
}
