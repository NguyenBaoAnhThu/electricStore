// Lưu trữ các file đã upload
let uploadedFiles = [];

document.getElementById('imageUpload').addEventListener('change', function(event) {
    const files = event.target.files;
    if (!files || files.length === 0) return;

    const imagePreview = document.getElementById('imagePreview');
    const uploadBox = document.getElementById('uploadBox');
    const mainImageBox = document.getElementById('mainImageBox');
    const maxImages = 9;

    // Kiểm tra số lượng ảnh hiện tại (trừ ô upload)
    const currentImages = document.querySelectorAll('.image-item:not(#uploadBox)').length;
    const remainingSlots = maxImages - currentImages;

    // Chỉ xử lý số ảnh còn lại được phép
    const filesToProcess = Array.from(files).slice(0, remainingSlots);

    filesToProcess.forEach((file, index) => {
        // Thêm file vào danh sách đã upload
        uploadedFiles.push(file);

        const reader = new FileReader();

        reader.onload = function(e) {
            // Nếu là ảnh đầu tiên và chưa có ảnh bìa
            if (mainImageBox.querySelector('img') === null) {
                mainImageBox.innerHTML = '';
                const img = document.createElement('img');
                img.src = e.target.result;

                const actions = document.createElement('div');
                actions.className = 'image-actions';

                const deleteBtn = document.createElement('button');
                deleteBtn.className = 'delete-btn';
                deleteBtn.innerHTML = '<i class="bi bi-x"></i>';
                deleteBtn.type = 'button'; // Để tránh submit form
                deleteBtn.onclick = function() {
                    // Xóa file khỏi danh sách
                    const fileIndex = uploadedFiles.indexOf(file);
                    if (fileIndex > -1) {
                        uploadedFiles.splice(fileIndex, 1);
                    }

                    // Xóa preview
                    mainImageBox.innerHTML = '';
                    mainImageBox.appendChild(createUploadLabel('Thêm ảnh bìa'));
                    updateHiddenInput();
                };

                actions.appendChild(deleteBtn);

                // Thêm label "Ảnh bìa"
                const mainLabel = document.createElement('div');
                mainLabel.className = 'main-label';

                mainImageBox.appendChild(img);
                mainImageBox.appendChild(actions);
                mainImageBox.appendChild(mainLabel);
            } else {
                // Tạo item mới cho các ảnh khác
                const newItem = document.createElement('div');
                newItem.className = 'image-item';

                const img = document.createElement('img');
                img.src = e.target.result;

                const actions = document.createElement('div');
                actions.className = 'image-actions';

                const deleteBtn = document.createElement('button');
                deleteBtn.className = 'delete-btn';
                deleteBtn.innerHTML = '<i class="bi bi-x"></i>';
                deleteBtn.type = 'button'; // Để tránh submit form
                deleteBtn.onclick = function() {
                    // Xóa file khỏi danh sách
                    const fileIndex = uploadedFiles.indexOf(file);
                    if (fileIndex > -1) {
                        uploadedFiles.splice(fileIndex, 1);
                    }

                    // Xóa preview
                    newItem.remove();
                    updateHiddenInput();

                    // Hiện ô upload nếu cần
                    if (document.querySelectorAll('.image-item').length <= maxImages) {
                        uploadBox.style.display = '';
                    }
                };

                actions.appendChild(deleteBtn);
                newItem.appendChild(img);
                newItem.appendChild(actions);

                // Chèn vào trước ô upload cuối cùng
                imagePreview.insertBefore(newItem, uploadBox);
            }

            updateHiddenInput();
        };

        reader.readAsDataURL(file);
    });

    // Reset input để có thể chọn lại cùng file
    this.value = '';

    // Ẩn ô upload nếu đã đạt giới hạn ảnh
    if (currentImages + filesToProcess.length >= maxImages) {
        uploadBox.style.display = 'none';
    }
});

// Helper function để tạo label upload
function createUploadLabel(text) {
    const label = document.createElement('label');
    label.className = 'image-upload';
    label.setAttribute('for', 'imageUpload');

    const icon = document.createElement('i');
    icon.className = 'bi bi-camera';

    const span = document.createElement('span');
    span.textContent = text;

    label.appendChild(icon);
    label.appendChild(span);

    return label;
}

// Cập nhật input ẩn để lưu thông tin file
function updateHiddenInput() {
    // Lưu số lượng ảnh đã chọn
    document.getElementById('selectedImages').value = uploadedFiles.length;
}

// Xử lý submit form - đảm bảo có ít nhất 1 ảnh
document.querySelector('form').addEventListener('submit', function(event) {
    if (uploadedFiles.length === 0) {
        event.preventDefault();
        Swal.fire({
            icon: 'error',
            title: 'Thiếu ảnh sản phẩm',
            text: 'Vui lòng tải lên ít nhất 1 ảnh sản phẩm',
            confirmButtonColor: '#ee4d2d'
        });
        return false;
    }
});

// Toggle sidebar
document.addEventListener('DOMContentLoaded', function() {
    const sidebarToggle = document.getElementById('sidebar-toggle');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', () => {
            document.body.classList.toggle('sidebar-collapsed');
        });
    }
});