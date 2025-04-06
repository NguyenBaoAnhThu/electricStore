document.addEventListener('DOMContentLoaded', function() {
    // Lấy các phần tử form
    const addBrandForm = document.getElementById('addBrandForm');
    const editBrandForm = document.getElementById('editBrandForm');

    // Thêm validation cho form thêm thương hiệu
    if (addBrandForm) {
        setupFormValidation(addBrandForm);
    }

    // Thêm validation cho form chỉnh sửa thương hiệu
    if (editBrandForm) {
        setupFormValidation(editBrandForm);
    }

    // Cấu hình modal chỉnh sửa thương hiệu
    setupEditBrandModal();
});

/**
 * Thiết lập validation cho form bằng AJAX
 * @param {HTMLFormElement} form - Form cần kiểm tra
 */
function setupFormValidation(form) {
    // Lấy các ô nhập liệu
    const nameInput = form.querySelector('input[name="name"]');
    const countryInput = form.querySelector('input[name="country"]');

    // Thiết lập sự kiện kiểm tra dữ liệu theo thời gian thực
    nameInput.addEventListener('blur', function() {
        validateField(this, validateBrandName);
    });

    countryInput.addEventListener('blur', function() {
        validateField(this, validateCountry);
    });

    // Kiểm tra dữ liệu khi gửi form
    form.addEventListener('submit', function(event) {
        // Ngăn chặn việc gửi form ngay lập tức
        event.preventDefault();

        // Kiểm tra tính hợp lệ của tất cả các ô nhập liệu
        const nameValid = validateField(nameInput, validateBrandName);
        const countryValid = validateField(countryInput, validateCountry);

        // Nếu tất cả đều hợp lệ, kiểm tra xem tên thương hiệu đã tồn tại hay chưa (chỉ áp dụng cho form thêm)
        if (nameValid && countryValid) {
            if (form.id === 'addBrandForm') {
                checkBrandNameExists(nameInput.value, function(exists) {
                    if (exists) {
                        showError(nameInput, 'Tên thương hiệu này đã tồn tại');
                    } else {
                        form.submit(); // Nếu hợp lệ, gửi form
                    }
                });
            } else {
                form.submit(); // Với form chỉnh sửa, chỉ cần kiểm tra hợp lệ là có thể gửi form
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
 * Kiểm tra tính hợp lệ của tên thương hiệu
 * @param {string} name - Tên thương hiệu cần kiểm tra
 * @returns {Object} Kết quả kiểm tra gồm trạng thái hợp lệ và thông báo lỗi (nếu có)
 */
function validateBrandName(name) {
    if (!name || name.trim() === '') {
        return {
            valid: false,
            message: 'Tên thương hiệu không được để trống'
        };
    }

    if (name.length < 2 || name.length > 100) {
        return {
            valid: false,
            message: 'Tên thương hiệu phải từ 2 đến 100 ký tự'
        };
    }

    // Kiểm tra ký tự đặc biệt
    if (!/^[\p{L}0-9\s]+$/u.test(name)) {
        return {
            valid: false,
            message: 'Tên thương hiệu không được chứa ký tự đặc biệt'
        };
    }

    return {
        valid: true
    };
}

/**
 * Kiểm tra tính hợp lệ của tên quốc gia
 * @param {string} country - Tên quốc gia cần kiểm tra
 * @returns {Object} Kết quả kiểm tra gồm trạng thái hợp lệ và thông báo lỗi (nếu có)
 */
function validateCountry(country) {
    if (!country || country.trim() === '') {
        return {
            valid: false,
            message: 'Xuất xứ không được để trống'
        };
    }

    if (country.length > 100) {
        return {
            valid: false,
            message: 'Xuất xứ không được dài quá 100 ký tự'
        };
    }

    // Kiểm tra ký tự đặc biệt (chỉ cho chữ cái, số và khoảng trắng)
    if (!/^[a-zA-ZÀ-ỹ0-9\s]+$/.test(country)) {
        return {
            valid: false,
            message: 'Xuất xứ không được chứa ký tự đặc biệt'
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
 * Kiểm tra xem tên thương hiệu đã tồn tại hay chưa bằng AJAX
 * @param {string} brandName - Tên thương hiệu cần kiểm tra
 * @param {Function} callback - Hàm callback trả về kết quả kiểm tra
 */
function checkBrandNameExists(brandName, callback) {
    // Tạo yêu cầu AJAX
    const xhr = new XMLHttpRequest();
    xhr.open('GET', '/Admin/brand-manager/check-name?name=' + encodeURIComponent(brandName), true);

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
 * Thiết lập modal chỉnh sửa thương hiệu
 */
function setupEditBrandModal() {
    // Lấy tất cả các nút "Chỉnh sửa"
    const editButtons = document.querySelectorAll('.editBrandBtn');
    editButtons.forEach(button => {
        button.addEventListener('click', function() {
            // Lấy thông tin thương hiệu từ thuộc tính `data-`
            const brandId = this.getAttribute('data-id');
            const brandName = this.getAttribute('data-name');
            const brandCountry = this.getAttribute('data-country');

            // Gán giá trị vào form chỉnh sửa
            document.getElementById('editBrandId').value = brandId;
            document.getElementById('editBrandName').value = brandName;
            document.getElementById('editBrandCountry').value = brandCountry;

            // Hiển thị modal chỉnh sửa
            const modal = new bootstrap.Modal(document.getElementById('editBrandModal'));
            modal.show();
        });
    });
}
document.getElementById('editBrandModal').addEventListener('hidden.bs.modal', function () {
    const backdrops = document.querySelectorAll('.modal-backdrop');
    backdrops.forEach(backdrop => backdrop.remove());
    document.body.classList.remove('modal-open'); // Xóa class gây lỗi
});
