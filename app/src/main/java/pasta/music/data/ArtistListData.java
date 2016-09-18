package pasta.music.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ArtistListData  implements Parcelable {
    public static final Parcelable.Creator<ArtistListData> CREATOR = new Parcelable.Creator<ArtistListData>() {
        public ArtistListData createFromParcel(Parcel in) {
            return new ArtistListData(in);
        }

        public ArtistListData[] newArray(int size) {
            return new ArtistListData[size];
        }
    };

    public String artistName;
    public String artistId;
    public int followers = -1;

    @Nullable
    public String artistImage, artistImageLarge;

    @Nullable
    public List<String> genres;
    public ArtistListData(String artistName, String artistId)
    {
        this.artistName=artistName;
        this.artistId=artistId;
    }

    public ArtistListData(Parcel in) {
        ReadFromParcel(in);
    }

    private void ReadFromParcel(Parcel in) {
        artistName = in.readString();
        artistId = in.readString();
        artistImage = in.readString();
        artistImageLarge = in.readString();
        followers = in.readInt();
        genres = new ArrayList<>();
        in.readStringList(genres);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(artistName);
        out.writeString(artistId);
        out.writeString(artistImage);
        out.writeString(artistImageLarge);
        out.writeInt(followers);
        out.writeStringList(genres);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}