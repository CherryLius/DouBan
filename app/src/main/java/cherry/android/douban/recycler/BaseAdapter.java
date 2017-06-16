package cherry.android.douban.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/6/6.
 */

public class BaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<T> mDataList;
    protected ItemViewDelegateManager<T, VH> mDelegateManager;

    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public BaseAdapter(List<T> data) {
        mDataList = data;
        mDelegateManager = ItemViewDelegateManager.newDelegateManager();
    }

    public BaseAdapter() {
        mDelegateManager = ItemViewDelegateManager.newDelegateManager();
    }

    @Override
    public final int getItemViewType(int position) {
        if (!useDelegate()) return super.getItemViewType(position);
        if (mDataList == null) return super.getItemViewType(position);
        return mDelegateManager.getViewType(mDataList.get(position), position);
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewDelegate delegate = mDelegateManager.getItemViewDelegate(viewType);
        int layoutId = delegate.getViewLayoutId();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        VH holder = (VH) delegate.createViewHolder(itemView);
        setListener(itemView, holder);
        return holder;
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        if (mDataList == null) return;
        mDelegateManager.convert(holder, mDataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDataList != null ? mDataList.size() : 0;
    }

    protected boolean useDelegate() {
        return mDelegateManager.getDelegateCount() > 0;
    }

    private void setListener(final View itemView, final VH holder) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    int position = holder.getAdapterPosition();
                    mItemClickListener.onItemClick(itemView, holder, position);
                }
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mItemLongClickListener != null) {
                    int position = holder.getAdapterPosition();
                    return mItemLongClickListener.onItemLongClick(itemView, holder, position);
                }
                return false;
            }
        });
    }

    public void addDelegate(ItemViewDelegate<T, VH> delegate) {
        mDelegateManager.addDelegate(delegate);
    }

    public void addDelegate(int viewType, ItemViewDelegate<T, VH> delegate) {
        mDelegateManager.addDelegate(viewType, delegate);
    }

    public void showData(List<T> data) {
        this.mDataList = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, RecyclerView.ViewHolder holder, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View itemView, RecyclerView.ViewHolder holder, int position);
    }
}
