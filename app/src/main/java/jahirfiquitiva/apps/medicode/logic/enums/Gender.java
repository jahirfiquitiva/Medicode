/*
 * Copyright (c) 2016. Jahir Fiquitiva
 *
 * Licensed under the CreativeCommons Attribution-ShareAlike
 * 4.0 International License. You may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *    http://creativecommons.org/licenses/by-sa/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jahirfiquitiva.apps.medicode.logic.enums;

import android.content.Context;
import android.support.annotation.StringRes;

import jahirfiquitiva.apps.medicode.R;

public enum Gender {
    MALE(R.string.male), FEMALE(R.string.female), OTHER(R.string.other);

    private int def;

    Gender(@StringRes int res) {
        this.def = res;
    }

    public String getDef(Context context) {
        return context.getResources().getString(def);
    }

    public static Gender getGender(Context context, String text) {
        if (text.compareToIgnoreCase(context.getResources().getString(R.string.male)) == 0) {
            return MALE;
        } else if (text.compareToIgnoreCase(context.getResources().getString(R.string.female)) ==
                0) {
            return FEMALE;
        } else if (text.compareToIgnoreCase(context.getResources().getString(R.string.other)) ==
                0) {
            return OTHER;
        }
        return null;
    }

}