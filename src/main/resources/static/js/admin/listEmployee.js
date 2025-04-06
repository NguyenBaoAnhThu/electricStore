
$(document).ready(function () {

    $(".form-control").on("input", function () {
        $(this).removeClass("is-invalid");
        $("#" + this.id + "Error").text("");
    });

    $("#addEmployeeModal").on("click", function (e) {
        if ($(e.target).hasClass("modal")) {
            $("#addEmployeeModal").modal("hide");
        }
    });

    $('#addEmployeeModal').on('hide.bs.modal', function (e) {
        if ($(".is-invalid").length > 0) {
            e.preventDefault();
        }
    });
    $("#addEmployeeForm").submit(function (event) {
        event.preventDefault();

        let employeeData = {
            employeeName: $("#employeeName").val(),
            employeeBirthday: $("#employeeBirthday").val(),
            email: $("#email").val(),
            employeePhone: $("#employeePhone").val(),
            employeeAddress: $("#employeeAddress").val(),
            employeePosition: $("#employeePosition").val(),
            username: $("#username").val(),
            password: $("#password").val()
        };

        $.ajax({
            url: "/Admin/employee-manager/create",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(employeeData),
            success: function (response) {
                $("#addEmployeeModal").modal("hide");
                setTimeout(() => {
                    location.reload();
                }, 3000);
                $("#successfulNotification").html(`
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <span>Thêm nhân viên thành công!</span>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                `);

            },
            error: function (xhr) {
                $(".invalid-feedback").text("");
                $(".form-control").removeClass("is-invalid");

                let errors = xhr.responseJSON;

                if (errors) {
                    for (let field in errors) {
                        let errorMessage = errors[field];
                        let inputField = $("#" + field);
                        let errorField = $("#" + field + "Error");

                        if (inputField.length > 0 && errorField.length > 0) {
                            errorField.text(errorMessage);
                            inputField.addClass("is-invalid");
                        }
                    }
                } else {
                    alert("Đã có lỗi xảy ra, vui lòng thử lại!");
                }
            }

        });
    });
});

