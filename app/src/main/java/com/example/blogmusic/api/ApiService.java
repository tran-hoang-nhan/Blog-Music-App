package com.example.blogmusic.api;

import com.example.blogmusic.ui.components.AdminResponse;
import com.example.blogmusic.ui.components.BotResponse;
import com.example.blogmusic.ui.components.ChatRequest;
import com.example.blogmusic.ui.components.EditProfileResponse;
import com.example.blogmusic.ui.components.FavoriteResponse;
import com.example.blogmusic.ui.components.Post;
import com.example.blogmusic.ui.components.PostDetail;
import com.example.blogmusic.ui.components.PostResponse;
import com.example.blogmusic.ui.components.ReviewAlbum;
import com.example.blogmusic.ui.components.ReviewAlbumDetail;
import com.example.blogmusic.ui.components.ReviewAlbumResponse;
import com.example.blogmusic.ui.components.SearchResponse;
import com.example.blogmusic.ui.components.AuthResponse.LoginResponse;
import com.example.blogmusic.ui.components.AuthResponse.RegisterResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;


public interface ApiService {
  // Gọi API CHO POST
    @GET("post/posts.php")
    Call<List<Post>> getPostsBySort(@Query("sort") String sortType, @Query("user_id") int userId);
    @GET("post/posts.php")
    Call<Post> getPostById(@Query("id") int id);
    @POST("post/posts.php")
    Call<Void> addPost(@Body Post post);

    @GET("post/post_detail.php")
    Call<PostDetail> getPostDetail(@Query("id") int id);
    @GET("post/related_post.php")
    Call<PostResponse> getRelatedPosts(@Query("author") String author, @Query("id") int postId);

  // Gọi API CHO REVIEW
    @GET("review/reviews.php")
    Call<List<ReviewAlbum>> getReviewsBySort(@Query("sort") String sortType, @Query("user_id") int userId);
    @POST("review/reviews.php")
    Call<Void> addReview(@Body ReviewAlbum review);
    @GET("review/review_detail.php")
    Call<ReviewAlbumDetail> getReviewDetail(@Query("id") int id);
    @GET("review/related_reviews.php")
    Call<ReviewAlbumResponse> getRelatedReviews(@Query("artist") String artist, @Query("id") int reviewId);


 // GỌI API CHO ...
    @GET("views.php")
    Call<Void> increaseViews(@Query("type") String type, @Query("id") int id);
    @GET("search.php")
    Call<SearchResponse> search(@Query("keyword") String keyword);
    @FormUrlEncoded
    @POST("user/login.php")
    Call<LoginResponse> login(@Field("email") String email, @Field("password") String password);
    @FormUrlEncoded
    @POST("user/register.php")
    Call<RegisterResponse> register(@Field("name") String name, @Field("email") String email, @Field("password") String password);
    @FormUrlEncoded
    @POST("user/favorite.php")
    Call<FavoriteResponse> favorite(@Field("user_id") int userId, @Field("post_id") Integer postId, @Field("review_id") Integer reviewId);
    @FormUrlEncoded
    @POST("user/unfavorite.php")
    Call<FavoriteResponse> unfavorite(@Field("user_id") int userId, @Field("post_id") Integer postId, @Field("review_id") Integer reviewId);
    @FormUrlEncoded
    @POST("count_favorite.php") Call<FavoriteResponse> countFavorites(@Field("post_id") int postId);
    @FormUrlEncoded
    @POST("login_google.php")
    Call<LoginResponse> loginWithGoogle(@Field("email") String email, @Field("name") String name, @Field("idToken") String idToken);
    @FormUrlEncoded
    @POST("admin/add_blog.php")
    Call<AdminResponse> addBlog(@FieldMap Map<String, String> data);
    @FormUrlEncoded
    @POST("admin/delete_blog.php")
    Call<AdminResponse> deleteBlog(@Field("type") String type, @Field("id_or_title") String idOrTitle);
    @FormUrlEncoded
    @POST("update_profile.php")
    Call<EditProfileResponse> updateProfile(@Field("user_id") int userId, @Field("name") String name, @Field("email") String email);
    @POST("chatbot_ai.php")
    Call<BotResponse> sendMessage(@Body ChatRequest request);
}
