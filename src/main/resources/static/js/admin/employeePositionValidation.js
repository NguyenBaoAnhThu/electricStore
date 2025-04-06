document.addEventListener('DOMContentLoaded', function() {
    // Lấy phần tử form
    const addPositionForm = document.getElementById('addPositionForm');
    const editPositionForm = document.getElementById('editPositionForm');

    // Thêm validation cho form thêm chức vụ
    if (addPositionForm) {
        setupPositionFormValidation(addPositionForm);
    }

    // Thêm validation cho form chỉnh sửa chức vụ
    if (editPositionForm) {
        setupPositionFormValidation(editPositionForm);
    }

    // Cấu hình modal chỉnh sửa chức vụ
    setupEditPositionModal();
});

/**
 * Thiết lập validation cho form chức vụ bằng AJAX
 * @param {HTMLFormElement} form - Form cần kiểm tra
 */
function setupPositionFormValidation(form) {
    // Lấy các ô nhập liệu
    const positionNameInput = form.querySelector('input[name="positionName"]');
    const positionDescriptionInput = form.querySelector('textarea[name="positionDescription"]');

    // Thiết lập sự kiện kiểm tra dữ liệu theo thời gian thực
    positionNameInput.addEventListener('blur', function() {
        validateField(this, validatePositionName);
    });

    positionDescriptionInput.addEventListener('blur', function() {
        validateField(this, validatePositionDescription);
    });

    // Kiểm tra dữ liệu khi gửi form
    form.addEventListener('submit', function(event) {
        // Ngăn chặn việc gửi form ngay lập tức
        event.preventDefault();

        // Kiểm tra tính hợp lệ của tất cả các ô nhập liệu
        const nameValid = validateField(positionNameInput, validatePositionName);
        const descriptionValid = validateField(positionDescriptionInput, validatePositionDescription);

        // Nếu tất cả đều hợp lệ, kiểm tra xem tên chức vụ đã tồn tại hay chưa (chỉ áp dụng cho form thêm)
        if (nameValid && descriptionValid) {
            if (form.id === 'addPositionForm') {
                checkPositionNameExists(positionNameInput.value, function(exists) {
                    if (exists) {
                        showError(positionNameInput, 'Tên chức vụ này đã tồn tại');
                    } else {
                        // Nếu hợp lệ, gửi dữ liệu bằng AJAX
                        submitPositionForm(form);
                    }
                });
            } else {
                // Với form chỉnh sửa, gửi dữ liệu bằng AJAX
                submitPositionForm(form);
            }
        }
    });
}

/**
 * Kiểm tra một ô nhập liệu
 * @param {HTMLInputElement} field - Ô nhập liệu cần kiểm tra
 * @param {Function} validationFunction - Hàm kiểm tra dữ liệu
 * @returns {boolean} Ô nhập liệu có hợp lệ hay không
 */
function validateField(field, validationFunction) {
    // Xóa thông báo lỗi cũ nếu có
    clearError(field);

    // Gọi hàm kiểm tra dữ liệu
    const result = validationFunction(field.value);

    // Nếu dữ liệu không hợp lệ, hiển thị lỗi
    if (!result.valid) {
        showError(field, result.message);
        return false;
    }

    return true;
}

/**
 * Kiểm tra tính hợp lệ của tên chức vụ
 * @param {string} name - Tên chức vụ cần kiểm tra
 * @returns {Object} Kết quả kiểm tra gồm trạng thái hợp lệ và thông báo lỗi (nếu có)
 */
function validatePositionName(name) {
    if (!name || name.trim() === '') {
        return {
            valid: false,
            message: 'Tên chức vụ không được để trống'
        };
    }

    if (name.length < 2 || name.length > 100) {
        return {
            valid: false,
            message: 'Tên chức vụ phải từ 2 đến 100 ký tự'
        };
    }

    return {
        valid: true
    };
}

/**
 * Kiểm tra tính hợp lệ của mô tả chức vụ
 * @param {string} description - Mô tả chức vụ cần kiểm tra
 * @returns {Object} Kết quả kiểm tra gồm trạng thái hợp lệ và thông báo lỗi (nếu có)
 */
function validatePositionDescription(description) {
    if (!description || description.trim() === '') {
        return {
            valid: false,
            message: 'Mô tả chức vụ không được để trống'
        };
    }

    if (description.length < 10 || description.length > 500) {
        return {
            valid: false,
            message: 'Mô tả chức vụ phải từ 10 đến 500 ký tự'
        };
    }

    return {
        valid: true
    };
}

/**
 * Hiển thị thông báo lỗi cho một ô nhập liệu
 * @param {HTMLInputElement} field - Ô nhập liệu có lỗi
 * @param {string} message - Nội dung thông báo lỗi
 */
function showError(field, message) {
    // Lấy hoặc tạo phần tử hiển thị lỗi
    let errorElement = field.nextElementSibling;
    if (!errorElement || !errorElement.classList.contains('error-message')) {
        errorElement = document.createElement('p');
        errorElement.className = 'error-message';
        errorElement.style.color = 'red';
        errorElement.style.fontSize = '14px';
        errorElement.style.marginTop = '5px';
        field.parentNode.insertBefore(errorElement, field.nextSibling);
    }

    // Hiển thị thông báo lỗi và thêm hiệu ứng lỗi cho ô nhập liệu
    errorElement.textContent = message;
    field.classList.add('is-invalid');
}

