<?php
header("Content-Type: application/json; charset=UTF-8");
require "../db.php";

$sql = "SELECT id, album_title, artist, rating, release_date ,review_date, image_cover, views, favorites 
        FROM reviews 
        ORDER BY review_date DESC"; 

$result = $conn->query($sql);

$reviews = [];

while ($row = $result->fetch_assoc()) {
    $reviews[] = $row;
}

echo json_encode($reviews, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE);
?>
