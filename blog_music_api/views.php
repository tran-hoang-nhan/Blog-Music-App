<?php
require_once("db.php");

$type = isset($_GET['type']) ? $_GET['type'] : '';
$id = isset($_GET['id']) ? intval($_GET['id']) : 0;

if (($type === 'post' || $type === 'review') && $id > 0) {
    $table = ($type === 'post') ? 'posts' : 'reviews';

    $stmt = $conn->prepare("UPDATE $table SET views = views + 1 WHERE id = ?");
    $stmt->bind_param("i", $id);
    $stmt->execute();

    echo json_encode(["success" => true]);
} else {
    echo json_encode(["success" => false, "message" => "Missing or invalid parameters"]);
}
?>