/**
 * Xóa thông báo lỗi của một ô nhập liệu
 * @param {HTMLInputElement} field - Ô nhập liệu cần xóa lỗi
 */
function clearError(field) {
    // Xóa phần tử hiển thị lỗi nếu có
    const errorElement = field.nextElementSibling;
    if (errorElement && errorElement.classList.contains('error-message')) {
        errorElement.remove();
    }

    // Loại bỏ hiệu ứng lỗi trên ô nhập liệu
    field.classList.remove('is-invalid');
}

/**
 * Kiểm tra xem tên chức vụ đã tồn tại hay chưa bằng AJAX
 * @param {string} positionName - Tên chức vụ cần kiểm tra
 * @param {Function} callback - Hàm callback trả về kết quả kiểm tra
 */
function checkPositionNameExists(positionName, callback) {
    // Tạo yêu cầu AJAX
    const xhr = new XMLHttpRequest();
    xhr.open('GET', '/Admin/employee-position/check-name?name=' + encodeURIComponent(positionName), true);

    xhr.onload = function() {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            callback(response.exists);
        } else {
            // Nếu có lỗi, mặc định tên không tồn tại để tránh ngăn chặn việc gửi form
            callback(false);
        }
    };

    xhr.onerror = function() {
        // Nếu có lỗi, mặc định tên không tồn tại để tránh ngăn chặn việc gửi form
        callback(false);
    };

    xhr.send();
}

/**
 * Gửi form thêm/chỉnh sửa chức vụ bằng AJAX
 * @param {HTMLFormElement} form - Form cần gửi
 */
function submitPositionForm(form) {
    // Tạo FormData từ form
    const formData = new FormData(form);

    // Tạo đối tượng dữ liệu để gửi dưới dạng JSON
    const positionData = {};
    formData.forEach((value, key) => {
        positionData[key] = value;
    });

    // Tạo yêu cầu AJAX
    const xhr = new XMLHttpRequest();
    const isEditForm = form.id === 'editPositionForm';
    const url = isEditForm ? '/Admin/employee-position/edit' : '/Admin/employee-position/create';
    xhr.open('POST', url, true);
    xhr.setRequestHeader('Content-Type', 'application/json');

    xhr.onload = function() {
        if (xhr.status === 200) {
            try {
                const response = JSON.parse(xhr.responseText);
                if (response.success) {
                    // Hiển thị thông báo thành công
                    showNotification('success', response.message);

                    // Đóng modal nếu đang mở
                    if (isEditForm) {
                        const modal = bootstrap.Modal.getInstance(document.getElementById('editPositionModal'));
                        if (modal) modal.hide();
                    } else {
                        const modal = bootstrap.Modal.getInstance(document.getElementById('addPositionModal'));
                        if (modal) modal.hide();
                    }

                    // Tải lại danh sách chức vụ
                    setTimeout(() => {
                        window.location.reload();
                    }, 1000);
                } else {
                    // Hiển thị thông báo lỗi
                    showNotification('error', response.message || 'Đã xảy ra lỗi khi lưu chức vụ');
                }
            } catch (e) {
                // Nếu kết quả không phải JSON, có thể server trả về trang HTML mới
                window.location.reload();
            }
        } else if (xhr.status === 400) {
            try {
                // Xử lý lỗi validation từ server
                const errors = JSON.parse(xhr.responseText);
                for (const field in errors) {
                    const input = form.querySelector(`[name="${field}"]`);
                    if (input) {
                        showError(input, errors[field]);
                    }
                }
            } catch (e) {
                showNotification('error', 'Dữ liệu không hợp lệ, vui lòng kiểm tra lại');
            }
        } else {
            showNotification('error', 'Đã xảy ra lỗi, vui lòng thử lại sau');
        }
    };

    xhr.onerror = function() {
        showNotification('error', 'Không thể kết nối đến máy chủ, vui lòng thử lại sau');
    };

    xhr.send(JSON.stringify(positionData));
}

/**
 * Hiển thị thông báo cho người dùng
 * @param {string} type - Loại thông báo (success/error)
 * @param {string} message - Nội dung thông báo
 */
function showNotification(type, message) {
    // Kiểm tra xem đã có phần tử thông báo chưa
    let notification = document.getElementById('notification');
    if (!notification) {
        // Tạo phần tử thông báo nếu chưa có
        notification = document.createElement('div');
        notification.id = 'notification';
        notification.style.position = 'fixed';
        notification.style.top = '20px';
        notification.style.right = '20px';
        notification.style.padding = '15px 20px';
        notification.style.borderRadius = '5px';
        notification.style.zIndex = '9999';
        notification.style.maxWidth = '300px';
        notification.style.boxShadow = '0 4px 8px rgba(0,0,0,0.2)';
        document.body.appendChild(notification);
    }

    // Thiết lập kiểu và nội dung thông báo
    if (type === 'success') {
        notification.style.backgroundColor = '#4CAF50';
        notification.style.color = 'white';
    } else {
        notification.style.backgroundColor = '#F44336';
        notification.style.color = 'white';
    }

    notification.textContent = message;

    // Hiển thị thông báo
    notification.style.display = 'block';

    // Tự động ẩn thông báo sau 3 giây
    setTimeout(function() {
        notification.style.display = 'none';
    }, 3000);
}