package com.example.android.miwok;

public class Word {

    /** Default translation for the word*/
    private String mDefaultTranslation;

    /** Miwok translation for the word*/
    private String mMIwokTranslation;

    /**Image resource ID*/
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    private static final int NO_IMAGE_PROVIDED = -1;

    /** Audio resource ID for the word*/
    private int mAudioResourceId;

    /**
     * Create a new Word object
     *
     * @param defaultTranslation is the word in a language that the user is already familiar with
     *                           (such as English)
     * @param miwokTrnslation is the word in the Miwok language
     * @param audioResourceId is the resource ID for the audio file associated with this word
     */

    public Word(String defaultTranslation, String miwokTrnslation, int audioResourceId) {
        mDefaultTranslation = defaultTranslation;
        mMIwokTranslation = miwokTrnslation;
        mAudioResourceId = audioResourceId;
    }

    /**
     * Create a new Word object
     *
     * @param defaultTranslation is the word in a language that the user is that the user is already familiar with
     *                           (such as English)
     * @param miwokTrnslation is the word in the Miwok language
     *
     * @param imageResourceId is the drawable resource ID for the image associated with the word
     *
     * @param audioResourceId is the resource ID for the audio file associated with this word
     */

    public Word(String defaultTranslation, String miwokTrnslation, int imageResourceId, int audioResourceId) {
        mDefaultTranslation = defaultTranslation;
        mMIwokTranslation = miwokTrnslation;
        mImageResourceId = imageResourceId;
        mAudioResourceId = audioResourceId;
    }

    /**
     * Get the default translation of the word
     */
    public String getDefaultTranslation(){
        return mDefaultTranslation;
    }

    /**
     * Get the Miwok translation of the word
     */
    public String getMiwokTranslation(){
        return mMIwokTranslation;
    }

    /**
     *Return the image resource ID of the word.
     */
    public int getImageResourceId(){
        return mImageResourceId;
    }

    /**
     * Returns whether or not there is an image for this word.
     *
     */
    public boolean hasImage(){
        return mImageResourceId != NO_IMAGE_PROVIDED; }

    /**
     * Return the audio resource ID of the word.
     * @return
     */
    public  int getAudioResourceId() {
        return mAudioResourceId;
    }
}

