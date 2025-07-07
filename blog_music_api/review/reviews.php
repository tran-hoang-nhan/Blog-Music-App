<?php
header("Content-Type: application/json; charset=UTF-8");
require "../db.php";

$sort = $_GET['sort'] ?? 'recent';
$user_id = isset($_GET['user_id']) ? intval($_GET['user_id']) : null;

switch ($sort) {
    case 'views':
        $orderBy = "views DESC";
        break;
    case 'favorites':
        $orderBy = "favorites DESC";
        break;
    case 'recent':
    default:
        $orderBy = "review_date DESC";
        break;
}
$sql = "SELECT id, album_title, artist, rating, release_date, review_date, image_cover, views, favorites 
        FROM reviews 
        ORDER BY $orderBy"; 
$result = $conn->query($sql);

$reviews = [];

while ($row = $result->fetch_assoc()) {
    $review_id = $row['id'];

    if ($user_id) {
        $check = $conn->prepare("SELECT 1 FROM favorites WHERE user_id = ? AND review_id = ?");
        $check->bind_param("ii", $user_id, $review_id);
        $check->execute();
        $check->store_result();
        $row['favorited'] = $check->num_rows > 0;
        $check->close();
    } else {
        $row['favorited'] = false;
    }
    file_put_contents("check_debug.log", "user_id=$user_id, review_id=$review_id, favorited=" . ($row['favorited'] ? 'true' : 'false') . "\n", FILE_APPEND);
    $reviews[] = $row;
}

// Trả về JSON
echo json_encode($reviews, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE);
?>
