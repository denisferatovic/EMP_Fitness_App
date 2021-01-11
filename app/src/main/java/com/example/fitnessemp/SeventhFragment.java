package com.example.fitnessemp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class SeventhFragment extends Fragment {


    TextView eat,active,guide,activity,basic;
    private YouTubePlayerView yt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seventh,container,false);
        setRetainInstance(true);


        if ( Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission
                ( this.getContext(), Manifest.permission.INTERNET ) != PackageManager.PERMISSION_GRANTED ) {  }

        TextView link1 = view.findViewById(R.id.activitiy);
        link1.setMovementMethod(LinkMovementMethod.getInstance());

        TextView link2 = view.findViewById(R.id.exercise);
        link2.setMovementMethod(LinkMovementMethod.getInstance());

        TextView link3 = view.findViewById(R.id.eating);
        link3.setMovementMethod(LinkMovementMethod.getInstance());

        TextView link4 = view.findViewById(R.id.track);
        link4.setMovementMethod(LinkMovementMethod.getInstance());

         yt = view.findViewById(R.id.activity_main_youtubePlayerView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}

