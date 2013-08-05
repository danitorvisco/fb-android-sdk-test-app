package com.fbandroidsdktestapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

public class MainActivity extends FragmentActivity implements OnItemClickListener {
	
    private static final String TAG = "MainActivity";

    private static final int SPLASH = 0;
    private static final int SELECTION = 1;
    private static final int SETTINGS = 2;
    private static final int FRAGMENT_COUNT = SETTINGS +1;
    
    private ListView list;
    String[] main_items = { 
      "Graph API",
      "FQL",
      "Dialogs",
      "Requests",
      "Sharing",
      "Object API",
      "App Install Ads",
      "Auth and Tokens"
    };

    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
    private MenuItem settings;
    private boolean isResumed = false;
    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    
    private TextView mText;
    private ProfilePictureView mUserPic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        FragmentManager fm = getSupportFragmentManager();
        fragments[SPLASH] = fm.findFragmentById(R.id.splashFragment);
        fragments[SELECTION] = fm.findFragmentById(R.id.selectionFragment);
        fragments[SETTINGS] = fm.findFragmentById(R.id.userSettingsFragment);

        FragmentTransaction transaction = fm.beginTransaction();
        for(int i = 0; i < fragments.length; i++) {
            transaction.hide(fragments[i]);
        }
        transaction.commit();
        
        list = (ListView) findViewById(R.id.main_list);

        list.setOnItemClickListener(this);
        list.setAdapter(new ArrayAdapter<String>(this, R.layout.main_list_item, main_items));
        
        mText = (TextView) MainActivity.this.findViewById(R.id.welcome);
        mUserPic = (ProfilePictureView) MainActivity.this.findViewById(R.id.user_pic);
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        isResumed = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        isResumed = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Session session = Session.getActiveSession();

        if (session != null && session.isOpened()) {
            // make request to the /me API
            Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

              // callback after Graph API response with user object
              @Override
              public void onCompleted(GraphUser user, Response response) {
                // it never gets here...
                if (user != null) {
                  TextView welcome = (TextView) findViewById(R.id.welcome);
                  welcome.setText(
                    "Hello " + user.getName() + "!" + "\n" +
                    "Explore our SDK for Android now."
                  );
                  ProfilePictureView profilePictureView;
                  profilePictureView = (ProfilePictureView) findViewById(R.id.user_pic);
                  profilePictureView.setProfileId(user.getId());
                }
              }
            });
            
            // if the session is already open, try to show the selection fragment
            showFragment(SELECTION, false);
        } else {
            // otherwise present the splash screen and ask the user to login.
            showFragment(SPLASH, false);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // only add the menu when the selection fragment is showing
        if (fragments[SELECTION].isVisible()) {
            if (menu.size() == 0) {
                settings = menu.add(R.string.settings);
            }
            return true;
        } else {
            menu.clear();
            settings = null;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.equals(settings)) {
            showFragment(SETTINGS, true);
            return true;
        }
        return false;
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (isResumed) {
            FragmentManager manager = getSupportFragmentManager();
            int backStackSize = manager.getBackStackEntryCount();
            for (int i = 0; i < backStackSize; i++) {
                manager.popBackStack();
            }
            
            if (state.equals(SessionState.OPENED)) {
                showFragment(SELECTION, false);
            } else if (state.isClosed()) {
                showFragment(SPLASH, false);
            }
        }
    }

    private void showFragment(int fragmentIndex, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            if (i == fragmentIndex) {
                transaction.show(fragments[i]);
            } else {
                transaction.hide(fragments[i]);
            }
        }
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
    
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
        switch (position) {
            case 0: {
            	Log.v(TAG, "index=" + position);
            }

            case 1: {
            	Log.v(TAG, "index=" + position);
            }

            case 2: {
            	Log.v(TAG, "index=" + position);
            }

            case 3: {
            	Log.v(TAG, "index=" + position);
            }

            case 4: {
            	Log.v(TAG, "index=" + position);
            }

            case 5: {
            	Log.v(TAG, "index=" + position);
            }

            case 6: {
            	Log.v(TAG, "index=" + position);
            }

            case 7: {
            	Log.v(TAG, "index=" + position);
            }
        }
    }
    
    /**
     * Definition of the list adapter
     */
    public class MainListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MainListAdapter() {
            mInflater = LayoutInflater.from(MainActivity.this.getBaseContext());
        }

        @Override
        public int getCount() {
            return main_items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View hView = convertView;
            if (convertView == null) {
                hView = mInflater.inflate(R.layout.main_list_item, null);
                ViewHolder holder = new ViewHolder();
                holder.main_list_item = (TextView) hView.findViewById(R.id.main_api_item);
                hView.setTag(holder);
            }

            ViewHolder holder = (ViewHolder) hView.getTag();

            holder.main_list_item.setText(main_items[position]);

            return hView;
        }

    }

    class ViewHolder {
        TextView main_list_item;
    }
}

