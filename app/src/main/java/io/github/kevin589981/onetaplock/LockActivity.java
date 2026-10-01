package io.github.kevin589981.onetaplock;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/** A launcher entry point that performs one lock action and then leaves no running activity. */
public final class LockActivity extends Activity {
    private static final int REQUEST_ENABLE_ADMIN = 1;

    private DevicePolicyManager policyManager;
    private ComponentName admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // A restored activity is not a new tap. In particular, waking the phone
        // must never trigger another lock after Android recreates this activity.
        if (savedInstanceState != null) {
            finishAndRemoveTask();
            return;
        }

        policyManager = getSystemService(DevicePolicyManager.class);
        admin = new ComponentName(this, LockAdminReceiver.class);

        if (policyManager.isAdminActive(admin)) {
            lockOnce();
        } else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, admin);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    getString(R.string.admin_explanation));
            try {
                startActivityForResult(intent, REQUEST_ENABLE_ADMIN);
            } catch (RuntimeException exception) {
                Toast.makeText(this, R.string.admin_unavailable, Toast.LENGTH_LONG).show();
                finishAndRemoveTask();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_ADMIN && policyManager.isAdminActive(admin)) {
            lockOnce();
        } else {
            finishAndRemoveTask();
        }
    }

    private void lockOnce() {
        try {
            policyManager.lockNow();
        } catch (SecurityException exception) {
            Toast.makeText(this, R.string.lock_failed, Toast.LENGTH_LONG).show();
        } finally {
            finishAndRemoveTask();
        }
    }
}
