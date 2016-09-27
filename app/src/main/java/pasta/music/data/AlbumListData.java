package pasta.music.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class AlbumListData implements Parcelable {
    public static final Parcelable.Creator<AlbumListData> CREATOR = new Parcelable.Creator<AlbumListData>() {
        public AlbumListData createFromParcel(Parcel in) {
            return new AlbumListData(in);
        }

        public AlbumListData[] newArray(int size) {
            return new AlbumListData[size];
        }
    };

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getTracks() {
        return tracks;
    }

    public void setTracks(int tracks) {
        this.tracks = tracks;
    }

    public List<ArtistListData> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistListData> artists) {
        this.artists = artists;
    }

    public String getAlbumImageLarge() {
        return albumImageLarge;
    }

    public void setAlbumImageLarge(String albumImageLarge) {
        this.albumImageLarge = albumImageLarge;
    }

    public String getAlbumImage() {
        return albumImage;
    }

    public void setAlbumImage(String albumImage) {
        this.albumImage = albumImage;
    }

    public String getAlbumDate() {
        return albumDate;
    }

    public void setAlbumDate(String albumDate) {
        this.albumDate = albumDate;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String albumName;
    public String albumId;
    public String albumDate;
    public String albumImage;
    public String albumImageLarge;
    public List<ArtistListData> artists;
    public int tracks;

    public AlbumListData(String albumId,String albumName,String albumDate,String albumImage,int tracks)
    {
        this.albumId=albumId;
        this.albumName=albumName;
        this.albumDate=albumDate;
        this.albumImage=albumImage;
        this.tracks=tracks;
    }
    public AlbumListData(Parcel in) {
        ReadFromParcel(in);
    }

    private void ReadFromParcel(Parcel in) {
        albumName = in.readString();
        albumId = in.readString();
        albumDate = in.readString();
        albumImage = in.readString();
        albumImageLarge = in.readString();
        artists = new ArrayList<>();
        in.readList(artists, ArtistListData.class.getClassLoader());
        tracks = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(albumName);
        out.writeString(albumId);
        out.writeString(albumDate);
        out.writeString(albumImage);
        out.writeString(albumImageLarge);
        out.writeList(artists);
        out.writeInt(tracks);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
