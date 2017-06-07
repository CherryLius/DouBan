package cherry.android.douban.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2017/6/6.
 */

public abstract class CommonAdapter<T, VH extends RecyclerView.ViewHolder> extends BaseAdapter<T, VH> {
    public CommonAdapter(List<T> data, int itemLayoutId) {
        super(data);
        addDelegateWithId(itemLayoutId);
    }

    public CommonAdapter(int itemLayoutId) {
        super();
        addDelegateWithId(itemLayoutId);
    }

    private void addDelegateWithId(final int itemLayoutId) {
        addDelegate(new ItemViewDelegate<T, VH>() {
            @Override
            public int getViewLayoutId() {
                return itemLayoutId;
            }

            @Override
            public boolean isMatchViewType(T t, int position) {
                return true;
            }

            @Override
            public VH createViewHolder(View itemView) {
                return CommonAdapter.this.createDefaultViewHolder(itemView);
            }

            @Override
            public void convert(VH holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }


        });
    }

    protected abstract void convert(VH holder, T t, int position);

    protected abstract VH createDefaultViewHolder(View itemView);
}
