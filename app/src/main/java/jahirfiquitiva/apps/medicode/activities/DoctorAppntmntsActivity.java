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

package jahirfiquitiva.apps.medicode.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import jahirfiquitiva.apps.medicode.R;
import jahirfiquitiva.apps.medicode.adapters.AppntmntAdapter;
import jahirfiquitiva.apps.medicode.logic.objects.Doctor;
import jahirfiquitiva.apps.medicode.logic.ListsManager;

public class DoctorAppntmntsActivity extends AppCompatActivity {

    private ListsManager manager;
    private AppntmntAdapter adapter;
    private RecyclerView rv;
    private Doctor doctor;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_doctor_appntmnts);

        doctor = (Doctor) getIntent().getSerializableExtra("doctor");
        manager = (ListsManager) getIntent().getSerializableExtra("manager");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.doctor_n, doctor
                    .getName()));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (doctor != null) {
            TextView id = (TextView) findViewById(R.id.id);
            id.setText(doctor.getId());
            TextView spec = (TextView) findViewById(R.id.specialization);
            spec.setText(doctor.getSpecialization());
        }

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rv.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        rv.setNestedScrollingEnabled(false);
        updateAdapter(doctor);

        FloatingActionButton addAppntmnt = (FloatingActionButton) findViewById(R.id.fab);
        addAppntmnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (manager.getPatients().size() > 0) {
                    Intent intent = new Intent(context, CreateAppntmntActivity.class);
                    intent.putExtra("doctor", doctor);
                    intent.putExtra("manager", manager);
                    startActivityForResult(intent, 13);
                } else {
                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.main), context
                            .getResources().getString(R.string.not_enough_patients), Snackbar
                            .LENGTH_INDEFINITE);
                    snackbar.setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishAndSendData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finishAndSendData();
    }

    private void finishAndSendData() {
        Intent intent = new Intent();
        intent.putExtra("doctor", doctor);
        intent.putExtra("manager", manager);
        setResult(11, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 13:
                if (data != null) {
                    manager = (ListsManager) data.getSerializableExtra("manager");
                    updateAdapter((Doctor) data.getSerializableExtra("doctor"));
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("manager", manager);
        outState.putSerializable("doctor", doctor);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        manager = (ListsManager) savedInstanceState.getSerializable("manager");
        updateAdapter((Doctor) savedInstanceState.getSerializable("doctor"));
    }

    private void updateAdapter(Doctor doctor) {
        if (doctor != null) {
            this.doctor = doctor;
            if (adapter != null) {
                adapter.updateList(manager.getDoctorAppntmnts(doctor));
            } else {
                this.adapter = new AppntmntAdapter(this, doctor, manager.getDoctorAppntmnts
                        (doctor));
                if (rv != null) {
                    rv.setAdapter(adapter);
                }
            }
        }
    }

}