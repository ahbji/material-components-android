/*
 * Copyright 2021 The Android Open Source Project
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import com.google.android.material.appbar.AppBarLayout;

import io.material.catalog.R;
import io.material.catalog.preferences.CatalogPreferencesDialogFragment;

/**
 * A fragment that displays a medium collapsing Top App Bar demo for the Catalog app.
 */
public class TopAppBarCollapsingCustomHeaderDemoFragment extends BaseTopAppBarCollapsingDemoFragment {

  private AppBarLayout appBarLayout;
  private Toolbar toolbar;
  private ViewGroup headerContent;

  @NonNull
  @Override
  public View onCreateDemoView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    View view = super.onCreateDemoView(layoutInflater, viewGroup, bundle);
    toolbar = view.findViewById(R.id.toolbar);
    toolbar.setTitle(R.string.cat_topappbar_custom_toolbar_title);
    AppCompatActivity activity = (AppCompatActivity) getActivity();
    activity.setSupportActionBar(toolbar);

    headerContent = view.findViewById(R.id.header_content);
    View.inflate(getContext(), R.layout.cat_topappbar_collapsing_header, headerContent);

    appBarLayout = view.findViewById(R.id.appbarlayout);
    ViewCompat.setOnApplyWindowInsetsListener(
        view,
        (v, insetsCompat) -> {
          appBarLayout
              .findViewById(R.id.collapsingtoolbarlayout)
              .setPadding(0, insetsCompat.getSystemWindowInsetTop() * 3, 0, 0);
          return insetsCompat;
        });

    toolbarToggle();

    return view;
  }

  private void toolbarToggle() {
    appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) ->{
      if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
        // CTL is collapsed
        headerContent.setVisibility(View.GONE);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setDisplayShowTitleEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      } else {
        // CTL is expanded or expanding, show top divider
        headerContent.setVisibility(View.VISIBLE);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
      }
    });
  }

  @LayoutRes
  protected int getCollapsingToolbarLayoutResId() {
    return R.layout.cat_topappbar_collapsing_custom_header_fragment;
  }
}
