use electricStore;
# Thêm dữ liệu vào Employee_Positions
INSERT INTO employee_positions (position_name, position_description)
VALUES ('Nhân Viên Kinh Doanh', 'Phụ trách tìm kiếm khách hàng và phát triển thị trường'),
       ('Nhân Viên Chăm sóc Khách Hàng', 'Hỗ trợ và giải quyết các vấn đề của khách hàng'),
       ('Nhân Viên Bán Hàng', 'Trực tiếp bán hàng tại cửa hàng'),
       ('Nhân Viên Thủ Kho', 'Quản lý nhập xuất kho và tồn kho'),
       ('Nhân Viên Kế Toán', 'Xử lý các vấn đề tài chính và kế toán');

# Thêm dữ liệu vào Roles
INSERT INTO role (role_name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_EMPLOYEE'),
       ('ROLE_BUSINESS'),
       ('ROLE_SALES'),
       ('ROLE_WAREHOUSE');

# Thêm dữ liệu vào Users (bổ sung thêm 6 người dùng mới)
INSERT INTO user (username, encryted_password, email, enabled, created_at, updated_at)
VALUES ('admin123', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'admin@gmail.com', true, NOW(), NOW()),
       ('hoang123', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'hoang12@gmail.com', true, NOW(), NOW()),
       ('vanhau123', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'vanhau12@gmail.com', true, NOW(), NOW()),
       ('tuantai345', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'tuantai12@gmail.com', true, NOW(), NOW()),
       ('thitrang05', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'trang12@gmail.com', true, NOW(), NOW()),
       ('hoaian678', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'hoaian123@gmail.com', true, NOW(), NOW()),
       ('khiem980', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'khiem12@gmail.com', true, NOW(), NOW()),
       ('nguyenduc123', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'duc12@gmail.com', true, NOW(), NOW()),
       ('phuongnha123', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'phuong12@gmail.com', true, NOW(), NOW()),
       ('vantuan', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'tuan56@gmail.com', true, NOW(), NOW()),
       ('thihuong456', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'huong456@gmail.com', true, NOW(), NOW()),
       ('quangminh789', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'minh789@gmail.com', true, NOW(), NOW()),
       ('thimai234', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'mai234@gmail.com', true, NOW(), NOW()),
       ('dinhtrung567', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'trung567@gmail.com', true, NOW(), NOW()),
       ('thithanh890', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'thanh890@gmail.com', true, NOW(), NOW()),
       ('vanhung123', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'hung123@gmail.com', true, NOW(), NOW());

# Gán Roles cho Users
INSERT INTO user_role (user_id, role_id)
VALUES (1, 1),  -- admin123 có role ADMIN
       (2, 2),  -- hoang123 có role EMPLOYEE
       (3, 2),  -- vanhau123 có role EMPLOYEE
       (4, 3),  -- tuantai345 có role BUSINESS
       (5, 3),  -- thitrang05 có role BUSINESS
       (6, 4),  -- hoaian678 có role SALES
       (7, 4),  -- khiem980 có role SALES
       (8, 5),  -- nguyenduc123 có role WAREHOUSE
       (9, 5),  -- phuongnha123 có role WAREHOUSE
       (10, 2), -- vantuan có role EMPLOYEE
       (11, 2), -- thihuong456 có role EMPLOYEE
       (12, 4), -- quangminh789 có role SALES
       (13, 2), -- thimai234 có role EMPLOYEE
       (14, 5), -- dinhtrung567 có role WAREHOUSE
       (15, 3), -- thithanh890 có role BUSINESS
       (16, 2); -- vanhung123 có role EMPLOYEE

# Thêm dữ liệu vào Employees (15 nhân viên được gán user_id)
INSERT INTO employees (employee_name, employee_birthday, employee_address, employee_phone, employee_work, position_id, is_disabled, user_id)
VALUES
    ('Nguyễn Hoàng', '2000-12-12', 'Hà Nội', '0933371781', 'Full-time', 1, false, 2),
    ('Trương Văn Hậu', '2001-09-12', 'Hà Nội', '0955571781', 'Full-time', 2, false, 3),
    ('Nguyễn Tuấn Tài', '1998-12-12', 'Hà Nội', '0944441781', 'Full-time', 3, false, 4),
    ('Ngô Thị Trang', '2002-02-12', 'Hà Nội', '0955555781', 'Part-time', 5, false, 5),
    ('Lê Hoài An', '1995-05-15', 'Hồ Chí Minh', '0912345678', 'Full-time', 1, false, 6),
    ('Trần Minh Khiêm', '1999-08-23', 'Đà Nẵng', '0987654321', 'Full-time', 3, false, 7),
    ('Nguyễn Đức', '1997-11-30', 'Hải Phòng', '0977123456', 'Full-time', 4, false, 8),
    ('Lê Phương Nha', '2000-04-17', 'Cần Thơ', '0966789012', 'Part-time', 2, false, 9),
    ('Nguyễn Văn Tuấn', '1996-07-22', 'Huế', '0944567890', 'Full-time', 5, false, 10),
    ('Phạm Thị Hương', '1993-10-05', 'Nha Trang', '0922123456', 'Full-time', 1, false, 11),
    ('Đỗ Quang Minh', '1998-01-20', 'Vũng Tàu', '0911234567', 'Part-time', 3, false, 12),
    ('Hoàng Thị Mai', '1994-06-28', 'Quảng Ninh', '0933456789', 'Full-time', 2, false, 13),
    ('Vũ Đình Trung', '1992-03-14', 'Hà Nội', '0955678901', 'Full-time', 4, false, 14),
    ('Trần Thị Thanh', '1997-09-08', 'Đà Nẵng', '0966890123', 'Part-time', 5, false, 15),
    ('Nguyễn Văn Hùng', '1991-12-25', 'Hồ Chí Minh', '0988901234', 'Full-time', 1, false, 16);

# Thêm dữ liệu vào customer
INSERT INTO customers (customer_name, phone_number, address, email, birth_date, is_disabled)
VALUES
    ('Trần Hoài An', '0981828128', 'Hà Nội', 'hoaian@gmail.com', '2002-06-12', false),
    ('Tuấn Khiêm', '0971218291', 'Đà Nẵng', 'tuankhiem@gmail.com', '2002-06-12', false),
    ('Nguyễn Văn Đức', '0912118128', 'Huế', 'vanduc@gmail.com', '2002-06-14', false),
    ('Phương Nha', '0989129112', 'Hà Nội', 'phuongnha@gmail.com', '2001-06-12', false),
    ('Trần Văn Tuấn', '0912991991', 'Hải Phòng', 'vantuan@gmail.com', '2002-12-12', false),
    ('Nguyễn Thị Hoa', '0912345678', 'Hồ Chí Minh', 'hoa.nguyen@gmail.com', '1995-04-15', false),
    ('Lê Văn Minh', '0923456789', 'Cần Thơ', 'minhle@gmail.com', '1990-08-21', false),
    ('Trần Thị Lan', '0934567890', 'Đà Lạt', 'lantran@gmail.com', '1988-11-30', false),
    ('Phạm Quốc Bảo', '0945678901', 'Nha Trang', 'baopham@gmail.com', '1993-02-25', false),
    ('Vũ Thị Thu', '0956789012', 'Vũng Tàu', 'thuvu@gmail.com', '1997-07-10', false),
    ('Đỗ Văn Hùng', '0967890123', 'Quảng Ninh', 'hungdo@gmail.com', '1985-09-18', false),
    ('Nguyễn Thành Nam', '0978901234', 'Bình Dương', 'namnguyen@gmail.com', '1992-12-05', false),
    ('Mai Thị Linh', '0989012345', 'Hà Nội', 'linhmai@gmail.com', '1998-03-22', false),
    ('Hoàng Văn Thắng', '0990123456', 'Hải Dương', 'thanghoang@gmail.com', '1994-05-17', false),
    ('Trần Thị Thảo', '0901234567', 'Thanh Hóa', 'thaotran@gmail.com', '1996-10-08', false);

INSERT INTO admins (admin_name, department, user_id)
VALUES ('Nguyễn Quản Trị', 'IT', 1);


# Thêm dữ liệu vào categories (chỉ giữ 3 danh mục)
INSERT INTO categories (name, description, create_at, update_at)
VALUES ('Điện thoại', 'Các dòng điện thoại thông minh mới nhất', NOW(), NOW()),
       ('Máy tính bảng', 'Máy tính bảng phục vụ công việc và giải trí', NOW(), NOW()),
       ('Laptop', 'Laptop dành cho học tập, làm việc, gaming', NOW(), NOW());

# Thêm dữ liệu vào brand (giữ nguyên)
INSERT INTO brands (name, status, country, create_at, update_at)
VALUES ('Apple', true, 'Mỹ', NOW(), NOW()),
       ('Samsung', true, 'Hàn Quốc', NOW(), NOW()),
       ('Sony', true, 'Nhật Bản', NOW(), NOW()),
       ('Asus', true, 'Đài Loan', NOW(), NOW()),
       ('Dell', true, 'Mỹ', NOW(), NOW()),
       ('Xiaomi', true, 'Trung Quốc', NOW(), NOW()),
       ('Huawei', true, 'Trung Quốc', NOW(), NOW()),
       ('LG', true, 'Hàn Quốc', NOW(), NOW()),
       ('Oppo', true, 'Trung Quốc', NOW(), NOW()),
       ('Lenovo', true, 'Trung Quốc', NOW(), NOW());

# Thêm dữ liệu vào supplier (giữ nguyên)
INSERT INTO suppliers (supplier_code, name, address, phone, email)
VALUES ('AP001', 'Apple Store', 'California, USA', '0981921280', 'apple@gmail.com'),
       ('SS001', 'Samsung Store', 'Seoul, Korea', '0911921281', 'samsung@gmail.com'),
       ('OP001', 'Oppo Store', 'Guangdong, China', '0981921182', 'oppo@gmail.com'),
       ('XM001', 'Xiaomi Store', 'Beijing, China', '0001921283', 'xiaomi@gmail.com'),
       ('SN001', 'Sony Distributor', 'Tokyo, Japan', '0981921284', 'sony@gmail.com'),
       ('AS001', 'Asus Vietnam', 'Hanoi, Vietnam', '0981921285', 'asus@gmail.com'),
       ('DL001', 'Dell Vietnam', 'HCMC, Vietnam', '0981921286', 'dell@gmail.com'),
       ('HW001', 'Huawei Store', 'Shenzhen, China', '0981921287', 'huawei@gmail.com'),
       ('LG001', 'LG Electronics', 'Seoul, Korea', '0981921288', 'lg@gmail.com'),
       ('LN001', 'Lenovo Shop', 'Shanghai, China', '0981921289', 'lenovo@gmail.com');

# Thêm dữ liệu vào products (sửa lại mã sản phẩm theo SP0001, SP0002, ...)
INSERT INTO products (create_at, description, main_image_url, name, price, stock, update_at, brand_id, category_id, supplier_id, product_code)
VALUES
-- Smartphones (Danh mục 1: Điện thoại)
(NOW(), 'Xiaomi Redmi Note 12 Pro - Hiệu năng ổn định', 'https://byvn.net/2qP6', 'Xiaomi Redmi Note 12 Pro', 8990000, 100, NOW(), 6, 1, 4, 'SP0001'),
(NOW(), 'Realme GT Neo 5 - Sạc siêu nhanh', 'https://byvn.net/kvZe', 'Realme GT Neo 5', 10990000, 80, NOW(), 9, 1, 9, 'SP0002'),
(NOW(), 'Oppo Find X5 Pro - Thiết kế sang trọng', 'https://byvn.net/XHnW', 'Oppo Find X5 Pro', 19990000, 50, NOW(), 9, 1, 3, 'SP0003'),
(NOW(), 'Vivo X90 Pro - Camera chuyên nghiệp', 'https://byvn.net/3hgh', 'Vivo X90 Pro', 22990000, 60, NOW(), 1, 1, 1, 'SP0004'),
(NOW(), 'Honor Magic5 Pro - Công nghệ cao cấp', 'https://byvn.net/rhiq', 'Honor Magic5 Pro', 20990000, 45, NOW(), 1, 1, 1, 'SP0005'),
(NOW(), 'Samsung Galaxy S23 Ultra - Flagship hàng đầu', 'https://byvn.net/s23u', 'Samsung Galaxy S23 Ultra', 29990000, 40, NOW(), 2, 1, 2, 'SP0006'),
(NOW(), 'iPhone 15 Pro Max - Đỉnh cao công nghệ', 'https://byvn.net/ip15pm', 'iPhone 15 Pro Max', 33990000, 55, NOW(), 1, 1, 1, 'SP0007'),
(NOW(), 'Xiaomi 14 Ultra - Camera đỉnh cao', 'https://byvn.net/xm14u', 'Xiaomi 14 Ultra', 25990000, 35, NOW(), 6, 1, 4, 'SP0008'),

-- Tablets (Danh mục 2: Máy tính bảng)
(NOW(), 'Huawei MatePad Pro 11 - Màn hình sắc nét', 'https://byvn.net/U9pt', 'Huawei MatePad Pro 11', 16990000, 70, NOW(), 7, 2, 8, 'SP0009'),
(NOW(), 'Xiaomi Pad 6 Pro - Hiệu năng mạnh mẽ', 'https://byvn.net/RX3m', 'Xiaomi Pad 6 Pro', 14990000, 65, NOW(), 6, 2, 4, 'SP0010'),
(NOW(), 'Realme Pad 2 - Giải trí đa năng', 'https://byvn.net/E4HL', 'Realme Pad 2', 8990000, 90, NOW(), 9, 2, 3, 'SP0011'),
(NOW(), 'iPad Pro M2 12.9 - Hiệu suất vượt trội', 'https://byvn.net/ipadm2', 'iPad Pro M2 12.9', 31990000, 40, NOW(), 1, 2, 1, 'SP0012'),
(NOW(), 'Samsung Galaxy Tab S9 Ultra - Màn hình lớn', 'https://byvn.net/tabs9u', 'Samsung Galaxy Tab S9 Ultra', 26990000, 30, NOW(), 2, 2, 2, 'SP0013'),
(NOW(), 'Lenovo Tab P12 Pro - Giải trí đỉnh cao', 'https://byvn.net/lenovop12', 'Lenovo Tab P12 Pro', 17990000, 25, NOW(), 10, 2, 10, 'SP0014'),

-- Laptops (Danh mục 3: Laptop)
(NOW(), 'HP Spectre x360 - Laptop 2 trong 1', 'https://byvn.net/KNjw', 'HP Spectre x360', 39990000, 40, NOW(), 1, 3, 1, 'SP0015'),
(NOW(), 'Acer Predator Helios 300 - Gaming', 'https://byvn.net/9cmk', 'Acer Predator Helios 300', 35990000, 50, NOW(), 1, 3, 1, 'SP0016'),
(NOW(), 'Lenovo ThinkPad X1 Carbon - Doanh nhân', 'https://byvn.net/6Mva', 'Lenovo ThinkPad X1 Carbon', 45990000, 35, NOW(), 10, 3, 10, 'SP0017'),
(NOW(), 'Microsoft Surface Laptop 5 - Sang trọng', 'https://byvn.net/Ga38', 'Microsoft Surface Laptop 5', 37990000, 45, NOW(), 1, 3, 1, 'SP0018'),
(NOW(), 'MacBook Pro 16 M3 Max - Mạnh mẽ', 'https://byvn.net/mbpm3', 'MacBook Pro 16 M3 Max', 89990000, 20, NOW(), 1, 3, 1, 'SP0019'),
(NOW(), 'Dell XPS 15 - Thiết kế cao cấp', 'https://byvn.net/dellxps15', 'Dell XPS 15', 55990000, 30, NOW(), 5, 3, 7, 'SP0020'),
(NOW(), 'Asus ROG Zephyrus G16 - Gaming cao cấp', 'https://byvn.net/asusrog', 'Asus ROG Zephyrus G16', 49990000, 25, NOW(), 4, 3, 6, 'SP0021'),
(NOW(), 'MSI Creator Z16 - Dành cho sáng tạo', 'https://byvn.net/msicre', 'MSI Creator Z16', 51990000, 15, NOW(), 1, 3, 1, 'SP0022');

# Thêm dữ liệu vào ProductDetail với cấu trúc mới
INSERT INTO product_details (screen_size, camera, front_camera, color, description, cpu, gpu, ram, rom, os, os_version, battery, screen_type, screen_resolution, ports, weight, create_at, update_at, product_id)
VALUES
-- Điện thoại
(6.67, 108, 16, 'Xanh', 'Màn hình AMOLED 120Hz, camera AI', 'MediaTek Dimensity 1080', 'Mali-G68 MC4', '8GB', '256GB', 'Android', '12 (MIUI 14)', '5000mAh', 'AMOLED', '2400x1080', 'USB Type-C, 3.5mm jack', 187, NOW(), NOW(), 1),
(6.74, 50, 16, 'Đen', 'Sạc siêu nhanh 240W, màn hình 144Hz', 'Snapdragon 8+ Gen 1', 'Adreno 730', '12GB', '256GB', 'Android', '13 (Realme UI 4.0)', '4600mAh', 'AMOLED', '2772x1240', 'USB Type-C', 199, NOW(), NOW(), 2),
(6.7, 50, 32, 'Trắng', 'Camera Hasselblad, màn hình LTPO', 'Snapdragon 8 Gen 1', 'Adreno 730', '12GB', '256GB', 'Android', '12 (ColorOS 12.1)', '5000mAh', 'AMOLED', '3216x1440', 'USB Type-C', 218, NOW(), NOW(), 3),
(6.78, 50, 32, 'Xám', 'Camera Zeiss, cảm biến 1-inch', 'MediaTek Dimensity 9200', 'Immortalis-G715 MC11', '12GB', '256GB', 'Android', '13 (Funtouch OS 13)', '4870mAh', 'AMOLED', '2800x1260', 'USB Type-C', 215, NOW(), NOW(), 4),
(6.81, 50, 12, 'Xanh dương', 'Camera 200MP, màn hình LTPO', 'Snapdragon 8 Gen 2', 'Adreno 740', '12GB', '512GB', 'Android', '13 (Magic UI 7.0)', '5100mAh', 'OLED', '2848x1312', 'USB Type-C', 219, NOW(), NOW(), 5),
(6.8, 200, 12, 'Đen Phantom', 'Bút S-Pen, zoom quang 10x', 'Snapdragon 8 Gen 2', 'Adreno 740', '12GB', '512GB', 'Android', '13 (One UI 5.1)', '5000mAh', 'Dynamic AMOLED 2X', '3088x1440', 'USB Type-C', 234, NOW(), NOW(), 6),
(6.7, 48, 12, 'Titan Tự nhiên', 'Khung titan, Dynamic Island', 'A17 Pro', 'Apple GPU (6-core)', '8GB', '512GB', 'iOS', '17', '4422mAh', 'Super Retina XDR OLED', '2796x1290', 'USB Type-C', 221, NOW(), NOW(), 7),
(6.73, 50, 32, 'Đen', 'Camera Leica, chụp đêm tốt', 'Snapdragon 8 Gen 2', 'Adreno 740', '16GB', '512GB', 'Android', '14 (MIUI 15)', '5000mAh', 'AMOLED', '3200x1440', 'USB Type-C', 227, NOW(), NOW(), 8),

-- Máy tính bảng
(11.0, 13, 8, 'Xám', 'Hỗ trợ bút M-Pencil', 'Snapdragon 888', 'Adreno 660', '8GB', '256GB', 'HarmonyOS', '3.0', '8300mAh', 'OLED', '2560x1600', 'USB Type-C', 449, NOW(), NOW(), 9),
(11.2, 50, 20, 'Bạc', 'Hỗ trợ bút, tần số quét 144Hz', 'Snapdragon 8+ Gen 1', 'Adreno 730', '8GB', '256GB', 'Android', '13 (MIUI 14)', '8600mAh', 'IPS LCD', '2880x1800', 'USB Type-C', 490, NOW(), NOW(), 10),
(11.5, 8, 5, 'Xanh lá', 'Màn hình lớn, pin khỏe', 'MediaTek Helio G99', 'Mali-G57 MC2', '8GB', '256GB', 'Android', '13 (Realme UI 4.0)', '8360mAh', 'IPS LCD', '2000x1200', 'USB Type-C, 3.5mm jack', 538, NOW(), NOW(), 11),
(12.9, 12, 12, 'Space Gray', 'Chip M2, màn hình mini-LED', 'Apple M2', 'Apple M2 GPU (10-core)', '16GB', '1TB', 'iPadOS', '17', '10758mAh', 'Liquid Retina XDR', '2732x2048', 'USB Type-C', 682, NOW(), NOW(), 12),
(14.6, 13, 12, 'Graphite', 'Màn hình lớn nhất, S-Pen', 'Snapdragon 8 Gen 2', 'Adreno 740', '12GB', '512GB', 'Android', '13 (One UI 5.1)', '11200mAh', 'Dynamic AMOLED 2X', '2960x1848', 'USB Type-C', 732, NOW(), NOW(), 13),
(12.6, 13, 8, 'Storm Gray', 'Màn hình AMOLED rộng', 'Snapdragon 8 Gen 1', 'Adreno 730', '8GB', '256GB', 'Android', '13', '10200mAh', 'AMOLED', '2560x1600', 'USB Type-C, 3.5mm jack', 565, NOW(), NOW(), 14),

-- Laptop
(13.5, NULL, NULL, 'Xanh Navy', 'Laptop xoay gập 360 độ', 'Intel Core i7-1355U', 'Intel Iris Xe', '16GB', '1TB SSD', 'Windows', '11 Home', '66Wh', 'OLED Touch', '3000x2000', '2x Thunderbolt 4, USB-A, 3.5mm jack', 1360, NOW(), NOW(), 15),
(15.6, NULL, NULL, 'Đen', 'Laptop gaming hiệu năng cao', 'Intel Core i7-12700H', 'NVIDIA RTX 3070 Ti 8GB', '16GB', '1TB SSD', 'Windows', '11 Home', '90Wh', 'IPS, 165Hz', '2560x1440', '3x USB-A, USB-C, HDMI, Ethernet, 3.5mm jack', 2400, NOW(), NOW(), 16),
(14.0, NULL, NULL, 'Đen', 'Laptop doanh nhân cao cấp', 'Intel Core i7-1360P', 'Intel Iris Xe', '16GB', '1TB SSD', 'Windows', '11 Pro', '57Wh', 'IPS', '2880x1800', '2x Thunderbolt 4, USB-A, HDMI, 3.5mm jack', 1120, NOW(), NOW(), 17),
(13.5, NULL, NULL, 'Bạch kim', 'Thiết kế sang trọng, pin dài', 'Intel Core i7-1255U', 'Intel Iris Xe', '16GB', '512GB SSD', 'Windows', '11 Home', '47.4Wh', 'PixelSense Touch', '2256x1504', 'USB-A, Thunderbolt 4, 3.5mm jack', 1270, NOW(), NOW(), 18),
(16.2, NULL, NULL, 'Space Black', 'Hiệu năng mạnh mẽ, màn hình mini-LED', 'Apple M3 Max (16-core)', 'Apple M3 Max (40-core)', '64GB', '2TB SSD', 'macOS', 'Sonoma', '100Wh', 'Liquid Retina XDR, 120Hz', '3456x2234', '3x Thunderbolt 4, HDMI, SD card, 3.5mm jack', 2170, NOW(), NOW(), 19),
(15.6, NULL, NULL, 'Platinum', 'Thiết kế mỏng nhẹ, màn hình OLED', 'Intel Core i9-13900H', 'NVIDIA RTX 4070 8GB', '32GB', '1TB SSD', 'Windows', '11 Pro', '86Wh', 'OLED', '3456x2160', '2x Thunderbolt 4, USB-A, HDMI, SD card, 3.5mm jack', 1920, NOW(), NOW(), 20),
(16.0, NULL, NULL, 'Off Black', 'Laptop gaming cao cấp', 'Intel Core i9-13900H', 'NVIDIA RTX 4080 12GB', '32GB', '2TB SSD', 'Windows', '11 Pro', '90Wh', 'ROG Nebula Display, 240Hz', '2560x1600', '2x USB-C, 2x USB-A, HDMI, LAN, 3.5mm jack', 2100, NOW(), NOW(), 21),
(16.0, NULL, NULL, 'Xám Core Black', 'Laptop cho nhà sáng tạo', 'Intel Core i7-12700H', 'NVIDIA RTX 3070 8GB', '32GB', '1TB SSD', 'Windows', '11 Pro', '90Wh', 'Touch', '2560x1600', '2x Thunderbolt 4, USB-A, HDMI, SD card, 3.5mm jack', 2350, NOW(), NOW(), 22);

# Thêm dữ liệu vào ware_house với giá nhập phù hợp (thấp hơn giá bán khoảng 20-30%)
INSERT INTO ware_house (import_date, price, product_id) VALUES
                                                            ('2023-11-15', 6990000, 1),  -- Xiaomi Redmi Note 12 Pro (giá bán: 8990000)
                                                            ('2023-11-20', 8490000, 2),  -- Realme GT Neo 5 (giá bán: 10990000)
                                                            ('2023-12-05', 15990000, 3), -- Oppo Find X5 Pro (giá bán: 19990000)
                                                            ('2023-12-10', 17990000, 4), -- Vivo X90 Pro (giá bán: 22990000)
                                                            ('2023-12-15', 16990000, 5), -- Honor Magic5 Pro (giá bán: 20990000)
                                                            ('2024-01-05', 23990000, 6), -- Samsung Galaxy S23 Ultra (giá bán: 29990000)
                                                            ('2024-01-10', 27990000, 7), -- iPhone 15 Pro Max (giá bán: 33990000)
                                                            ('2024-01-20', 20990000, 8), -- Xiaomi 14 Ultra (giá bán: 25990000)
                                                            ('2024-02-01', 13490000, 9), -- Huawei MatePad Pro 11 (giá bán: 16990000)
                                                            ('2024-02-10', 11990000, 10), -- Xiaomi Pad 6 Pro (giá bán: 14990000)
                                                            ('2024-02-15', 6990000, 11),  -- Realme Pad 2 (giá bán: 8990000)
                                                            ('2024-02-25', 26990000, 12), -- iPad Pro M2 12.9 (giá bán: 31990000)
                                                            ('2024-03-01', 21990000, 13), -- Samsung Galaxy Tab S9 Ultra (giá bán: 26990000)
                                                            ('2024-03-05', 14490000, 14), -- Lenovo Tab P12 Pro (giá bán: 17990000)
                                                            ('2024-03-10', 32990000, 15), -- HP Spectre x360 (giá bán: 39990000)
                                                            ('2024-03-15', 29990000, 16), -- Acer Predator Helios 300 (giá bán: 35990000)
                                                            ('2024-03-20', 38990000, 17), -- Lenovo ThinkPad X1 Carbon (giá bán: 45990000)
                                                            ('2024-03-25', 31990000, 18), -- Microsoft Surface Laptop 5 (giá bán: 37990000)
                                                            ('2024-04-01', 75990000, 19), -- MacBook Pro 16 M3 Max (giá bán: 89990000)
                                                            ('2024-04-05', 46990000, 20), -- Dell XPS 15 (giá bán: 55990000)
                                                            ('2024-04-10', 41990000, 21), -- Asus ROG Zephyrus G16 (giá bán: 49990000)
                                                            ('2024-04-15', 43990000, 22); -- MSI Creator Z16 (giá bán: 51990000)
insert into customers (address, birth_date, customer_name, is_disabled, phone_number)
values ('Ha Noi', '2002-06-12', 'Tran Hoai An', true, '0981828128'),
       ('Da Nang', '2002-06-12', 'Tuan Khiem', true, '0971218291'),
       ('Hue', '2002-06-14', 'Nguyen Van Duc', true, '0912118128'),
       ('Ha Noi', '2001-06-12', 'Phuong Nha', true, '0989129112'),
       ('Hai Phong', '2002-12-12', 'Tran Van Tuan', true, '0912991991'),
       ('Bac Ninh', '2000-06-12', 'Huynh Chung', true, '0981812791'),
       ('Ha Noi', '2002-06-07', 'Van Nam', true, '0398591028'),
       ('Can Tho', '1992-06-14', 'Phan Thanh Trung', true, '0912112312'),
       ('Ca Mau', '1993-06-12', 'Ho Thi Uyen', true, '0912819112'),
       ('Da Nang', '2000-12-02', 'Tran Dang Khoa', true, '0989890012'),
       ('Quang Binh', '2000-07-03', 'Le Trong Si', true, '0900081288'),
       ('Ha Noi', '2002-06-07', 'Nguyen Thanh Tung', true, '0390001821'),
       ('Nam Dinh', '1995-03-15', 'Le Thanh Long', true, '0987654321'),
       ('Hai Duong', '1988-11-22', 'Nguyen Thi Mai', true, '0912345678'),
       ('Vinh Phuc', '2000-07-30', 'Tran Duc Manh', true, '0976543210'),
       ('Thai Binh', '1992-05-18', 'Pham Van Cuong', true, '0945678912'),
       ('Ninh Binh', '1997-12-05', 'Hoang Thi Lan', true, '0934567890'),
       ('Ha Nam', '1990-09-25', 'Vu Van Tai', true, '0956789012'),
       ('Bac Ninh', '2002-01-10', 'Do Manh Hung', true, '0967890123'),
       ('Hung Yen', '1993-06-20', 'Mai Van Khanh', true, '0978901234'),
       ('Hai Phong', '1998-04-12', 'Nguyen Van Thang', true, '0989012345'),
       ('Lang Son', '1991-08-07', 'Tran Thi Huong', true, '0990123456');



INSERT INTO order_products (create_at, payment_status, status, total_price, customer_id)
VALUES
-- Đơn hàng của khách hàng 1
('2024-01-10', 'PENDING', 'DELIVERED', 80000, 1),
('2024-02-15', 'COMPLETED', 'DELIVERED', 120000, 1),
('2024-03-05', 'PENDING', 'DELIVERED', 45000, 1),
('2024-04-12', 'FAILED', 'CANCELLED', 60000, 1),
('2024-05-20', 'COMPLETED', 'DELIVERED', 95000, 1),

-- Đơn hàng của khách hàng 2
('2024-01-18', 'PENDING', 'DELIVERED', 70000, 2),
('2024-02-22', 'COMPLETED', 'DELIVERED', 110000, 2),
('2024-03-14', 'PENDING', 'DELIVERED', 40000, 2),
('2024-04-30', 'FAILED', 'CANCELLED', 65000, 2),
('2024-06-05', 'COMPLETED', 'DELIVERED', 99000, 2),

-- Đơn hàng của khách hàng 3
('2024-01-05', 'PENDING', 'DELIVERED', 75000, 3),
('2024-02-27', 'COMPLETED', 'DELIVERED', 125000, 3),
('2024-03-10', 'PENDING', 'DELIVERED', 42000, 3),
('2024-05-07', 'FAILED', 'CANCELLED', 59000, 3),
('2024-07-12', 'COMPLETED', 'DELIVERED', 97000, 3),

-- Đơn hàng của khách hàng 4
('2024-02-22', 'PENDING', 'DELIVERED', 77000, 4),
('2024-03-12', 'COMPLETED', 'DELIVERED', 118000, 4),
('2024-04-25', 'PENDING', 'DELIVERED', 46000, 4),
('2024-06-08', 'FAILED', 'CANCELLED', 62000, 4),
('2024-08-18', 'COMPLETED', 'DELIVERED', 98000, 4),

-- Đơn hàng của khách hàng 5
('2024-01-30', 'PENDING', 'DELIVERED', 81000, 5),
('2024-04-01', 'COMPLETED', 'DELIVERED', 130000, 5),
('2024-06-19', 'PENDING', 'DELIVERED', 48000, 5),
('2024-09-27', 'FAILED', 'CANCELLED', 63000, 5),
('2024-11-10', 'COMPLETED', 'DELIVERED', 96000, 5),

-- Đơn hàng của khách hàng 6
('2024-02-15', 'PENDING', 'DELIVERED', 70000, 6),
('2024-05-22', 'COMPLETED', 'DELIVERED', 115000, 6),
('2024-07-14', 'PENDING', 'DELIVERED', 41000, 6),
('2024-10-30', 'FAILED', 'CANCELLED', 62000, 6),
('2025-01-05', 'COMPLETED', 'DELIVERED', 100000, 6),

--
('2023-01-15', 'PENDING', 'DELIVERED', 85000, 13),
('2023-02-20', 'COMPLETED', 'DELIVERED', 130000, 13),
('2023-03-10', 'PENDING', 'DELIVERED', 47000, 13),
('2023-04-25', 'FAILED', 'CANCELLED', 62000, 13),
('2023-05-30', 'COMPLETED', 'DELIVERED', 99000, 13),

('2023-02-05', 'PENDING', 'DELIVERED', 75000, 14),
('2023-03-12', 'COMPLETED', 'DELIVERED', 115000, 14),
('2023-04-18', 'PENDING', 'DELIVERED', 43000, 14),
('2023-05-22', 'FAILED', 'CANCELLED', 58000, 14),
('2023-06-28', 'COMPLETED', 'DELIVERED', 92000, 14),

('2022-12-10', 'PENDING', 'DELIVERED', 80000, 15),
('2023-01-25', 'COMPLETED', 'DELIVERED', 125000, 15),
('2023-02-15', 'PENDING', 'DELIVERED', 45000, 15),
('2023-03-30', 'FAILED', 'CANCELLED', 60000, 15),
('2023-04-22', 'COMPLETED', 'DELIVERED', 96000, 15),

('2022-11-20', 'PENDING', 'DELIVERED', 78000, 16),
('2023-01-05', 'COMPLETED', 'DELIVERED', 120000, 16),
('2023-02-28', 'PENDING', 'DELIVERED', 44000, 16),
('2023-04-15', 'FAILED', 'CANCELLED', 59000, 16),
('2023-05-18', 'COMPLETED', 'DELIVERED', 93000, 16),

('2022-10-12', 'PENDING', 'DELIVERED', 82000, 17),
('2023-01-30', 'COMPLETED', 'DELIVERED', 135000, 17),
('2023-03-05', 'PENDING', 'DELIVERED', 46000, 17),
('2023-04-20', 'FAILED', 'CANCELLED', 63000, 17),
('2023-06-10', 'COMPLETED', 'DELIVERED', 97000, 17),

('2022-01-05', 'PENDING', 'DELIVERED', 72000, 1),
('2022-01-15', 'COMPLETED', 'DELIVERED', 95000, 1),
('2022-02-10', 'PENDING', 'DELIVERED', 48000, 1),
('2022-03-20', 'FAILED', 'CANCELLED', 65000, 1),
('2022-04-12', 'COMPLETED', 'DELIVERED', 88000, 1),

('2022-01-22', 'PENDING', 'DELIVERED', 76000, 2),
('2022-02-08', 'COMPLETED', 'DELIVERED', 105000, 2),
('2022-03-17', 'PENDING', 'DELIVERED', 43000, 2),
('2022-04-30', 'FAILED', 'CANCELLED', 59000, 2),
('2022-05-25', 'COMPLETED', 'DELIVERED', 92000, 2),

('2022-01-03', 'PENDING', 'DELIVERED', 74000, 3),
('2022-02-14', 'COMPLETED', 'DELIVERED', 118000, 3),
('2022-03-05', 'PENDING', 'DELIVERED', 45000, 3),
('2022-04-22', 'FAILED', 'CANCELLED', 61000, 3),
('2022-05-16', 'COMPLETED', 'DELIVERED', 95000, 3),

('2022-01-30', 'PENDING', 'DELIVERED', 79000, 4),
('2022-02-18', 'COMPLETED', 'DELIVERED', 112000, 4),
('2022-03-25', 'PENDING', 'DELIVERED', 47000, 4),
('2022-04-15', 'FAILED', 'CANCELLED', 63000, 4),
('2022-05-10', 'COMPLETED', 'DELIVERED', 91000, 4),

('2022-01-12', 'PENDING', 'DELIVERED', 82000, 5),
('2022-02-27', 'COMPLETED', 'DELIVERED', 125000, 5),
('2022-03-15', 'PENDING', 'DELIVERED', 46000, 5),
('2022-04-08', 'FAILED', 'CANCELLED', 62000, 5),
('2022-05-03', 'COMPLETED', 'DELIVERED', 94000, 5),

-- Tiếp tục cho các khách hàng khác
('2022-01-07', 'PENDING', 'DELIVERED', 71000, 6),
('2022-02-16', 'COMPLETED', 'DELIVERED', 98000, 6),
('2022-03-10', 'PENDING', 'DELIVERED', 44000, 6),
('2022-04-25', 'FAILED', 'CANCELLED', 58000, 6),
('2022-05-20', 'COMPLETED', 'DELIVERED', 90000, 6),

('2022-01-25', 'PENDING', 'DELIVERED', 77000, 7),
('2022-02-05', 'COMPLETED', 'DELIVERED', 106000, 7),
('2022-03-22', 'PENDING', 'DELIVERED', 42000, 7),
('2022-04-18', 'FAILED', 'CANCELLED', 60000, 7),
('2022-05-12', 'COMPLETED', 'DELIVERED', 93000, 7),

('2022-01-18', 'PENDING', 'DELIVERED', 75000, 8),
('2022-02-28', 'COMPLETED', 'DELIVERED', 115000, 8),
('2022-03-07', 'PENDING', 'DELIVERED', 45000, 8),
('2022-04-10', 'FAILED', 'CANCELLED', 59000, 8),
('2022-05-05', 'COMPLETED', 'DELIVERED', 92000, 8),

('2022-01-20', 'PENDING', 'DELIVERED', 80000, 9),
('2022-02-12', 'COMPLETED', 'DELIVERED', 108000, 9),
('2022-03-30', 'PENDING', 'DELIVERED', 47000, 9),
('2022-04-05', 'FAILED', 'CANCELLED', 64000, 9),
('2022-05-15', 'COMPLETED', 'DELIVERED', 96000, 9),

('2022-01-14', 'PENDING', 'DELIVERED', 73000, 10),
('2022-02-23', 'COMPLETED', 'DELIVERED', 120000, 10),
('2022-03-12', 'PENDING', 'DELIVERED', 43000, 10),
('2022-04-28', 'FAILED', 'CANCELLED', 57000, 10),
('2022-05-08', 'COMPLETED', 'DELIVERED', 89000, 10),

-- Khách hàng mới từ script trước
('2022-06-15', 'PENDING', 'DELIVERED', 84000, 13),
('2022-07-20', 'COMPLETED', 'DELIVERED', 128000, 13),
('2022-08-10', 'PENDING', 'DELIVERED', 46000, 13),
('2022-09-25', 'FAILED', 'CANCELLED', 61000, 13),
('2022-10-30', 'COMPLETED', 'DELIVERED', 98000, 13),

('2022-06-05', 'PENDING', 'DELIVERED', 76000, 14),
('2022-07-12', 'COMPLETED', 'DELIVERED', 114000, 14),
('2022-08-18', 'PENDING', 'DELIVERED', 44000, 14),
('2022-09-22', 'FAILED', 'CANCELLED', 59000, 14),
('2022-10-28', 'COMPLETED', 'DELIVERED', 93000, 14),

('2022-06-22', 'PENDING', 'DELIVERED', 81000, 15),
('2022-07-30', 'COMPLETED', 'DELIVERED', 126000, 15),
('2022-08-15', 'PENDING', 'DELIVERED', 45000, 15),
('2022-09-10', 'FAILED', 'CANCELLED', 62000, 15),
('2022-10-05', 'COMPLETED', 'DELIVERED', 97000, 15),

('2022-06-10', 'PENDING', 'DELIVERED', 79000, 16),
('2022-07-25', 'COMPLETED', 'DELIVERED', 121000, 16),
('2022-08-28', 'PENDING', 'DELIVERED', 47000, 16),
('2022-09-05', 'FAILED', 'CANCELLED', 64000, 16),
('2022-10-15', 'COMPLETED', 'DELIVERED', 95000, 16),

('2022-06-18', 'PENDING', 'DELIVERED', 83000, 17),
('2022-07-08', 'COMPLETED', 'DELIVERED', 132000, 17),
('2022-08-05', 'PENDING', 'DELIVERED', 48000, 17),
('2022-09-30', 'FAILED', 'CANCELLED', 65000, 17),
('2022-10-20', 'COMPLETED', 'DELIVERED', 99000, 17);

INSERT INTO order_details (price, quantity, order_id, product_id) VALUES
-- Đơn hàng 1
(4500, 2, 1, 1),
(5000, 3, 1, 2),
(1200, 1, 1, 3),

-- Đơn hàng 2
(2000, 2, 2, 4),
(3000, 1, 2, 5),
(1800, 4, 2, 6),

-- Đơn hàng 3
(7500, 2, 3, 7),
(2200, 3, 3, 8),
(5300, 1, 3, 9),

-- Đơn hàng 4
(8900, 2, 4, 10),
(4000, 1, 4, 11),

-- Đơn hàng 5
(6500, 3, 5, 12),
(2400, 2, 5, 13),

-- Đơn hàng 6
(7200, 1, 6, 14),
(5100, 2, 6, 15),
(3200, 3, 6, 1),

-- Đơn hàng 7
(8100, 1, 7, 12),
(9100, 2, 7, 13),

-- Đơn hàng 8
(4300, 2, 8, 2),
(3700, 3, 8, 3),
(5600, 1, 8, 4),

-- Đơn hàng 9
(3000, 2, 9, 1),
(4100, 1, 9, 2),

-- Đơn hàng 10
(2200, 4, 10, 3),
(8800, 2, 10, 4),
(7400, 3, 10, 5),

-- Đơn hàng 11
(4100, 2, 11, 6),
(6300, 1, 11, 7),
(3100, 3, 11, 8),

-- Đơn hàng 12
(5300, 2, 12, 9),
(3200, 1, 12, 10),
(4100, 4, 12, 11),

-- Đơn hàng 13
(2800, 2, 13, 12),
(4300, 3, 13, 13),

-- Đơn hàng 14
(6700, 1, 14, 15),
(5200, 2, 14, 11),
(2300, 3, 14, 8),

-- Đơn hàng 15
(7100, 1, 15, 1),
(8900, 2, 15, 6),

-- Đơn hàng 16
(3900, 2, 16, 7),
(4100, 3, 16, 8),
(5600, 1, 16, 5),

-- Đơn hàng 17
(4100, 2, 17, 1),
(2900, 1, 17, 2),

-- Đơn hàng 18
(2200, 4, 18, 3),
(8800, 2, 18, 4),
(7400, 3, 18, 5),

-- Đơn hàng 19
(3100, 2, 19, 6),
(5100, 1, 19, 7),
(4900, 3, 19, 8),

-- Đơn hàng 20
(6200, 2, 20, 9),
(3100, 1, 20, 10),
(4500, 4, 20, 11),

-- Đơn hàng 21
(2700, 2, 21, 12),
(4200, 3, 21, 13),

-- Đơn hàng 22
(5600, 1, 22, 15),
(5900, 2, 22, 11),
(2300, 3, 22, 8),

-- Đơn hàng 23
(7200, 1, 23, 1),
(6800, 2, 23, 14),

-- Đơn hàng 24
(3900, 2, 24, 2),
(4100, 3, 24, 3),
(5600, 1, 24, 4),

-- Đơn hàng 25
(4900, 2, 25, 1),
(3200, 1, 25, 2),

-- Đơn hàng 26
(3800, 2, 26, 3),
(7200, 1, 26, 5),

-- Đơn hàng 27
(4100, 3, 27, 6),
(5300, 2, 27, 7),

-- Đơn hàng 28
(6400, 1, 28, 8),
(4700, 2, 28, 9),
(3200, 3, 28, 10),

-- Đơn hàng 29
(5900, 2, 29, 11),
(7100, 1, 29, 12),

-- Đơn hàng 30
(4800, 3, 30, 13),
(3900, 2, 30, 14),
(5700, 1, 30, 15),

-- Đơn hàng của khách hàng 13
(5500, 2, 31, 1),
(6000, 3, 31, 2),
(1500, 1, 31, 3),

-- Đơn hàng của khách hàng 14
(2500, 2, 32, 4),
(3500, 1, 32, 5),
(2000, 4, 32, 6),

-- Đơn hàng của khách hàng 15
(8000, 2, 33, 7),
(2700, 3, 33, 8),
(5500, 1, 33, 9),

-- Đơn hàng của khách hàng 16
(9200, 2, 34, 10),
(4500, 1, 34, 11),

-- Đơn hàng của khách hàng 17
(6800, 3, 35, 12),
(2900, 2, 35, 13),

-- Tiếp tục cho các đơn hàng khác tương tự
(7500, 1, 36, 14),
(5300, 2, 36, 15),
(3400, 3, 36, 1),

(8300, 1, 37, 12),
(9500, 2, 37, 13),

(4700, 2, 38, 2),
(3900, 3, 38, 3),
(5800, 1, 38, 4),

(3200, 2, 39, 1),
(4300, 1, 39, 2),

(2500, 4, 40, 3),
(9000, 2, 40, 4),
(7600, 3, 40, 5),

(4300, 2, 41, 6),
(6500, 1, 41, 7),
(3300, 3, 41, 8),

(5500, 2, 42, 9),
(3400, 1, 42, 10),
(4300, 4, 42, 11),

(3000, 2, 43, 12),
(4500, 3, 43, 13),

(6900, 1, 44, 15),
(5400, 2, 44, 11),
(2500, 3, 44, 8),

(7300, 1, 45, 1),
(9100, 2, 45, 6),

-- Đơn hàng của khách hàng 1 năm 2022
(4200, 2, 46, 1),
(5100, 3, 46, 2),
(1300, 1, 46, 3),

-- Tiếp tục thêm chi tiết đơn hàng cho tất cả các đơn hàng mới
(2300, 2, 47, 4),
(3400, 1, 47, 5),
(1900, 4, 47, 6),

(7800, 2, 48, 7),
(2600, 3, 48, 8),
(5300, 1, 48, 9),

(9000, 2, 49, 10),
(4400, 1, 49, 11),

(6700, 3, 50, 12),
(2800, 2, 50, 13),

-- Tiếp tục với các đơn hàng khác
(7300, 1, 51, 14),
(5200, 2, 51, 15),
(3300, 3, 51, 1),

(8200, 1, 52, 12),
(9400, 2, 52, 13),

(4600, 2, 53, 2),
(3800, 3, 53, 3),
(5700, 1, 53, 4),

(3100, 2, 54, 1),
(4200, 1, 54, 2),

(2400, 4, 55, 3),
(8800, 2, 55, 4),
(7500, 3, 55, 5),

-- Tiếp tục với các đơn hàng còn lại (tôi đã rút ngắn để minh họa)
(4200, 2, 56, 6),
(6400, 1, 56, 7),
(3200, 3, 56, 8),

(5400, 2, 57, 9),
(3300, 1, 57, 10),
(4200, 4, 57, 11),

(2900, 2, 58, 12),
(4400, 3, 58, 13),

(6800, 1, 59, 15),
(5300, 2, 59, 11),
(2400, 3, 59, 8),

(7200, 1, 60, 1),
(9000, 2, 60, 6);


insert into payments (payment_method, status, order_id)
values ('CASH', 'PENDING', 1),
       ('CASH', 'PENDING', 2),
       ('CASH', 'PENDING', 3),
       ('CASH', 'PENDING', 4),
       ('CASH', 'PENDING', 5),
       ('CASH', 'PENDING', 6),
       ('CASH', 'PENDING', 7),
       ('CASH', 'PENDING', 8),
       ('CASH', 'PENDING', 9),
       ('CASH', 'PENDING', 10),
       ('CASH', 'PENDING', 11),
       ('CASH', 'PENDING', 12),
       ('CASH', 'PENDING', 13),
       ('CASH', 'PENDING', 14),
       ('CASH', 'PENDING', 15),
       ('CASH', 'PENDING', 16),
       ('CASH', 'PENDING', 17),
       ('CASH', 'PENDING', 18),
       ('CASH', 'PENDING', 19),
       ('CASH', 'PENDING', 20),
       ('CASH', 'PENDING', 21),
       ('CASH', 'PENDING', 22),
       ('CASH', 'PENDING', 23),
       ('CASH', 'PENDING', 24),
       ('CASH', 'PENDING', 25) ,
       ('CASH', 'PENDING', 26),
       ('CASH', 'PENDING', 27),
       ('CASH', 'PENDING', 28),
       ('CASH', 'PENDING', 29),
       ('CASH', 'PENDING', 30),
       ('CASH', 'PENDING', 31),
       ('CASH', 'PENDING', 32),
       ('CASH', 'PENDING', 33),
       ('CASH', 'PENDING', 34),
       ('CASH', 'PENDING', 35),
       ('CASH', 'PENDING', 36),
       ('CASH', 'PENDING', 37),
       ('CASH', 'PENDING', 38),
       ('CASH', 'PENDING', 39),
       ('CASH', 'PENDING', 40),
       ('CASH', 'PENDING', 41),
       ('CASH', 'PENDING', 42),
       ('CASH', 'PENDING', 43),
       ('CASH', 'PENDING', 44),
       ('CASH', 'PENDING', 45),
       ('CASH', 'PENDING', 46),
       ('CREDIT_CARD', 'PENDING', 47),
       ('ONLINE_BANKING', 'PENDING', 48),
       ('CASH', 'PENDING', 49),
       ('CREDIT_CARD', 'PENDING', 50),
       ('ONLINE_BANKING', 'PENDING', 51),
       ('CASH', 'PENDING', 52),
       ('CREDIT_CARD', 'PENDING', 53),
       ('ONLINE_BANKING', 'PENDING', 54),
       ('CASH', 'PENDING', 55),
       ('CREDIT_CARD', 'PENDING', 56),
       ('ONLINE_BANKING', 'PENDING', 57),
       ('CASH', 'PENDING', 58),
       ('CREDIT_CARD', 'PENDING', 59),
       ('ONLINE_BANKING', 'PENDING', 60);