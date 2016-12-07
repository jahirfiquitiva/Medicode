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

package jahirfiquitiva.apps.medicode.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import jahirfiquitiva.apps.medicode.base.ListsManager;
import jahirfiquitiva.apps.medicode.fragments.AppntmntsFragment;
import jahirfiquitiva.apps.medicode.fragments.DoctorsFragment;
import jahirfiquitiva.apps.medicode.fragments.PatientsFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private ListsManager manager;

    public PagerAdapter(FragmentManager fm, ListsManager manager) {
        super(fm);
        this.manager = manager;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Log.d("APP", "Doctors: " + manager.getDoctors().size());
                return DoctorsFragment.newInstance(manager.getDoctors());
            case 1:
                Log.d("APP", "Patients: " + manager.getPatients().size());
                return PatientsFragment.newInstance(manager.getPatients());
            case 2:
                Log.d("APP", "Appointments: " + manager.getAppntmnts().size());
                return AppntmntsFragment.newInstance(manager.getAppntmnts());
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}