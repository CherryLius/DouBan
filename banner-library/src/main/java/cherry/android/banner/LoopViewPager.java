package cherry.android.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/3.
 */

public class LoopViewPager extends ViewPager {

    private LoopPagerAdapterWrapper mAdapterWrapper;
    private List<OnPageChangeListener> mOnPageChangeListeners;
    private OnPageChangeListener mOnPageChangeListener;
    private boolean mBoundaryCaching;

    public LoopViewPager(Context context) {
        this(context, null);
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.addOnPageChangeListener(mWrapperPageChangeListener);
    }

    public void setBoundaryCaching(boolean flag) {
        mBoundaryCaching = flag;
        if (mAdapterWrapper != null)
            mAdapterWrapper.setBoundaryCaching(mBoundaryCaching);
    }

    @Override
    public PagerAdapter getAdapter() {
        return mAdapterWrapper != null ? mAdapterWrapper.getRealAdapter() : null;
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        mAdapterWrapper = new LoopPagerAdapterWrapper(adapter);
        mAdapterWrapper.setBoundaryCaching(mBoundaryCaching);
        super.setAdapter(mAdapterWrapper);
        setCurrentItem(0, false);
    }

    @Override
    public int getCurrentItem() {
        return mAdapterWrapper != null ? mAdapterWrapper.toRealPosition(super.getCurrentItem()) : 0;
    }

    @Override
    public void setCurrentItem(int item) {
        if (getCurrentItem() != item) {
            setCurrentItem(item, true);
        }
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        int realItem = mAdapterWrapper.toRealPosition(item);
        super.setCurrentItem(realItem, smoothScroll);
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        if (mOnPageChangeListeners == null) {
            mOnPageChangeListeners = new ArrayList<>();
        }
        mOnPageChangeListeners.add(listener);
    }

    @Override
    public void removeOnPageChangeListener(OnPageChangeListener listener) {
        if (mOnPageChangeListeners != null)
            mOnPageChangeListeners.remove(listener);
    }

    @Override
    public void clearOnPageChangeListeners() {
        if (mOnPageChangeListeners != null)
            mOnPageChangeListeners.clear();
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mOnPageChangeListener = listener;
    }

    private void notifyOnPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mOnPageChangeListener != null)
            mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        if (mOnPageChangeListeners != null)
            for (int i = 0; i < mOnPageChangeListeners.size(); i++) {
                mOnPageChangeListeners.get(i).onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
    }

    private void notifyOnPageSelected(int position) {
        if (mOnPageChangeListener != null)
            mOnPageChangeListener.onPageSelected(position);
        if (mOnPageChangeListeners != null)
            for (int i = 0; i < mOnPageChangeListeners.size(); i++) {
                mOnPageChangeListeners.get(i).onPageSelected(position);
            }
    }

    private void notifyOnPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
        if (mOnPageChangeListeners != null) {
            for (int i = 0; i < mOnPageChangeListeners.size(); i++) {
                mOnPageChangeListeners.get(i).onPageScrollStateChanged(state);
            }
        }
    }

    private OnPageChangeListener mWrapperPageChangeListener = new OnPageChangeListener() {
        private float previousOffset = -1;
        private float previousPosition = -1;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int realPosition = position;
            if (mAdapterWrapper != null) {
                realPosition = mAdapterWrapper.toRealPosition(position);
                if (positionOffset == 0 && previousOffset == 0
                        && (position == 0 || position == mAdapterWrapper.getCount() - 1)) {
                    setCurrentItem(realPosition);
                }
            }
            previousOffset = positionOffset;
            if (realPosition != mAdapterWrapper.getRealCount() - 1) {
                notifyOnPageScrolled(realPosition, positionOffset, positionOffsetPixels);
            } else {
                if (positionOffset > 0.5f) {
                    notifyOnPageScrolled(0, 0, 0);
                } else {
                    notifyOnPageScrolled(realPosition, 0, 0);
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            final int realPosition = mAdapterWrapper.toRealPosition(position);
            if (previousPosition != realPosition) {
                previousPosition = realPosition;
                notifyOnPageSelected(realPosition);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mAdapterWrapper != null) {
                final int position = LoopViewPager.super.getCurrentItem();
                final int realPosition = mAdapterWrapper.toRealPosition(position);
                if (state == ViewPager.SCROLL_STATE_IDLE
                        && (position == 0 || position == mAdapterWrapper.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }
            notifyOnPageScrollStateChanged(state);
        }
    };
}
