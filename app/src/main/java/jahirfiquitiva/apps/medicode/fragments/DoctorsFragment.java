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

package jahirfiquitiva.apps.medicode.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import jahirfiquitiva.apps.medicode.R;
import jahirfiquitiva.apps.medicode.adapters.ListsAdapter;
import jahirfiquitiva.apps.medicode.base.Doctor;


public class DoctorsFragment extends Fragment {

    private ArrayList<Doctor> list;

    public static Fragment newInstance(ArrayList<Doctor> list) {
        DoctorsFragment fragment = new DoctorsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("list", list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            list = getArguments().getParcelableArrayList("list");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.list, container, false);

        ListsAdapter adapter = new ListsAdapter(getContext());
        adapter.setDoctors(list);

        RecyclerView rv = (RecyclerView) layout.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                false));
        rv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        rv.setAdapter(adapter);

        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public DoctorsFragment() {
    }

}