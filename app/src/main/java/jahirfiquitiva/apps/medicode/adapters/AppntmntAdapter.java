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

package jahirfiquitiva.apps.medicode.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import jahirfiquitiva.apps.medicode.R;
import jahirfiquitiva.apps.medicode.logic.objects.Appntmnt;
import jahirfiquitiva.apps.medicode.logic.objects.Doctor;
import jahirfiquitiva.apps.medicode.logic.objects.Patient;

/**
 * @author Jahir Fiquitiva
 */
public class AppntmntAdapter extends RecyclerView.Adapter<AppntmntAdapter.AppntmntHolder> {

    private Context context;
    private Doctor doctor;
    private Patient patient;
    private ArrayList<Appntmnt> list = new ArrayList<>();

    public AppntmntAdapter(Context context, ArrayList<Appntmnt> nList) {
        this.context = context;
        list.addAll(nList);
    }

    public AppntmntAdapter(Context context, Doctor doctor, ArrayList<Appntmnt> list) {
        this(context, list);
        this.doctor = doctor;
    }

    public AppntmntAdapter(Context context, Patient patient, ArrayList<Appntmnt> list) {
        this(context, list);
        this.patient = patient;
    }

    @Override
    public AppntmntHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppntmntHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
                .appntmnt, parent, false));
    }

    @Override
    public void onBindViewHolder(AppntmntHolder holder, int position) {
        Appntmnt appntmnt = list.get(holder.getAdapterPosition());
        holder.docPat.setText(doctor != null ? context.getResources().getString(R.string
                .patient_n, appntmnt.getPatient().getName()) : patient != null ? context
                .getResources().getString(R.string
                        .doctor_n, appntmnt.getDoctor().getName()) : context.getResources()
                .getString(R
                        .string.error));
        if (patient != null) {
            holder.docSpec.setText(context.getString(R.string.appntmnt_with, appntmnt.getDoctor()
                    .getSpecialization()));
            holder.docSpec.setVisibility(View.VISIBLE);
        }
        if (doctor != null || patient != null) {
            holder.date.setText(appntmnt.getDate());
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void updateList(ArrayList<Appntmnt> nList) {
        list.clear();
        list.addAll(nList);
        notifyDataSetChanged();
    }

    class AppntmntHolder extends RecyclerView.ViewHolder {
        final TextView docPat;
        final TextView docSpec;
        final TextView date;

        public AppntmntHolder(View itemView) {
            super(itemView);
            docPat = (TextView) itemView.findViewById(R.id.detail);
            docSpec = (TextView) itemView.findViewById(R.id.appointmentWith);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }
}
