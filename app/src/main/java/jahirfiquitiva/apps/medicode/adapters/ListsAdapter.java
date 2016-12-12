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
import android.support.annotation.ColorRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.PersonHolder> {

    private ArrayList<Doctor> doctors;
    private ArrayList<Patient> patients;
    private ItemClickListener listener;
    private Context context;

    public ListsAdapter(Context context, ItemClickListener listener) {
        this.context = context;
        this.listener = listener;
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
                holder.content.setText(context.getResources().getString(R.string.cc, doctors
                        .get(position).getId()));
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

    public void setDoctors(ArrayList<Doctor> list) {
        Log.d("Medicode", "Adding " + (list != null ? list.size() : 0) + " doctor");
        if (list != null && (!(list.isEmpty()))) {
            this.doctors = list;
            // this.notifyItemRangeChanged(0, list.size());
        } else {
            this.doctors = new ArrayList<>();
            // this.notifyItemRangeChanged(0, 0);
        }
    }

    public void setPatients(ArrayList<Patient> list) {
        if (list != null) {
            this.patients = list;
            // this.notifyItemRangeChanged(0, list.size());
        } else {
            this.patients = new ArrayList<>();
            // this.notifyItemRangeChanged(0, 0);
        }
    }

    public void clearList(int set) {
        switch (set) {
            case 0:
                if (doctors != null) {
                    doctors.clear();
                }
                break;
            case 1:
                if (patients != null) {
                    patients.clear();
                }
                break;
        }
    }

    private void setupClickListener(final PersonHolder holder) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null && ((doctors != null && doctors.size() > 0) || (patients !=
                        null && patients.size() > 0)))
                    listener.onItemClick(holder.getAdapterPosition());
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
        void onItemClick(int position);
    }

}