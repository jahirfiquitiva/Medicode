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

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;

import jahirfiquitiva.apps.medicode.R;
import jahirfiquitiva.apps.medicode.adapters.PagerAdapter;
import jahirfiquitiva.apps.medicode.fragments.PersonFragment;
import jahirfiquitiva.apps.medicode.logic.ListsManager;
import jahirfiquitiva.apps.medicode.persistence.SerializableFile;
import jahirfiquitiva.apps.medicode.utils.IconTintUtils;
import jahirfiquitiva.apps.medicode.utils.PermissionsUtils;

/**
 * @author Jahir Fiquitiva
 */
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_STORAGE_ACCESS = 5;
    private ViewPager pager;
    private FloatingActionButton fab;
    private Context context;
    private ListsManager manager;
    private SerializableFile file;
    private MenuItem mSearchItem;
    private SearchView mSearchView;
    private int lastSelected = 0;
    private boolean open = true;
    private boolean finishActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);

        manager = new ListsManager();

        requestPermissions(false, false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        animateFab(-1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent create = null;
                boolean doctor = true;
                switch (lastSelected) {
                    case 0:
                        create = new Intent(context, CreateDoctorActivity.class);
                        break;
                    case 1:
                        doctor = false;
                        create = new Intent(context, CreatePatientActivity.class);
                        break;
                }
                if (create != null) {
                    create.putExtra("manager", manager);
                    startActivityForResult(create, doctor ? 10 : 11);
                }
            }
        });

        tabs.removeAllTabs();
        tabs.addTab(tabs.newTab().setIcon(IconTintUtils.getTintedIcon(this, R.drawable
                .ic_doctor, (lastSelected == 0) ?
                android.R.color.white : R.color.unselectedIcon)));
        tabs.addTab(tabs.newTab().setIcon(
                IconTintUtils.getTintedIcon(this, R.drawable.ic_patient, (lastSelected == 1) ?
                        android.R.color.white : R.color.unselectedIcon)));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                if (tab.getIcon() != null)
                    tab.getIcon().setColorFilter(ContextCompat.getColor(context, android.R.color
                            .white), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                if (tab.getIcon() != null)
                    tab.getIcon().setColorFilter(ContextCompat.getColor(context, R.color
                            .unselectedIcon), PorterDuff.Mode.SRC_IN);
            }
        });
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs) {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                animateFab(position);
                if (lastSelected != position) search(null, true);
                lastSelected = position;
                if (mSearchView != null) {
                    mSearchView.setInputType(position == 0 ? InputType
                            .TYPE_TEXT_FLAG_CAP_CHARACTERS : InputType.TYPE_CLASS_NUMBER);
                    mSearchView.setQueryHint(getString(R.string.search_x, getTabName
                            (lastSelected)));
                }
                invalidateOptionsMenu();
            }
        });
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager(), manager));
    }

    @Override
    protected void onResume() {
        super.onResume();
        search(null, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cleanSearch();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode >= 10 && requestCode <= 14) {
            if (data != null) {
                updatePager((ListsManager) data.getSerializableExtra("manager"));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        setupSearch(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                new MaterialDialog.Builder(this)
                        .title(R.string.about)
                        .content(getResources().getString(R.string.jahir_info).replace("\\n", "\n"))
                        .positiveText(android.R.string.ok)
                        .positiveColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        .icon(ContextCompat.getDrawable(this, R.mipmap.logo))
                        .show();
                break;
            case R.id.save:
                requestPermissions(true, false);
                break;
            case R.id.appntmnt:
                Intent createAppntmnt = new Intent(this, CreateAppntmntActivity.class);
                createAppntmnt.putExtra("manager", manager);
                startActivityForResult(createAppntmnt, 14);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new MaterialDialog.Builder(this)
                .title(R.string.exit)
                .content(R.string.exit_confirmation)
                .positiveText(android.R.string.yes)
                .negativeText(android.R.string.no)
                .positiveColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .negativeColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction
                            which) {
                        if (manager.hasBeenModified()) {
                            new MaterialDialog.Builder(context)
                                    .title(R.string.save_data)
                                    .content(R.string.save_data_confirmation)
                                    .positiveText(android.R.string.yes)
                                    .negativeText(android.R.string.no)
                                    .positiveColor(ContextCompat.getColor(context, R.color
                                            .colorPrimary))
                                    .negativeColor(ContextCompat.getColor(context, R.color
                                            .colorPrimary))
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull
                                                DialogAction which) {
                                            open = false;
                                            requestPermissions(true, true);
                                        }
                                    })
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull
                                                DialogAction which) {
                                            finish();
                                        }
                                    })
                                    .show();
                        } else {
                            finish();
                        }
                    }
                })
                .show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("last", lastSelected);
        outState.putSerializable("manager", manager);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastSelected = savedInstanceState.getInt("last", 0);
        search(null, true);
        updatePager((ListsManager) savedInstanceState.getSerializable("manager"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_ACCESS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadOrSaveAppData(open, finishActivity);
            } else {
                Snackbar.make(findViewById(R.id.pager), R.string
                        .permission_denied_sec, Snackbar
                        .LENGTH_LONG)
                        .show();
            }
        }
    }

    private void requestPermissions(final boolean save, final boolean finishActivity) {
        this.finishActivity = finishActivity;
        PermissionsUtils.checkPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, new
                        PermissionsUtils.PermissionRequestListener() {
                            @Override
                            public void onPermissionRequest() {
                                Snackbar.make(findViewById(R.id.pager), R.string
                                        .permission_rationale, Snackbar
                                        .LENGTH_LONG)
                                        .setAction(android.R.string.ok, new View
                                                .OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                ActivityCompat.requestPermissions(((Activity)
                                                                context), new
                                                                String[]{Manifest.permission
                                                                .WRITE_EXTERNAL_STORAGE},
                                                        REQUEST_STORAGE_ACCESS);
                                            }
                                        })
                                        .show();
                            }

                            @Override
                            public void onPermissionDenied() {
                                Snackbar.make(findViewById(R.id.pager), R.string
                                        .permission_denied, Snackbar
                                        .LENGTH_LONG)
                                        .setAction(android.R.string.ok, new View
                                                .OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                onPermissionRequest();
                                            }
                                        })
                                        .show();
                            }

                            @Override
                            public void onPermissionCompletelyDenied() {
                                Snackbar.make(findViewById(R.id.pager), R.string
                                        .permission_completely_denied, Snackbar
                                        .LENGTH_LONG)
                                        .show();
                            }

                            @Override
                            public void onPermissionGranted() {
                                loadOrSaveAppData(open && (!save), finishActivity);
                            }
                        });
    }

    private void loadOrSaveAppData(boolean load, boolean finish) {
        this.file = new SerializableFile(this, "data.dat");
        if (load) {
            loadAppData();
        } else {
            saveAppData(finish);
        }
    }

    private void loadAppData() {
        file.open();
        if (file.hasContent()) {
            try {
                final ListsManager mngAux = (ListsManager) file.getObject();
                if (mngAux != null) {
                    new MaterialDialog.Builder(context)
                            .title(R.string.load_data)
                            .content(R.string.load_data_content)
                            .positiveColor(ContextCompat.getColor(context, R.color.colorPrimary))
                            .negativeColor(ContextCompat.getColor(context, R.color
                                    .colorPrimary))
                            .positiveText(android.R.string.yes)
                            .negativeText(android.R.string.no)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull
                                        DialogAction
                                        which) {
                                    updatePager(mngAux);
                                }
                            })
                            .show();
                    open = false;
                }
            } catch (IOException | ClassNotFoundException e) {
                // Ignore
            }
        }
    }

    @SuppressLint("ShowToast")
    private void saveAppData(boolean finish) {
        search(null, true);
        if (file != null) {
            file.open();
            try {
                file.saveObject(manager);
                showToastAndFinish(Toast.makeText(context, "Datos guardados correctamente", Toast
                        .LENGTH_SHORT), finish);
            } catch (IOException e) {
                showToastAndFinish(Toast.makeText(context, "Ocurrió un error al guardar el " +
                                "archivo.",
                        Toast.LENGTH_SHORT), finish);
            } catch (ClassNotFoundException ignored) {
            }
        }
    }

    public void setManager(ListsManager manager) {
        this.manager = manager;
    }

    private void setupSearch(Menu menu) {
        mSearchItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        try {
            ((EditText) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
                    .setHintTextColor(ContextCompat.getColor(context, R.color
                            .semitransparent_white));
            mSearchView.setInputType(lastSelected == 0 ? InputType
                    .TYPE_TEXT_FLAG_CAP_CHARACTERS : InputType.TYPE_CLASS_NUMBER);
        } catch (Exception ignored) {
        }
        MenuItemCompat.setOnActionExpandListener(mSearchItem, new MenuItemCompat
                .OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                if (lastSelected == 0) {
                    search(null, manager.getDoctors().size() <= 0);
                    if (manager.getDoctors().size() > 0) {
                        return true;
                    } else {
                        Snackbar.make(findViewById(R.id.pager), getString(R.string
                                        .not_enough_to_search, getString(R.string.doctors)
                                        .toLowerCase())
                                , Snackbar
                                        .LENGTH_LONG).show();
                        return false;
                    }
                } else if (lastSelected == 1) {
                    search(null, manager.getPatients().size() <= 0);
                    if (manager.getPatients().size() > 0) {
                        return true;
                    } else {
                        Snackbar.make(findViewById(R.id.pager), getString(R.string
                                .not_enough_to_search, getString(R.string.patients).toLowerCase()
                        ), Snackbar
                                .LENGTH_LONG).show();
                        return false;
                    }
                }
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                search(null, true);
                updatePager(manager);
                return true;
            }
        });
        mSearchView.setQueryHint(getString(R.string.search_x, getTabName(lastSelected)));
        mSearchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus && mSearchItem != null) {
                    mSearchItem.collapseActionView();
                }
                try {
                    ((SearchView) view).onActionViewCollapsed();
                } catch (Exception ignored) {
                }
                search(null, true);
                updatePager(manager);
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                search(s, false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search(s, false);
                return true;
            }
        });

        mSearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    private void animateFab(int pos) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            hideFab(pos);
            showFab();
        }
    }

    private void showFab() {
        if (fab != null) {
            fab.setScaleX(0);
            fab.setScaleY(0);
            fab.animate()
                    .scaleX(1)
                    .scaleY(1)
                    .setStartDelay(100)
                    .setDuration(250);
        }
    }

    private void hideFab(int pos) {
        if (fab != null) {
            fab.animate()
                    .scaleX(0)
                    .scaleY(0)
                    .setStartDelay(100)
                    .setDuration(250);
            switch (pos) {
                default:
                case 0:
                    fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor
                            (context, R.color.fab_one)));
                    break;
                case 1:
                    fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor
                            (context, R.color.fab_two)));
                    break;
            }
        }
    }

    private void updatePager(ListsManager mng) {
        if (mng == null) return;
        setManager(mng);
        if (pager != null) {
            if (pager.getAdapter() != null) {
                ((PagerAdapter) pager.getAdapter()).setManager(mng);
            }
            pager.setCurrentItem(lastSelected);
        }
    }

    private void showToastAndFinish(final Toast toast, boolean finish) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(toast.getDuration());
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        toast.show();
        if (finish)
            thread.start();
    }

    private String getTabName(int position) {
        if (position == 0) {
            return getResources().getString(R.string.doctor).toLowerCase();
        } else if (position == 1) {
            return getResources().getString(R.string.patient).toLowerCase();
        } else {
            return "";
        }
    }

    private void search(String s, boolean reset) {
        Fragment frag = getSupportFragmentManager().findFragmentByTag("page:" + lastSelected);
        if (frag != null) {
            if (frag instanceof PersonFragment) {
                ((PersonFragment) frag).performSearch(s, reset);
            }
        }
    }

    private void cleanSearch() {
        search(null, true);
        if (mSearchView != null) {
            mSearchView.setInputType(lastSelected == 0 ? InputType
                    .TYPE_TEXT_FLAG_CAP_CHARACTERS : InputType.TYPE_CLASS_NUMBER);
            mSearchView.setQuery("", false);
        }
    }

}