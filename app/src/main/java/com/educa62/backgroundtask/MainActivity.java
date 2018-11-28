package com.educa62.backgroundtask;

import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        findViewById(R.id.btnThread).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnThread:
                runThread();
                break;
            case R.id.btnAsyncTask:
                runAsyncTask();
                break;
            case R.id.btnScheduler:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    runScheduler();
                } else {
                    // TODO
                    // runJobDispatcher();
                }
                break;
            case R.id.btnService:
                runService();
                break;
            default:

        }
    }

    private void runJobDispatcher() {
        // panggil Firebase Job Dispatcher untuk menggantikan Job Scheduler
    }

    private void runThread() {
        // munculkan dialog sebelum thread dijalankan
        dialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // berjalan di worker thread

                // simulasi proses dengan menunggu 3 detik
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // jalankan aksi di ui thread karena harus mengubah UI (dialog)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                });
            }
        }).start();
    }

    private void runAsyncTask() {
        new MyAsyncTask(dialog).execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void runScheduler() {
        ComponentName serviceComponent = new ComponentName(this, MyJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(1000); // wait at least
        builder.setOverrideDeadline(3 * 1000); // maximum delay
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());

    }

    private void runService() {
        MyIntentService.startActionBaz(this, "hello", "world");
    }

    static class MyAsyncTask extends AsyncTask<Integer, Void, Void> {
        private ProgressDialog dialog;

        MyAsyncTask(ProgressDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        // fungsi ini berjalan di ui thread
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        // fungsi ini berjalan di ui thread
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
        }
    }
}
