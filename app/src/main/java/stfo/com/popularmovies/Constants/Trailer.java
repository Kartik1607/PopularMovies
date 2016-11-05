package stfo.com.popularmovies.Constants;

import android.net.Uri;

/**
 * Created by Kartik Sharma on 03/10/16.
 */
public class Trailer {
    private String video_url, youtube_id, thumbnail_url, name;

    public Trailer(String youtube_id, String name) {
        this.youtube_id = youtube_id;
        this.name = name;


        Uri video_uri = Uri.parse(Utility.youtubeBasePathApi).buildUpon()
                .appendQueryParameter("v", youtube_id)
                .build();

        video_url = video_uri.toString();

        Uri thumbnail_uri = Uri.parse(Utility.youtubeThumbnailBasePathApi).buildUpon()
                .appendPath(youtube_id)
                .appendPath(Utility.youtubeQuality)
                .build();

        thumbnail_url = thumbnail_uri.toString();
    }

    public String getVideo_url() {
        return video_url;
    }

    public String getYoutube_id() {
        return youtube_id;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public String getName() {
        return name;
    }
}
