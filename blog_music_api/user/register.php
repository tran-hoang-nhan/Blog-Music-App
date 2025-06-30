    <?php
    include '../db.php';

    $name = $_POST['name'];
    $email = $_POST['email'];
    $password = $_POST['password'];

    $sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("sss", $name, $email, $password);

    if ($stmt->execute()) {
        echo json_encode(["status" => true, "message" => "Register success"]);
    } else {
        echo json_encode(["status" => false, "message" => "Register failed"]);
    }

    $stmt->close();
    $conn->close();
    ?>
