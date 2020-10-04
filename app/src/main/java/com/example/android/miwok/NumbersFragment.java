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

public class NumbersFragment extends Fragment {

    private MediaPlayer mMediaPlayer;

    private AudioManager mAudioManager;

        private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangelistener = new AudioManager.OnAudioFocusChangeListener() {
               @Override
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
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

    public NumbersFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.word_list,container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        //Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("one","lutti",R.drawable.number_one, R.raw.number_one));
        words.add(new Word("two","otiiko", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("three","tolookosu", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("four","oyyisa", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("five","massokka", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("six","temmoka", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("seven","kenekaku", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("eight","kawinta", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("nine","wo'e", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("ten","na'aacha", R.drawable.number_ten, R.raw.number_ten));

        WordAdapter adapter =
                new WordAdapter(getActivity(), words, R.color.category_numbers);

        ListView listView = (ListView) rootView.findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                releaseMediaPlayer();

                Word word = words.get(position);

                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangelistener,

                        AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // We have a audio focus now

                    mMediaPlayer = MediaPlayer.create(getActivity(), word.getAudioResourceId());

                    //Start the audio file
                    mMediaPlayer.start();

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
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangelistener);

        }
    }
}