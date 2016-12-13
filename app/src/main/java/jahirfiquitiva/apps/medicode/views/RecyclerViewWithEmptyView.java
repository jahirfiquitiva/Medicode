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

package jahirfiquitiva.apps.medicode.views;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RecyclerViewWithEmptyView extends RecyclerView {
    @Nullable
    View emptyView;
    @Nullable
    View searchingView;

    @IntDef({STATE_NORMAL, STATE_SEARCHING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    public static final int STATE_NORMAL = 0;
    public static final int STATE_SEARCHING = 1;

    @State
    private int state = STATE_NORMAL;

    public RecyclerViewWithEmptyView(Context context) {
        super(context);
    }

    public RecyclerViewWithEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewWithEmptyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    void checkIfEmptyOrSearching() {
        if (getAdapter() != null) {
            switch (state) {
                case STATE_NORMAL:
                    if (emptyView != null) {
                        emptyView.setVisibility(getAdapter().getItemCount() > 0 ? GONE : VISIBLE);
                        if (searchingView != null) {
                            searchingView.setVisibility(GONE);
                        }
                        setVisibility(getAdapter().getItemCount() > 0 ? VISIBLE : GONE);
                    }
                    break;
                case STATE_SEARCHING:
                    if (searchingView != null) {
                        searchingView.setVisibility(getAdapter().getItemCount() > 0 ? GONE :
                                VISIBLE);
                        if (emptyView != null) {
                            emptyView.setVisibility(GONE);
                        }
                        setVisibility(getAdapter().getItemCount() > 0 ? VISIBLE : GONE);
                    }
                    break;
            }
        }
    }

    final
    @NonNull
    AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            checkIfEmptyOrSearching();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            checkIfEmptyOrSearching();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            checkIfEmptyOrSearching();
        }
    };

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
        checkIfEmptyOrSearching();
    }

    @State
    public int getState() {
        return state;
    }

    public void setState(@State int state) {
        this.state = state;
        checkIfEmptyOrSearching();
    }

    public void setEmptyView(@Nullable View emptyView) {
        this.emptyView = emptyView;
        checkIfEmptyOrSearching();
    }

    public void setSearchingView(@Nullable View searchingView) {
        this.searchingView = searchingView;
        checkIfEmptyOrSearching();
    }

}