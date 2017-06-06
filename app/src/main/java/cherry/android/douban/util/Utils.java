package cherry.android.douban.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.compat.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Administrator on 2017/6/2.
 */

public final class Utils {
    public static <T> T getSystemService(Context context, String serviceName) {
        return (T) context.getSystemService(serviceName);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = getSystemService(context, Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null)
            return activeNetwork.isAvailable();
        return false;
    }

    public static boolean ping() {
        try {
            String url = "www.baidu.com";
            Process p = Runtime.getRuntime().exec("/system/bin/ping -c 3 -w 100 " + url);
            if (BuildConfig.DEBUG) {
                InputStream input = p.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                StringBuilder builder = new StringBuilder();
                String buffer;
                while ((buffer = in.readLine()) != null) {
                    builder.append(buffer)
                            .append('\n');
                }
                Logger.e("Test", "ping result=" + builder.toString());
            }
            //ping 状态
            int status = p.waitFor();
            Logger.i("Test", "status=" + status);
            return status == 0;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String list2String(List<String> list) {
        return list2String(list, new IPicker<String>() {
            @Override
            public String pick(String s) {
                return s;
            }
        });
    }

    public static <T> String list2String(List<T> list, IPicker<T> picker) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(picker.pick(list.get(i)));
            if (i < list.size() - 1)
                builder.append('/');

        }
        return builder.toString();
    }

    public interface IPicker<T> {
        String pick(T t);
    }
}
