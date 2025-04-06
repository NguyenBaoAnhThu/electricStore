// Đề xuất nội dung cho file header.js
document.addEventListener("DOMContentLoaded", function() {
    // Hàm đồng hồ
    function time() {
        var today = new Date();
        var weekday = ["Chủ Nhật", "Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy"];
        var day = weekday[today.getDay()];
        var dd = today.getDate();
        var mm = today.getMonth() + 1;
        var yyyy = today.getFullYear();
        var h = today.getHours();
        var m = today.getMinutes();
        var s = today.getSeconds();

        m = checkTime(m);
        s = checkTime(s);
        var nowTime = h + " giờ " + m + " phút " + s + " giây";

        if (dd < 10) dd = '0' + dd;
        if (mm < 10) mm = '0' + mm;

        var todayString = day + ', ' + dd + '/' + mm + '/' + yyyy;
        var tmp = '<span class="date">' + todayString + ' - ' + nowTime + '</span>';

        var clockElement = document.getElementById("clock");
        if (clockElement) {
            clockElement.innerHTML = tmp;
        }

        setTimeout(time, 1000);
    }

    function checkTime(i) {
        return i < 10 ? "0" + i : i;
    }

    // Bắt đầu đồng hồ
    time();
});