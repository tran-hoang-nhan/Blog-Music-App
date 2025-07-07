<?php
require_once "../db.php";

$artist = $_GET['artist'];
$exclude_id = $_GET['review_id'];

$sql = "SELECT * FROM review_album WHERE artist = ? AND id != ? ORDER BY review_date DESC LIMIT 4";
$stmt = $conn->prepare($sql);
$stmt->bind_param("si", $artist, $exclude_id);
$stmt->execute();
$result = $stmt->get_result();

$reviews = [];
while ($row = $result->fetch_assoc()) {
    $reviews[] = $row;
}

echo json_encode(["reviews" => $reviews]);
