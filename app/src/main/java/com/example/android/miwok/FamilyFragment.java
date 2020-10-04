package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FamilyFragment extends Fragment {

    private MediaPlayer mMediaPlayer;

    private AudioManager mAudioManager;

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {

                public void onAudioFocusChange(int focusChange) {
                    if ( focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // resume playback
                        mMediaPlayer.start();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        //stop playback and clean up resources
                        releaseMediaPlayer();
                    }
                }

            };

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file
     */

    private  MediaPlayer.OnCompletionListener mCompletionlistener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list,container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        //Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("father","apa", R.drawable.family_father,R.raw.family_father));
        words.add(new Word("mother","ata", R.drawable.family_mother,R.raw.family_mother));
        words.add(new Word("son","angsi", R.drawable.family_son,R.raw.family_son));
        words.add(new Word("daughter","tune", R.drawable.family_daughter,R.raw.family_daughter));
        words.add(new Word("older brother","taachi", R.drawable.family_older_brother,R.raw.family_older_brother));
        words.add(new Word("younger brother","chalitti", R.drawable.family_younger_brother,R.raw.family_younger_brother));
        words.add(new Word("older sister","tete", R.drawable.family_older_sister,R.raw.family_older_sister));
        words.add(new Word("younger sister","kolliti", R.drawable.family_younger_sister,R.raw.family_younger_sister));
        words.add(new Word("grandmother","ama", R.drawable.family_grandmother,R.raw.family_grandmother));
        words.add(new Word("grandfather","paapa", R.drawable.family_grandfather,R.raw.family_grandfather));

        WordAdapter adapter =
                new WordAdapter(getActivity(), words,R.color.category_family);

        ListView listView = (ListView) rootView.findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = words.get(position);

                releaseMediaPlayer();

                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN
                );

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // We have a audio focus now

                    mMediaPlayer  = MediaPlayer.create(getActivity(), word.getAudioResourceId());

                    //Start the audio file
                    mMediaPlayer  .start();

                    //Set up a listener on the media player, so that we can stop and release the
                    // media player once the sounds has finished playing.
                    mMediaPlayer.setOnCompletionListener(mCompletionlistener);

                }

            }

        });

        return rootView;
    }
    @Override
    public void onStop() {
        super.onStop();
        //when the activity is stopped release the media player resources because we won't
        // be playing any more sounds.
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            // Abandon audio focus when playback complete
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}
