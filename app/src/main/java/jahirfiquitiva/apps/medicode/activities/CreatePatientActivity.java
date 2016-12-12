/*
 * Copyright (c) 2016. Jahir Fiquitiva. Android Developer. All rights reserved.
 */

package jahirfiquitiva.apps.medicode.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import jahirfiquitiva.apps.medicode.R;
import jahirfiquitiva.apps.medicode.logic.ListsManager;
import jahirfiquitiva.apps.medicode.logic.enums.Gender;

public class CreatePatientActivity extends AppCompatActivity {

    private Context context;
    private ListsManager manager;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        manager = (ListsManager) getIntent().getSerializableExtra("manager");

        setContentView(R.layout.activity_create_patient);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final EditText name = (EditText) findViewById(R.id.name);
        final EditText id = (EditText) findViewById(R.id.id);
        final LinearLayout genderLayout = (LinearLayout) findViewById(R.id.genderLayout);
        final TextView gender = (TextView) findViewById(R.id.gender);

        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        genderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                new MaterialDialog.Builder(context)
                        .title(R.string.gender)
                        .items(R.array.genders)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int
                                    position, CharSequence text) {
                                gender.setText(text);
                            }
                        })
                        .show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String patName = name.getText().toString().trim();
                String patID = id.getText().toString().trim();
                Gender patGender = Gender.getGender(context, gender.getText().toString().trim());
                if (patName.length() > 0 && patID.length() > 3
                        && patGender != null) {

                    if (snackbar != null && snackbar.isShown()) {
                        snackbar.dismiss();
                    }

                    // TODO Add patient properly
                    final boolean added = false;
                    // final boolean added = manager.addPatient(new Patient());

                    MaterialDialog dialog = new MaterialDialog.Builder(context)
                            .title(added ? R.string.success : R.string.error)
                            .content(added ? R.string.patient_success : R.string.patient_error)
                            .positiveText(added ? android.R.string.yes : android.R.string.ok)
                            .cancelable(false).autoDismiss(false).canceledOnTouchOutside(false)
                            .positiveColor(ContextCompat.getColor(context, R.color.colorPrimary))
                            .negativeColor(ContextCompat.getColor(context, R.color.colorPrimary))
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull
                                        DialogAction which) {
                                    dialog.dismiss();
                                    name.setText("");
                                    id.setText("");
                                    gender.setText("");
                                    name.requestFocus();
                                    InputMethodManager imm = (InputMethodManager)
                                            getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
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
                    snackbar = Snackbar.make(findViewById(R.id.main), context
                            .getResources().getString(R.string.patient_not_filled), Snackbar
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
        intent.putExtra("manager", manager);
        setResult(10, intent);
        hideKeyboard();
        finish();
    }

    private void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(findViewById(R.id.main).getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}