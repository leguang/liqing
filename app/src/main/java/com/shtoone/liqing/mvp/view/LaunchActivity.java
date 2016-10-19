package com.shtoone.liqing.mvp.view;

import android.os.Bundle;

import com.shtoone.liqing.R;
import com.shtoone.liqing.mvp.view.base.BaseActivity;
import com.shtoone.liqing.mvp.view.others.SplashFragment;
import com.shtoone.liqing.common.Constants;
import com.shtoone.liqing.utils.ToastUtils;

/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public class LaunchActivity extends BaseActivity {

    private static final String TAG = LaunchActivity.class.getSimpleName();
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_launch_activity, SplashFragment.newInstance());
        }
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public boolean swipeBackPriority() {
        return false;
    }

    @Override
    public void onBackPressedSupport() {

        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            ToastUtils.showInfoToast(getApplicationContext(), Constants.PRESS_AGAIN);
        }
    }
}
