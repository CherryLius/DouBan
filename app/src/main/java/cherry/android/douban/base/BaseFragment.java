package cherry.android.douban.base;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2017/6/2.
 */

public class BaseFragment extends Fragment {
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
