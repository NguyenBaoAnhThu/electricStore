
document.addEventListener("DOMContentLoaded", function () {
    // Validation cho form thêm chức vụ
    const addPositionForm = document.getElementById("addPositionForm");

    if (addPositionForm) {
        addPositionForm.addEventListener("submit", function (event) {
            if (!validatePositionForm()) {
                event.preventDefault();
                event.stopPropagation();
            }
            addPositionForm.classList.add('was-validated');
        });
    }

    // Xử lý nút xóa chức vụ
    document.querySelectorAll(".delete-position").forEach(button => {
        button.addEventListener("click", function () {
            const positionId = this.getAttribute("data-position-id");
            deletePosition(positionId);
        });
    });
});

function validatePositionForm() {
    const positionName = document.getElementById("positionName").value.trim();
    const positionDescription = document.getElementById("positionDescription").value.trim();
    let isValid = true;

    // Validate tên chức vụ
    if (positionName === "") {
        document.getElementById("positionName").classList.add("is-invalid");
        document.getElementById("positionName").nextElementSibling.textContent =
            "Tên chức vụ không được để trống!";
        isValid = false;
    } else {
        document.getElementById("positionName").classList.remove("is-invalid");
        document.getElementById("positionName").classList.add("is-valid");
    }

    // Validate mô tả chức vụ
    if (positionDescription === "") {
        document.getElementById("positionDescription").classList.add("is-invalid");
        document.getElementById("positionDescription").nextElementSibling.textContent =
            "Mô tả chức vụ không được để trống!";
        isValid = false;
    } else {
        document.getElementById("positionDescription").classList.remove("is-invalid");
        document.getElementById("positionDescription").classList.add("is-valid");
    }

    return isValid;
}

function deletePosition(positionId) {
    if (!positionId) {
        Swal.fire({
            icon: "warning",
            title: "Lỗi dữ liệu!",
            text: "Không tìm thấy ID chức vụ.",
            confirmButtonText: "OK"
        });
        return;
    }

    Swal.fire({
        title: "Bạn có chắc chắn?",
        text: "Chức vụ này sẽ bị xóa!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#d33",
        cancelButtonColor: "#3085d6",
        confirmButtonText: "Xóa",
        cancelButtonText: "Hủy"
    }).then((result) => {
        if (result.isConfirmed) {
            fetch("/Admin/employee-position/delete/" + positionId, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                }
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        Swal.fire({
                            icon: "success",
                            title: "Thành công!",
                            text: "Chức vụ đã được xóa.",
                            confirmButtonText: "OK"
                        }).then(() => {
                            location.reload();
                        });
                    } else {
                        Swal.fire({
                            icon: "error",
                            title: "Lỗi!",
                            text: data.message || "Có lỗi xảy ra, vui lòng thử lại!",
                            confirmButtonText: "OK"
                        });
                    }
                })
                .catch(error => {
                    console.error("Lỗi:", error);
                    Swal.fire({
                        icon: "error",
                        title: "Lỗi hệ thống!",
                        text: "Không thể kết nối đến server.",
                        confirmButtonText: "OK"
                    });
                });
        }
    });
}

