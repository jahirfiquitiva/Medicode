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

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import jahirfiquitiva.apps.medicode.R;
import jahirfiquitiva.apps.medicode.logic.objects.Appntmnt;
import jahirfiquitiva.apps.medicode.logic.objects.Doctor;
import jahirfiquitiva.apps.medicode.logic.ListsManager;
import jahirfiquitiva.apps.medicode.logic.objects.Patient;

public class CreateAppntmntActivity extends AppCompatActivity {

    private ListsManager manager;
    private Doctor doctor;
    private Patient patient;
    private Context context;
    private String selectedDate = "";
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final TextView docName = (TextView) findViewById(R.id.doctorName);
        if (doctor != null) {
            docName.setText(doctor.getName());
        } else {
            docName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new MaterialDialog.Builder(context)
                            .title(R.string.doctor)
                            .items(manager.getDoctors())
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View itemView, int
                                        position, CharSequence text) {
                                    docName.setText(text);
                                    setDoctor(manager.getDoctors().get(position));
                                }
                            })
                            .show();
                }
            });
        }

        final TextView patientName = (TextView) findViewById(R.id.patientName);
        if (patient != null) {
            patientName.setText(patient.getName());
        } else {
            patientName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new MaterialDialog.Builder(context)
                            .title(R.string.patient)
                            .items(manager.getPatients())
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View itemView, int
                                        position, CharSequence text) {
                                    patientName.setText(text);
                                    setPatient(manager.getPatients().get(position));
                                }
                            })
                            .show();
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
                        final Calendar c = Calendar.getInstance();
                        int currentHour = c.get(Calendar.HOUR_OF_DAY);
                        int currentMinute = c.get(Calendar.MINUTE);
                        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                                selectedDate = context.getResources().getString(R.string.full_date,
                                        String.valueOf(day),
                                        new DateFormatSymbols().getMonths()[month],
                                        String.valueOf(year),
                                        String.valueOf(hour),
                                        String.valueOf(minutes),
                                        isAMOrPM(hour)
                                );
                                dateText.setText(selectedDate);
                            }
                        }, currentHour, currentMinute, DateFormat.is24HourFormat(context)).show();
                    }
                }, currentYear, currentMonth, currentDay);
                dateDialog.getDatePicker().setMinDate(Calendar.getInstance().getTime().getTime());
                Calendar nDate = Calendar.getInstance();
                nDate.add(Calendar.MONTH, 1);
                dateDialog.getDatePicker().setMaxDate(nDate.getTime().getTime());
                dateDialog.show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (doctor != null && patient != null && (!(selectedDate.isEmpty()))) {

                    //doctor.addAppntmnt(new Appntmnt(doctor, patient, selectedDate));

                    final boolean added = manager.addAppntmnt(new Appntmnt(doctor, patient,
                            selectedDate));

                    MaterialDialog dialog = new MaterialDialog.Builder(context)
                            .title(added ? R.string.success : R.string.error)
                            .content(added ? R.string.appntmnt_success : R.string.appntmnt_error)
                            .positiveText(added ? android.R.string.yes : android.R.string.ok)
                            .cancelable(false).autoDismiss(false).canceledOnTouchOutside(false)
                            .positiveColor(ContextCompat.getColor(context, R.color.darkAccent))
                            .negativeColor(ContextCompat.getColor(context, R.color.darkAccent))
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
                            .LENGTH_INDEFINITE);
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
        intent.putExtra("patient", patient);
        intent.putExtra("manager", manager);
        setResult(13, intent);
        finish();
    }

    private String isAMOrPM(int hour) {
        if (!DateFormat.is24HourFormat(this)) {
            if (hour >= 12) {
                return "pm";
            } else {
                return "am";
            }
        } else {
            return "";
        }
    }

    private void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    private void setPatient(Patient patient) {
        this.patient = patient;
    }

}