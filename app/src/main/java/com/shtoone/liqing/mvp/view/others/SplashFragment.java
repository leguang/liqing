package com.shtoone.liqing.mvp.view.others;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shtoone.liqing.BaseApplication;
import com.shtoone.liqing.R;
import com.shtoone.liqing.mvp.contract.SplashContract;
import com.shtoone.liqing.mvp.presenter.SplashPresenter;
import com.shtoone.liqing.mvp.view.base.BaseFragment;
import com.shtoone.liqing.common.Constants;
import com.shtoone.liqing.utils.SharedPreferencesUtils;
import com.shtoone.liqing.widget.CircleTextProgressbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public class SplashFragment extends BaseFragment<SplashContract.Presenter> implements SplashContract.View {

    private static final String TAG = SplashFragment.class.getSimpleName();
    //目的是为了判断网络请求时，用户是否退出
    private boolean isExit;
    private boolean isFirstentry;

    @BindView(R.id.ctp_skip)
    CircleTextProgressbar ctpSkip;

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @NonNull
    @Override
    protected SplashContract.Presenter createPresenter() {
        return new SplashPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.checkLogin();

        ctpSkip.setOutLineColor(Color.TRANSPARENT);
        ctpSkip.setInCircleColor(Color.parseColor("#99000000"));
        ctpSkip.setProgressColor(Color.RED);
        ctpSkip.setProgressLineWidth(3);
        ctpSkip.setTimeMillis(Constants.DEFAULT_TIMEOUT * 1000);
        ctpSkip.setText(Constants.DEFAULT_TIMEOUT + "s");
        ctpSkip.setCountdownProgressListener(1, new CircleTextProgressbar.OnCountdownProgressListener() {
            int intTime = Constants.DEFAULT_TIMEOUT;
            double temp = 100.0 / Constants.DEFAULT_TIMEOUT;

            @Override
            public void onProgress(int what, int progress) {
                if (((int) (progress % temp)) == 0) {
                    ctpSkip.setText((--intTime) + "s");
                }
            }
        });
        ctpSkip.start();
        isFirstentry = (boolean) SharedPreferencesUtils.get(BaseApplication.mContext, Constants.ISFIRSTENTRY, true);
    }

    @OnClick(R.id.ctp_skip)
    public void onClick() {


    }

    @Override
    public void go2LoginOrGuide() {
        if (isExit) {
            return;
        }
        if (isFirstentry) {
            start(GuideFragment.newInstance());
        } else {
            start(LoginFragment.newInstance());
        }
    }

    @Override
    public void go2Main() {
        if (isExit) {
            return;
        }
        start(MainFragment.newInstance());
    }

    @Override
    public void onDestroy() {
        isExit = true;
        super.onDestroy();
    }
}
