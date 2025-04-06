document.addEventListener("DOMContentLoaded", function () {
    let selectAll = document.getElementById("selectAll");
    let checkboxes = document.querySelectorAll(".brandCheckbox");
    let deleteBtn = document.getElementById("deleteBtn");

    // Chọn tất cả
    selectAll.addEventListener("change", function () {
        checkboxes.forEach(checkbox => {
            checkbox.checked = this.checked;
        });
    });

});

document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".editBrandBtn").forEach(button => {
        button.addEventListener("click", function () {
            let brandId = this.getAttribute("data-id");
            let brandName = this.getAttribute("data-name");
            let brandCountry = this.getAttribute("data-country");

            document.getElementById("editBrandId").value = brandId;
            document.getElementById("editBrandName").value = brandName;
            document.getElementById("editBrandCountry").value = brandCountry;

            new bootstrap.Modal(document.getElementById("editBrandModal")).show();
        });
    });
});

function deleteSelectedBrand() {
    let selectedIds = [];
    document.querySelectorAll(".brandCheckbox:checked").forEach(checkbox => {
        selectedIds.push(checkbox.value);
    });

    if (selectedIds.length === 0) {
        Swal.fire({
            icon: "warning",
            title: "Chưa chọn thương hiệu!",
            text: "Vui lòng chọn ít nhất một thương hiệu trước khi xóa.",
            confirmButtonText: "OK"
        });
        return;
    }

    Swal.fire({
        title: "Bạn có chắc chắn?",
        text: "Những thương hiệu đã chọn sẽ bị xóa!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#d33",
        cancelButtonColor: "#3085d6",
        confirmButtonText: "Xóa",
        cancelButtonText: "Hủy"
    }).then((result) => {
        if (result.isConfirmed) {
            fetch("/Admin/brand-manager/delete", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(selectedIds)
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        Swal.fire({
                            icon: "success",
                            title: "Thành công!",
                            text: "Thương hiệu đã được xóa.",
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