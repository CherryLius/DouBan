package cherry.android.douban.recycler.wrapper;

import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import cherry.android.douban.recycler.ViewHolder;

/**
 * Created by Administrator on 2017/6/6.
 */

public class HeaderAndFooterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();

    private RecyclerView.Adapter mInnerAdapter;

    public HeaderAndFooterWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            return new ViewHolder(mHeaderViews.get(viewType));
        } else if (mFooterViews.get(viewType) != null) {
            return new ViewHolder(mFooterViews.get(viewType));
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position))
            return mFooterViews.keyAt(position - getHeaderCount() - getRealItemCount());
        return mInnerAdapter.getItemViewType(position - getHeaderCount());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position) || isFooterViewPos(position))
            return;
        mInnerAdapter.onBindViewHolder(holder, position - getHeaderCount());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isHeaderViewPos(position) || isFooterViewPos(position)) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null)
                        return spanSizeLookup.getSpanSize(position);
                    return 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                lp.setFullSpan(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getRealItemCount() + getFooterCount();
    }

    private int getHeaderCount() {
        return mHeaderViews.size();
    }

    private int getFooterCount() {
        return mFooterViews.size();
    }

    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }

    private boolean isHeaderViewPos(int position) {
        return position < getHeaderCount();
    }

    private boolean isFooterViewPos(int position) {
        return position >= getHeaderCount() + getRealItemCount();
    }

    public void addHeaderView(@NonNull View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFooterView(@NonNull View view) {
        mFooterViews.put(mFooterViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }
}
