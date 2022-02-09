/*
 * Copyright 2018 The Android Open Source Project
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

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import com.google.android.material.snackbar.Snackbar;

import io.material.catalog.R;

/** An activity that displays a Top App Bar Dark Action Bar demo for the Catalog app. */
public class TopAppBarDarkActionBarDemoActivity extends AppCompatActivity {

  private ActionMode actionMode;
  private boolean inActionMode;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.cat_topappbar_action_bar_activity);

    TextView demoDescriptionTextView = findViewById(R.id.action_bar_demo_description);
    demoDescriptionTextView.setText(getActionBarDemoDescription());

    Button actionModeButton = findViewById(R.id.action_bar_demo_action_mode_button);
    actionModeButton.setOnClickListener(
        v -> {
          inActionMode = !inActionMode;
          if (inActionMode) {
            if (actionMode == null) {
              actionMode =
                  startSupportActionMode(
                      new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                          getMenuInflater().inflate(R.menu.cat_topappbar_menu_actionmode, menu);
                          return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                          return false;
                        }

                        @Override
                        public boolean onActionItemClicked(
                            ActionMode actionMode, MenuItem menuItem) {
                          return handleActionModeMenuItem(menuItem);
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode am) {
                          actionMode = null;
                        }
                      });
            }
            actionMode.setTitle(R.string.cat_topappbar_action_bar_action_mode_title);
            actionMode.setSubtitle(R.string.cat_topappbar_action_bar_action_mode_subtitle);
          } else {
            actionMode.finish();
          }
        });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.cat_topappbar_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    return showSnackbar(this, item) || super.onOptionsItemSelected(item);
  }

  private boolean handleActionModeMenuItem(MenuItem item) {
    return showSnackbar(this, item);
  }

  public boolean showSnackbar(Activity activity, MenuItem menuItem) {
    if (menuItem.getItemId() == android.R.id.home) {
      return false;
    }

    Snackbar.make(
        activity.findViewById(android.R.id.content), menuItem.getTitle(), Snackbar.LENGTH_SHORT)
        .show();
    return true;
  }

  @StringRes
  public int getActionBarDemoDescription() {
    return R.string.cat_topappbar_action_bar_description;
  }
}
