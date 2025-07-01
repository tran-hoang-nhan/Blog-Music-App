package com.example.blogmusic.ui.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddBlogFragment extends Fragment {

    private AdminViewModel adminViewModel;

    private EditText edtTitle, edtAuthor, edtImageUrl;
    private Spinner spinnerType, spinnerGenre;
    private Button btnSubmit;

    // Post fields
    private EditText edtPostSubtitle, edtPostIntro, edtPostContent, edtPostConclusion, edtPostTags;

    // Review fields
    private EditText edtReviewSubtitle, edtReviewSummary, edtReviewTracklist, edtReviewContent, edtReviewScore, edtReviewConclusion, edtReviewTags, edtReleaseDate;

    private LinearLayout layoutPost, layoutReview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_blog, container, false);

        adminViewModel = new ViewModelProvider(this).get(AdminViewModel.class);

        // Chung
        edtTitle = view.findViewById(R.id.edt_title);
        edtAuthor = view.findViewById(R.id.edt_author);
        edtImageUrl = view.findViewById(R.id.edt_image_url);
        spinnerType = view.findViewById(R.id.spinner_type);
        spinnerGenre = view.findViewById(R.id.spinner_genre);
        btnSubmit = view.findViewById(R.id.btn_submit);

        layoutPost = view.findViewById(R.id.layout_post);
        layoutReview = view.findViewById(R.id.layout_review);

        // Post fields
        edtPostSubtitle = view.findViewById(R.id.edt_post_subtitle);
        edtPostIntro = view.findViewById(R.id.edt_post_intro);
        edtPostContent = view.findViewById(R.id.edt_post_content);
        edtPostConclusion = view.findViewById(R.id.edt_post_conclusion);
        edtPostTags = view.findViewById(R.id.edt_post_tags);

        // Review fields
        edtReviewSubtitle = view.findViewById(R.id.edt_review_subtitle);
        edtReviewSummary = view.findViewById(R.id.edt_review_summary);
        edtReviewTracklist = view.findViewById(R.id.edt_review_tracklist);
        edtReviewContent = view.findViewById(R.id.edt_review_content);
        edtReviewScore = view.findViewById(R.id.edt_review_score);
        edtReviewConclusion = view.findViewById(R.id.edt_review_conclusion);
        edtReviewTags = view.findViewById(R.id.edt_review_tags);
        edtReleaseDate = view.findViewById(R.id.edt_release_date);


        // Spinner setup
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.blog_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        ArrayAdapter<CharSequence> genreAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.genre_list, android.R.layout.simple_spinner_item);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenre.setAdapter(genreAdapter);

        // Spinner change listener
        spinnerType.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> adapterView, View view, int i, long l) {
                String selected = adapterView.getItemAtPosition(i).toString();
                if (selected.equals("post")) {
                    layoutPost.setVisibility(View.VISIBLE);
                    layoutReview.setVisibility(View.GONE);
                } else {
                    layoutPost.setVisibility(View.GONE);
                    layoutReview.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> adapterView) {
                layoutPost.setVisibility(View.GONE);
                layoutReview.setVisibility(View.GONE);
            }
        });

        btnSubmit.setOnClickListener(v -> {
            String type = spinnerType.getSelectedItem().toString();
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            Map<String, String> data = new HashMap<>();
            data.put("type", type);
            data.put("image_cover", edtImageUrl.getText().toString());

            if (type.equals("post")) {
                data.put("title", edtTitle.getText().toString());
                data.put("author", edtAuthor.getText().toString());
                data.put("subtitle", edtPostSubtitle.getText().toString());
                data.put("introduction", edtPostIntro.getText().toString());
                data.put("main_content", edtPostContent.getText().toString());
                data.put("conclusion", edtPostConclusion.getText().toString());
                data.put("tags", edtPostTags.getText().toString());
                data.put("date", currentDate);
            } else {
                data.put("album_title", edtTitle.getText().toString());
                data.put("artist", edtAuthor.getText().toString());
                data.put("genre", spinnerGenre.getSelectedItem().toString());
                data.put("reviewer", edtAuthor.getText().toString());

                String releaseDate = edtReleaseDate.getText().toString().trim();
                if (!releaseDate.isEmpty()) {
                    data.put("release_date", releaseDate);
                } else {
                    Toast.makeText(getContext(), "Vui lòng nhập ngày phát hành", Toast.LENGTH_SHORT).show();
                    return;
                }

                data.put("review_date", currentDate);
                data.put("subtitle", edtReviewSubtitle.getText().toString());
                data.put("summary", edtReviewSummary.getText().toString());
                data.put("tracklist", edtReviewTracklist.getText().toString());
                data.put("main_content", edtReviewContent.getText().toString());
                data.put("score", edtReviewScore.getText().toString());
                data.put("conclusion", edtReviewConclusion.getText().toString());
                data.put("tags", edtReviewTags.getText().toString());

            }

            adminViewModel.addBlog(data);
        });

        adminViewModel.getAddBlogResult().observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                Log.d("ADD_BLOG_RESPONSE", "status=" + response.isStatus() + ", message=" + response.getMessage());
                if (response.isStatus()) {
                    Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Thêm thất bại: " + response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("ADD_BLOG_RESPONSE", "Response null");
                Toast.makeText(getContext(), "Không nhận được phản hồi từ server", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
