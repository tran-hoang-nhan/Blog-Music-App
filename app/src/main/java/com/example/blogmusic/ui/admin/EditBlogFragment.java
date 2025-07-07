    package com.example.blogmusic.ui.admin;

    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.LinearLayout;
    import android.widget.Spinner;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;
    import androidx.lifecycle.ViewModelProvider;

    import com.example.blogmusic.R;
    import com.example.blogmusic.api.ApiService;
    import com.example.blogmusic.network.RetrofitClient;
    import com.example.blogmusic.ui.components.PostDetail;
    import com.example.blogmusic.ui.components.ReviewAlbumDetail;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Map;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class EditBlogFragment extends Fragment {

        private AdminViewModel adminViewModel;

        private Spinner spinnerType, spinnerId, spinnerGenre;
        private EditText edtTitle, edtAuthor, edtImageUrl;
        private EditText edtPostSubtitle, edtPostIntro, edtPostContent, edtPostConclusion, edtPostTags;
        private EditText edtReviewSubtitle, edtReviewSummary, edtReviewTracklist, edtReviewContent,
                edtReviewScore, edtReviewConclusion, edtReviewTags, edtReleaseDate, edtReviewRating;
        private LinearLayout layoutPost, layoutReview;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_edit_blog, container, false);

            adminViewModel = new ViewModelProvider(this).get(AdminViewModel.class);
            adminViewModel.getEditBlogResult().observe(getViewLifecycleOwner(), response -> {
                if (response != null) {
                    if (response.isStatus()) {
                        Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Thất bại: " + response.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Không có phản hồi từ server", Toast.LENGTH_SHORT).show();
                }
            });

            spinnerType = view.findViewById(R.id.spinner_type);
            spinnerId = view.findViewById(R.id.spinner_id);
            spinnerGenre = view.findViewById(R.id.spinner_genre);

            edtTitle = view.findViewById(R.id.edt_title);
            edtAuthor = view.findViewById(R.id.edt_author);
            edtImageUrl = view.findViewById(R.id.edt_image_url);

            layoutPost = view.findViewById(R.id.layout_post);
            layoutReview = view.findViewById(R.id.layout_review);

            edtPostSubtitle = view.findViewById(R.id.edt_post_subtitle);
            edtPostIntro = view.findViewById(R.id.edt_post_intro);
            edtPostContent = view.findViewById(R.id.edt_post_content);
            edtPostConclusion = view.findViewById(R.id.edt_post_conclusion);
            edtPostTags = view.findViewById(R.id.edt_post_tags);

            edtReviewSubtitle = view.findViewById(R.id.edt_review_subtitle);
            edtReviewSummary = view.findViewById(R.id.edt_review_summary);
            edtReviewTracklist = view.findViewById(R.id.edt_review_tracklist);
            edtReviewContent = view.findViewById(R.id.edt_review_content);
            edtReviewRating = view.findViewById(R.id.edt_review_rating);
            edtReviewScore = view.findViewById(R.id.edt_review_score);
            edtReviewConclusion = view.findViewById(R.id.edt_review_conclusion);
            edtReviewTags = view.findViewById(R.id.edt_review_tags);
            edtReleaseDate = view.findViewById(R.id.edt_release_date);

            Button btnLoad = view.findViewById(R.id.btn_load);
            Button btnUpdate = view.findViewById(R.id.btn_update);

            ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.blog_types, android.R.layout.simple_spinner_item);
            typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerType.setAdapter(typeAdapter);

            ArrayAdapter<CharSequence> genreAdapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.genre_list, android.R.layout.simple_spinner_item);
            genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerGenre.setAdapter(genreAdapter);

            spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                    String selectedType = spinnerType.getSelectedItem().toString();
                    layoutPost.setVisibility(View.GONE);
                    layoutReview.setVisibility(View.GONE);
                    loadBlogIds(selectedType);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            btnLoad.setOnClickListener(v -> {
                String type = spinnerType.getSelectedItem().toString();
                String idStr = (String) spinnerId.getSelectedItem();
                if (idStr == null) {
                    Toast.makeText(getContext(), "Vui lòng chọn ID", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (type.equals("post")) {
                    int postId = Integer.parseInt(spinnerId.getSelectedItem().toString());
                    ApiService api = RetrofitClient.getInstance().create(ApiService.class);
                    Call<PostDetail> call = api.getPostDetail(postId);
                    call.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(@NonNull Call<PostDetail> call, @NonNull Response<PostDetail> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                fillPostData(response.body());
                            } else {
                                Toast.makeText(getContext(), "Không tìm thấy bài viết", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<PostDetail> call, @NonNull Throwable t) {
                            Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    int reviewId = Integer.parseInt(spinnerId.getSelectedItem().toString());
                    ApiService api = RetrofitClient.getInstance().create(ApiService.class);
                    Call<ReviewAlbumDetail> call = api.getReviewDetail(reviewId);
                    call.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(@NonNull Call<ReviewAlbumDetail> call, @NonNull Response<ReviewAlbumDetail> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                fillReviewData(response.body());
                            } else {
                                Toast.makeText(getContext(), "Không tìm thấy review", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ReviewAlbumDetail> call, @NonNull Throwable t) {
                            Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

            btnUpdate.setOnClickListener(v -> {
                String type = spinnerType.getSelectedItem().toString();
                String idStr = (String) spinnerId.getSelectedItem();
                if (idStr == null) {
                    Toast.makeText(getContext(), "Chưa chọn ID để cập nhật", Toast.LENGTH_SHORT).show();
                    return;
                }
                int id = Integer.parseInt(idStr);
                Map<String, String> data = new java.util.HashMap<>();
                data.put("image_cover", edtImageUrl.getText().toString());

                if (type.equals("post")) {
                    data.put("title", edtTitle.getText().toString());
                    data.put("author", edtAuthor.getText().toString());
                    data.put("subtitle", edtPostSubtitle.getText().toString());
                    data.put("introduction", edtPostIntro.getText().toString());
                    data.put("main_content", edtPostContent.getText().toString());
                    data.put("conclusion", edtPostConclusion.getText().toString());
                    data.put("tags", edtPostTags.getText().toString());
                } else {
                    data.put("album_title", edtTitle.getText().toString());
                    data.put("artist", edtAuthor.getText().toString());
                    data.put("genre", spinnerGenre.getSelectedItem().toString());
                    data.put("reviewer", edtAuthor.getText().toString());
                    data.put("release_date", edtReleaseDate.getText().toString());
                    data.put("subtitle", edtReviewSubtitle.getText().toString());
                    data.put("summary", edtReviewSummary.getText().toString());
                    data.put("tracklist", edtReviewTracklist.getText().toString());
                    data.put("main_content", edtReviewContent.getText().toString());
                    data.put("rating", edtReviewRating.getText().toString());
                    data.put("score", edtReviewScore.getText().toString());
                    data.put("conclusion", edtReviewConclusion.getText().toString());
                    data.put("tags", edtReviewTags.getText().toString());
                }
                data.put("type", type);
                data.put("id", idStr);
                adminViewModel.editBlog(data);
            });


            return view;
        }

        private void loadBlogIds(String type) {
            ApiService api = RetrofitClient.getInstance().create(ApiService.class);
            Call<List<Integer>> call = api.getBlogIds(type);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<List<Integer>> call, @NonNull Response<List<Integer>> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        List<String> idList = new ArrayList<>();
                        for (int id : response.body()) {
                            idList.add(String.valueOf(id));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, idList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerId.setAdapter(adapter);
                    } else {
                        spinnerId.setAdapter(null);
                        Toast.makeText(getContext(), "Không có ID phù hợp", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Integer>> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Lỗi kết nối khi tải ID", Toast.LENGTH_SHORT).show();
                }
            });

        }

        private void fillPostData(PostDetail post) {
            if (post == null) return;
            layoutPost.setVisibility(View.VISIBLE);
            layoutReview.setVisibility(View.GONE);

            edtTitle.setText(post.getTitle());
            edtAuthor.setText(post.getAuthor());
            edtImageUrl.setText(post.getImageCover());
            edtPostSubtitle.setText(post.getSubtitle());
            edtPostIntro.setText(post.getIntroduction());
            edtPostContent.setText(post.getMainContent());
            edtPostConclusion.setText(post.getConclusion());
            edtPostTags.setText(post.getTags());
        }

        private void fillReviewData(ReviewAlbumDetail review) {
            if (review == null) return;
            layoutPost.setVisibility(View.GONE);
            layoutReview.setVisibility(View.VISIBLE);

            edtTitle.setText(review.getAlbumTitle());
            edtAuthor.setText(review.getArtist());
            edtImageUrl.setText(review.getImageCover());
            edtReviewSubtitle.setText(review.getSubtitle());
            edtReviewSummary.setText(review.getSummary());
            edtReviewTracklist.setText(review.getTracklist());
            edtReviewContent.setText(review.getMain_content());
            edtReviewScore.setText(String.valueOf(review.getScore()));
            edtReviewConclusion.setText(review.getConclusion());
            edtReviewTags.setText(review.getTags());
            edtReleaseDate.setText(review.getReleaseDate());

        }
    }
