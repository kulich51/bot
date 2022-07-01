package pro.sky.bot.model;

import java.util.Arrays;

public class Photo {

    private byte[] data;
    private String mediaType;

    public Photo(byte[] data, String mediaType) {
        this.data = data;
        this.mediaType = mediaType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "data=" + Arrays.toString(data) +
                ", mediaType='" + mediaType + '\'' +
                '}';
    }
}
