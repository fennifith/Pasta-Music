package pasta.music.data;

import android.os.Parcel;
import android.os.Parcelable;

public class PlaylistListData  implements Parcelable {
    public static final Parcelable.Creator<PlaylistListData> CREATOR = new Parcelable.Creator<PlaylistListData>() {
        public PlaylistListData createFromParcel(Parcel in) {
            return new PlaylistListData(in);
        }

        public PlaylistListData[] newArray(int size) {
            return new PlaylistListData[size];
        }
    };

    public String playlistName;
    public String playlistId;
    public String playlistImage;
    public String playlistImageLarge;
    public String playlistOwnerName;
    public String playlistOwnerId;
    public int tracks;

    public PlaylistListData(Parcel in) {
        ReadFromParcel(in);
    }

    private void ReadFromParcel(Parcel in) {
        playlistName = in.readString();
        playlistId = in.readString();
        playlistImage = in.readString();
        playlistImageLarge = in.readString();
        playlistOwnerName = in.readString();
        playlistOwnerId = in.readString();
        tracks = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(playlistName);
        out.writeString(playlistId);
        out.writeString(playlistImage);
        out.writeString(playlistImageLarge);
        out.writeString(playlistOwnerName);
        out.writeString(playlistOwnerId);
        out.writeInt(tracks);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
