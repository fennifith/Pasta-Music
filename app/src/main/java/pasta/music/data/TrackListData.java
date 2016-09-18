package pasta.music.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class TrackListData  implements Parcelable {

    public static final Parcelable.Creator<TrackListData> CREATOR = new Parcelable.Creator<TrackListData>() {
        public TrackListData createFromParcel(Parcel in) {
            return new TrackListData(in);
        }

        public TrackListData[] newArray(int size) {
            return new TrackListData[size];
        }
    };
    public String trackName, albumName, albumId, trackImage, trackImageLarge, trackDuration;
    public List<ArtistListData> artists;
    public String trackUri, trackId;

    public TrackListData(String trackName, String albumName, String trackUri) {
        this.trackName = trackName;
        this.albumName = albumName;
        this.trackUri = trackUri;
    }

    public TrackListData(Parcel in) {
        ReadFromParcel(in);
    }

    public TrackListData clone() {
        try {
            return (TrackListData) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    private void ReadFromParcel(Parcel in) {
        trackName = in.readString();
        albumName = in.readString();
        albumId = in.readString();
        trackImage = in.readString();
        trackImageLarge = in.readString();
        trackDuration = in.readString();
        artists = new ArrayList<>();
        in.readList(artists, ArtistListData.class.getClassLoader());
        trackUri = in.readString();
        trackId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(trackName);
        out.writeString(albumName);
        out.writeString(albumId);
        out.writeString(trackImage);
        out.writeString(trackImageLarge);
        out.writeString(trackDuration);
        out.writeList(artists);
        out.writeString(trackUri);
        out.writeString(trackId);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
