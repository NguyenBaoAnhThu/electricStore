document.addEventListener("DOMContentLoaded", function () {
    let selectAll = document.getElementById("selectAll");
    let checkboxes = document.querySelectorAll(".employeeCheckbox");
    let deleteBtn = document.getElementById("deleteBtn");
    let activateBtn = document.getElementById("activateBtn");

    // Chọn tất cả
    selectAll.addEventListener("change", function () {
        checkboxes.forEach(checkbox => {
            checkbox.checked = this.checked;
        });
        updateActionButtons();
    });

    // Thêm sự kiện cho từng checkbox
    checkboxes.forEach(checkbox => {
        checkbox.addEventListener("change", updateActionButtons);
    });

    // Hàm kiểm tra trạng thái checkbox và hiển thị nút phù hợp
    function updateActionButtons() {
        let selectedCheckboxes = document.querySelectorAll(".employeeCheckbox:checked");

        if (selectedCheckboxes.length === 0) {
            deleteBtn.style.display = "none";
            activateBtn.style.display = "none";
            return;
        }

        let allDisabled = true;
        let allActive = true;

        selectedCheckboxes.forEach(checkbox => {
            let row = checkbox.closest("tr");
            let statusText = row.querySelector(".status-badge").textContent.trim();

            if (statusText === "Đang Hoạt động") {
                allDisabled = false;
            } else {
                allActive = false;
            }
        });

        if (allDisabled) {
            activateBtn.style.display = "block";
            deleteBtn.style.display = "none";
        } else if (allActive) {
            activateBtn.style.display = "none";
            deleteBtn.style.display = "block";
        } else {
            activateBtn.style.display = "none";
            deleteBtn.style.display = "none";
        }
    }
});

function disableSelectedEmployees() {
    let selectedIds = [];
    document.querySelectorAll(".employeeCheckbox:checked").forEach(checkbox => {
        selectedIds.push(checkbox.value);
    });

    if (selectedIds.length === 0) {
        Swal.fire({
            icon: "warning",
            title: "Chưa chọn nhân viên!",
            text: "Vui lòng chọn ít nhất một nhân viên trước khi xóa.",
            confirmButtonText: "OK"
        });
        return;
    }

    Swal.fire({
        title: "Bạn có chắc chắn?",
        text: "Những nhân viên đã chọn sẽ bị vô hiệu hóa!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#d33",
        cancelButtonColor: "#3085d6",
        confirmButtonText: "Xóa",
        cancelButtonText: "Hủy"
    }).then((result) => {
        if (result.isConfirmed) {
            fetch("/Admin/employee-manager/disable", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ employeeIds: selectedIds })
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        Swal.fire({
                            icon: "success",
                            title: "Thành công!",
                            text: "Nhân viên đã được vô hiệu hóa.",
                            confirmButtonText: "OK"
                        }).then(() => {
                            location.reload();
                        });
                    } else {
                        Swal.fire({
                            icon: "error",
                            title: "Lỗi!",
                            text: "Có lỗi xảy ra, vui lòng thử lại!",
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
function activateSelectedEmployees() {
    let selectedIdsForActivate = [];
    document.querySelectorAll(".employeeCheckbox:checked").forEach(checkbox => {
        selectedIdsForActivate.push(checkbox.value);
    });

    if (selectedIdsForActivate.length === 0) {
        Swal.fire({
            icon: "warning",
            title: "Chưa chọn nhân viên!",
            text: "Vui lòng chọn ít nhất một nhân viên trước khi khôi phục.",
            confirmButtonText: "OK"
        });
        return;
    }

    Swal.fire({
        title: "Bạn có chắc chắn?",
        text: "Những nhân viên đã chọn sẽ được khôi phục!",
        icon: "info",
        showCancelButton: true,
        confirmButtonColor: "#d33",
        cancelButtonColor: "#3085d6",
        confirmButtonText: "Khôi phục",
        cancelButtonText: "Hủy"
    }).then((result) => {
        if (result.isConfirmed) {
            fetch("/Admin/employee-manager/disable", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({employeeIds: selectedIdsForActivate})
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        Swal.fire({
                            icon: "success",
                            title: "Thành công!",
                            text: "Nhân viên đã được khôi phục.",
                            confirmButtonText: "OK"
                        }).then(() => {
                            location.reload();
                        });
                    } else {
                        Swal.fire({
                            icon: "error",
                            title: "Lỗi!",
                            text: "Có lỗi xảy ra, vui lòng thử lại!",
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
