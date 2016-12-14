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

package jahirfiquitiva.apps.medicode.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import jahirfiquitiva.apps.medicode.R;
import jahirfiquitiva.apps.medicode.activities.DoctorAppntmntsActivity;
import jahirfiquitiva.apps.medicode.activities.MainActivity;
import jahirfiquitiva.apps.medicode.activities.PatientAppntmntsActivity;
import jahirfiquitiva.apps.medicode.adapters.ListsAdapter;
import jahirfiquitiva.apps.medicode.logic.ListsManager;
import jahirfiquitiva.apps.medicode.logic.objects.Doctor;
import jahirfiquitiva.apps.medicode.logic.objects.Patient;
import jahirfiquitiva.apps.medicode.views.RecyclerViewWithEmptyView;

public class PersonFragment extends Fragment {

    private ListsManager manager;
    private RecyclerViewWithEmptyView rv;
    private ListsAdapter adapter;
    private ArrayList<Patient> patients;
    private ArrayList<Doctor> doctors;
    private WeakReference<MainActivity> activity;
    private boolean doctorsFrag = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = new WeakReference<>((MainActivity) context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.doctorsFrag = getArguments().getBoolean("doctorsFrag");
            this.manager = (ListsManager) getArguments().getSerializable("manager");
            if (manager != null) {
                if (doctorsFrag) {
                    this.doctors = manager.getDoctors();
                } else {
                    this.patients = manager.getPatients();
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.list, container, false);

        if (doctorsFrag) {
            adapter = new ListsAdapter(activity.get(), getItemClickListener(), doctors);
        } else {
            adapter = new ListsAdapter(activity.get(), patients, getItemClickListener());
        }

        rv = (RecyclerViewWithEmptyView) layout.findViewById(R.id.rv);

        LinearLayout emptyView = (LinearLayout) layout.findViewById(R.id.empty);
        ImageView emptyIcon = (ImageView) layout.findViewById(R.id.emptyIcon);
        TextView emptyText = (TextView) layout.findViewById(R.id.emptyText);

        emptyIcon.setImageDrawable(ContextCompat.getDrawable(activity.get(), doctorsFrag ? R
                .drawable.ic_no_doctors : R.drawable.ic_no_patients));
        emptyText.setText(getString(R.string.not_enough_to_show, getString(doctorsFrag ? R
                .string.doctors : R.string.patients).toLowerCase()));

        LinearLayout searchView = (LinearLayout) layout.findViewById(R.id.searching);

        rv.setEmptyView(emptyView);
        rv.setSearchingView(searchView);

        rv.setLayoutManager(new LinearLayoutManager(activity.get(), LinearLayoutManager.VERTICAL,
                false));
        rv.addItemDecoration(new DividerItemDecoration(activity.get(), LinearLayoutManager
                .VERTICAL));
        rv.setAdapter(adapter);

        return layout;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public PersonFragment() {
    }

    public static Fragment newInstance(ListsManager manager, boolean doctor) {
        PersonFragment fragment = new PersonFragment();
        Bundle args = new Bundle();
        args.putBoolean("doctorsFrag", doctor);
        args.putSerializable("manager", manager);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        (activity.get()).onActivityResult(requestCode, resultCode, data);
    }

    private ListsAdapter.ItemClickListener getItemClickListener() {
        return new ListsAdapter.ItemClickListener
                () {
            @Override
            public void onDoctorClick(Doctor doctor) {
                if (doctor != null) {
                    rv.setState(RecyclerViewWithEmptyView.STATE_NORMAL);
                    Intent intent = new Intent(activity.get(), DoctorAppntmntsActivity.class);
                    intent.putExtra("doctor", doctor);
                    intent.putExtra("manager", manager);
                    startActivityForResult(intent, 12);
                }
            }

            @Override
            public void onPatientClick(Patient patient) {
                if (patient != null) {
                    rv.setState(RecyclerViewWithEmptyView.STATE_NORMAL);
                    Intent intent = new Intent(activity.get(), PatientAppntmntsActivity.class);
                    intent.putExtra("patient", patient);
                    intent.putExtra("manager", manager);
                    startActivityForResult(intent, 13);
                }
            }
        };
    }

    public void performSearch(String query, boolean reset) {
        if (rv != null) {
            rv.setState(reset ? RecyclerViewWithEmptyView.STATE_NORMAL : RecyclerViewWithEmptyView
                    .STATE_SEARCHING);
        }
        if (adapter != null) {
            if (doctorsFrag) {
                adapter.filterDoctors(query);
            } else {
                adapter.filterPatients(query);
            }
        }
    }

    public ListsAdapter getAdapter() {
        return adapter;
    }

}