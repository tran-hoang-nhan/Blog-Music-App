<?php
include '../db.php';
header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] === 'GET' && isset($_GET['id'])) {
    $id = intval($_GET['id']);

    // Lấy thông tin chi tiết của bài viết
     $stmt = $conn->prepare("
        SELECT p.title, p.author, p.date, p.image_cover, 
        d.post_id, d.subtitle, d.introduction, d.main_content, d.conclusion, d.tags
        FROM posts p
        JOIN post_detail d ON p.id = d.post_id
        WHERE p.id = ?" );
    $stmt->bind_param("i", $id);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($post = $result->fetch_assoc()) {
        // Lấy media liên quan đến bài viết và nhóm theo position
        $mediaStmt = $conn->prepare("SELECT file_url, type, position FROM media WHERE post_id = ?");
        $mediaStmt->bind_param("i", $id);
        $mediaStmt->execute();
        $mediaResult = $mediaStmt->get_result();

        $mediaByPosition = [
            'subtitle' => [],
            'introduction' => [],
            'main_content' => [],
            'conclusion' => [],
            'tags' => []
        ];

        while ($m = $mediaResult->fetch_assoc()) {
            $position = $m['position'];
            if (isset($mediaByPosition[$position])) {
                $mediaByPosition[$position][] = [
                    'file_url' => $m['file_url'],
                    'type' => $m['type']
                ];
            }
        }
        $post['media'] = $mediaByPosition;
        echo json_encode($post, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE);
    } else {
        echo json_encode(["error" => "Post not found"]);
    }
} else {
    echo json_encode(["error" => "Invalid request"]);
}
?>
