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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import jahirfiquitiva.apps.medicode.R;
import jahirfiquitiva.apps.medicode.activities.DoctorAppntmntsActivity;
import jahirfiquitiva.apps.medicode.activities.MainActivity;
import jahirfiquitiva.apps.medicode.activities.PatientAppntmntsActivity;
import jahirfiquitiva.apps.medicode.adapters.ListsAdapter;
import jahirfiquitiva.apps.medicode.logic.ListsManager;
import jahirfiquitiva.apps.medicode.logic.objects.Doctor;
import jahirfiquitiva.apps.medicode.logic.objects.Patient;

public class PersonFragment extends Fragment {

    private ListsManager manager;
    private LinearLayoutManager layoutManager;
    private ListsAdapter adapter;
    private ArrayList<Patient> patients;
    private ArrayList<Patient> filteredPatients;
    private ArrayList<Doctor> doctors;
    private ArrayList<Doctor> filteredDoctors;
    private boolean doctorsFrag = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.doctorsFrag = getArguments().getBoolean("doctorsFrag");
            this.manager = (ListsManager) getArguments().getSerializable("manager");
            if (manager != null) {
                this.doctors = manager.getDoctors();
                this.patients = manager.getPatients();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.list, container, false);

        if (doctorsFrag) {
            adapter = new ListsAdapter(getActivity(), getItemClickListener(), doctors);
        } else {
            adapter = new ListsAdapter(getActivity(), patients, getItemClickListener());
        }

        RecyclerView rv = (RecyclerView) layout.findViewById(R.id.rv);
        setupLinearLayout();
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager
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
        ((MainActivity) getActivity()).onActivityResult(requestCode, resultCode, data);
    }

    private void setupLinearLayout() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,
                false) {
            @Override
            public void onLayoutChildren(final RecyclerView.Recycler recycler, final RecyclerView
                    .State state) {
                super.onLayoutChildren(recycler, state);
                layoutScrollable();
            }
        };
    }

    private boolean layoutScrollable() {
        if (layoutManager == null) return false;
        if (doctors == null || doctors.isEmpty() || patients == null || patients.isEmpty())
            return false;
        final int firstVisibleItemPosition =
                layoutManager.findFirstCompletelyVisibleItemPosition();
        if (firstVisibleItemPosition != 0) {
            if (firstVisibleItemPosition == -1) {
                return false;
            }
        }
        return (layoutManager.findLastCompletelyVisibleItemPosition() !=
                ((doctors != null ? doctors.size() : patients != null ? patients.size() : 0) - 1));
    }

    private ListsAdapter.ItemClickListener getItemClickListener() {
        return new ListsAdapter.ItemClickListener
                () {
            @Override
            public void onItemClick(int position) {
                Intent intent = null;
                if (doctorsFrag) {
                    if (doctors != null && doctors.size() > 0) {
                        intent = new Intent(getActivity(), DoctorAppntmntsActivity.class);
                        intent.putExtra("doctor", doctors.get(position));
                    }
                } else {
                    if (patients != null && patients.size() > 0) {
                        intent = new Intent(getActivity(), PatientAppntmntsActivity.class);
                        intent.putExtra("patient", patients.get(position));
                    }
                }
                if (intent != null) {
                    intent.putExtra("manager", manager);
                    startActivityForResult(intent, doctorsFrag ? 12 : 13);
                }
            }
        };
    }

    public void performSearch(String query) {
        if (doctorsFrag) {
            filterDoctors(query);
        } else {
            filterPatients(query);
        }
    }

    private synchronized void filterDoctors(CharSequence s) {
        if (doctors != null && !(doctors.isEmpty())) {
            if (s == null || s.toString().trim().isEmpty()) {
                Log.d("Medicode", "Nothing to search setting default doctors");
                filteredDoctors = null;
                setDoctors(null);
            } else {
                Log.d("Medicode", "Searching for doctor: " + s.toString());
                if (filteredDoctors != null) {
                    filteredDoctors.clear();
                }
                filteredDoctors = new ArrayList<>();
                String search = s.toString().toLowerCase();
                for (Doctor doctor : doctors) {
                    if (doctor.getName().toLowerCase().contains(search)) {
                        filteredDoctors.add(doctor);
                    }
                }
                Log.d("Medicode", "Putting " + filteredDoctors.size() + " doctors in list");
                setDoctors(filteredDoctors);
            }
        }
    }

    private synchronized void filterPatients(CharSequence s) {
        if ((patients != null) && (!(patients.isEmpty()))) {
            if (s == null || s.toString().trim().isEmpty()) {
                filteredPatients = null;
                setPatients(null);
            } else {
                if (filteredPatients != null) {
                    filteredPatients.clear();
                }
                filteredPatients = new ArrayList<>();
                String search = s.toString().toLowerCase();
                for (Patient patient : patients) {
                    if (patient.getName().toLowerCase().contains(search)) {
                        filteredPatients.add(patient);
                    }
                }
                setPatients(filteredPatients);
            }
        }
    }

    public ListsAdapter getAdapter() {
        return adapter;
    }

    public void setDoctors(ArrayList<Doctor> list) {
        Log.d("Medicode", "Adding " + (list != null ? list.size() : 0) + " doctors");
        if (list != null && (!(list.isEmpty()))) {
            if (doctors != null) {
                doctors.clear();
                doctors.addAll(list);
            }
        } else {
            this.doctors = new ArrayList<>();
        }
        adapter.notifyDataSetChanged();
    }

    public void setPatients(ArrayList<Patient> list) {
        if (list != null) {
            if (patients != null) {
                patients.clear();
                patients.addAll(list);
            }
        } else {
            this.patients = new ArrayList<>();
        }
        adapter.notifyDataSetChanged();
    }

}