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

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

import jahirfiquitiva.apps.medicode.R;
import jahirfiquitiva.apps.medicode.logic.ListsManager;
import jahirfiquitiva.apps.medicode.logic.objects.Appntmnt;
import jahirfiquitiva.apps.medicode.logic.objects.Doctor;
import jahirfiquitiva.apps.medicode.logic.objects.Patient;

public class CreateAppntmntActivity extends AppCompatActivity {

    private static final int INITIAL_HOUR = 8;
    private static final int FINAL_HOUR = 18;
    private static final int TIME_INTERVAL = 20;
    private ListsManager manager;
    private Doctor doctor;
    private Patient patient;
    private Context context;
    private String selectedDate = "";
    private MaterialDialog dialog;
    private TextView docName;
    private TextView patientName;
    private ArrayList<String> workingHours = new ArrayList<>();
    private boolean hadDoctor = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_create_appntmnt);

        setDoctor((Doctor) getIntent().getSerializableExtra("doctor"));
        setPatient((Patient) getIntent().getSerializableExtra("patient"));
        manager = (ListsManager) getIntent().getSerializableExtra("manager");

        hadDoctor = doctor != null && patient == null;

        setupWorkingHours();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        docName = (TextView) findViewById(R.id.doctorName);
        if (doctor != null) {
            docName.setText(doctor.getName());
        } else {
            docName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissPreviousDialog();
                    dialog = new MaterialDialog.Builder(context)
                            .title(R.string.doctor)
                            .items(getAllDoctors())
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View itemView, int
                                        position, CharSequence text) {
                                    if (position == 0) {
                                        createDoctor();
                                    } else {
                                        setDoctor(manager.getDoctors().get(position - 1));
                                        docName.setText(manager.getDoctors().get(position - 1)
                                                .getName());
                                    }
                                }
                            }).build();
                    dialog.show();
                }
            });
        }

        patientName = (TextView) findViewById(R.id.patientName);
        if (patient != null) {
            patientName.setText(patient.getName());
        } else {
            patientName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissPreviousDialog();
                    dialog = new MaterialDialog.Builder(context)
                            .title(R.string.patient)
                            .items(getAllPatients())
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View itemView, int
                                        position, CharSequence text) {
                                    if (position == 0) {
                                        createPatient();
                                    } else {
                                        setPatient(manager.getPatients().get(position - 1));
                                        patientName.setText(manager.getPatients().get(position - 1)
                                                .getName());
                                    }
                                }
                            }).build();
                    dialog.show();
                }
            });
        }

        final LinearLayout date = (LinearLayout) findViewById(R.id.date);
        final TextView dateText = (TextView) findViewById(R.id.detailedDate);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int currentYear = c.get(Calendar.YEAR);
                int currentMonth = c.get(Calendar.MONTH);
                int currentDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dateDialog = new DatePickerDialog(context, new DatePickerDialog
                        .OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, final int year, final int month,
                                          final int day) {
                        dismissPreviousDialog();
                        dialog = new MaterialDialog.Builder(context)
                                .title(R.string.appointment_time)
                                .positiveText(android.R.string.ok)
                                .items(workingHours)
                                .itemsCallbackSingleChoice(-1, new MaterialDialog
                                        .ListCallbackSingleChoice() {
                                    @Override
                                    public boolean onSelection(MaterialDialog dialog, View
                                            itemView, int which, CharSequence text) {
                                        if (which >= 0) {
                                            selectedDate = context.getString(R.string.full_date,
                                                    String.valueOf(day),
                                                    new DateFormatSymbols().getMonths()[month],
                                                    String.valueOf(year),
                                                    text.toString()
                                            );
                                            dateText.setText(selectedDate);
                                        } else {
                                            selectedDate = "";
                                            dateText.setText("");
                                            Snackbar.make(findViewById(R.id.main),
                                                    context.getString(R.string
                                                            .time_not_properly_set),
                                                    Snackbar.LENGTH_SHORT).show();
                                        }
                                        return true;
                                    }
                                })
                                .build();
                        dialog.show();
                    }
                }, currentYear, currentMonth, currentDay);
                dateDialog.getDatePicker().setMinDate(Calendar.getInstance().getTime().getTime());
                c.add(Calendar.MONTH, 1);
                dateDialog.getDatePicker().setMaxDate(c.getTime().getTime());
                dateDialog.show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (doctor != null && patient != null && (!(selectedDate.isEmpty()))) {
                    final boolean added = manager.addAppntmnt(new Appntmnt(doctor, patient,
                            selectedDate), hadDoctor);
                    dismissPreviousDialog();
                    dialog = new MaterialDialog.Builder(context)
                            .title(added ? R.string.success : R.string.error)
                            .content(added ? context.getString(R.string.appntmnt_success) :
                                    getErrorMessage())
                            .positiveText(added ? android.R.string.yes : android.R.string.ok)
                            .cancelable(false).autoDismiss(false).canceledOnTouchOutside(false)
                            .positiveColor(ContextCompat.getColor(context, R.color.colorPrimary))
                            .negativeColor(ContextCompat.getColor(context, R.color.colorPrimary))
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull
                                        DialogAction which) {
                                    dialog.dismiss();
                                    if (hadDoctor) {
                                        patientName.setText("");
                                        patient = null;
                                    } else {
                                        docName.setText("");
                                        doctor = null;
                                    }
                                    dateText.setText("");
                                    selectedDate = "";
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull
                                        DialogAction which) {
                                    finishAndSendData();
                                }
                            })
                            .build();

                    if (added) {
                        dialog.setActionButton(DialogAction.NEGATIVE, android.R.string.no);
                    }

                    dialog.show();
                } else {
                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.main), context
                            .getResources().getString(R.string.appntmnt_not_filled), Snackbar
                            .LENGTH_LONG);
                    snackbar.setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    ((TextView) snackbar.getView().findViewById(android.support.design.R.id
                            .snackbar_text)).setMaxLines(6);
                    snackbar.show();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissPreviousDialog();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 || requestCode == 11) {
            if (data != null) {
                this.manager = ((ListsManager) data.getSerializableExtra("manager"));
            }
        }
    }

    private void finishAndSendData() {
        Intent intent = new Intent();
        intent.putExtra("doctor", doctor);
        intent.putExtra("patient", patient);
        intent.putExtra("manager", manager);
        setResult(14, intent);
        finish();
    }

    private void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    private void setPatient(Patient patient) {
        this.patient = patient;
    }

    private void dismissPreviousDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private String getErrorMessage() {
        return context.getString(R.string.appntmnt_error, context.getString
                (hadDoctor ? R.string.patient : R.string.doctor)
                .toLowerCase());
    }

    private ArrayList<String> getAllDoctors() {
        ArrayList<String> texts = new ArrayList<>();
        texts.add(getString(R.string.create_new));
        for (Doctor doctor : manager.getDoctors()) {
            texts.add(doctor.toString());
        }
        return texts;
    }

    private ArrayList<String> getAllPatients() {
        ArrayList<String> texts = new ArrayList<>();
        texts.add(getString(R.string.create_new));
        for (Patient patient : manager.getPatients()) {
            texts.add(patient.toString());
        }
        return texts;
    }

    private void createDoctor() {
        if (docName != null) docName.setText("");
        setDoctor(null);
        Intent create = new Intent(context, CreateDoctorActivity.class);
        create.putExtra("manager", manager);
        startActivityForResult(create, 10);
    }

    private void createPatient() {
        if (patientName != null) patientName.setText("");
        setPatient(null);
        Intent create = new Intent(context, CreatePatientActivity.class);
        create.putExtra("manager", manager);
        startActivityForResult(create, 11);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("manager", manager);
        outState.putSerializable("doctor", doctor);
        outState.putSerializable("patient", patient);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.manager = ((ListsManager) savedInstanceState.getSerializable("manager"));
        this.doctor = (Doctor) savedInstanceState.getSerializable("doctor");
        this.patient = (Patient) savedInstanceState.getSerializable("patient");
    }

    private void setupWorkingHours() {
        workingHours.clear();
        boolean is24HoursFormat = DateFormat.is24HourFormat(this);
        for (int h = INITIAL_HOUR; h < FINAL_HOUR; h++) {
            for (int m = 0; m < 60; m += TIME_INTERVAL) {
                if (h != 12 && h != 13) {
                    workingHours.add(formatTimeNumber((!is24HoursFormat ? h > 12 ? h - 12 : h :
                            h)) + ":" + formatTimeNumber(m) + " " +
                            getAMOrPM(is24HoursFormat, h));
                }
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private String formatTimeNumber(int number) {
        return String.format("%02d", number);
    }


    private String getAMOrPM(boolean is24HoursFormat, int hour) {
        if (!is24HoursFormat) {
            if (hour >= 12) {
                return "pm";
            } else {
                return "am";
            }
        } else {
            return "";
        }
    }

}