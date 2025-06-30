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
        $orderBy = "date DESC";
        break;
}

$sql = "SELECT id, title, author, date, image_cover, views, favorites 
        FROM posts 
        ORDER BY $orderBy";

$result = $conn->query($sql);

$posts = [];
while ($row = $result->fetch_assoc()) {
    $post_id = $row['id'];

    if ($user_id) {
        $check = $conn->prepare("SELECT 1 FROM favorites WHERE user_id = ? AND post_id = ?");
        $check->bind_param("ii", $user_id, $post_id);
        $check->execute();
        $check->store_result();
        $row['favorited'] = $check->num_rows > 0;
        $check->close();
    } else {
        $row['favorited'] = false;
    }
    file_put_contents("check_debug.log", "user_id=$user_id, post_id=$post_id, favorited=" . ($row['favorited'] ? 'true' : 'false') . "\n", FILE_APPEND);
    $posts[] = $row;
}

echo json_encode($posts, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE);
?>
