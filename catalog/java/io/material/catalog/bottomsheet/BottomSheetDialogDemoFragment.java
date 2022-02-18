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

public class BottomSheetDialogDemoFragment extends DemoFragment {

  private WindowPreferencesManager windowPreferencesManager;
  BottomSheetDialog bottomSheetDialog;
  private WindowInsetsCompat windowInsets;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    windowPreferencesManager = new WindowPreferencesManager(getContext());
  }

  @Override
  public View onCreateDemoView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    View view = layoutInflater.inflate(R.layout.cat_bottomsheet_dialog_demo_fragment, viewGroup, false);
    // 构建 BottomSheetDialog
    bottomSheetDialog = new BottomSheetDialog(getContext());
    bottomSheetDialog.setContentView(R.layout.cat_bottomsheet_content);
    bottomSheetDialog.setDismissWithAnimation(true);
    windowPreferencesManager.applyEdgeToEdgePreference(bottomSheetDialog.getWindow());
    View bottomSheetInternal = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
    BottomSheetBehavior.from(bottomSheetInternal).setPeekHeight(400);

    View button = view.findViewById(R.id.bottomsheet_button);
    button.setOnClickListener(v -> {
      bottomSheetDialog.show();
      bottomSheetDialog.setTitle(getText(R.string.cat_bottomsheet_title));
      Button button0 = bottomSheetInternal.findViewById(R.id.cat_bottomsheet_modal_button);
      button0.setOnClickListener(v0 ->
        Toast.makeText(
                v.getContext(),
                R.string.cat_bottomsheet_button_clicked,
                Toast.LENGTH_SHORT)
            .show());
      SwitchMaterial enabledSwitch =
          bottomSheetInternal.findViewById(R.id.cat_bottomsheet_modal_enabled_switch);
      enabledSwitch.setOnCheckedChangeListener(
          (buttonSwitch, isSwitchChecked) -> {
            CharSequence updatedText =
                getText(
                    isSwitchChecked
                        ? R.string.cat_bottomsheet_button_label_enabled
                        : R.string.cat_bottomsheet_button_label_disabled);
            button0.setText(updatedText);
            button0.setEnabled(isSwitchChecked);
          });
    });

    SwitchMaterial expansionSwitch = view.findViewById(R.id.cat_bottomsheet_expansion_switch);
    SwitchMaterial fullScreenSwitch = view.findViewById(R.id.cat_fullscreen_switch);
    expansionSwitch.setOnCheckedChangeListener(
        (buttonView, isChecked) -> {
          ViewGroup.LayoutParams lp = bottomSheetInternal.getLayoutParams();
          lp.height = isChecked ? 400 : getBottomSheetDialogDefaultHeight();
          bottomSheetInternal.setLayoutParams(lp);
          fullScreenSwitch.setEnabled(!isChecked);
          view.requestLayout();
        });
    fullScreenSwitch.setOnCheckedChangeListener(
        (buttonView, isChecked) -> {
          setBottomSheetHeights(view, isChecked);
          expansionSwitch.setEnabled(!isChecked);
        });

    TextView dialogText = bottomSheetInternal.findViewById(R.id.bottomsheet_state);
    BottomSheetBehavior.from(bottomSheetInternal)
        .addBottomSheetCallback(createBottomSheetCallback(dialogText));

    ViewCompat.setOnApplyWindowInsetsListener(
        view,
        (ignored, insets) -> {
          windowInsets = insets;
          setBottomSheetHeights(view, fullScreenSwitch.isChecked());
          return insets;
        });
    return view;
  }

  private int getBottomSheetDialogDefaultHeight() {
    return getWindowHeight() * 2 / 3;
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
    View modalBottomSheetChildView = bottomSheetDialog.findViewById(R.id.bottom_drawer_2);
    ViewGroup.LayoutParams layoutParams = modalBottomSheetChildView.getLayoutParams();
    BottomSheetBehavior<FrameLayout> modalBottomSheetBehavior = bottomSheetDialog.getBehavior();
    boolean fitToContents = true;
    float halfExpandedRatio = 0.5f;
    int windowHeight = getWindowHeight();
    if (layoutParams != null) {
      if (fullScreen) {
        // 全屏
        layoutParams.height = windowHeight;
        fitToContents = false;
        halfExpandedRatio = 0.7f;
      } else {
        // 默认高度
        layoutParams.height = getBottomSheetDialogDefaultHeight();
      }
      modalBottomSheetChildView.setLayoutParams(layoutParams);
      modalBottomSheetBehavior.setFitToContents(fitToContents);
      modalBottomSheetBehavior.setHalfExpandedRatio(halfExpandedRatio);
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
