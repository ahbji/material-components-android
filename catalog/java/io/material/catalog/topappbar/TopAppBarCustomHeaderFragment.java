/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.material.catalog.topappbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import io.material.catalog.R;
import io.material.catalog.feature.DemoFragment;
import io.material.catalog.preferences.CatalogPreferencesDialogFragment;

/** A fragment that displays the main Top App Bar demo for the Catalog app. */
public class TopAppBarCustomHeaderFragment extends DemoFragment {

  private ImageButton closeFragmentButton;
  private ImageButton preferencesButton;

  @Override
  public void onCreate(@Nullable Bundle bundle) {
    super.onCreate(bundle);
    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateDemoView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    View view = layoutInflater.inflate(R.layout.cat_topappbar_custom_header_fragment, viewGroup, false);

    ViewGroup content = view.findViewById(R.id.header_content);
    View.inflate(getContext(), R.layout.cat_topappbar_header, content);

    preferencesButton = view.findViewById(R.id.cat_toc_preferences_button);
    closeFragmentButton = view.findViewById(R.id.cat_close_fragment_button);

    initPreferencesButton();
    initCloseFragmentButton();

    return view;
  }

  private void initPreferencesButton() {
    preferencesButton.setOnClickListener(
        v -> new CatalogPreferencesDialogFragment().show(
            getParentFragmentManager(), "preferences-screen"));
  }

  private void initCloseFragmentButton() {
    closeFragmentButton.setOnClickListener(
        v -> {
          getActivity().getSupportFragmentManager().popBackStack();
        }
    );
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
    super.onCreateOptionsMenu(menu, menuInflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean shouldShowDefaultDemoActionBar() {
    return false;
  }
}
