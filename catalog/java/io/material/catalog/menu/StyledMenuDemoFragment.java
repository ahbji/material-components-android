/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.material.catalog.menu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.ListPopupWindow;

import com.google.android.material.snackbar.Snackbar;

import io.material.catalog.R;
import io.material.catalog.feature.DemoFragment;

/**
 * A fragment that displays the main menu demo for the Catalog app.
 */
public class StyledMenuDemoFragment extends DemoFragment {

  private static final int ICON_MARGIN = 8;
  private static final String CLIP_DATA_LABEL = "Sample text to copy";
  private static final String KEY_POPUP_ITEM_LAYOUT = "popup_item_layout";
  @LayoutRes
  private int popupItemLayout;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState != null) {
      popupItemLayout = savedInstanceState.getInt(KEY_POPUP_ITEM_LAYOUT);
    }
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(KEY_POPUP_ITEM_LAYOUT, popupItemLayout);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
    menuInflater.inflate(R.menu.popup_menu, menu);
    super.onCreateOptionsMenu(menu, menuInflater);
  }

  @Override
  public View onCreateDemoView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    View view = layoutInflater.inflate(R.layout.cat_styled_menu_fragment, viewGroup, false);

    Button listPopupWindowButton = view.findViewById(R.id.list_popup_window);
    ListPopupWindow listPopupWindow = initializeListPopupMenu(listPopupWindowButton);
    listPopupWindowButton.setOnClickListener(v -> listPopupWindow.show());
    return view;
  }

  public void setPopupItemLayoutRes(@LayoutRes int popupItemRes) {
    popupItemLayout = popupItemRes;
  }

  private ListPopupWindow initializeListPopupMenu(View v) {
    Context wrapper = new ContextThemeWrapper(getContext(), R.style.Widget_App_PopupMenu);
    ListPopupWindow listPopupWindow =
        new ListPopupWindow(wrapper, null, R.attr.listPopupWindowStyle);
    ArrayAdapter<CharSequence> adapter =
        new ArrayAdapter<>(
            getContext(),
            popupItemLayout,
            getResources().getStringArray(R.array.cat_list_popup_window_content));
    listPopupWindow.setAdapter(adapter);
    listPopupWindow.setAnchorView(v);
    listPopupWindow.setOnItemClickListener(
        (parent, view, position, id) -> {
          Snackbar.make(
                  getActivity().findViewById(android.R.id.content),
                  adapter.getItem(position).toString(),
                  Snackbar.LENGTH_LONG)
              .show();
          listPopupWindow.dismiss();
        });
    return listPopupWindow;
  }
}
