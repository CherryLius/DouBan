package cherry.android.douban.recycler;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2017/6/6.
 */

public class ItemViewDelegateManager<T, VH extends RecyclerView.ViewHolder> {

    private SparseArrayCompat<ItemViewDelegate<T, VH>> mDelegates = new SparseArrayCompat<>();

    private ItemViewDelegateManager() {
    }

    public void addDelegate(ItemViewDelegate<T, VH> delegate) {
        if (delegate == null) return;
        mDelegates.put(mDelegates.size(), delegate);
    }

    public void addDelegate(int viewType, ItemViewDelegate<T, VH> delegate) {
        if (mDelegates.get(viewType) != null) {
            throw new IllegalStateException("already add a delegate at viewType="
                    + viewType + ", delegate=" + mDelegates.get(viewType));
        }
        mDelegates.put(viewType, delegate);
    }

    public int getViewType(T item, int position) {
        for (int i = 0; i < mDelegates.size(); i++) {
            ItemViewDelegate<T, VH> delegate = mDelegates.get(i);
            if (delegate.isMatchViewType(item, position)) {
                return mDelegates.keyAt(i);
            }
        }
        throw new IllegalStateException("cannot match a viewType at position=" + position);
    }

    public void convert(VH holder, T item, int position) {
        for (int i = 0; i < mDelegates.size(); i++) {
            ItemViewDelegate<T, VH> delegate = mDelegates.get(i);
            if (delegate.isMatchViewType(item, position)) {
                delegate.convert(holder, item, position);
                return;
            }
        }
        throw new IllegalStateException("cannot match a viewType at position=" + position);
    }

    public ItemViewDelegate getItemViewDelegate(int viewType) {
        return mDelegates.get(viewType);
    }

    public int getDelegateCount() {
        return mDelegates.size();
    }

    public static ItemViewDelegateManager newDelegateManager() {
        return new ItemViewDelegateManager();
    }
}
