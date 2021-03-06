package com.wingjay.jianshi.ui.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.wingjay.jianshi.R;
import com.wingjay.jianshi.global.JianShiApplication;
import com.wingjay.jianshi.prefs.UserPrefs;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import timber.log.Timber;

public class BaseActivity extends AppCompatActivity {

  //todo(wingjay) Add RxLifecycle function
  protected boolean isVisible = false;

  protected View containerView;
  protected String TAG = getClass().getSimpleName() + ": %s";

  private boolean isNeedRegister = false;

  protected void setNeedRegister() {
    this.isNeedRegister = true;
  }

  @Inject
  UserPrefs userPrefs;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    JianShiApplication.getAppComponent().inject(this);
    Timber.d(TAG, "onCreate");
  }

  @Override
  public void setContentView(@LayoutRes int layoutResID) {
    super.setContentView(layoutResID);
    ButterKnife.inject(this);

    containerView = findViewById(R.id.layout_container);
    setContainerBgColorFromPrefs();
  }

  protected void setContainerBgColorFromPrefs() {
    if (containerView != null) {
      containerView.setBackgroundResource(userPrefs.getBackgroundColor());
    }
  }
  protected void setContainerBgColor(int colorRes) {
    if (containerView != null) {
      containerView.setBackgroundResource(colorRes);
    }
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    Timber.d(TAG, "onRestart");
  }

  @Override
  protected void onStart() {
    super.onStart();
    Timber.d(TAG, "onStart");
    if (isNeedRegister) {
      EventBus.getDefault().register(this);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    isVisible = true;
    Timber.d(TAG, "onResume");
  }

  @Override
  protected void onPause() {
    super.onPause();
    isVisible = false;
    Timber.d(TAG, "onPause");
  }

  @Override
  protected void onStop() {
    super.onStop();
    Timber.d(TAG, "onStop");
    if (EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().unregister(this);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Timber.d(TAG, "onDestroy");
  }

  public boolean isUISafe() {
    return isVisible;
  }

  protected void makeToast(@NonNull String content) {
    Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
  }
}
