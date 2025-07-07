<?php
header("Content-Type: application/json; charset=UTF-8");
require_once("db.php");

$keyword = isset($_GET['keyword']) ? trim($_GET['keyword']) : '';

if (empty($keyword)) {
    echo json_encode([
        "posts" => [],
        "reviews" => []
    ]);
    exit;
}

$keyword = "%{$keyword}%";


$postQuery = $conn->prepare("SELECT id, title, author, date, image_cover, views, favorites
                             FROM posts
                             WHERE title LIKE ? OR author LIKE ?");
$postQuery->bind_param("ss", $keyword, $keyword);
$postQuery->execute();
$postResult = $postQuery->get_result();

$posts = [];
while ($row = $postResult->fetch_assoc()) {
    $posts[] = $row;
}

$reviewQuery = $conn->prepare("SELECT id, album_title, artist, review_date, image_cover, rating, views, favorites
                               FROM reviews
                               WHERE album_title LIKE ? OR artist LIKE ? OR genre LIKE ?");
$reviewQuery->bind_param("sss", $keyword, $keyword, $keyword);
$reviewQuery->execute();
$reviewResult = $reviewQuery->get_result();

$reviews = [];
while ($row = $reviewResult->fetch_assoc()) {
    $reviews[] = $row;
}

echo json_encode([
    "posts" => $posts,
    "reviews" => $reviews
], JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE);
