package io.material.catalog.bottomsheet;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.switchmaterial.SwitchMaterial;

import io.material.catalog.R;
import io.material.catalog.feature.DemoFragment;
import io.material.catalog.windowpreferences.WindowPreferencesManager;

public class PersistentBottomSheetDemoFragment extends DemoFragment {
  private WindowInsetsCompat windowInsets;

  @Override
  public View onCreateDemoView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    View view = layoutInflater.inflate(R.layout.cat_persistent_bottomsheet_fragment, viewGroup, false /* attachToRoot */);
    View bottomSheetPersistent = view.findViewById(R.id.bottom_drawer);

    SwitchMaterial expansionSwitch = view.findViewById(R.id.cat_bottomsheet_expansion_switch);
    SwitchMaterial fullScreenSwitch = view.findViewById(R.id.cat_fullscreen_switch);
    fullScreenSwitch.setOnCheckedChangeListener(
        (buttonView, isChecked) -> {
          setBottomSheetHeights(view, isChecked);
          expansionSwitch.setEnabled(!isChecked);
        });
    expansionSwitch.setOnCheckedChangeListener(
        (buttonView, isChecked) -> {
          ViewGroup.LayoutParams lp = bottomSheetPersistent.getLayoutParams();
          lp.height = isChecked ? 400 : getBottomSheetPersistentDefaultHeight();
          bottomSheetPersistent.setLayoutParams(lp);

          fullScreenSwitch.setEnabled(!isChecked);

          view.requestLayout();
        });

    ViewCompat.setOnApplyWindowInsetsListener(
        view,
        (ignored, insets) -> {
          windowInsets = insets;
          setBottomSheetHeights(view, fullScreenSwitch.isChecked());
          return insets;
        });
    BottomSheetBehavior.from(bottomSheetPersistent).addBottomSheetCallback(
        createBottomSheetCallback(view.findViewById(R.id.cat_persistent_bottomsheet_state))
    );
    return view;
  }

  private int getBottomSheetPersistentDefaultHeight() {
    return getWindowHeight() * 3 / 5;
  }

  private int getWindowHeight() {
    // Calculate window height for fullscreen use
    DisplayMetrics displayMetrics = new DisplayMetrics();
    ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    // Allow Fullscreen BottomSheet to expand beyond system windows and draw under status bar.
    int height = displayMetrics.heightPixels;
    if (windowInsets != null) {
      height += windowInsets.getSystemWindowInsetTop();
      height += windowInsets.getSystemWindowInsetBottom();
    }
    return height;
  }

  private void setBottomSheetHeights(View view, boolean fullScreen) {
    View bottomSheetChildView = view.findViewById(R.id.bottom_drawer);
    ViewGroup.LayoutParams params = bottomSheetChildView.getLayoutParams();
    BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetChildView);
    bottomSheetBehavior.setUpdateImportantForAccessibilityOnSiblings(fullScreen);
    boolean fitToContents = true;
    float halfExpandedRatio = 0.5f;
    int windowHeight = getWindowHeight();
    if (params != null) {
      if (fullScreen) {
        // 全屏
        params.height = windowHeight;
        fitToContents = false;
        halfExpandedRatio = 0.7f;
      } else {
        // 默认高度
        params.height = getBottomSheetPersistentDefaultHeight();
      }
      bottomSheetChildView.setLayoutParams(params);
      bottomSheetBehavior.setFitToContents(fitToContents);
      bottomSheetBehavior.setHalfExpandedRatio(halfExpandedRatio);
    }
  }

  private BottomSheetBehavior.BottomSheetCallback createBottomSheetCallback(@NonNull TextView text) {
    BottomSheetBehavior.BottomSheetCallback bottomSheetCallback =
        new BottomSheetBehavior.BottomSheetCallback() {
          @Override
          public void onStateChanged(@NonNull View bottomSheet, int newState) {

            switch (newState) {
              case BottomSheetBehavior.STATE_DRAGGING:
                text.setText(R.string.cat_bottomsheet_state_dragging);
                break;
              case BottomSheetBehavior.STATE_EXPANDED:
                text.setText(R.string.cat_bottomsheet_state_expanded);
                break;
              case BottomSheetBehavior.STATE_COLLAPSED:
                text.setText(R.string.cat_bottomsheet_state_collapsed);
                break;
              case BottomSheetBehavior.STATE_HALF_EXPANDED:
                // 全屏时有效
                BottomSheetBehavior<View> bottomSheetBehavior =
                    BottomSheetBehavior.from(bottomSheet);
                text.setText(
                    getString(
                        R.string.cat_bottomsheet_state_half_expanded,
                        bottomSheetBehavior.getHalfExpandedRatio()));
                break;
              default:
                break;
            }
          }

          @Override
          public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
        };
    return bottomSheetCallback;
  }
}
