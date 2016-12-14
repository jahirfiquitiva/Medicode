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
import android.support.annotation.ColorRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import jahirfiquitiva.apps.medicode.R;
import jahirfiquitiva.apps.medicode.logic.enums.Gender;
import jahirfiquitiva.apps.medicode.logic.objects.Doctor;
import jahirfiquitiva.apps.medicode.logic.objects.Patient;
import jahirfiquitiva.apps.medicode.utils.IconTintUtils;

/**
 * @author Jahir Fiquitiva
 */
public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.PersonHolder> {

    private Context context;
    private ItemClickListener listener;
    private ArrayList<Doctor> orgDoctors;
    private ArrayList<Patient> orgPatients;
    private ArrayList<Doctor> doctors;
    private ArrayList<Patient> patients;

    public ListsAdapter(Context context, ItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public ListsAdapter(Context context, ItemClickListener listener, ArrayList<Doctor> nDoctors) {
        this(context, listener);
        this.orgDoctors = new ArrayList<>();
        this.orgDoctors.addAll(nDoctors);
        this.doctors = new ArrayList<>();
        resetDoctors(nDoctors);
    }


    public ListsAdapter(Context context, ArrayList<Patient> nPatients, ItemClickListener listener) {
        this(context, listener);
        this.orgPatients = new ArrayList<>();
        this.orgPatients.addAll(nPatients);
        this.patients = new ArrayList<>();
        resetPatients(nPatients);
    }

    @Override
    public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PersonHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item,
                parent, false));
    }

    @Override
    public void onBindViewHolder(final PersonHolder holder, int position) {
        if (doctors != null && doctors.size() > 0) {
            holder.title.setText(doctors.get(position).getName());
            if (context != null) {
                holder.content.setText(context.getResources().getString(R.string.prof_card_n,
                        doctors.get(position).getId()));
                holder.icon.setImageDrawable(IconTintUtils.getTintedIcon(context, R.drawable
                        .ic_doctor, R.color.colorPrimary));
                holder.specialization.setText(context.getResources().getString(R.string
                        .specialization_n, doctors.get(position).getSpecialization()));
                holder.specialization.setVisibility(View.VISIBLE);
            }
            setupClickListener(holder);
        } else if (patients != null && patients.size() > 0) {
            holder.title.setText(patients.get(position).getName());
            if (context != null) {
                holder.content.setText(context.getResources().getString(R.string.cc, patients
                        .get(position).getId()));
                holder.icon.setImageDrawable(IconTintUtils.getTintedIcon(context, R.drawable
                        .ic_patient, getGenderColor(patients.get(position).getGender())));
            }
            setupClickListener(holder);
        }
    }

    @Override
    public int getItemCount() {
        return doctors != null ? doctors.size() : patients != null ? patients.size() : 0;
    }

    public synchronized void filterDoctors(CharSequence s) {
        doctors.clear();
        if (s == null || s.toString().trim().isEmpty()) {
            resetDoctors(orgDoctors);
        } else {
            String search = s.toString().toLowerCase();
            for (Doctor doctor : orgDoctors) {
                if (doctor.getId().toLowerCase().contains(search)) {
                    doctors.add(doctor);
                }
            }
        }
        notifyDataSetChanged();
    }

    public synchronized void filterPatients(CharSequence s) {
        patients.clear();
        if (s == null || s.toString().trim().isEmpty()) {
            resetPatients(orgPatients);
        } else {
            String search = s.toString().toLowerCase();
            for (Patient patient : orgPatients) {
                if (patient.getId().toLowerCase().contains(search)) {
                    patients.add(patient);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void resetDoctors(ArrayList<Doctor> nDoctors) {
        doctors.clear();
        doctors.addAll(nDoctors);
    }

    private void resetPatients(ArrayList<Patient> nPatients) {
        patients.clear();
        patients.addAll(nPatients);
    }

    private void setupClickListener(final PersonHolder holder) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    if (doctors != null && doctors.size() > 0) {
                        listener.onDoctorClick(doctors.get(holder.getAdapterPosition()));
                    } else if (patients != null && patients.size() > 0) {
                        listener.onPatientClick(patients.get(holder.getAdapterPosition()));
                    }
                }
            }
        });
    }

    class PersonHolder extends RecyclerView.ViewHolder {
        final View view;
        final ImageView icon;
        final TextView title;
        final TextView content;
        final TextView specialization;

        PersonHolder(View item) {
            super(item);
            view = item;
            icon = (ImageView) item.findViewById(R.id.icon);
            title = (TextView) item.findViewById(R.id.title);
            content = (TextView) item.findViewById(R.id.content);
            specialization = (TextView) item.findViewById(R.id.specialization);
        }

    }

    @ColorRes
    private int getGenderColor(Gender gender) {
        switch (gender) {
            default:
            case MALE:
                return R.color.colorPrimary;
            case FEMALE:
                return R.color.pink;
            case OTHER:
                return R.color.orange;
        }
    }

    public interface ItemClickListener {
        void onDoctorClick(Doctor doctor);

        void onPatientClick(Patient patient);
    }

}