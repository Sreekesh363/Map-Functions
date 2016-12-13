package mappins.sreekesh.com.mappins;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView splashImage = (ImageView) findViewById(R.id.splash_image);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(splashImage);
        Glide.with(this).load(R.drawable.gears).into(imageViewTarget);

        try {
            ScheduledFuture<?> countdown = scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    Intent mapIntent = new Intent(SplashActivity.this,MapsActivity.class);
                    mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mapIntent);
                }}, 3000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            Log.e(TAG, "onMapReady Exception:" + e.toString());
        }
    }
}
