<?php
include '../db.php';

$user_id = $_POST['user_id'] ?? null;
$post_id = $_POST['post_id'] ?? null;
$review_id = $_POST['review_id'] ?? null;

if (!$user_id || (!$post_id && !$review_id)) {
    echo json_encode(['status' => false, 'message' => 'Thiếu thông tin']);
    exit;
}

try {
    if ($post_id) {
        $stmt = $conn->prepare("DELETE FROM favorites WHERE user_id = ? AND post_id = ?");
        $stmt->bind_param("ii", $user_id, $post_id);
    } else {
        $stmt = $conn->prepare("DELETE FROM favorites WHERE user_id = ? AND review_id = ?");
        $stmt->bind_param("ii", $user_id, $review_id);
    }

    $stmt->execute();

    echo json_encode(['status' => true, 'message' => 'Đã bỏ tim']);
} catch (Exception $e) {
    echo json_encode(['status' => false, 'message' => $e->getMessage()]);
}
?>
