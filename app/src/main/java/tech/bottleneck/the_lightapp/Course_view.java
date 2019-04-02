package tech.bottleneck.the_lightapp;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

import java.io.File;

import io.realm.Realm;
import tech.bottleneck.the_lightapp.model.DownloadTable;
import tech.bottleneck.the_lightapp.model.Result;

//extnds appcompat

public class Course_view extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    public static final String API_KEY = "AIzaSyBN01zvkjgU1ElgJiTmQCZwj2rtqlFQfgc";
    public static String VIDEO_ID = "";
    public static String AUDIO_URL = "";
    public static String PDF_URL = "";
    ProgressBar progressBar;
    SeekBar seekBar;
    boolean wasPlaying = false;
    MediaPlayer mp;
    FloatingActionButton fab;
    BottomNavigationView bottombar;
    Button btnPdf;
    private Result lessons_modelList;
    private TextView name, descr/*seekbarHint*/;
    private ProgressDialog pDialog;
    PDFView pdfViewer;
    private Handler mSeekbarUpdateHandler = new Handler();
    private Runnable mUpdateSeekbar = new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(mp.getCurrentPosition());
            mSeekbarUpdateHandler.postDelayed(this, 50);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);
        bottombar = findViewById(R.id.bottmnav);
        pdfViewer = findViewById(R.id.pdfViewer);
        progressBar = (ProgressBar) findViewById(R.id.pbar);

        name = findViewById(R.id.namec);
        descr = findViewById(R.id.disccourseview);
        //videoView = findViewById(R.id.vview);
        fab = findViewById(R.id.button);
        WebView webView = findViewById(R.id.webView);
        seekBar = findViewById(R.id.audiobar);
//        seekbarHint = findViewById(R.id.textView);
        Bottombarass();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSong();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mp != null && fromUser) {
                    mp.seekTo(progress);
                }
            }

        });

        lessons_modelList = getIntent().getParcelableExtra("RESULT");
        setData();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        String a = "<iframe width=\"100%\" height=\"100%\" src=\"" + VIDEO_ID + "\" frameborder=\"0\" allowfullscreen></iframe>";
        webView.loadData(a, "text/html", "utf-8");

        btnPdf = findViewById(R.id.btnPdf);

        Realm realm = Realm.getDefaultInstance();
        Uri uri = Uri.parse(PDF_URL);
        DownloadTable table = realm.where(DownloadTable.class).equalTo("id", uri.getLastPathSegment()).findFirst();
        if (table != null)
            btnPdf.setText("View");

        realm.close();

        btnPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnPdf.getText().equals("View"))
                    downloadPDF();
                else
                    viewPDF();
            }
        });

//        webView.loadUrl(VIDEO_ID);

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (mp != null) {
//                    int mCurrentPosition = mp.getCurrentPosition() / 1000;
//                    seekBar.setProgress(mCurrentPosition);
//                    seekBar.setMax(mp.getDuration());
//                }
//                mHandler.postDelayed(this, 1000);
//            }
//        });
    }

    private void viewPDF() {

        Uri uri = Uri.parse(PDF_URL);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), uri.getLastPathSegment());

        Log.e("AA", "viewPDF: "+Uri.fromFile(file).toString());

        pdfViewer.fromUri(Uri.fromFile(file))
                .enableSwipe(true)
                .swipeHorizontal(true)
                .enableDoubletap(true)
                .defaultPage(0)
                .load();

        pdfViewer.setVisibility(View.VISIBLE);

//        Uri path = Uri.fromFile(file);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(path, "application/pdf");
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        try {
//            startActivity(intent);
//        } catch (ActivityNotFoundException e) {
//            Toast.makeText(this, "PDF Viewer not found", Toast.LENGTH_SHORT).show();
//        }

    }

    private void setData() {
        name.setText(lessons_modelList.getTitle());
        descr.setText(lessons_modelList.getDescription());
        VIDEO_ID = lessons_modelList.getVideo();
        AUDIO_URL = "http://loopdata.in/uploads/sem1/audio/" + lessons_modelList.getAudio();
        PDF_URL = "http://loopdata.in/uploads/sem1/pdf/" + lessons_modelList.getPdf();
        Toast.makeText(Course_view.this, VIDEO_ID, Toast.LENGTH_LONG).show();
    }


    //media player

    private void Bottombarass() {
        bottombar.setVisibility(View.GONE);
        bottombar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {


                    case R.id.hme:
                        Intent intent = new Intent(getApplicationContext(), Homepage.class);
                        startActivity(intent);
                        return true;


                    case R.id.msge:
                        Intent intent1 = new Intent(getApplicationContext(), Messages.class);
                        startActivity(intent1);
                        return true;
                    case R.id.exm:
                        Intent intent4 = new Intent(getApplicationContext(), Examination.class);
                        startActivity(intent4);
                        return true;

                    case R.id.pfil:
                        Intent intent3 = new Intent(getApplicationContext(), Profile.class);
                        startActivity(intent3);
                        return true;


                }
                return false;

            }
        });
    }

    public void playSong() {
        if (mp == null) {
            try {
                mp = new MediaPlayer();
                Log.e("AA", "playSong: "+AUDIO_URL);
                mp.setDataSource(AUDIO_URL);
                mp.prepare();
                mp.start();
                fab.setImageResource(android.R.drawable.ic_media_pause);
                seekBar.setMax(mp.getDuration());
                mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
            } catch (Exception ignore) {
            }

        } else {
            if (mp.isPlaying()) {
                mp.pause();
                fab.setImageResource(android.R.drawable.ic_media_play);
                mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
            } else {
                mp.start();
                fab.setImageResource(android.R.drawable.ic_media_pause);
                mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearMediaPlayer();
    }

    private void clearMediaPlayer() {

        if (mp == null)
            return;

        mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);

        mp.stop();
        mp.release();
        mp = null;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        if (!b) {
            youTubePlayer.loadVideo(VIDEO_ID);
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Failured to Initialize!", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void downloadPDF() {

        if (!permissionGranded())
            return;

        Toast.makeText(this, "Starting download", Toast.LENGTH_SHORT).show();

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        final Uri uri = Uri.parse(PDF_URL);
        try {
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setVisibleInDownloadsUi(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.getLastPathSegment());
            assert downloadManager != null;
            downloadManager.enqueue(request);

            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(final Context context, Intent intent) {

                    DownloadTable table = new DownloadTable();
                    table.setId(uri.getLastPathSegment());
                    File file = new File(Environment.DIRECTORY_DOWNLOADS, uri.getLastPathSegment());
                    table.setPath(file.getPath());

                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(table);
                    realm.commitTransaction();
                    realm.close();

                    btnPdf.setText("View");
                    Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show();
                    unregisterReceiver(this);

                }
            }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        } catch (Exception ignore) {
        }
    }

    private boolean permissionGranded() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                return false;
            } else
                return true;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (ActivityCompat.checkSelfPermission(Course_view.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                downloadPDF();
            }
        }
    }

    @Override
    public void onBackPressed() {

        if(pdfViewer.getVisibility() == View.VISIBLE) {
            pdfViewer.setVisibility(View.GONE);
            return;
        }

        super.onBackPressed();
    }
}

