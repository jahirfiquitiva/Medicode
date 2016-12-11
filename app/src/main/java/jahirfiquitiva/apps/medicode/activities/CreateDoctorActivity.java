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
import jahirfiquitiva.apps.medicode.logic.objects.Doctor;
import jahirfiquitiva.apps.medicode.logic.ListsManager;

public class CreateDoctorActivity extends AppCompatActivity {

    private Context context;
    private ListsManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        manager = (ListsManager) getIntent().getSerializableExtra("manager");

        setContentView(R.layout.activity_create_doctor);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final EditText name = (EditText) findViewById(R.id.name);
        final EditText id = (EditText) findViewById(R.id.id);
        final LinearLayout specLayout = (LinearLayout) findViewById(R.id.specializationLayout);
        final TextView specialization = (TextView) findViewById(R.id.specialization);
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        specLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                new MaterialDialog.Builder(context)
                        .title(R.string.specialization)
                        .items(R.array.specializations)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int
                                    position, CharSequence text) {
                                specialization.setText(text);
                            }
                        })
                        .show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String docName = name.getText().toString().trim();
                String docID = id.getText().toString().trim();
                String docSpecialization = specialization.getText().toString().trim();
                if (docName.length() > 0 && docID.length() > 3
                        && docSpecialization.length() > 0) {

                    final boolean added = manager.addDoctor(new Doctor(docName, docID,
                            docSpecialization));

                    MaterialDialog dialog = new MaterialDialog.Builder(context)
                            .title(added ? R.string.success : R.string.error)
                            .content(added ? R.string.doctor_success : R.string.doctor_error)
                            .positiveText(added ? android.R.string.yes : android.R.string.ok)
                            .cancelable(false).autoDismiss(false).canceledOnTouchOutside(false)
                            .positiveColor(ContextCompat.getColor(context, R.color.darkAccent))
                            .negativeColor(ContextCompat.getColor(context, R.color.darkAccent))
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull
                                        DialogAction which) {
                                    dialog.dismiss();
                                    name.setText("");
                                    id.setText("");
                                    specialization.setText("");
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
                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.main), context
                            .getResources().getString(R.string.doctor_not_filled), Snackbar
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