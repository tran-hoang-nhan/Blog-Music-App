<?php
include '../db.php';

$user_id = isset($_POST['user_id']) ? (int)$_POST['user_id'] : null;
$post_id = isset($_POST['post_id']) ? (int)$_POST['post_id'] : null;
$review_id = isset($_POST['review_id']) ? (int)$_POST['review_id'] : null;

if (!$user_id || (!$post_id && !$review_id)) {
    echo json_encode(['status' => false, 'message' => 'Thiếu thông tin']);
    exit;
}

try {
    if ($post_id) {
        // Thêm vào bảng favorites
        $stmt = $conn->prepare("INSERT IGNORE INTO favorites(user_id, post_id) VALUES (?, ?)");
        $stmt->bind_param("ii", $user_id, $post_id);
        $stmt->execute();
        $stmt->close();

        // Đếm lại số lượng
        $countStmt = $conn->prepare("SELECT COUNT(*) FROM favorites WHERE post_id = ?");
        $countStmt->bind_param("i", $post_id);
        $countStmt->execute();
        $countStmt->bind_result($count);
        $countStmt->fetch();
        $countStmt->close();

        // Cập nhật bảng posts
        $updateStmt = $conn->prepare("UPDATE posts SET favorites = ? WHERE id = ?");
        $updateStmt->bind_param("ii", $count, $post_id);
        $updateStmt->execute();
        $updateStmt->close();

        echo json_encode([
            'status' => true,
            'message' => 'Đã thả tim bài viết',
            'favorites' => $count,
            'favorited' => true
        ]);
    } else {
        // Thêm vào bảng favorites
        $stmt = $conn->prepare("INSERT IGNORE INTO favorites(user_id, review_id) VALUES (?, ?)");
        $stmt->bind_param("ii", $user_id, $review_id);
        $stmt->execute();
        $stmt->close();

        // Đếm lại số lượng
        $countStmt = $conn->prepare("SELECT COUNT(*) FROM favorites WHERE review_id = ?");
        $countStmt->bind_param("i", $review_id);
        $countStmt->execute();
        $countStmt->bind_result($count);
        $countStmt->fetch();
        $countStmt->close();

        // Cập nhật bảng reviews
        $updateStmt = $conn->prepare("UPDATE reviews SET favorites = ? WHERE id = ?");
        $updateStmt->bind_param("ii", $count, $review_id);
        $updateStmt->execute();
        $updateStmt->close();

        echo json_encode([
            'status' => true,
            'message' => 'Đã thả tim review',
            'favorites' => $count,
            'favorited' => true
        ]);
    }
} catch (Exception $e) {
    echo json_encode(['status' => false, 'message' => $e->getMessage()]);
}
?>
