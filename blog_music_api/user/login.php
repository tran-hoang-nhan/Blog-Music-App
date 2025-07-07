<?php
include '../db.php';
$email = $_POST['email'];
$password = $_POST['password'];

$sql = "SELECT * FROM users WHERE email=? AND password=?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("ss", $email, $password);
$stmt->execute();
$result = $stmt->get_result();

if ($row = $result->fetch_assoc()) {
    echo json_encode([
        "status" => true,
        "message" => "Login success",
        "user_id" => $row['user_id'],    
        "name" => $row['name'],
        "email" => $row['email']
    ]);
} else {
    echo json_encode(["status" => false, "message" => "Invalid credentials"]);
}

$stmt->close();
$conn->close();
?>
