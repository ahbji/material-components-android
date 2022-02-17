package io.material.catalog.navigationrail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigationrail.NavigationRailView;
import com.google.android.material.snackbar.Snackbar;

import io.material.catalog.R;
import io.material.catalog.feature.DemoFragment;

public class NavigationRailSimpleDemoFragment extends DemoFragment {

  @Nullable
  NavigationRailView navigationRailView;

  @Override
  public View onCreateDemoView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    View view = layoutInflater.inflate(R.layout.cat_navigation_rail_simple_demo_fragment, viewGroup, false);
    initNavigationRail(requireContext(), view);

    NavigationBarView.OnItemSelectedListener navigationRailItemSelectedListener =
        item -> {
          Snackbar.make(view, "NavigationRail item: " + item.getTitle() + " press again!", Snackbar.LENGTH_SHORT).show();
          return true;
        };
    setOnRailItemSelectedListeners(navigationRailItemSelectedListener);

    NavigationBarView.OnItemReselectedListener navigationItemReselectedListener =
        item -> {
          handleRailItem(view, item);
        };
    setOnRailItemReselectedListeners(navigationItemReselectedListener);
    return view;
  }

  private void handleRailItem(View view, MenuItem item) {
    handleAllNavigationRailSelections(item.getItemId());

    TextView page1Text = view.findViewById(R.id.page_1);
    TextView page2Text = view.findViewById(R.id.page_2);
    TextView page3Text = view.findViewById(R.id.page_3);
    TextView page4Text = view.findViewById(R.id.page_4);
    TextView page5Text = view.findViewById(R.id.page_5);
    TextView page6Text = view.findViewById(R.id.page_6);
    TextView page7Text = view.findViewById(R.id.page_7);

    int itemId = item.getItemId();
    page1Text.setVisibility(itemId == R.id.action_page_1 ? View.VISIBLE : View.GONE);
    page2Text.setVisibility(itemId == R.id.action_page_2 ? View.VISIBLE : View.GONE);
    page3Text.setVisibility(itemId == R.id.action_page_3 ? View.VISIBLE : View.GONE);
    page4Text.setVisibility(itemId == R.id.action_page_4 ? View.VISIBLE : View.GONE);
    page5Text.setVisibility(itemId == R.id.action_page_5 ? View.VISIBLE : View.GONE);
    page6Text.setVisibility(itemId == R.id.action_page_6 ? View.VISIBLE : View.GONE);
    page7Text.setVisibility(itemId == R.id.action_page_7 ? View.VISIBLE : View.GONE);
  }

  private void setOnRailItemSelectedListeners(NavigationBarView.OnItemSelectedListener onItemSelectedListener) {
    navigationRailView.setOnItemSelectedListener(onItemSelectedListener);
  }

  private void setOnRailItemReselectedListeners(NavigationBarView.OnItemReselectedListener onItemReselectedListener) {
    navigationRailView.setOnItemReselectedListener(onItemReselectedListener);
  }

  private void handleAllNavigationRailSelections(int itemId) {
    handleNavigationRailItemSelections(navigationRailView, itemId);
  }

  private static void handleNavigationRailItemSelections(NavigationRailView view, int itemId) {
    view.getMenu().findItem(itemId).setChecked(true);
  }

  private void initNavigationRail(@NonNull Context context, @NonNull View view) {
    navigationRailView = view.findViewById(R.id.cat_navigation_rail);

    @LayoutRes int demoControls = getNavigationRailDemoControlsLayout();
    if (demoControls != 0) {
      ViewGroup controlsView = view.findViewById(R.id.demo_controls);
      View.inflate(context, getNavigationRailDemoControlsLayout(), controlsView);
    }
  }

  @LayoutRes
  protected int getNavigationRailDemoControlsLayout() {
    return 0;
  }
}
