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

package jahirfiquitiva.apps.medicode.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import jahirfiquitiva.apps.medicode.R;
import jahirfiquitiva.apps.medicode.adapters.AppntmntAdapter;
import jahirfiquitiva.apps.medicode.logic.ListsManager;
import jahirfiquitiva.apps.medicode.logic.enums.Gender;
import jahirfiquitiva.apps.medicode.logic.objects.Patient;
import jahirfiquitiva.apps.medicode.utils.IconTintUtils;
import jahirfiquitiva.apps.medicode.views.RecyclerViewWithEmptyView;

public class PatientAppntmntsActivity extends AppCompatActivity {

    private ListsManager manager;
    private AppntmntAdapter adapter;
    private RecyclerViewWithEmptyView rv;
    private Patient patient;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_patient_appntmnts);

        patient = (Patient) getIntent().getSerializableExtra("patient");
        manager = (ListsManager) getIntent().getSerializableExtra("manager");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.patient_n, patient
                    .getName()));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (patient != null) {
            TextView id = (TextView) findViewById(R.id.id);
            id.setText(patient.getId());
            TextView age = (TextView) findViewById(R.id.age);
            age.setText(String.valueOf(patient.getAge()));
            TextView gender = (TextView) findViewById(R.id.gender);
            gender.setText(ListsManager.makeNameCamelCase(patient.getGender().getDef(context)));
            ImageView genderIcon = (ImageView) findViewById(R.id.genderIcon);
            genderIcon.setImageDrawable(IconTintUtils.getTintedIcon(context, R.drawable
                    .ic_gender, getGenderColor(patient.getGender())));
            TextView rh = (TextView) findViewById(R.id.rh);
            rh.setText(patient.getRh());
            TextView eps = (TextView) findViewById(R.id.eps);
            eps.setText(patient.getEps());
        }

        rv = (RecyclerViewWithEmptyView) findViewById(R.id.rv);
        LinearLayout emptyView = (LinearLayout) findViewById(R.id.empty);
        rv.setEmptyView(emptyView);
        rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rv.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        rv.setNestedScrollingEnabled(false);
        updateAdapter(patient);

        FloatingActionButton addAppntmnt = (FloatingActionButton) findViewById(R.id.fab);
        addAppntmnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CreateAppntmntActivity.class);
                intent.putExtra("patient", patient);
                intent.putExtra("manager", manager);
                startActivityForResult(intent, 14);
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
        intent.putExtra("patient", patient);
        intent.putExtra("manager", manager);
        setResult(13, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 14:
                if (data != null) {
                    manager = (ListsManager) data.getSerializableExtra("manager");
                    updateAdapter((Patient) data.getSerializableExtra("patient"));
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("manager", manager);
        outState.putSerializable("patient", patient);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        manager = (ListsManager) savedInstanceState.getSerializable("manager");
        updateAdapter((Patient) savedInstanceState.getSerializable("patient"));
    }

    private void updateAdapter(Patient patient) {
        if (patient != null) {
            this.patient = patient;
            if (adapter != null) {
                adapter.updateList(manager.getPatientAppntmnts(patient));
            } else {
                this.adapter = new AppntmntAdapter(this, patient, manager.getPatientAppntmnts
                        (patient));
                if (rv != null) {
                    rv.setAdapter(adapter);
                }
            }
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

}