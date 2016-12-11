/*
 * Copyright (c) 2016. Jahir Fiquitiva
 *
 * 	Licensed under the CreativeCommons Attribution-ShareAlike
 * 	4.0 International License. You may not use this file except in compliance
 * 	with the License. You may obtain a copy of the License at
 *
 * 	   http://creativecommons.org/licenses/by-sa/4.0/legalcode
 *
 * 	Unless required by applicable law or agreed to in writing, software
 * 	distributed under the License is distributed on an "AS IS" BASIS,
 * 	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 	See the License for the specific language governing permissions and
 * 	limitations under the License.
 */

package jahirfiquitiva.apps.medicode.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

public class PermissionsUtils {

    /*
    * Check if version is marshmallow and above.
    * Used in deciding to ask runtime permission
    * */
    public static boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    private static boolean shouldAskPermission(Context context, String permission) {
        if (shouldAskPermission()) {
            int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("NewApi")
    public static void checkPermission(Context context, String permission, PermissionRequestListener
            listener) {
        /*
        * If permission is not granted
        * */
        if (shouldAskPermission(context, permission)) {
            /*
            * If permission denied previously
            * */
            if (((Activity) context).shouldShowRequestPermissionRationale(permission)) {
                listener.onPermissionDenied();
            } else {
                /*
                * Permission denied or first time requested
                * */
                if (PreferencesUtils.isFirstTimeAskingPermission(context, permission)) {
                    PreferencesUtils.firstTimeAskingPermission(context, permission, false);
                    listener.onPermissionRequest();
                } else {
                    /*
                    * Handle the feature without permission or ask user to manually allow permission
                    * */
                    listener.onPermissionCompletelyDenied();
                }
            }
        } else {
            listener.onPermissionGranted();
        }
    }

    /*
    * Callback on various cases on checking permission
    *
    * 1.  Below M, runtime permission not needed. In that case onPermissionGranted() would be
     * called.
    *     If permission is already granted, onPermissionGranted() would be called.
    *
    * 2.  Above M, if the permission is being asked first time onPermissionAsk() would be
    * called.
    *
    * 3.  Above M, if the permission is previously asked but not granted,
    * onPermissionPreviouslyDenied()
    *     would be called.
    *
    * 4.  Above M, if the permission is disabled by device policy or the user checked "Never
    * ask again"
    *     check box on previous request permission, onPermissionDisabled() would be called.
    * */
    public interface PermissionRequestListener {
        void onPermissionRequest();

        void onPermissionDenied();

        void onPermissionCompletelyDenied();

        void onPermissionGranted();
    }
}