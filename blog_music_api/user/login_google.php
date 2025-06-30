<?php
header('Content-Type: application/json');
require_once "../db.php"; 

$email = $_POST['email'] ?? '';
$name = $_POST['name'] ?? '';

if (empty($email) || empty($name)) {
    echo json_encode([
        "status" => false,
        "message" => "Thiếu email hoặc tên"
    ]);
    exit;
}
$stmt = $conn->prepare("SELECT id, name, email FROM users WHERE email = ?");
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result && $result->num_rows > 0) {
    $user = $result->fetch_assoc();

    echo json_encode([
        "status" => true,
        "userId" => $user['id'],
        "name" => $user['name'],
        "email" => $user['email']
    ]);
} else {
    $stmt = $conn->prepare("INSERT INTO users (name, email) VALUES (?, ?)");
    $stmt->bind_param("ss", $name, $email);
    if ($stmt->execute()) {
        $userId = $conn->insert_id;

        echo json_encode([
            "status" => true,
            "userId" => $userId,
            "name" => $name,
            "email" => $email
        ]);
    } else {
        echo json_encode([
            "status" => false,
            "message" => "Không thể thêm người dùng"
        ]);
    }
}

$conn->close();
