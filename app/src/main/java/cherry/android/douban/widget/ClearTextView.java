package cherry.android.douban.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import cherry.android.douban.R;

/**
 * Created by LHEE on 2017/7/20.
 */

public class ClearTextView extends AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {
    private Drawable mClearDrawable;
    private boolean mHasFocus;
    private OnTextClearListener mListener;

    public ClearTextView(Context context) {
        this(context, null);
    }

    public ClearTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClearTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = ContextCompat.getDrawable(getContext(), R.mipmap.ic_delete_input);
        }
        mClearDrawable.setBounds(0, 0, 30, 30);
        setClearDrawableVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    private void setClearDrawableVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], right, compoundDrawables[3]);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        mHasFocus = hasFocus;
        if (hasFocus) {
            setClearDrawableVisible(getText().length() > 0);
        } else {
            setClearDrawableVisible(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mHasFocus) {
            setClearDrawableVisible(s.length() > 0);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && event.getX() < (getWidth() - getPaddingRight());
                if (touchable) {
                    setText("");
                    if (mListener != null) {
                        mListener.onClear(this);
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public void setOnTextClearListener(OnTextClearListener l) {
        mListener = l;
    }

    public interface OnTextClearListener {
        void onClear(View v);
    }
}
