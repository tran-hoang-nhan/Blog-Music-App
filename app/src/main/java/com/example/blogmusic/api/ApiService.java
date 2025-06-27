package com.example.blogmusic.api;

import com.example.blogmusic.ui.components.Post;
import com.example.blogmusic.ui.components.PostDetail;
import com.example.blogmusic.ui.components.PostResponse;
import com.example.blogmusic.ui.components.ReviewAlbum;
import com.example.blogmusic.ui.components.ReviewAlbumDetail;
import com.example.blogmusic.ui.components.SearchResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Body;

public interface ApiService {
  // Gọi API CHO POST
    @GET("post/posts.php")
    Call<List<Post>> getAllPosts();
    @GET("post/posts.php")
    Call<Post> getPostById(@Query("id") int id);
    @POST("post/posts.php")
    Call<Void> addPost(@Body Post post);
    @GET("post/post_recent.php")
    Call<List<Post>> getRecentPosts();
    @GET("post/post_detail.php")
    Call<PostDetail> getPostDetail(@Query("id") int id);
  @GET("post/related_post.php")
  Call<PostResponse> getRelatedPosts(@Query("author") String author, @Query("id") int postId);


  // Gọi API CHO REVIEW
    @GET("review/reviews.php")
    Call<List<ReviewAlbum>> getReviews();
    @POST("review/reviews.php")
    Call<Void> addReview(@Body ReviewAlbum review);
    @GET("review/review_detail.php")
    Call<ReviewAlbumDetail> getReviewDetail(@Query("id") int id);
    @GET("review/review_recent.php")
    Call<List<ReviewAlbum>> getRecentReviews();
    // GỌI API CHO ...
    @GET("views.php")
    Call<Void> increaseViews(@Query("type") String type, @Query("id") int id);
    @GET("search.php")
    Call<SearchResponse> search(@Query("keyword") String keyword);
}
