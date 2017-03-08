
package com.auto.jarvis.libraryicognite.models.output;

import com.google.gson.annotations.SerializedName;

public class UserProfileInfo {

    @SerializedName("code")
    private Object mCode;
    @SerializedName("data")
    private Data mData;
    @SerializedName("soundMessage")
    private Object mSoundMessage;
    @SerializedName("succeed")
    private Boolean mSucceed;
    @SerializedName("textMessage")
    private Object mTextMessage;

    public Object getCode() {
        return mCode;
    }

    public Data getData() {
        return mData;
    }

    public Object getSoundMessage() {
        return mSoundMessage;
    }

    public Boolean getSucceed() {
        return mSucceed;
    }

    public Object getTextMessage() {
        return mTextMessage;
    }

    public static class Builder {

        private Object mCode;
        private Data mData;
        private Object mSoundMessage;
        private Boolean mSucceed;
        private Object mTextMessage;

        public UserProfileInfo.Builder withCode(Object code) {
            mCode = code;
            return this;
        }

        public UserProfileInfo.Builder withData(Data data) {
            mData = data;
            return this;
        }

        public UserProfileInfo.Builder withSoundMessage(Object soundMessage) {
            mSoundMessage = soundMessage;
            return this;
        }

        public UserProfileInfo.Builder withSucceed(Boolean succeed) {
            mSucceed = succeed;
            return this;
        }

        public UserProfileInfo.Builder withTextMessage(Object textMessage) {
            mTextMessage = textMessage;
            return this;
        }

        public UserProfileInfo build() {
            UserProfileInfo UserProfileInfo = new UserProfileInfo();
            UserProfileInfo.mCode = mCode;
            UserProfileInfo.mData = mData;
            UserProfileInfo.mSoundMessage = mSoundMessage;
            UserProfileInfo.mSucceed = mSucceed;
            UserProfileInfo.mTextMessage = mTextMessage;
            return UserProfileInfo;
        }

    }

}
