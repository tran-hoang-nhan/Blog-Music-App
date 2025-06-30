<?php
include '../db.php';
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

if ($_SERVER['REQUEST_METHOD'] === 'GET' && isset($_GET['id'])) {
    $id = intval($_GET['id']);  

    // Lấy thông tin từ reviews + review_detail
    $stmt = $conn->prepare("
        SELECT r.album_title, r.artist, r.reviewer, r.release_date, r.review_date, r.image_cover,
               d.review_id, d.subtitle, d.summary, d.tracklist,
               d.main_content, d.score, d.conclusion, d.tags
        FROM reviews r
        JOIN review_detail d ON r.id = d.review_id
        WHERE r.id = ?
    ");
    $stmt->bind_param("i", $id);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($review = $result->fetch_assoc()) {

        // Lấy media theo review_id
        $mediaStmt = $conn->prepare("
            SELECT file_url, type, position
            FROM media
            WHERE review_id = ?
        ");
        $mediaStmt->bind_param("i", $id); 
        $mediaStmt->execute();
        $mediaResult = $mediaStmt->get_result();

        $mediaByPosition = [
            'subtitle' => [],
            'summary' => [],
            'tracklist' => [],
            'main_content' => [],
            'score' => [],
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

        $review['media'] = $mediaByPosition;

        echo json_encode($review, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);

    } else {
        http_response_code(404);
        echo json_encode(["error" => "Review not found"]);
    }
} else {
    http_response_code(400);
    echo json_encode(["error" => "Invalid request"]);
}
?>
