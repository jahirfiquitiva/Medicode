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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import jahirfiquitiva.apps.medicode.R;
import jahirfiquitiva.apps.medicode.base.Appntmnt;
import jahirfiquitiva.apps.medicode.base.Doctor;
import jahirfiquitiva.apps.medicode.base.Patient;
import jahirfiquitiva.apps.medicode.utils.IconTintUtils;

public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.DoctorsHolder> {

    private ArrayList<Doctor> doctors;
    private ArrayList<Patient> patients;
    private ArrayList<Appntmnt> appntmnts;
    private Context context;

    public ListsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public DoctorsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DoctorsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item,
                parent, false));
    }

    @Override
    public void onBindViewHolder(DoctorsHolder holder, int position) {
        if (doctors != null && doctors.size() > 0) {
            holder.title.setText(doctors.get(position).getName());
            if (context != null) {
                holder.content.setText(context.getResources().getString(R.string.cc, doctors
                        .get(position).getId()));
                holder.icon.setImageDrawable(IconTintUtils.getTintedIcon(context, R.drawable
                        .ic_doctor, R.color.colorPrimary));
            }
        } else if (patients != null && patients.size() > 0) {
            holder.title.setText(patients.get(position).getName());
            if (context != null) {
                holder.content.setText(context.getResources().getString(R.string.cc, patients
                        .get(position).getId()));
                holder.icon.setImageDrawable(IconTintUtils.getTintedIcon(context, R.drawable
                                .ic_patient,
                        patients.get(position).isGender() ? R.color.colorPrimary : R.color.pink
                ));
            }
        } else if (appntmnts != null && appntmnts.size() > 0) {
            holder.content.setText(appntmnts.get(position).getDate());
            if (context != null) {
                holder.title.setText(context.getResources().getString(R.string.appntmnt, String
                        .valueOf(position + 1)));
                holder.icon.setImageDrawable(IconTintUtils.getTintedIcon(context, R.drawable
                        .ic_appntmnt, R.color.colorPrimary));
            }
        }
    }

    @Override
    public int getItemCount() {
        return doctors != null ? doctors.size() : patients != null ? patients.size() : appntmnts
                != null ? appntmnts.size() : 0;
    }

    class DoctorsHolder extends RecyclerView.ViewHolder {
        final ImageView icon;
        final TextView title;
        final TextView content;

        public DoctorsHolder(View item) {
            super(item);
            icon = (ImageView) item.findViewById(R.id.icon);
            title = (TextView) item.findViewById(R.id.title);
            content = (TextView) item.findViewById(R.id.content);
        }
    }

    public void setDoctors(ArrayList<Doctor> doctors) {
        this.doctors = doctors;
    }

    public void setPatients(ArrayList<Patient> patients) {
        this.patients = patients;
    }

    public void setAppntmnts(ArrayList<Appntmnt> appntmnts) {
        this.appntmnts = appntmnts;
    }

}