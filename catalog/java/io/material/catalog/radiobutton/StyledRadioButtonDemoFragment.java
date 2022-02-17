package io.material.catalog.radiobutton;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import io.material.catalog.R;
import io.material.catalog.feature.DemoFragment;

/** A fragment that displays the main RadioButton demo for the Catalog app. */
public class StyledRadioButtonDemoFragment extends DemoFragment {

  @Override
  public View onCreateDemoView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    View view =
        layoutInflater.inflate(R.layout.cat_styled_radiobutton_fragment, viewGroup, false /* attachToRoot */);
    RadioGroup radioGroup = view.findViewById(R.id.cat_radiobutton_radioGroup);

    radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
      switch (checkedId) {
        case R.id.enabled_selected:
          Snackbar.make(group, "Selected", Snackbar.LENGTH_SHORT).show();
          break;
        default:
          break;
      }
    });
    return view;
  }
}
