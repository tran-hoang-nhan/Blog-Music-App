<?php
include 'db.php';

$post_id = $_POST['post_id'] ?? null;
$review_id = $_POST['review_id'] ?? null;

if (!$post_id && !$review_id) {
    echo json_encode(['status' => false, 'message' => 'Thiếu post_id hoặc review_id']);
    exit;
}
try {
    $isPost = $post_id !== null;
    $targetId = $isPost ? $post_id : $review_id;
    $column = $isPost ? 'post_id' : 'review_id';
    $table = $isPost ? 'posts' : 'reviews';

    // Đếm số lượt yêu thích
    $stmt = $conn->prepare("SELECT COUNT(*) FROM favorites WHERE $column = ?");
    $stmt->bind_param("i", $targetId);
    $stmt->execute();
    $stmt->bind_result($count);
    $stmt->fetch();
    $stmt->close();

    // Cập nhật số lượt vào bảng chính
    $update = $conn->prepare("UPDATE $table SET favorites = ? WHERE id = ?");
    $update->bind_param("ii", $count, $targetId);
    $update->execute();
    $update->close();

    echo json_encode(['status' => true, 'favorites' => $count]);
} catch (Exception $e) {
    echo json_encode(['status' => false, 'message' => $e->getMessage()]);
}
?>
