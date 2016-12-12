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
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDTintHelper;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import jahirfiquitiva.apps.medicode.R;

public class LoginActivity extends AppCompatActivity {

    private Context context;
    private EditText userInput;
    private EditText passwordInput;
    private EditText masterPasswordInput;
    private View positiveAction;
    private Properties props;
    private boolean valid = false;
    private int attempts = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_login);

        props = new Properties();

        try {
            props.load(getResources().getAssets().open("users.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        AppCompatButton createBtn = (AppCompatButton) findViewById(R.id.create_account);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateUserDialog();
            }
        });
        */

        AppCompatButton btn = (AppCompatButton) findViewById(R.id.login_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                // showLoginDialog();
            }
        });

    }

    private void showLoginDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(R.string.login)
                .customView(R.layout.login_dialog, true)
                .positiveColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .positiveText(R.string.login)
                .negativeColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull
                            DialogAction which) {
                        if (userExists(userInput.getText().toString().trim())) {
                            if (isCorrectPassword(userInput.getText().toString().trim(),
                                    passwordInput.getText().toString().trim())) {
                                valid = true;
                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                            } else {
                                attempts += 1;
                                new MaterialDialog.Builder(context)
                                        .title(R.string.error)
                                        .content(R.string.invalid_user)
                                        .positiveText(android.R.string.ok)
                                        .positiveColor(ContextCompat.getColor(context, R.color
                                                .colorPrimary))
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog,
                                                                @NonNull DialogAction which) {
                                                showCloseAppDialog();
                                            }
                                        })
                                        .show();
                            }
                        } else {
                            attempts += 1;
                            new MaterialDialog.Builder(context)
                                    .title(R.string.error)
                                    .content(R.string.user_does_not_exist)
                                    .positiveText(android.R.string.ok)
                                    .positiveColor(ContextCompat.getColor(context, R.color
                                            .colorPrimary))
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog,
                                                            @NonNull DialogAction which) {
                                            showCloseAppDialog();
                                        }
                                    })
                                    .show();
                            showCloseAppDialog();
                        }
                    }
                })
                .build();

        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);

        userInput = (EditText) dialog.getCustomView().findViewById(R.id.user);

        //noinspection ConstantConditions
        passwordInput = (EditText) dialog.getCustomView().findViewById(R.id.password);
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                positiveAction.setEnabled(((userInput.getText().toString().trim().length() > 0)
                        && (s.toString().trim().length() > 0)));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        CheckBox checkbox = (CheckBox) dialog.getCustomView().findViewById(R.id
                .showPassword);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                passwordInput.setInputType(!isChecked ? InputType
                        .TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT);
                passwordInput.setTransformationMethod(!isChecked ?
                        PasswordTransformationMethod.getInstance() : null);
                passwordInput.setSelection(passwordInput.getText().length());
            }
        });

        MDTintHelper.setTint(checkbox, ContextCompat.getColor(context, R.color.colorPrimary));
        MDTintHelper.setTint(userInput, ContextCompat.getColor(context, R.color.colorPrimary));
        MDTintHelper.setTint(passwordInput, ContextCompat.getColor(context, R.color.colorPrimary));

        positiveAction.setEnabled(false);

        dialog.show();
    }

    private void showCloseAppDialog() {
        if (!valid && (attempts >= 3)) {
            new MaterialDialog.Builder(context)
                    .title(R.string.error)
                    .content(R.string.closing_app)
                    .positiveText(android.R.string.ok)
                    .positiveColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .autoDismiss(false)
                    .cancelable(false)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull
                                DialogAction which) {
                            finish();
                        }
                    })
                    .show();
        }
    }

    private void showCreateUserDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(R.string.login)
                .customView(R.layout.login_dialog, true)
                .positiveColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .positiveText(R.string.login)
                .negativeColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull
                            DialogAction which) {
                        // TODO: Create a new user
                    }
                })
                .build();

        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);

        //noinspection ConstantConditions
        passwordInput = (EditText) dialog.getCustomView().findViewById(R.id.password);
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                positiveAction.setEnabled(s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        CheckBox checkbox = (CheckBox) dialog.getCustomView().findViewById(R.id
                .showPassword);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                passwordInput.setInputType(!isChecked ? InputType
                        .TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT);
                passwordInput.setTransformationMethod(!isChecked ?
                        PasswordTransformationMethod.getInstance() : null);
                passwordInput.setSelection(passwordInput.getText().length());
            }
        });

        MDTintHelper.setTint(checkbox, ContextCompat.getColor(context, R.color.colorPrimary));
        MDTintHelper.setTint(passwordInput, ContextCompat.getColor(context, R.color.colorPrimary));

        positiveAction.setEnabled(false);

        dialog.show();
    }

    @SuppressWarnings("LoopStatementThatDoesntLoop")
    private boolean isCorrectPassword(String user, String password) {
        if (props == null) return false;
        Enumeration<Object> keys = props.keys();
        if (keys == null) return false;
        while (keys.hasMoreElements()) {
            String key = keys.nextElement().toString();
            if (key.equals(user)) {
                return props.getProperty(key).equals(password);
            }
        }
        return false;
    }

    private boolean userExists(String user) {
        return props != null && props.containsKey(user);
    }

}