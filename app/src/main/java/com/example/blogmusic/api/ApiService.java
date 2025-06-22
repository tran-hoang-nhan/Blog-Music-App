package com.example.blogmusic.api;

import com.example.blogmusic.ui.components.Post;
import com.example.blogmusic.ui.components.PostDetail;
import com.example.blogmusic.ui.components.ReviewAlbum;
import com.example.blogmusic.ui.components.ReviewAlbumDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Body;

public interface ApiService {

    // Bài viết (Post)
    @GET("posts.php")
    Call<List<Post>> getAllPosts();

    @GET("posts.php")
    Call<Post> getPostById(@Query("id") int id);

    @POST("posts.php")
    Call<Void> addPost(@Body Post post);

    // Review album
    @GET("reviews.php")
    Call<List<ReviewAlbum>> getReviews();

    @GET("reviews.php")
    Call<ReviewAlbum> getReviewById(@Query("id") int id);

    @POST("reviews.php")
    Call<Void> addReview(@Body ReviewAlbum review);

    @GET("post_detail.php")
    Call<PostDetail> getPostDetail(@Query("id") int id);

    @GET("review_detail.php")
    Call<ReviewAlbumDetail> getReviewDetail(@Query("id") int id);

}
