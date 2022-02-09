/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.material.catalog.bottomappbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import io.material.catalog.R;
import io.material.catalog.feature.DemoFragment;
import io.material.catalog.feature.OnBackPressedHandler;
import io.material.catalog.themeswitcher.ThemeSwitcherHelper;

/**
 * A fragment that displays the main Bottom App Bar demos for the Catalog app.
 */
public class BottomAppBarSimpleDemoFragment extends DemoFragment implements OnBackPressedHandler {

  protected BottomAppBar bar;
  protected CoordinatorLayout coordinatorLayout;
  protected FloatingActionButton fab;

  @Nullable
  private ThemeSwitcherHelper themeSwitcherHelper;
  private BottomSheetBehavior<View> bottomDrawerBehavior;

  @Override
  public void onCreate(@Nullable Bundle bundle) {
    super.onCreate(bundle);
    setHasOptionsMenu(true);

    // The theme switcher helper is used in an adhoc way with the toolbar since the BottomAppBar is
    // set as the action bar.
    themeSwitcherHelper = new ThemeSwitcherHelper(getParentFragmentManager());
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
    menuInflater.inflate(R.menu.demo_primary_alternate, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem menuItem) {
    showSnackbar(menuItem.getTitle());
    return true;
  }

  @LayoutRes
  public int getBottomAppBarContent() {
    return R.layout.cat_bottomappbar_simple_demo_fragment;
  }

  @Override
  public View onCreateDemoView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    View view = layoutInflater.inflate(getBottomAppBarContent(), viewGroup, false);

    Toolbar toolbar = view.findViewById(R.id.toolbar);
    toolbar.setTitle(getDefaultDemoTitle());
    themeSwitcherHelper.onCreateOptionsMenu(toolbar.getMenu(), getActivity().getMenuInflater());
    toolbar.setOnMenuItemClickListener(themeSwitcherHelper::onOptionsItemSelected);
    toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

    coordinatorLayout = view.findViewById(R.id.coordinator_layout);
    bar = view.findViewById(R.id.bar);
    ((AppCompatActivity) getActivity()).setSupportActionBar(bar);

    setUpBottomDrawer(view);

    fab = view.findViewById(R.id.fab);
    fab.setOnClickListener(v -> showSnackbar(fab.getContentDescription()));
    NavigationView navigationView = view.findViewById(R.id.navigation_view);
    navigationView.setNavigationItemSelectedListener(
        item -> {
          showSnackbar(item.getTitle());
          return false;
        });

    return view;
  }

  @Override
  public boolean onBackPressed() {
    if (bottomDrawerBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
      bottomDrawerBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
      return true;
    }
    return false;
  }

  @Override
  public boolean shouldShowDefaultDemoActionBar() {
    return false;
  }

  protected void setUpBottomDrawer(View view) {
    View bottomDrawer = coordinatorLayout.findViewById(R.id.bottom_drawer);
    bottomDrawerBehavior = BottomSheetBehavior.from(bottomDrawer);
    bottomDrawerBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    bar.setNavigationOnClickListener(
        v -> bottomDrawerBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED)
    );
    bar.setNavigationIcon(R.drawable.ic_drawer_menu_24px);
  }

  private void showSnackbar(CharSequence text) {
    Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_SHORT)
        .setAnchorView(fab.getVisibility() == View.VISIBLE ? fab : bar)
        .show();
  }
}
