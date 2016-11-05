package stfo.com.popularmovies.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import stfo.com.popularmovies.BuildConfig;

public class Utility {

    public static final String basePathApi = "http://api.themoviedb.org/3/";
    public static final String youtubeBasePathApi = "http://youtube.com/watch";
    public static final String youtubeThumbnailBasePathApi = "http://img.youtube.com/vi/";
    public static final String youtubeQuality = "hqdefault.jpg";
    public static String basePathImageApi = "http://image.tmdb.org/t/p/";

    public static final String KEY_API = "api_key";
    public static final String KEY_CONFIGURE_API = "configuration";
    public static final String KEY_MOVIE_API = "movie";
    public static final String KEY_POPULAR_API = "popular";
    public static final String KEY_TOP_RATED_API = "top_rated";
    public static final String KEY_REVIEWS_API = "reviews";
    public static final String KEY_CREDITS_API = "credits";
    public static final String KEY_PAGE_API = "page";
    public static final String KEY_PERSON_API = "person";
    public static final String KEY_MOVIE_CREDITS_API = "movie_credits";
    public static final String KEY_IMAGES_API = "images";
    public static final String KEY_VIDEOS_API = "videos";
    public static final String KEY_SEARCH_API = "search";
    public static final String KEY_MULTI_API = "multi";
    public static final String KEY_QUERY_API = "query";


    public static final String VALUE_API_KEY = BuildConfig.THE_MOVIE_DB_API_TOKEN;


    public static final String JSON_KEY_API_IMAGES = "images";
    public static final String JSON_KEY_API_BASE_URL = "base_url";
    public static final String JSON_KEY_API_RESULTS = "results";
    public static final String JSON_KEY_API_POSTER_PATH = "poster_path";
    public static final String JSON_KEY_API_BACKDROP_PATH = "backdrop_path";
    public static final String JSON_KEY_API_TITLE = "title";
    public static final String JSON_KEY_API_RELEASE_DATE = "release_date";
    public static final String JSON_KEY_API_ID = "id";
    public static final String JSON_KEY_API_OVERVIEW = "overview";
    public static final String JSON_KEY_API_RATING = "vote_average";
    public static final String JSON_KEY_API_VOTE_COUNT = "vote_count";
    public static final String JSON_KEY_API_TAGLINE = "tagline";
    public static final String JSON_KEY_API_RUNTIME = "runtime";
    public static final String JSON_KEY_API_AUTHOR = "author";
    public static final String JSON_KEY_API_CONTENT = "content";
    public static final String JSON_KEY_API_CASTS = "cast";
    public static final String JSON_KEY_API_CAST_CHARACTER = "character";
    public static final String JSON_KEY_API_CAST_NAME = "name";
    public static final String JSON_KEY_API_CAST_PROFILE_PATH = "profile_path";
    public static final String JSON_KEY_API_PERSON_BIOGRAPHY = "biography";
    public static final String JSON_KEY_API_PERSON_BIRTHDAY = "birthday";
    public static final String JSON_KEY_API_PERSON_DEATHDAY = "deathday";
    public static final String JSON_KEY_API_YOUTUBE_KEY = "key";
    public static final String JSON_KEY_API_BACKDROPS = "backdrops";
    public static final String JSON_KEY_API_FILE_PATH = "file_path";
    public static final String JSON_KEY_API_NAME = "name";
    public static final String JSON_KEY_API_MEDIA_TYPE = "media_type";


    public static final String LOG_TAG = "POPULAR_MOVIES_";
    public static final String TAG = "POPULAR_MOVIES_";
    public static final String KEY_APP_INT_EXTRA_DATA = "KEY_APP_INT_EXTRA_DATA";
    public static final String KEY_APP_INT_EXTRA_DATA_SECONDARY = "KEY_APP_INT_EXTRA_DATA_SEC";
    public static final String KEY_APP_INT_EXTRA_COLOR = "KEY_APP_INT_EXTRA_COLOR";
    public static final String KEY_APP_BOOL_FAV = "KEY_APP_BOOL_FAV";
    public static final String KEY_APP_STRING_DATA = "KEY_APP_STRING_DATA";


    public static String KEY_USER_SELECTED_FILER = KEY_POPULAR_API;

    public static String formatDate(String input) throws ParseException{
        SimpleDateFormat inputFormat =  new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy");
        Date date = inputFormat.parse(input);
        return outputFormat.format(date);
    }



    public static String getw92Path(String path){
        return Utility.basePathImageApi + "w92" + path;
    }

    public static String getw300Path(String path){
        return Utility.basePathImageApi + "w300" + path;
    }

    public static String getw780Path(String path){
        return Utility.basePathImageApi + "w780" + path;
    }

    public static String getOriginalPath(String path){
        return Utility.basePathImageApi + "original" + path;
    }
}
