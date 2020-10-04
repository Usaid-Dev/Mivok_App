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

public class PhrasesFragment extends Fragment  {

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
        words.add(new Word("Where are you going?","minto wukus",R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?","tinna oyaase'na",R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...","oyaaset...",R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?","michaksas?",R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I'am feeling good.","kuchi achit",R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?","aanas'aa?",R.raw.phrase_are_you_coming));
        words.add(new Word("Yes,I'm coming.","haa'aanam",R.raw.phrase_yes_im_coming));
        words.add(new Word("I'm coming.","aanem",R.raw.phrase_im_coming));
        words.add(new Word("Lets'go.","yoowutis",R.raw.phrase_lets_go));
        words.add(new Word("Come here.","anni'nem",R.raw.phrase_come_here));

        WordAdapter adapter =
                new WordAdapter(getActivity(), words,R.color.category_phrases);

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