<?php
include "../db.php";

if (!isset($_GET['author']) || !isset($_GET['id'])) {
    http_response_code(400); // Bad Request
    echo json_encode(["error" => "Missing parameters"]);
    exit;
}

$author = $_GET['author'];
$post_id = intval($_GET['id']);

$sql = "SELECT * FROM posts WHERE author = ? AND id != ? ORDER BY date DESC LIMIT 4";
$stmt = $conn->prepare($sql);
$stmt->bind_param("si", $author, $post_id);
$stmt->execute();
$result = $stmt->get_result();

$data = [];
while($row = $result->fetch_assoc()) {
    $data[] = $row;
}

echo json_encode(["posts" => $data]);
?>
