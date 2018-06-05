package com.hanadkubat.kuvalefitness.Fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.hanadkubat.kuvalefitness.R;

/**
 * Created by vale on 6/5/2018.
 */

public class WelcomeFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_welcome, null);

        VideoView vvLogo = view.findViewById(R.id.vvLogo);

        String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.logo;
        vvLogo.setVideoURI(Uri.parse(path));
        vvLogo.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer)
            {
                mediaPlayer.start();
            }
        });
        vvLogo.start();

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        getActivity().setTitle(R.string.title_welcome);
    }
}
