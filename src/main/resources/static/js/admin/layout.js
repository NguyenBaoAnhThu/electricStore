// Toggle sidebar
document.getElementById('sidebar-toggle').addEventListener('click', () => {
    document.body.classList.toggle('sidebar-collapsed');
});

// Toggle submenu
document.querySelectorAll('.treeview > a').forEach(item => {
    item.addEventListener('click', event => {
        event.preventDefault();
        const parent = item.parentElement;
        parent.classList.toggle('open');
    });
});
function confirmDelete() {
    return confirm('Bạn có chắc chắn muốn xóa không?');
}

document.addEventListener("DOMContentLoaded", function() {
    const userProfile = document.querySelector('.user-profile');
    const userDropdownToggle = document.getElementById("user-dropdown-toggle");
    const userDropdownMenu = document.getElementById("user-dropdown-menu");

    userDropdownToggle.addEventListener("click", function(event) {
        event.preventDefault();
        userDropdownMenu.classList.toggle("show");
        userProfile.classList.toggle("active");
    });

    document.addEventListener("click", function(event) {
        if (!userProfile.contains(event.target)) {
            userDropdownMenu.classList.remove("show");
            userProfile.classList.remove("active");
        }
    });

    // Sidebar Toggle
    const sidebarToggle = document.getElementById("sidebar-toggle");
    if (sidebarToggle) {
        sidebarToggle.addEventListener("click", function() {
            document.body.classList.toggle("sidebar-collapsed");
        });
    }
});
