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
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import jahirfiquitiva.apps.medicode.R;
import jahirfiquitiva.apps.medicode.adapters.PagerAdapter;
import jahirfiquitiva.apps.medicode.base.Doctor;
import jahirfiquitiva.apps.medicode.base.ListsManager;
import jahirfiquitiva.apps.medicode.base.Patient;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabs;
    private ViewPager pager;
    private ListsManager manager;
    private int lastSelected = 0;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);

        manager = new ListsManager();
        addSomeItems();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        tabs = (TabLayout) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Creando datos de la lista: " + String.valueOf
                        (lastSelected + 1), Toast.LENGTH_SHORT).show();
            }
        });

        //tabs.removeAllTabs();
        tabs.addTab(tabs.newTab().setIcon(ContextCompat.getDrawable(this, R.drawable.ic_doctor)));
        tabs.addTab(tabs.newTab().setIcon(ContextCompat.getDrawable(this, R.drawable.ic_patient)));
        tabs.addTab(tabs.newTab().setIcon(ContextCompat.getDrawable(this, R.drawable
                .ic_appntmnts)));
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
                if (lastSelected != position) {
                    //TODO: Do something
                }
                lastSelected = position;
            }
        });
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager(), manager));
        pager.setOffscreenPageLimit(3);
    }

    private void addSomeItems() {
        manager.addDoctor(new Doctor("Juanito", "123123", "Perrear"));
        manager.addDoctor(new Doctor("Julian", "12432354", "Mamar gallo"));
        manager.addDoctor(new Doctor("Checho", "4363", "Enchuquizarse"));
        manager.addPatient(new Patient("Jahir", "1242", true, 19));
        manager.addPatient(new Patient("Fulanita", "1243214", false, 24));
        manager.addPatient(new Patient("Saturnino", "35543", true, 32));
        manager.addPatient(new Patient("Florentina", "56431242", false, 39));
    }

}