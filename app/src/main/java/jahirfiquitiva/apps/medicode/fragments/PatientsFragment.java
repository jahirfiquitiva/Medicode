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
import jahirfiquitiva.apps.medicode.logic.objects.Patient;


public class PatientsFragment extends Fragment {

    private ArrayList<Patient> list;
    private ListsAdapter adapter;
    private LinearLayoutManager layoutManager;
    private boolean scrollable;

    public PatientsFragment() {
    }

    public static Fragment newInstance(ArrayList<Patient> list) {
        PatientsFragment fragment = new PatientsFragment();
        Bundle args = new Bundle();
        args.putSerializable("list", list);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            list = (ArrayList<Patient>) getArguments().getSerializable("list");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.list, container, false);

        adapter = new ListsAdapter(getActivity(), null);
        adapter.setPatients(list);

        RecyclerView rv = (RecyclerView) layout.findViewById(R.id.rv);
        setupLinearLayout();
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager
                .VERTICAL));
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

    public void update(ArrayList<Patient> list) {
        this.list = list;
        if (adapter != null) {
            adapter.setPatients(list);
            adapter.notifyDataSetChanged();
        }
    }

    private void setupLinearLayout() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,
                false) {
            @Override
            public void onLayoutChildren(final RecyclerView.Recycler recycler, final RecyclerView
                    .State state) {
                super.onLayoutChildren(recycler, state);
                setScrollable(layoutScrollable());
            }
        };
    }

    private boolean layoutScrollable() {
        if (layoutManager == null) return false;
        if (list.isEmpty()) return false;

        final int firstVisibleItemPosition =
                layoutManager.findFirstCompletelyVisibleItemPosition();
        if (firstVisibleItemPosition != 0) {
            if (firstVisibleItemPosition == -1) {
                return false;
            }
        }
        return (layoutManager.findLastCompletelyVisibleItemPosition() != (list.size() - 1));
    }

    public boolean isScrollable() {
        return scrollable;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    public void updateScrollable() {
        this.scrollable = layoutScrollable();
    }
}