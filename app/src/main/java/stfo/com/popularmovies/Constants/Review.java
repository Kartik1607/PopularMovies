package stfo.com.popularmovies.Constants;

/**
 * Created by Kartik Sharma on 15/09/16.
 */
public class Review{
    private  String author, review;

    public Review(String author, String review) {
        this.author = author;
        this.review = review;
    }

    public String getAuthor() {
        return author;
    }

    public String getReview() {
        return review;
    }
}

