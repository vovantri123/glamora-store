-- ============================================
-- ROLES & PERMISSIONS
-- ============================================
INSERT INTO roles (name, description) VALUES
('ADMIN', 'Quản trị viên - Toàn quyền quản lý hệ thống'),
('USER', 'Khách hàng - Quyền mua hàng và quản lý đơn hàng cá nhân');

INSERT INTO permissions (name, description) VALUES
('USER_READ', 'Xem thông tin người dùng'),
('USER_WRITE', 'Tạo và cập nhật người dùng'),
('USER_DELETE', 'Xóa người dùng'),
('PRODUCT_READ', 'Xem thông tin sản phẩm'),
('PRODUCT_WRITE', 'Tạo và cập nhật sản phẩm'),
('PRODUCT_DELETE', 'Xóa sản phẩm'),
('ORDER_READ', 'Xem thông tin đơn hàng'),
('ORDER_WRITE', 'Tạo và cập nhật đơn hàng'),
('ORDER_DELETE', 'Xóa đơn hàng'),
('CATEGORY_MANAGE', 'Quản lý danh mục'),
('VOUCHER_MANAGE', 'Quản lý mã giảm giá');

INSERT INTO role_permissions (role_name, permission_name) VALUES
-- Admin có tất cả quyền
('ADMIN', 'USER_READ'), ('ADMIN', 'USER_WRITE'), ('ADMIN', 'USER_DELETE'),
('ADMIN', 'PRODUCT_READ'), ('ADMIN', 'PRODUCT_WRITE'), ('ADMIN', 'PRODUCT_DELETE'),
('ADMIN', 'ORDER_READ'), ('ADMIN', 'ORDER_WRITE'), ('ADMIN', 'ORDER_DELETE'),
('ADMIN', 'CATEGORY_MANAGE'), ('ADMIN', 'VOUCHER_MANAGE'),
-- User chỉ xem và mua hàng
('USER', 'PRODUCT_READ'), ('USER', 'ORDER_READ');

-- ============================================
-- USERS (Password: 12341234)
-- ============================================
INSERT INTO users (email, full_name, password, gender, dob, is_deleted, created_at, avatar) VALUES
('admin@gmail.com', 'Nguyễn Văn Admin', '$2a$10$iNjWX4Qmk14lTn4L30KzouZnMJ/mecJwVIonOUHIhEItP0n77ylOe', 'MALE', '1990-01-15', false, CURRENT_TIMESTAMP, 'https://i.pravatar.cc/150?img=12'),
('user@gmail.com', 'Trần Thị Mai', '$2a$10$iNjWX4Qmk14lTn4L30KzouZnMJ/mecJwVIonOUHIhEItP0n77ylOe', 'FEMALE', '1992-05-20', false, CURRENT_TIMESTAMP, 'https://i.pravatar.cc/150?img=47'),
('vovantri204@gmail.com', 'Võ Văn Trí', '$2a$10$iNjWX4Qmk14lTn4L30KzouZnMJ/mecJwVIonOUHIhEItP0n77ylOe', 'MALE', '2003-08-25', false, CURRENT_TIMESTAMP, 'https://i.pravatar.cc/150?img=33'),
('lethihoa@gmail.com', 'Lê Thị Hoa', '$2a$10$iNjWX4Qmk14lTn4L30KzouZnMJ/mecJwVIonOUHIhEItP0n77ylOe', 'FEMALE', '1998-12-10', false, CURRENT_TIMESTAMP, 'https://i.pravatar.cc/150?img=45'),
('phamducan@gmail.com', 'Phạm Đức An', '$2a$10$iNjWX4Qmk14lTn4L30KzouZnMJ/mecJwVIonOUHIhEItP0n77ylOe', 'MALE', '1995-03-18', false, CURRENT_TIMESTAMP, 'https://i.pravatar.cc/150?img=15');

-- Gán role cho users
INSERT INTO user_roles (user_id, role_name) VALUES
((SELECT id FROM users WHERE email = 'admin@gmail.com'), 'ADMIN'),
((SELECT id FROM users WHERE email = 'user@gmail.com'), 'USER'),
((SELECT id FROM users WHERE email = 'vovantri204@gmail.com'), 'ADMIN'),
((SELECT id FROM users WHERE email = 'lethihoa@gmail.com'), 'USER'),
((SELECT id FROM users WHERE email = 'phamducan@gmail.com'), 'USER');

-- ============================================
-- CATEGORIES - Danh mục thời trang
-- ============================================
-- Danh mục cha level 1
INSERT INTO categories (name, description, image_url, is_deleted, parent_id, created_at) VALUES
('Thời Trang Nam', 'Tất cả sản phẩm thời trang dành cho nam giới', 'https://images.unsplash.com/photo-1490578474895-699cd4e2cf59?w=500', false, NULL, CURRENT_TIMESTAMP),
('Thời Trang Nữ', 'Tất cả sản phẩm thời trang dành cho nữ giới', 'https://images.unsplash.com/photo-1483985988355-763728e1935b?w=500', false, NULL, CURRENT_TIMESTAMP);

-- Danh mục con level 2 của "Thời Trang Nam"  
INSERT INTO categories (name, description, image_url, is_deleted, parent_id, created_at) VALUES
('Áo Nam', 'Áo thun, áo sơ mi, áo khoác nam', 'https://images.unsplash.com/photo-1620799140408-edc6dcb6d633?w=500', false, (SELECT id FROM categories WHERE name = 'Thời Trang Nam'), CURRENT_TIMESTAMP),
('Quần Nam', 'Quần jean, quần tây, quần short nam', 'https://images.unsplash.com/photo-1473966968600-fa801b869a1a?w=500', false, (SELECT id FROM categories WHERE name = 'Thời Trang Nam'), CURRENT_TIMESTAMP),
('Phụ Kiện Nam', 'Thắt lưng, ví, túi xách, mũ nam', 'https://images.unsplash.com/photo-1523779105320-d1cd346ff52b?w=500', false, (SELECT id FROM categories WHERE name = 'Thời Trang Nam'), CURRENT_TIMESTAMP);

-- Danh mục con level 2 của "Thời Trang Nữ"
INSERT INTO categories (name, description, image_url, is_deleted, parent_id, created_at) VALUES
('Áo Nữ', 'Áo thun, áo kiểu, áo sơ mi nữ', 'https://images.unsplash.com/photo-1564859228273-274232fdb516?w=500', false, (SELECT id FROM categories WHERE name = 'Thời Trang Nữ'), CURRENT_TIMESTAMP),
('Quần Nữ', 'Quần jean, quần tây, quần short nữ', 'https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=500', false, (SELECT id FROM categories WHERE name = 'Thời Trang Nữ'), CURRENT_TIMESTAMP),
('Phụ Kiện Nữ', 'Túi xách, ví, mũ, khăn nữ', 'https://images.unsplash.com/photo-1566150905458-1bf1fc113f0d?w=500', false, (SELECT id FROM categories WHERE name = 'Thời Trang Nữ'), CURRENT_TIMESTAMP),
('Đầm', 'Đầm dự tiệc, đầm công sở, đầm maxi', 'https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=500', false, (SELECT id FROM categories WHERE name = 'Thời Trang Nữ'), CURRENT_TIMESTAMP),
('Váy', 'Váy midi, váy maxi, chân váy xòe', 'https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?w=500', false, (SELECT id FROM categories WHERE name = 'Thời Trang Nữ'), CURRENT_TIMESTAMP);

-- Danh mục chi tiết level 3 cho "Áo Nam"
INSERT INTO categories (name, description, image_url, is_deleted, parent_id, created_at) VALUES
('Áo Thun Nam', 'Áo thun cotton, áo polo nam', 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=500', false, (SELECT id FROM categories WHERE name = 'Áo Nam'), CURRENT_TIMESTAMP),
('Áo Sơ Mi Nam', 'Áo sơ mi ngắn tay, dài tay', 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=500', false, (SELECT id FROM categories WHERE name = 'Áo Nam'), CURRENT_TIMESTAMP),
('Áo Khoác Nam', 'Áo khoác jean, áo hoodie, áo bomber', 'https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=500', false, (SELECT id FROM categories WHERE name = 'Áo Nam'), CURRENT_TIMESTAMP);

-- Danh mục chi tiết level 3 cho "Quần Nam"
INSERT INTO categories (name, description, image_url, is_deleted, parent_id, created_at) VALUES
('Quần Jean Nam', 'Quần jean slim, regular, baggy', 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=500', false, (SELECT id FROM categories WHERE name = 'Quần Nam'), CURRENT_TIMESTAMP),
('Quần Tây Nam', 'Quần tây công sở, quần kaki', 'https://images.unsplash.com/photo-1624378439575-d8705ad7ae80?w=500', false, (SELECT id FROM categories WHERE name = 'Quần Nam'), CURRENT_TIMESTAMP),
('Quần Short Nam', 'Quần short thể thao, quần soóc jean', 'https://images.unsplash.com/photo-1591195853828-11db59a44f6b?w=500', false, (SELECT id FROM categories WHERE name = 'Quần Nam'), CURRENT_TIMESTAMP);

-- Danh mục chi tiết level 3 cho "Áo Nữ"
INSERT INTO categories (name, description, image_url, is_deleted, parent_id, created_at) VALUES
('Áo Thun Nữ', 'Áo thun basic, áo croptop', 'https://images.unsplash.com/photo-1618354691373-d851c5c3a990?w=500', false, (SELECT id FROM categories WHERE name = 'Áo Nữ'), CURRENT_TIMESTAMP),
('Áo Kiểu Nữ', 'Áo kiểu công sở, áo babydoll', 'https://images.unsplash.com/photo-1485968579580-b6d095142e6e?w=500', false, (SELECT id FROM categories WHERE name = 'Áo Nữ'), CURRENT_TIMESTAMP),
('Áo Khoác Nữ', 'Áo blazer, áo cardigan, áo hoodie nữ', 'https://images.unsplash.com/photo-1591369822096-ffd140ec948f?w=500', false, (SELECT id FROM categories WHERE name = 'Áo Nữ'), CURRENT_TIMESTAMP);

-- Danh mục chi tiết level 3 cho "Quần Nữ"
INSERT INTO categories (name, description, image_url, is_deleted, parent_id, created_at) VALUES
('Quần Jean Nữ', 'Quần jean ống đứng, ống loe, skinny', 'https://images.unsplash.com/photo-1582552938357-32b906df40cb?w=500', false, (SELECT id FROM categories WHERE name = 'Quần Nữ'), CURRENT_TIMESTAMP),
('Quần Tây Nữ', 'Quần tây công sở, quần baggy', 'https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=500', false, (SELECT id FROM categories WHERE name = 'Quần Nữ'), CURRENT_TIMESTAMP),
('Quần Short Nữ', 'Quần short jean, quần soóc vải', 'https://images.unsplash.com/photo-1591195850148-d86c61e2238f?w=500', false, (SELECT id FROM categories WHERE name = 'Quần Nữ'), CURRENT_TIMESTAMP);

-- ============================================
-- ATTRIBUTES - Thuộc tính sản phẩm
-- ============================================
INSERT INTO attributes (name, created_at) VALUES
('Màu sắc', CURRENT_TIMESTAMP),
('Kích thước', CURRENT_TIMESTAMP);

INSERT INTO attribute_values (value, attribute_id, created_at) VALUES
-- Màu sắc
('Đen', (SELECT id FROM attributes WHERE name = 'Màu sắc'), CURRENT_TIMESTAMP),
('Trắng', (SELECT id FROM attributes WHERE name = 'Màu sắc'), CURRENT_TIMESTAMP),
('Xanh Navy', (SELECT id FROM attributes WHERE name = 'Màu sắc'), CURRENT_TIMESTAMP),
('Xanh Denim', (SELECT id FROM attributes WHERE name = 'Màu sắc'), CURRENT_TIMESTAMP),
('Be', (SELECT id FROM attributes WHERE name = 'Màu sắc'), CURRENT_TIMESTAMP),
('Xám', (SELECT id FROM attributes WHERE name = 'Màu sắc'), CURRENT_TIMESTAMP),
('Hồng', (SELECT id FROM attributes WHERE name = 'Màu sắc'), CURRENT_TIMESTAMP),
('Đỏ', (SELECT id FROM attributes WHERE name = 'Màu sắc'), CURRENT_TIMESTAMP),
('Vàng', (SELECT id FROM attributes WHERE name = 'Màu sắc'), CURRENT_TIMESTAMP),
('Nâu', (SELECT id FROM attributes WHERE name = 'Màu sắc'), CURRENT_TIMESTAMP),
-- Kích thước
('S', (SELECT id FROM attributes WHERE name = 'Kích thước'), CURRENT_TIMESTAMP),
('M', (SELECT id FROM attributes WHERE name = 'Kích thước'), CURRENT_TIMESTAMP),
('L', (SELECT id FROM attributes WHERE name = 'Kích thước'), CURRENT_TIMESTAMP),
('XL', (SELECT id FROM attributes WHERE name = 'Kích thước'), CURRENT_TIMESTAMP),
('XXL', (SELECT id FROM attributes WHERE name = 'Kích thước'), CURRENT_TIMESTAMP),
('28', (SELECT id FROM attributes WHERE name = 'Kích thước'), CURRENT_TIMESTAMP),
('29', (SELECT id FROM attributes WHERE name = 'Kích thước'), CURRENT_TIMESTAMP),
('30', (SELECT id FROM attributes WHERE name = 'Kích thước'), CURRENT_TIMESTAMP),
('31', (SELECT id FROM attributes WHERE name = 'Kích thước'), CURRENT_TIMESTAMP),
('32', (SELECT id FROM attributes WHERE name = 'Kích thước'), CURRENT_TIMESTAMP);

-- ============================================
-- PAYMENT METHODS
-- ============================================
INSERT INTO payment_methods (name, is_active, logo_url, description, created_at) VALUES
('COD', true, 'https://img.freepik.com/premium-vector/cash-delivery-label_686319-773.jpg', 'Thanh toán tiền mặt khi nhận hàng', CURRENT_TIMESTAMP),
('VNPay', true, 'https://vinadesign.vn/uploads/images/2023/05/vnpay-logo-vinadesign-25-12-57-55.jpg', 'Thanh toán qua VNPay - Hỗ trợ thẻ ATM, Visa, MasterCard', CURRENT_TIMESTAMP);

-- ============================================
-- VOUCHERS - Mã giảm giá
-- ============================================
INSERT INTO vouchers (code, name, description, discount_type, discount_value, min_order_value, max_discount_amount, start_date, end_date, usage_limit, usage_per_user, used_count, is_active, is_deleted, created_at) VALUES
('GLAMORA2025', 'Chào xuân 2025', 'Giảm 15% cho đơn hàng đầu tiên', 'PERCENTAGE', 15.00, 300000.00, 100000.00, '2025-01-01', '2025-12-31', 1000, 1, 0, true, false, CURRENT_TIMESTAMP),
('FREESHIP50K', 'Miễn phí vận chuyển', 'Giảm 50K phí ship cho đơn từ 500K', 'FIXED_AMOUNT', 50000.00, 500000.00, NULL, '2025-01-01', '2025-12-31', 500, 3, 0, true, false, CURRENT_TIMESTAMP),
('NEWMEMBER', 'Thành viên mới', 'Giảm 100K cho khách hàng mới', 'FIXED_AMOUNT', 100000.00, 400000.00, NULL, '2025-01-01', '2025-12-31', NULL, 1, 0, true, false, CURRENT_TIMESTAMP),
('SUMMER30', 'Khuyến mãi hè', 'Giảm 30% tối đa 200K', 'PERCENTAGE', 30.00, 600000.00, 200000.00, '2025-06-01', '2025-08-31', 200, 2, 0, true, false, CURRENT_TIMESTAMP),
('VIP200K', 'Ưu đãi VIP', 'Giảm 200K cho đơn từ 2 triệu', 'FIXED_AMOUNT', 200000.00, 2000000.00, NULL, '2025-01-01', '2025-12-31', 100, 1, 0, true, false, CURRENT_TIMESTAMP);

-- ============================================
-- PRODUCTS - SẢN PHẨM THỜI TRANG NAM
-- ============================================

-- Áo Thun Nam
INSERT INTO products (name, description, category_id, is_deleted, created_at) VALUES
('Áo Thun Nam Basic Cotton', '<p>Áo thun nam basic chất liệu cotton 100% thoáng mát, thấm hút mồ hôi tốt. Form áo regular fit vừa vặn, phù hợp mọi dáng người.</p><p>Thiết kế tối giản, dễ phối đồ với quần jean, quần kaki.</p>', (SELECT id FROM categories WHERE name = 'Áo Thun Nam'), false, CURRENT_TIMESTAMP),
('Áo Polo Nam Cao Cấp', '<p>Áo polo nam cao cấp với thiết kế thanh lịch, phù hợp cho môi trường công sở và dạo phố.</p><p>Chất vải pique cotton thoáng khí, form dáng slim fit tôn dáng.</p>', (SELECT id FROM categories WHERE name = 'Áo Thun Nam'), false, CURRENT_TIMESTAMP),
('Áo Thun Nam Oversize Streetwear', '<p>Áo thun oversize theo phong cách streetwear Hàn Quốc.</p><p>Form rộng thoải mái, họa tiết in lụa độc đáo, chất liệu cotton mềm mại.</p>', (SELECT id FROM categories WHERE name = 'Áo Thun Nam'), false, CURRENT_TIMESTAMP),

-- Áo Sơ Mi Nam
('Áo Sơ Mi Nam Trắng Công Sở', '<p>Áo sơ mi trắng lịch sự, thiết yếu cho tủ đồ công sở.</p><p>Chất liệu cotton pha polyester không nhăn, dễ giặt ủi.</p>', (SELECT id FROM categories WHERE name = 'Áo Sơ Mi Nam'), false, CURRENT_TIMESTAMP),
('Áo Sơ Mi Kẻ Sọc Oxford', '<p>Áo sơ mi kẻ sọc phong cách Oxford sang trọng.</p><p>Chất vải oxford dày dặn, form regular fit thoải mái.</p>', (SELECT id FROM categories WHERE name = 'Áo Sơ Mi Nam'), false, CURRENT_TIMESTAMP),
('Áo Sơ Mi Linen Mùa Hè', '<p>Áo sơ mi linen mát mẻ cho mùa hè.</p><p>Chất vải linen thoáng khí, phong cách năng động trẻ trung.</p>', (SELECT id FROM categories WHERE name = 'Áo Sơ Mi Nam'), false, CURRENT_TIMESTAMP),

-- Áo Khoác Nam
('Áo Khoác Jean Nam Wash Vintage', '<p>Áo khoác jean phong cách vintage với hiệu ứng wash tạo vết rách nhẹ.</p><p>Form regular fit, phù hợp cả nam và nữ.</p>', (SELECT id FROM categories WHERE name = 'Áo Khoác Nam'), false, CURRENT_TIMESTAMP),
('Áo Hoodie Nam Basic', '<p>Áo hoodie nỉ bông basic ấm áp cho mùa đông.</p><p>Chất nỉ bông dày dặn, mũ khoác tiện dụng.</p>', (SELECT id FROM categories WHERE name = 'Áo Khoác Nam'), false, CURRENT_TIMESTAMP),
('Áo Bomber Nam Cao Cấp', '<p>Áo bomber jacket phong cách pilot năng động.</p><p>Thiết kế 2 lớp giữ ấm tốt, nhiều túi tiện dụng.</p>', (SELECT id FROM categories WHERE name = 'Áo Khoác Nam'), false, CURRENT_TIMESTAMP),

-- Quần Jean Nam
('Quần Jean Nam Slim Fit', '<p>Quần jean nam slim fit tôn dáng, ôm vừa phải.</p><p>Chất jean co giãn nhẹ thoải mái khi vận động.</p>', (SELECT id FROM categories WHERE name = 'Quần Jean Nam'), false, CURRENT_TIMESTAMP),
('Quần Jean Nam Straight Fit', '<p>Quần jean nam straight fit form dáng thẳng cổ điển.</p><p>Phù hợp mọi lứa tuổi, dễ phối đồ.</p>', (SELECT id FROM categories WHERE name = 'Quần Jean Nam'), false, CURRENT_TIMESTAMP),

-- Quần Tây Nam
('Quần Tây Nam Công Sở', '<p>Quần tây công sở lịch sự, form dáng chuẩn.</p><p>Chất kaki cao cấp không nhăn, dễ giặt ủi.</p>', (SELECT id FROM categories WHERE name = 'Quần Tây Nam'), false, CURRENT_TIMESTAMP),
('Quần Kaki Nam Túi Hộp', '<p>Quần kaki túi hộp phong cách cargo trẻ trung.</p><p>Nhiều túi tiện lợi, chất kaki dày dặn bền đẹp.</p>', (SELECT id FROM categories WHERE name = 'Quần Tây Nam'), false, CURRENT_TIMESTAMP);

-- ============================================
-- PRODUCTS - SẢN PHẨM THỜI TRANG NỮ
-- ============================================

-- Áo Thun Nữ
INSERT INTO products (name, description, category_id, is_deleted, created_at) VALUES
('Áo Thun Nữ Croptop Basic', '<p>Áo thun croptop nữ basic năng động.</p><p>Chất cotton mềm mại, form ôm vừa phải tôn dáng.</p>', (SELECT id FROM categories WHERE name = 'Áo Thun Nữ'), false, CURRENT_TIMESTAMP),
('Áo Thun Nữ Form Rộng', '<p>Áo thun form rộng thoải mái phong cách Hàn Quốc.</p><p>Dễ mix đồ, phù hợp đi chơi dạo phố.</p>', (SELECT id FROM categories WHERE name = 'Áo Thun Nữ'), false, CURRENT_TIMESTAMP),

-- Áo Kiểu Nữ
('Áo Kiểu Nữ Công Sở Tay Bồng', '<p>Áo kiểu tay bồng thanh lịch cho môi trường công sở.</p><p>Thiết kế sang trọng, chất liệu voan mềm mại.</p>', (SELECT id FROM categories WHERE name = 'Áo Kiểu Nữ'), false, CURRENT_TIMESTAMP),
('Áo Sơ Mi Nữ Trắng Lụa', '<p>Áo sơ mi lụa trắng cao cấp, sang trọng.</p><p>Chất lụa mềm mịn, form dáng thanh lịch.</p>', (SELECT id FROM categories WHERE name = 'Áo Kiểu Nữ'), false, CURRENT_TIMESTAMP),

-- Áo Khoác Nữ
('Áo Blazer Nữ Công Sở', '<p>Áo blazer nữ thanh lịch cho phụ nữ hiện đại.</p><p>Thiết kế tối giản, form dáng chuẩn Hàn Quốc.</p>', (SELECT id FROM categories WHERE name = 'Áo Khoác Nữ'), false, CURRENT_TIMESTAMP),
('Áo Cardigan Nữ Len Mỏng', '<p>Áo cardigan len mỏng nhẹ nhàng nữ tính.</p><p>Form dáng thanh lịch, dễ phối đồ.</p>', (SELECT id FROM categories WHERE name = 'Áo Khoác Nữ'), false, CURRENT_TIMESTAMP),

-- Quần Jean Nữ
('Quần Jean Nữ Ống Đứng', '<p>Quần jean nữ ống đứng phong cách vintage.</p><p>Chất jean cao cấp, form dáng tôn dáng.</p>', (SELECT id FROM categories WHERE name = 'Quần Jean Nữ'), false, CURRENT_TIMESTAMP),
('Quần Jean Nữ Skinny', '<p>Quần jean nữ skinny ôm dáng sexy.</p><p>Co giãn tốt, thoải mái khi vận động.</p>', (SELECT id FROM categories WHERE name = 'Quần Jean Nữ'), false, CURRENT_TIMESTAMP),

-- Quần Tây Nữ
('Quần Tây Nữ Ống Suông', '<p>Quần tây nữ ống suông thanh lịch công sở.</p><p>Chất liệu cao cấp, form dáng chuẩn.</p>', (SELECT id FROM categories WHERE name = 'Quần Tây Nữ'), false, CURRENT_TIMESTAMP),
('Quần Baggy Nữ Ulzzang', '<p>Quần baggy phong cách Hàn Quốc năng động.</p><p>Form rộng thoải mái, trendy.</p>', (SELECT id FROM categories WHERE name = 'Quần Tây Nữ'), false, CURRENT_TIMESTAMP),

-- Váy
('Chân Váy Chữ A Midi', '<p>Chân váy chữ A dáng midi thanh lịch.</p><p>Phù hợp đi làm và dạo phố.</p>', (SELECT id FROM categories WHERE name = 'Váy'), false, CURRENT_TIMESTAMP),
('Váy Xòe Dự Tiệc', '<p>Váy xòe dự tiệc sang trọng.</p><p>Thiết kế cúp ngực ôm eo tôn dáng.</p>', (SELECT id FROM categories WHERE name = 'Váy'), false, CURRENT_TIMESTAMP),
('Chân Váy Jean Xẻ Tà', '<p>Chân váy jean xẻ tà cá tính.</p><p>Phong cách trẻ trung năng động.</p>', (SELECT id FROM categories WHERE name = 'Váy'), false, CURRENT_TIMESTAMP),

-- Đầm
('Đầm Maxi Hoa Nhí', '<p>Đầm maxi họa tiết hoa nhí nữ tính.</p><p>Chất vải voan mềm mại, thoáng mát.</p>', (SELECT id FROM categories WHERE name = 'Đầm'), false, CURRENT_TIMESTAMP),
('Đầm Suông Công Sở', '<p>Đầm suông công sở thanh lịch.</p><p>Thiết kế tối giản, dễ mặc.</p>', (SELECT id FROM categories WHERE name = 'Đầm'), false, CURRENT_TIMESTAMP),
('Đầm Dự Tiệc Sang Trọng', '<p>Đầm dự tiệc thiết kế cao cấp.</p><p>Form dáng ôm body tôn dáng.</p>', (SELECT id FROM categories WHERE name = 'Đầm'), false, CURRENT_TIMESTAMP);

-- ============================================
-- PRODUCT VARIANTS - Biến thể sản phẩm
-- ============================================

-- Áo Thun Nam Basic Cotton (product_id=1) - 6 variants
INSERT INTO product_variants (sku, price, compare_at_price, stock, product_id, image_url, is_deleted, created_at) VALUES
('ATBC-DEN-M', 199000, 250000, 50, 1, 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=800', false, CURRENT_TIMESTAMP),
('ATBC-DEN-L', 199000, 250000, 45, 1, 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=800', false, CURRENT_TIMESTAMP),
('ATBC-TRANG-M', 199000, 250000, 40, 1, 'https://images.unsplash.com/photo-1583743814966-8936f5b7be1a?w=800', false, CURRENT_TIMESTAMP),
('ATBC-TRANG-L', 199000, 250000, 35, 1, 'https://images.unsplash.com/photo-1583743814966-8936f5b7be1a?w=800', false, CURRENT_TIMESTAMP),
('ATBC-XAM-M', 199000, 250000, 30, 1, 'https://images.unsplash.com/photo-1562157873-818bc0726f68?w=800', false, CURRENT_TIMESTAMP),
('ATBC-XAM-L', 199000, 250000, 25, 1, 'https://images.unsplash.com/photo-1562157873-818bc0726f68?w=800', false, CURRENT_TIMESTAMP);

-- Áo Polo Nam (product_id=2)
INSERT INTO product_variants (sku, price, compare_at_price, stock, product_id, image_url, is_deleted, created_at) VALUES
('POLO-DEN-M', 299000, 350000, 40, 2, 'https://images.unsplash.com/photo-1586790170083-2f9ceadc732d?w=800', false, CURRENT_TIMESTAMP),
('POLO-DEN-L', 299000, 350000, 35, 2, 'https://images.unsplash.com/photo-1586790170083-2f9ceadc732d?w=800', false, CURRENT_TIMESTAMP),
('POLO-NAVY-M', 299000, 350000, 30, 2, 'https://images.unsplash.com/photo-1626497764679-6725f9390a07?w=800', false, CURRENT_TIMESTAMP),
('POLO-NAVY-L', 299000, 350000, 25, 2, 'https://images.unsplash.com/photo-1626497764679-6725f9390a07?w=800', false, CURRENT_TIMESTAMP);

-- Áo Thun Oversize (product_id=3)
INSERT INTO product_variants (sku, price, compare_at_price, stock, product_id, image_url, is_deleted, created_at) VALUES
('OVER-DEN-L', 249000, 299000, 60, 3, 'https://images.unsplash.com/photo-1562157873-818bc0726f68?w=800', false, CURRENT_TIMESTAMP),
('OVER-DEN-XL', 249000, 299000, 50, 3, 'https://images.unsplash.com/photo-1562157873-818bc0726f68?w=800', false, CURRENT_TIMESTAMP),
('OVER-TRANG-L', 249000, 299000, 45, 3, 'https://images.unsplash.com/photo-1583743814966-8936f5b7be1a?w=800', false, CURRENT_TIMESTAMP),
('OVER-TRANG-XL', 249000, 299000, 40, 3, 'https://images.unsplash.com/photo-1583743814966-8936f5b7be1a?w=800', false, CURRENT_TIMESTAMP);

-- Áo Sơ Mi Trắng (product_id=4)
INSERT INTO product_variants (sku, price, compare_at_price, stock, product_id, image_url, is_deleted, created_at) VALUES
('SM-TRANG-M', 279000, 320000, 55, 4, 'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=800', false, CURRENT_TIMESTAMP),
('SM-TRANG-L', 279000, 320000, 50, 4, 'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=800', false, CURRENT_TIMESTAMP),
('SM-TRANG-XL', 279000, 320000, 40, 4, 'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=800', false, CURRENT_TIMESTAMP);

-- Áo Sơ Mi Kẻ (product_id=5)
INSERT INTO product_variants (sku, price, compare_at_price, stock, product_id, image_url, is_deleted, created_at) VALUES
('SMK-NAVY-M', 289000, 340000, 45, 5, 'https://images.unsplash.com/photo-1603252109303-2751441dd157?w=800', false, CURRENT_TIMESTAMP),
('SMK-NAVY-L', 289000, 340000, 40, 5, 'https://images.unsplash.com/photo-1603252109303-2751441dd157?w=800', false, CURRENT_TIMESTAMP);

-- Áo Sơ Mi Linen (product_id=6)
INSERT INTO product_variants (sku, price, compare_at_price, stock, product_id, image_url, is_deleted, created_at) VALUES
('LINEN-BE-M', 349000, 420000, 35, 6, 'https://images.unsplash.com/photo-1598032895397-b9c88a8df1f7?w=800', false, CURRENT_TIMESTAMP),
('LINEN-BE-L', 349000, 420000, 30, 6, 'https://images.unsplash.com/photo-1598032895397-b9c88a8df1f7?w=800', false, CURRENT_TIMESTAMP),
('LINEN-TRANG-M', 349000, 420000, 25, 6, 'https://images.unsplash.com/photo-1620799139834-6b8f844a2ecc?w=800', false, CURRENT_TIMESTAMP);

-- Áo Khoác Jean (product_id=7)
INSERT INTO product_variants (sku, price, compare_at_price, stock, product_id, image_url, is_deleted, created_at) VALUES
('JEAN-DENIM-M', 599000, 750000, 30, 7, 'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=800', false, CURRENT_TIMESTAMP),
('JEAN-DENIM-L', 599000, 750000, 25, 7, 'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=800', false, CURRENT_TIMESTAMP),
('JEAN-DENIM-XL', 599000, 750000, 20, 7, 'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=800', false, CURRENT_TIMESTAMP);

-- Áo Hoodie (product_id=8)
INSERT INTO product_variants (sku, price, compare_at_price, stock, product_id, image_url, is_deleted, created_at) VALUES
('HOOD-DEN-L', 399000, 480000, 50, 8, 'https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=800', false, CURRENT_TIMESTAMP),
('HOOD-DEN-XL', 399000, 480000, 45, 8, 'https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=800', false, CURRENT_TIMESTAMP),
('HOOD-XAM-L', 399000, 480000, 40, 8, 'https://images.unsplash.com/photo-1578587018452-892bacefd3f2?w=800', false, CURRENT_TIMESTAMP),
('HOOD-XAM-XL', 399000, 480000, 35, 8, 'https://images.unsplash.com/photo-1578587018452-892bacefd3f2?w=800', false, CURRENT_TIMESTAMP);

-- Áo Bomber (product_id=9)
INSERT INTO product_variants (sku, price, compare_at_price, stock, product_id, image_url, is_deleted, created_at) VALUES
('BOMBER-DEN-M', 699000, 850000, 20, 9, 'https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=800', false, CURRENT_TIMESTAMP),
('BOMBER-DEN-L', 699000, 850000, 18, 9, 'https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=800', false, CURRENT_TIMESTAMP),
('BOMBER-NAU-M', 699000, 850000, 15, 9, 'https://images.unsplash.com/photo-1578932750294-f5075e85f44a?w=800', false, CURRENT_TIMESTAMP);

-- Quần Jean Nam (product_id=10)
INSERT INTO product_variants (sku, price, compare_at_price, stock, product_id, image_url, is_deleted, created_at) VALUES
('JEAN-DEN-29', 449000, 550000, 40, 10, 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=800', false, CURRENT_TIMESTAMP),
('JEAN-DEN-30', 449000, 550000, 45, 10, 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=800', false, CURRENT_TIMESTAMP),
('JEAN-DEN-31', 449000, 550000, 40, 10, 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=800', false, CURRENT_TIMESTAMP),
('JEAN-DEN-32', 449000, 550000, 35, 10, 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=800', false, CURRENT_TIMESTAMP);

-- Quần Tây (product_id=11)
INSERT INTO product_variants (sku, price, compare_at_price, stock, product_id, image_url, is_deleted, created_at) VALUES
('TAY-DEN-29', 399000, 480000, 35, 11, 'https://images.unsplash.com/photo-1624378439575-d8705ad7ae80?w=800', false, CURRENT_TIMESTAMP),
('TAY-DEN-30', 399000, 480000, 40, 11, 'https://images.unsplash.com/photo-1624378439575-d8705ad7ae80?w=800', false, CURRENT_TIMESTAMP),
('TAY-XAM-29', 399000, 480000, 30, 11, 'https://images.unsplash.com/photo-1594938291221-94f18cbb5660?w=800', false, CURRENT_TIMESTAMP),
('TAY-XAM-30', 399000, 480000, 35, 11, 'https://images.unsplash.com/photo-1594938291221-94f18cbb5660?w=800', false, CURRENT_TIMESTAMP);

-- Quần Kaki (product_id=12)
INSERT INTO product_variants (sku, price, compare_at_price, stock, product_id, image_url, is_deleted, created_at) VALUES
('KAKI-BE-30', 379000, 450000, 45, 12, 'https://images.unsplash.com/photo-1473966968600-fa801b869a1a?w=800', false, CURRENT_TIMESTAMP),
('KAKI-BE-31', 379000, 450000, 40, 12, 'https://images.unsplash.com/photo-1473966968600-fa801b869a1a?w=800', false, CURRENT_TIMESTAMP),
('KAKI-NAU-30', 379000, 450000, 35, 12, 'https://images.unsplash.com/photo-1506629082955-511b1aa562c8?w=800', false, CURRENT_TIMESTAMP);

-- Áo Croptop Nữ (product_id=13)
INSERT INTO product_variants (sku, price, compare_at_price, stock, product_id, image_url, is_deleted, created_at) VALUES
('CROP-DEN-S', 149000, 199000, 60, 13, 'https://images.unsplash.com/photo-1618354691373-d851c5c3a990?w=800', false, CURRENT_TIMESTAMP),
('CROP-DEN-M', 149000, 199000, 55, 13, 'https://images.unsplash.com/photo-1618354691373-d851c5c3a990?w=800', false, CURRENT_TIMESTAMP),
('CROP-TRANG-S', 149000, 199000, 50, 13, 'https://images.unsplash.com/photo-1581044777550-4cfa60707c03?w=800', false, CURRENT_TIMESTAMP),
('CROP-TRANG-M', 149000, 199000, 45, 13, 'https://images.unsplash.com/photo-1581044777550-4cfa60707c03?w=800', false, CURRENT_TIMESTAMP);

-- Áo Kiểu Tay Bồng (product_id=14)
INSERT INTO product_variants (sku, price, compare_at_price, stock, product_id, image_url, is_deleted, created_at) VALUES
('KIEU-TRANG-M', 329000, 399000, 40, 14, 'https://images.unsplash.com/photo-1485968579580-b6d095142e6e?w=800', false, CURRENT_TIMESTAMP),
('KIEU-TRANG-L', 329000, 399000, 35, 14, 'https://images.unsplash.com/photo-1485968579580-b6d095142e6e?w=800', false, CURRENT_TIMESTAMP),
('KIEU-HONG-M', 329000, 399000, 30, 14, 'https://images.unsplash.com/photo-1564859228273-274232fdb516?w=800', false, CURRENT_TIMESTAMP);

-- Áo Sơ Mi Lụa Nữ (product_id=15)
INSERT INTO product_variants (sku, price, compare_at_price, stock, product_id, image_url, is_deleted, created_at) VALUES
('SMNU-TRANG-M', 399000, 499000, 35, 15, 'https://images.unsplash.com/photo-1594846497743-9f0dd0d6d3b6?w=800', false, CURRENT_TIMESTAMP),
('SMNU-TRANG-L', 399000, 499000, 30, 15, 'https://images.unsplash.com/photo-1594846497743-9f0dd0d6d3b6?w=800', false, CURRENT_TIMESTAMP);

-- Áo Blazer Nữ (product_id=16)
INSERT INTO product_variants (sku, price, compare_at_price, stock, product_id, image_url, is_deleted, created_at) VALUES
('BLAZ-DEN-M', 699000, 850000, 25, 16, 'https://images.unsplash.com/photo-1591369822096-ffd140ec948f?w=800', false, CURRENT_TIMESTAMP),
('BLAZ-DEN-L', 699000, 850000, 20, 16, 'https://images.unsplash.com/photo-1591369822096-ffd140ec948f?w=800', false, CURRENT_TIMESTAMP),
('BLAZ-BE-M', 699000, 850000, 18, 16, 'https://images.unsplash.com/photo-1594223515816-1ad1f9166a4b?w=800', false, CURRENT_TIMESTAMP);

-- Quần Jean Nữ (product_id=17)
INSERT INTO product_variants (sku, price, compare_at_price, stock, product_id, image_url, is_deleted, created_at) VALUES
('JEANNU-DENIM-28', 429000, 520000, 40, 17, 'https://images.unsplash.com/photo-1582552938357-32b906df40cb?w=800', false, CURRENT_TIMESTAMP),
('JEANNU-DENIM-29', 429000, 520000, 45, 17, 'https://images.unsplash.com/photo-1582552938357-32b906df40cb?w=800', false, CURRENT_TIMESTAMP),
('JEANNU-DENIM-30', 429000, 520000, 35, 17, 'https://images.unsplash.com/photo-1582552938357-32b906df40cb?w=800', false, CURRENT_TIMESTAMP);

-- Chân Váy Midi (product_id=18)
INSERT INTO product_variants (sku, price, compare_at_price, stock, product_id, image_url, is_deleted, created_at) VALUES
('VAY-DEN-S', 299000, 380000, 35, 18, 'https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?w=800', false, CURRENT_TIMESTAMP),
('VAY-DEN-M', 299000, 380000, 40, 18, 'https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?w=800', false, CURRENT_TIMESTAMP),
('VAY-BE-S', 299000, 380000, 30, 18, 'https://images.unsplash.com/photo-1566174053879-31528523f8ae?w=800', false, CURRENT_TIMESTAMP);

-- Đầm Maxi (product_id=19)
INSERT INTO product_variants (sku, price, compare_at_price, stock, product_id, image_url, is_deleted, created_at) VALUES
('MAXI-HOA-M', 549000, 680000, 25, 19, 'https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=800', false, CURRENT_TIMESTAMP),
('MAXI-HOA-L', 549000, 680000, 20, 19, 'https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=800', false, CURRENT_TIMESTAMP);

-- Váy Dự Tiệc (product_id=20)
INSERT INTO product_variants (sku, price, compare_at_price, stock, product_id, image_url, is_deleted, created_at) VALUES
('TIEC-DO-M', 799000, 999000, 15, 20, 'https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=800', false, CURRENT_TIMESTAMP),
('TIEC-DO-L', 799000, 999000, 12, 20, 'https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=800', false, CURRENT_TIMESTAMP),
('TIEC-DEN-M', 799000, 999000, 10, 20, 'https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=800', false, CURRENT_TIMESTAMP);

-- ============================================
-- PRODUCT VARIANT VALUES - Kết nối variant với attribute values
-- ============================================

-- Helper: Lấy ID của attribute values
-- Màu: Đen=1, Trắng=2, Navy=3, Denim=4, Be=5, Xám=6, Hồng=7, Đỏ=8, Vàng=9, Nâu=10
-- Size: S=11, M=12, L=13, XL=14, XXL=15, 28=16, 29=17, 30=18, 31=19, 32=20

-- Áo Thun Basic Cotton (variants 1-6)
INSERT INTO product_variant_values (variant_id, attribute_value_id, created_at) VALUES
(1, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(1, (SELECT id FROM attribute_values WHERE value = 'M' LIMIT 1), CURRENT_TIMESTAMP),
(2, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(2, (SELECT id FROM attribute_values WHERE value = 'L' LIMIT 1), CURRENT_TIMESTAMP),
(3, (SELECT id FROM attribute_values WHERE value = 'Trắng' LIMIT 1), CURRENT_TIMESTAMP),
(3, (SELECT id FROM attribute_values WHERE value = 'M' LIMIT 1), CURRENT_TIMESTAMP),
(4, (SELECT id FROM attribute_values WHERE value = 'Trắng' LIMIT 1), CURRENT_TIMESTAMP),
(4, (SELECT id FROM attribute_values WHERE value = 'L' LIMIT 1), CURRENT_TIMESTAMP),
(5, (SELECT id FROM attribute_values WHERE value = 'Xám' LIMIT 1), CURRENT_TIMESTAMP),
(5, (SELECT id FROM attribute_values WHERE value = 'M' LIMIT 1), CURRENT_TIMESTAMP),
(6, (SELECT id FROM attribute_values WHERE value = 'Xám' LIMIT 1), CURRENT_TIMESTAMP),
(6, (SELECT id FROM attribute_values WHERE value = 'L' LIMIT 1), CURRENT_TIMESTAMP);

-- Áo Polo (variants 7-10)
INSERT INTO product_variant_values (variant_id, attribute_value_id, created_at) VALUES
(7, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(7, (SELECT id FROM attribute_values WHERE value = 'M' LIMIT 1), CURRENT_TIMESTAMP),
(8, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(8, (SELECT id FROM attribute_values WHERE value = 'L' LIMIT 1), CURRENT_TIMESTAMP),
(9, (SELECT id FROM attribute_values WHERE value = 'Xanh Navy' LIMIT 1), CURRENT_TIMESTAMP),
(9, (SELECT id FROM attribute_values WHERE value = 'M' LIMIT 1), CURRENT_TIMESTAMP),
(10, (SELECT id FROM attribute_values WHERE value = 'Xanh Navy' LIMIT 1), CURRENT_TIMESTAMP),
(10, (SELECT id FROM attribute_values WHERE value = 'L' LIMIT 1), CURRENT_TIMESTAMP);

-- Áo Oversize (variants 11-14)
INSERT INTO product_variant_values (variant_id, attribute_value_id, created_at) VALUES
(11, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(11, (SELECT id FROM attribute_values WHERE value = 'L' LIMIT 1), CURRENT_TIMESTAMP),
(12, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(12, (SELECT id FROM attribute_values WHERE value = 'XL' LIMIT 1), CURRENT_TIMESTAMP),
(13, (SELECT id FROM attribute_values WHERE value = 'Trắng' LIMIT 1), CURRENT_TIMESTAMP),
(13, (SELECT id FROM attribute_values WHERE value = 'L' LIMIT 1), CURRENT_TIMESTAMP),
(14, (SELECT id FROM attribute_values WHERE value = 'Trắng' LIMIT 1), CURRENT_TIMESTAMP),
(14, (SELECT id FROM attribute_values WHERE value = 'XL' LIMIT 1), CURRENT_TIMESTAMP);

-- Áo Sơ Mi Trắng (variants 15-17)
INSERT INTO product_variant_values (variant_id, attribute_value_id, created_at) VALUES
(15, (SELECT id FROM attribute_values WHERE value = 'Trắng' LIMIT 1), CURRENT_TIMESTAMP),
(15, (SELECT id FROM attribute_values WHERE value = 'M' LIMIT 1), CURRENT_TIMESTAMP),
(16, (SELECT id FROM attribute_values WHERE value = 'Trắng' LIMIT 1), CURRENT_TIMESTAMP),
(16, (SELECT id FROM attribute_values WHERE value = 'L' LIMIT 1), CURRENT_TIMESTAMP),
(17, (SELECT id FROM attribute_values WHERE value = 'Trắng' LIMIT 1), CURRENT_TIMESTAMP),
(17, (SELECT id FROM attribute_values WHERE value = 'XL' LIMIT 1), CURRENT_TIMESTAMP);

-- Áo Sơ Mi Kẻ (variants 18-19)
INSERT INTO product_variant_values (variant_id, attribute_value_id, created_at) VALUES
(18, (SELECT id FROM attribute_values WHERE value = 'Xanh Navy' LIMIT 1), CURRENT_TIMESTAMP),
(18, (SELECT id FROM attribute_values WHERE value = 'M' LIMIT 1), CURRENT_TIMESTAMP),
(19, (SELECT id FROM attribute_values WHERE value = 'Xanh Navy' LIMIT 1), CURRENT_TIMESTAMP),
(19, (SELECT id FROM attribute_values WHERE value = 'L' LIMIT 1), CURRENT_TIMESTAMP);

-- Áo Sơ Mi Linen (variants 20-22)
INSERT INTO product_variant_values (variant_id, attribute_value_id, created_at) VALUES
(20, (SELECT id FROM attribute_values WHERE value = 'Be' LIMIT 1), CURRENT_TIMESTAMP),
(20, (SELECT id FROM attribute_values WHERE value = 'M' LIMIT 1), CURRENT_TIMESTAMP),
(21, (SELECT id FROM attribute_values WHERE value = 'Be' LIMIT 1), CURRENT_TIMESTAMP),
(21, (SELECT id FROM attribute_values WHERE value = 'L' LIMIT 1), CURRENT_TIMESTAMP),
(22, (SELECT id FROM attribute_values WHERE value = 'Trắng' LIMIT 1), CURRENT_TIMESTAMP),
(22, (SELECT id FROM attribute_values WHERE value = 'M' LIMIT 1), CURRENT_TIMESTAMP);

-- Áo Khoác Jean (variants 23-25)
INSERT INTO product_variant_values (variant_id, attribute_value_id, created_at) VALUES
(23, (SELECT id FROM attribute_values WHERE value = 'Xanh Denim' LIMIT 1), CURRENT_TIMESTAMP),
(23, (SELECT id FROM attribute_values WHERE value = 'M' LIMIT 1), CURRENT_TIMESTAMP),
(24, (SELECT id FROM attribute_values WHERE value = 'Xanh Denim' LIMIT 1), CURRENT_TIMESTAMP),
(24, (SELECT id FROM attribute_values WHERE value = 'L' LIMIT 1), CURRENT_TIMESTAMP),
(25, (SELECT id FROM attribute_values WHERE value = 'Xanh Denim' LIMIT 1), CURRENT_TIMESTAMP),
(25, (SELECT id FROM attribute_values WHERE value = 'XL' LIMIT 1), CURRENT_TIMESTAMP);

-- Áo Hoodie (variants 26-29)
INSERT INTO product_variant_values (variant_id, attribute_value_id, created_at) VALUES
(26, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(26, (SELECT id FROM attribute_values WHERE value = 'L' LIMIT 1), CURRENT_TIMESTAMP),
(27, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(27, (SELECT id FROM attribute_values WHERE value = 'XL' LIMIT 1), CURRENT_TIMESTAMP),
(28, (SELECT id FROM attribute_values WHERE value = 'Xám' LIMIT 1), CURRENT_TIMESTAMP),
(28, (SELECT id FROM attribute_values WHERE value = 'L' LIMIT 1), CURRENT_TIMESTAMP),
(29, (SELECT id FROM attribute_values WHERE value = 'Xám' LIMIT 1), CURRENT_TIMESTAMP),
(29, (SELECT id FROM attribute_values WHERE value = 'XL' LIMIT 1), CURRENT_TIMESTAMP);

-- Áo Bomber (variants 30-32)
INSERT INTO product_variant_values (variant_id, attribute_value_id, created_at) VALUES
(30, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(30, (SELECT id FROM attribute_values WHERE value = 'M' LIMIT 1), CURRENT_TIMESTAMP),
(31, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(31, (SELECT id FROM attribute_values WHERE value = 'L' LIMIT 1), CURRENT_TIMESTAMP),
(32, (SELECT id FROM attribute_values WHERE value = 'Nâu' LIMIT 1), CURRENT_TIMESTAMP),
(32, (SELECT id FROM attribute_values WHERE value = 'M' LIMIT 1), CURRENT_TIMESTAMP);

-- Quần Jean Nam (variants 33-36)
INSERT INTO product_variant_values (variant_id, attribute_value_id, created_at) VALUES
(33, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(33, (SELECT id FROM attribute_values WHERE value = '29' LIMIT 1), CURRENT_TIMESTAMP),
(34, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(34, (SELECT id FROM attribute_values WHERE value = '30' LIMIT 1), CURRENT_TIMESTAMP),
(35, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(35, (SELECT id FROM attribute_values WHERE value = '31' LIMIT 1), CURRENT_TIMESTAMP),
(36, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(36, (SELECT id FROM attribute_values WHERE value = '32' LIMIT 1), CURRENT_TIMESTAMP);

-- Quần Tây Nam (variants 37-40)
INSERT INTO product_variant_values (variant_id, attribute_value_id, created_at) VALUES
(37, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(37, (SELECT id FROM attribute_values WHERE value = '29' LIMIT 1), CURRENT_TIMESTAMP),
(38, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(38, (SELECT id FROM attribute_values WHERE value = '30' LIMIT 1), CURRENT_TIMESTAMP),
(39, (SELECT id FROM attribute_values WHERE value = 'Xám' LIMIT 1), CURRENT_TIMESTAMP),
(39, (SELECT id FROM attribute_values WHERE value = '29' LIMIT 1), CURRENT_TIMESTAMP),
(40, (SELECT id FROM attribute_values WHERE value = 'Xám' LIMIT 1), CURRENT_TIMESTAMP),
(40, (SELECT id FROM attribute_values WHERE value = '30' LIMIT 1), CURRENT_TIMESTAMP);

-- Quần Kaki Nam (variants 41-43)
INSERT INTO product_variant_values (variant_id, attribute_value_id, created_at) VALUES
(41, (SELECT id FROM attribute_values WHERE value = 'Be' LIMIT 1), CURRENT_TIMESTAMP),
(41, (SELECT id FROM attribute_values WHERE value = '30' LIMIT 1), CURRENT_TIMESTAMP),
(42, (SELECT id FROM attribute_values WHERE value = 'Be' LIMIT 1), CURRENT_TIMESTAMP),
(42, (SELECT id FROM attribute_values WHERE value = '31' LIMIT 1), CURRENT_TIMESTAMP),
(43, (SELECT id FROM attribute_values WHERE value = 'Nâu' LIMIT 1), CURRENT_TIMESTAMP),
(43, (SELECT id FROM attribute_values WHERE value = '30' LIMIT 1), CURRENT_TIMESTAMP);

-- Áo Croptop Nữ (variants 44-47)
INSERT INTO product_variant_values (variant_id, attribute_value_id, created_at) VALUES
(44, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(44, (SELECT id FROM attribute_values WHERE value = 'S' LIMIT 1), CURRENT_TIMESTAMP),
(45, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(45, (SELECT id FROM attribute_values WHERE value = 'M' LIMIT 1), CURRENT_TIMESTAMP),
(46, (SELECT id FROM attribute_values WHERE value = 'Trắng' LIMIT 1), CURRENT_TIMESTAMP),
(46, (SELECT id FROM attribute_values WHERE value = 'S' LIMIT 1), CURRENT_TIMESTAMP),
(47, (SELECT id FROM attribute_values WHERE value = 'Trắng' LIMIT 1), CURRENT_TIMESTAMP),
(47, (SELECT id FROM attribute_values WHERE value = 'M' LIMIT 1), CURRENT_TIMESTAMP);

-- Áo Kiểu Tay Bồng (variants 48-50)
INSERT INTO product_variant_values (variant_id, attribute_value_id, created_at) VALUES
(48, (SELECT id FROM attribute_values WHERE value = 'Trắng' LIMIT 1), CURRENT_TIMESTAMP),
(48, (SELECT id FROM attribute_values WHERE value = 'M' LIMIT 1), CURRENT_TIMESTAMP),
(49, (SELECT id FROM attribute_values WHERE value = 'Trắng' LIMIT 1), CURRENT_TIMESTAMP),
(49, (SELECT id FROM attribute_values WHERE value = 'L' LIMIT 1), CURRENT_TIMESTAMP),
(50, (SELECT id FROM attribute_values WHERE value = 'Hồng' LIMIT 1), CURRENT_TIMESTAMP),
(50, (SELECT id FROM attribute_values WHERE value = 'M' LIMIT 1), CURRENT_TIMESTAMP);

-- Áo Sơ Mi Lụa Nữ (variants 51-52)
INSERT INTO product_variant_values (variant_id, attribute_value_id, created_at) VALUES
(51, (SELECT id FROM attribute_values WHERE value = 'Trắng' LIMIT 1), CURRENT_TIMESTAMP),
(51, (SELECT id FROM attribute_values WHERE value = 'M' LIMIT 1), CURRENT_TIMESTAMP),
(52, (SELECT id FROM attribute_values WHERE value = 'Trắng' LIMIT 1), CURRENT_TIMESTAMP),
(52, (SELECT id FROM attribute_values WHERE value = 'L' LIMIT 1), CURRENT_TIMESTAMP);

-- Áo Blazer Nữ (variants 53-55)
INSERT INTO product_variant_values (variant_id, attribute_value_id, created_at) VALUES
(53, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(53, (SELECT id FROM attribute_values WHERE value = 'M' LIMIT 1), CURRENT_TIMESTAMP),
(54, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(54, (SELECT id FROM attribute_values WHERE value = 'L' LIMIT 1), CURRENT_TIMESTAMP),
(55, (SELECT id FROM attribute_values WHERE value = 'Be' LIMIT 1), CURRENT_TIMESTAMP),
(55, (SELECT id FROM attribute_values WHERE value = 'M' LIMIT 1), CURRENT_TIMESTAMP);

-- Quần Jean Nữ (variants 56-58)
INSERT INTO product_variant_values (variant_id, attribute_value_id, created_at) VALUES
(56, (SELECT id FROM attribute_values WHERE value = 'Xanh Denim' LIMIT 1), CURRENT_TIMESTAMP),
(56, (SELECT id FROM attribute_values WHERE value = '28' LIMIT 1), CURRENT_TIMESTAMP),
(57, (SELECT id FROM attribute_values WHERE value = 'Xanh Denim' LIMIT 1), CURRENT_TIMESTAMP),
(57, (SELECT id FROM attribute_values WHERE value = '29' LIMIT 1), CURRENT_TIMESTAMP),
(58, (SELECT id FROM attribute_values WHERE value = 'Xanh Denim' LIMIT 1), CURRENT_TIMESTAMP),
(58, (SELECT id FROM attribute_values WHERE value = '30' LIMIT 1), CURRENT_TIMESTAMP);

-- Chân Váy Midi (variants 59-61)
INSERT INTO product_variant_values (variant_id, attribute_value_id, created_at) VALUES
(59, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(59, (SELECT id FROM attribute_values WHERE value = 'S' LIMIT 1), CURRENT_TIMESTAMP),
(60, (SELECT id FROM attribute_values WHERE value = 'Đen' LIMIT 1), CURRENT_TIMESTAMP),
(60, (SELECT id FROM attribute_values WHERE value = 'M' LIMIT 1), CURRENT_TIMESTAMP),
(61, (SELECT id FROM attribute_values WHERE value = 'Be' LIMIT 1), CURRENT_TIMESTAMP),
(61, (SELECT id FROM attribute_values WHERE value = 'S' LIMIT 1), CURRENT_TIMESTAMP);

-- Đầm Maxi (variants 62-63)
INSERT INTO product_variant_values (variant_id, attribute_value_id, created_at) VALUES
(62, (SELECT id FROM attribute_values WHERE value = 'Hồng' LIMIT 1), CURRENT_TIMESTAMP),
(62, (SELECT id FROM attribute_values WHERE value = 'M' LIMIT 1), CURRENT_TIMESTAMP),
(63, (SELECT id FROM attribute_values WHERE value = 'Hồng' LIMIT 1), CURRENT_TIMESTAMP),
(63, (SELECT id FROM attribute_values WHERE value = 'L' LIMIT 1), CURRENT_TIMESTAMP);

-- ============================================
-- PRODUCT IMAGES - Hình ảnh sản phẩm (chỉ ảnh hiển thị của product, không liên kết variant)
-- ============================================

-- Áo Thun Nam Basic (product_id=1)
INSERT INTO product_images (image_url, alt_text, is_thumbnail, display_order, product_id, created_at) VALUES
('https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=800', 'Áo thun nam basic đen', true, 1, 1, CURRENT_TIMESTAMP),
('https://images.unsplash.com/photo-1583743814966-8936f5b7be1a?w=800', 'Áo thun nam basic trắng', false, 2, 1, CURRENT_TIMESTAMP),
('https://images.unsplash.com/photo-1562157873-818bc0726f68?w=800', 'Áo thun nam basic xám', false, 3, 1, CURRENT_TIMESTAMP);

-- Áo Polo (product_id=2)
INSERT INTO product_images (image_url, alt_text, is_thumbnail, display_order, product_id, created_at) VALUES
('https://images.unsplash.com/photo-1586790170083-2f9ceadc732d?w=800', 'Áo polo nam đen', true, 1, 2, CURRENT_TIMESTAMP),
('https://images.unsplash.com/photo-1626497764679-6725f9390a07?w=800', 'Áo polo nam xanh navy', false, 2, 2, CURRENT_TIMESTAMP);

-- Áo Oversize (product_id=3)
INSERT INTO product_images (image_url, alt_text, is_thumbnail, display_order, product_id, created_at) VALUES
('https://images.unsplash.com/photo-1562157873-818bc0726f68?w=800', 'Áo thun oversize đen', true, 1, 3, CURRENT_TIMESTAMP),
('https://images.unsplash.com/photo-1583743814966-8936f5b7be1a?w=800', 'Áo thun oversize trắng', false, 2, 3, CURRENT_TIMESTAMP);

-- Áo Sơ Mi (product_id=4)
INSERT INTO product_images (image_url, alt_text, is_thumbnail, display_order, product_id, created_at) VALUES
('https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=800', 'Áo sơ mi trắng công sở', true, 1, 4, CURRENT_TIMESTAMP),
('https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=800', 'Chi tiết áo sơ mi', false, 2, 4, CURRENT_TIMESTAMP);

-- Áo Sơ Mi Kẻ (product_id=5)
INSERT INTO product_images (image_url, alt_text, is_thumbnail, display_order, product_id, created_at) VALUES
('https://images.unsplash.com/photo-1603252109303-2751441dd157?w=800', 'Áo sơ mi kẻ sọc navy', true, 1, 5, CURRENT_TIMESTAMP),
('https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=800', 'Chi tiết áo sơ mi kẻ', false, 2, 5, CURRENT_TIMESTAMP);

-- Áo Sơ Mi Linen (product_id=6)
INSERT INTO product_images (image_url, alt_text, is_thumbnail, display_order, product_id, created_at) VALUES
('https://images.unsplash.com/photo-1598032895397-b9c88a8df1f7?w=800', 'Áo sơ mi linen be', true, 1, 6, CURRENT_TIMESTAMP),
('https://images.unsplash.com/photo-1620799139834-6b8f844a2ecc?w=800', 'Áo sơ mi linen trắng', false, 2, 6, CURRENT_TIMESTAMP);

-- Áo Khoác Jean (product_id=7)
INSERT INTO product_images (image_url, alt_text, is_thumbnail, display_order, product_id, created_at) VALUES
('https://images.unsplash.com/photo-1551028719-00167b16eac5?w=800', 'Áo khoác jean vintage', true, 1, 7, CURRENT_TIMESTAMP),
('https://images.unsplash.com/photo-1576566588028-4147f3842f27?w=800', 'Detail áo khoác jean', false, 2, 7, CURRENT_TIMESTAMP);

-- Áo Hoodie (product_id=8)
INSERT INTO product_images (image_url, alt_text, is_thumbnail, display_order, product_id, created_at) VALUES
('https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=800', 'Áo hoodie đen', true, 1, 8, CURRENT_TIMESTAMP),
('https://images.unsplash.com/photo-1578587018452-892bacefd3f2?w=800', 'Áo hoodie xám', false, 2, 8, CURRENT_TIMESTAMP);

-- Áo Bomber (product_id=9)
INSERT INTO product_images (image_url, alt_text, is_thumbnail, display_order, product_id, created_at) VALUES
('https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=800', 'Áo bomber đen', true, 1, 9, CURRENT_TIMESTAMP),
('https://images.unsplash.com/photo-1578932750294-f5075e85f44a?w=800', 'Áo bomber nâu', false, 2, 9, CURRENT_TIMESTAMP);

-- Quần Jean Nam (product_id=10)
INSERT INTO product_images (image_url, alt_text, is_thumbnail, display_order, product_id, created_at) VALUES
('https://images.unsplash.com/photo-1542272604-787c3835535d?w=800', 'Quần jean nam slim fit', true, 1, 10, CURRENT_TIMESTAMP),
('https://images.unsplash.com/photo-1473966968600-fa801b869a1a?w=800', 'Chi tiết quần jean', false, 2, 10, CURRENT_TIMESTAMP);

-- Quần Jean Straight (product_id=11)
INSERT INTO product_images (image_url, alt_text, is_thumbnail, display_order, product_id, created_at) VALUES
('https://images.unsplash.com/photo-1542272604-787c3835535d?w=800', 'Quần jean straight đen', true, 1, 11, CURRENT_TIMESTAMP),
('https://images.unsplash.com/photo-1604176354204-9268737828e4?w=800', 'Chi tiết quần jean', false, 2, 11, CURRENT_TIMESTAMP);

-- Quần Tây Nam (product_id=12)
INSERT INTO product_images (image_url, alt_text, is_thumbnail, display_order, product_id, created_at) VALUES
('https://images.unsplash.com/photo-1624378439575-d8705ad7ae80?w=800', 'Quần tây công sở đen', true, 1, 12, CURRENT_TIMESTAMP),
('https://images.unsplash.com/photo-1594938291221-94f18cbb5660?w=800', 'Quần tây xám', false, 2, 12, CURRENT_TIMESTAMP);

-- Áo Croptop Nữ (product_id=13)
INSERT INTO product_images (image_url, alt_text, is_thumbnail, display_order, product_id, created_at) VALUES
('https://images.unsplash.com/photo-1618354691373-d851c5c3a990?w=800', 'Áo croptop nữ đen', true, 1, 13, CURRENT_TIMESTAMP),
('https://images.unsplash.com/photo-1581044777550-4cfa60707c03?w=800', 'Áo croptop nữ trắng', false, 2, 13, CURRENT_TIMESTAMP);

-- Áo Kiểu Tay Bồng (product_id=14)
INSERT INTO product_images (image_url, alt_text, is_thumbnail, display_order, product_id, created_at) VALUES
('https://images.unsplash.com/photo-1485968579580-b6d095142e6e?w=800', 'Áo kiểu tay bồng trắng', true, 1, 14, CURRENT_TIMESTAMP),
('https://images.unsplash.com/photo-1564859228273-274232fdb516?w=800', 'Áo kiểu hồng', false, 2, 14, CURRENT_TIMESTAMP);

-- Áo Sơ Mi Lụa Nữ (product_id=15)
INSERT INTO product_images (image_url, alt_text, is_thumbnail, display_order, product_id, created_at) VALUES
('https://images.unsplash.com/photo-1594846497743-9f0dd0d6d3b6?w=800', 'Áo sơ mi lụa trắng', true, 1, 15, CURRENT_TIMESTAMP),
('https://images.unsplash.com/photo-1609251297586-5d8e7c2e5f2c?w=800', 'Chi tiết áo sơ mi lụa', false, 2, 15, CURRENT_TIMESTAMP);

-- Áo Blazer Nữ (product_id=16)
INSERT INTO product_images (image_url, alt_text, is_thumbnail, display_order, product_id, created_at) VALUES
('https://images.unsplash.com/photo-1591369822096-ffd140ec948f?w=800', 'Áo blazer đen', true, 1, 16, CURRENT_TIMESTAMP),
('https://images.unsplash.com/photo-1594223515816-1ad1f9166a4b?w=800', 'Áo blazer be', false, 2, 16, CURRENT_TIMESTAMP);

-- Chân Váy Midi (product_id=18)
INSERT INTO product_images (image_url, alt_text, is_thumbnail, display_order, product_id, created_at) VALUES
('https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?w=800', 'Chân váy chữ A midi đen', true, 1, 18, CURRENT_TIMESTAMP),
('https://images.unsplash.com/photo-1566174053879-31528523f8ae?w=800', 'Chân váy be', false, 2, 18, CURRENT_TIMESTAMP);

-- Đầm Maxi (product_id=19)
INSERT INTO product_images (image_url, alt_text, is_thumbnail, display_order, product_id, created_at) VALUES
('https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=800', 'Đầm maxi hoa', true, 1, 19, CURRENT_TIMESTAMP),
('https://images.unsplash.com/photo-1572804013309-59a88b7e92f1?w=800', 'Chi tiết đầm maxi', false, 2, 19, CURRENT_TIMESTAMP);

-- ============================================
-- CARTS & ADDRESSES - Giỏ hàng & Địa chỉ mẫu
-- ============================================

-- Tạo giỏ hàng cho users
INSERT INTO carts (user_id, created_at) VALUES
((SELECT id FROM users WHERE email = 'vovantri204@gmail.com'), CURRENT_TIMESTAMP),
((SELECT id FROM users WHERE email = 'lethihoa@gmail.com'), CURRENT_TIMESTAMP),
((SELECT id FROM users WHERE email = 'phamducan@gmail.com'), CURRENT_TIMESTAMP);

-- Địa chỉ cho user Võ Văn Trí
INSERT INTO addresses (user_id, receiver_name, receiver_phone, province, district, ward, street_detail, is_default, is_deleted, latitude, longitude, created_at) VALUES
((SELECT id FROM users WHERE email = 'vovantri204@gmail.com'), 'Võ Văn Trí', '0901234567', 'TP. Hồ Chí Minh', 'Quận 1', 'Phường Bến Nghé', '123 Nguyễn Huệ', true, false, 10.7769, 106.7009, CURRENT_TIMESTAMP),
((SELECT id FROM users WHERE email = 'vovantri204@gmail.com'), 'Võ Văn Trí', '0901234567', 'TP. Hồ Chí Minh', 'Quận Thủ Đức', 'Phường Linh Trung', '456 Đường Hoàng Diệu 2', false, false, 10.8709, 106.8006, CURRENT_TIMESTAMP);

-- Địa chỉ cho user Lê Thị Hoa
INSERT INTO addresses (user_id, receiver_name, receiver_phone, province, district, ward, street_detail, is_default, is_deleted, latitude, longitude, created_at) VALUES
((SELECT id FROM users WHERE email = 'lethihoa@gmail.com'), 'Lê Thị Hoa', '0912345678', 'Hà Nội', 'Quận Hoàn Kiếm', 'Phường Hàng Bạc', '789 Hàng Bạc', true, false, 21.0285, 105.8542, CURRENT_TIMESTAMP);

-- Địa chỉ cho user Phạm Đức An
INSERT INTO addresses (user_id, receiver_name, receiver_phone, province, district, ward, street_detail, is_default, is_deleted, latitude, longitude, created_at) VALUES
((SELECT id FROM users WHERE email = 'phamducan@gmail.com'), 'Phạm Đức An', '0923456789', 'Đà Nẵng', 'Quận Hải Châu', 'Phường Thạch Thang', '321 Trần Phú', true, false, 16.0544, 108.2022, CURRENT_TIMESTAMP);

-- Thêm items vào giỏ hàng (sử dụng SKU để tìm variant)
INSERT INTO cart_items (cart_id, variant_id, quantity, created_at) VALUES
((SELECT id FROM carts WHERE user_id = (SELECT id FROM users WHERE email = 'vovantri204@gmail.com')),
 (SELECT id FROM product_variants WHERE sku = 'ATBC-DEN-M'), 2, CURRENT_TIMESTAMP),
((SELECT id FROM carts WHERE user_id = (SELECT id FROM users WHERE email = 'vovantri204@gmail.com')),
 (SELECT id FROM product_variants WHERE sku = 'JEAN-DEN-30'), 1, CURRENT_TIMESTAMP),
((SELECT id FROM carts WHERE user_id = (SELECT id FROM users WHERE email = 'lethihoa@gmail.com')),
 (SELECT id FROM product_variants WHERE sku = 'CROP-DEN-S'), 1, CURRENT_TIMESTAMP),
((SELECT id FROM carts WHERE user_id = (SELECT id FROM users WHERE email = 'lethihoa@gmail.com')),
 (SELECT id FROM product_variants WHERE sku = 'MAXI-HOA-M'), 1, CURRENT_TIMESTAMP);

-- ============================================
-- USER VOUCHERS - Gán voucher cho users
-- ============================================
INSERT INTO user_vouchers (user_id, voucher_id, is_deleted, created_at) VALUES
((SELECT id FROM users WHERE email = 'vovantri204@gmail.com'), (SELECT id FROM vouchers WHERE code = 'GLAMORA2025'), false, CURRENT_TIMESTAMP),
((SELECT id FROM users WHERE email = 'vovantri204@gmail.com'), (SELECT id FROM vouchers WHERE code = 'FREESHIP50K'), false, CURRENT_TIMESTAMP),
((SELECT id FROM users WHERE email = 'lethihoa@gmail.com'), (SELECT id FROM vouchers WHERE code = 'NEWMEMBER'), false, CURRENT_TIMESTAMP),
((SELECT id FROM users WHERE email = 'phamducan@gmail.com'), (SELECT id FROM vouchers WHERE code = 'GLAMORA2025'), false, CURRENT_TIMESTAMP);

-- ============================================
-- ORDERS - Đơn hàng mẫu
-- ============================================
INSERT INTO orders (order_code, user_id, address_id, status, subtotal, discount_amount, distance, shipping_fee, total_amount, note, recipient_name, recipient_phone, payment_method_id, created_at) VALUES
('GLA20250101001',
 (SELECT id FROM users WHERE email = 'vovantri204@gmail.com'),
 (SELECT id FROM addresses WHERE user_id = (SELECT id FROM users WHERE email = 'vovantri204@gmail.com') AND is_default = true),
 'COMPLETED', 647000, 0, 5.2, 10400, 657400, 'Giao hàng giờ hành chính', 'Võ Văn Trí', '0987654321', (SELECT id FROM payment_methods WHERE name = 'COD'), CURRENT_TIMESTAMP - INTERVAL '10 days'),
('GLA20250115002',
 (SELECT id FROM users WHERE email = 'lethihoa@gmail.com'),
 (SELECT id FROM addresses WHERE user_id = (SELECT id FROM users WHERE email = 'lethihoa@gmail.com') LIMIT 1),
 'SHIPPING', 549000, 0, 7.8, 15600, 564600, NULL, 'Lê Thị Hoa', '0912345678', (SELECT id FROM payment_methods WHERE name = 'VNPay'), CURRENT_TIMESTAMP - INTERVAL '2 days'),
('GLA20250118003',
 (SELECT id FROM users WHERE email = 'phamducan@gmail.com'),
 (SELECT id FROM addresses WHERE user_id = (SELECT id FROM users WHERE email = 'phamducan@gmail.com') LIMIT 1),
 'CONFIRMED', 449000, 0, 3.5, 7000, 456000, 'Gọi trước khi giao', 'Phạm Đức An', '0901234567', (SELECT id FROM payment_methods WHERE name = 'VNPay'), CURRENT_TIMESTAMP - INTERVAL '1 day');

-- Order Items
INSERT INTO order_items (order_id, variant_id, quantity, price, total_price, created_at) VALUES
-- Order 1
((SELECT id FROM orders WHERE order_code = 'GLA20250101001'), (SELECT id FROM product_variants WHERE sku = 'ATBC-DEN-M'), 2, 199000, 398000, CURRENT_TIMESTAMP - INTERVAL '10 days'),
((SELECT id FROM orders WHERE order_code = 'GLA20250101001'), (SELECT id FROM product_variants WHERE sku = 'SM-TRANG-M'), 1, 249000, 249000, CURRENT_TIMESTAMP - INTERVAL '10 days'),
-- Order 2
((SELECT id FROM orders WHERE order_code = 'GLA20250115002'), (SELECT id FROM product_variants WHERE sku = 'MAXI-HOA-M'), 1, 549000, 549000, CURRENT_TIMESTAMP - INTERVAL '2 days'),
-- Order 3
((SELECT id FROM orders WHERE order_code = 'GLA20250118003'), (SELECT id FROM product_variants WHERE sku = 'JEAN-DEN-30'), 1, 449000, 449000, CURRENT_TIMESTAMP - INTERVAL '1 day');

-- ============================================
-- PAYMENTS - Thanh toán
-- ============================================
-- Payment cho đơn GLA20250101001 (COMPLETED) - User thử VNPay 2 lần rồi mới thanh toán COD
INSERT INTO payments (order_id, payment_method_id, amount, status, transaction_id, payment_date, failed_reason, pay_url, created_at, updated_at) VALUES
-- Lần 1: User tạo VNPay nhưng không thanh toán (CANCELLED do tạo payment mới)
((SELECT id FROM orders WHERE order_code = 'GLA20250101001'), 
 (SELECT id FROM payment_methods WHERE name = 'VNPay'), 
 574950, 'CANCELLED', NULL, NULL, 'Cancelled due to new payment creation', 
 'https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_TxnRef=12345678...', 
 CURRENT_TIMESTAMP - INTERVAL '10 days 2 hours', CURRENT_TIMESTAMP - INTERVAL '10 days 1 hour'),
-- Lần 2: User tạo VNPay lần 2 nhưng vẫn không thanh toán (EXPIRED sau 15 phút)
((SELECT id FROM orders WHERE order_code = 'GLA20250101001'), 
 (SELECT id FROM payment_methods WHERE name = 'VNPay'), 
 574950, 'EXPIRED', NULL, NULL, 'Payment URL expired after 15 minutes', 
 'https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_TxnRef=23456789...', 
 CURRENT_TIMESTAMP - INTERVAL '10 days 1 hour', CURRENT_TIMESTAMP - INTERVAL '10 days 45 minutes'),
-- Lần 3: User chọn COD và đã nhận hàng thành công (SUCCESS)
((SELECT id FROM orders WHERE order_code = 'GLA20250101001'), 
 (SELECT id FROM payment_methods WHERE name = 'COD'), 
 574950, 'SUCCESS', 'COD-GLA20250101001', CURRENT_TIMESTAMP - INTERVAL '3 days', NULL, NULL,
 CURRENT_TIMESTAMP - INTERVAL '10 days 30 minutes', CURRENT_TIMESTAMP - INTERVAL '3 days');

-- Payment cho đơn GLA20250115002 (SHIPPING) - User thanh toán VNPay thành công
INSERT INTO payments (order_id, payment_method_id, amount, status, transaction_id, payment_date, failed_reason, pay_url, created_at, updated_at) VALUES
((SELECT id FROM orders WHERE order_code = 'GLA20250115002'), 
 (SELECT id FROM payment_methods WHERE name = 'VNPay'), 
 579000, 'SUCCESS', '14153188', CURRENT_TIMESTAMP - INTERVAL '2 days', NULL, 
 'https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_TxnRef=14153188...', 
 CURRENT_TIMESTAMP - INTERVAL '2 days 5 minutes', CURRENT_TIMESTAMP - INTERVAL '2 days');

-- Payment cho đơn GLA20250118003 (CONFIRMED) - User thanh toán VNPay xong, chờ giao hàng
INSERT INTO payments (order_id, payment_method_id, amount, status, transaction_id, payment_date, failed_reason, pay_url, created_at, updated_at) VALUES
((SELECT id FROM orders WHERE order_code = 'GLA20250118003'), 
 (SELECT id FROM payment_methods WHERE name = 'VNPay'), 
 424000, 'SUCCESS', '14156892', CURRENT_TIMESTAMP - INTERVAL '1 day', NULL, 
 'https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_TxnRef=14156892...', 
 CURRENT_TIMESTAMP - INTERVAL '1 day 10 minutes', CURRENT_TIMESTAMP - INTERVAL '1 day');

-- Thêm 1 đơn CANCELLED với payment CANCELLED
INSERT INTO orders (order_code, user_id, address_id, status, subtotal, discount_amount, distance, shipping_fee, total_amount, note, canceled_reason, recipient_name, recipient_phone, payment_method_id, created_at) VALUES
('GLA20250117005',
 (SELECT id FROM users WHERE email = 'phamducan@gmail.com'),
 (SELECT id FROM addresses WHERE user_id = (SELECT id FROM users WHERE email = 'phamducan@gmail.com') LIMIT 1),
 'CANCELED', 249000, 0, 4.0, 25000, 274000, NULL, 'Khách hàng đổi ý không mua nữa', 'Phạm Đức An', '0901234567', (SELECT id FROM payment_methods WHERE name = 'VNPay'), CURRENT_TIMESTAMP - INTERVAL '3 days');

INSERT INTO order_items (order_id, variant_id, quantity, price, total_price, created_at) VALUES
((SELECT id FROM orders WHERE order_code = 'GLA20250117005'), (SELECT id FROM product_variants WHERE sku = 'OVER-DEN-L'), 1, 249000, 249000, CURRENT_TIMESTAMP - INTERVAL '3 days');

INSERT INTO payments (order_id, payment_method_id, amount, status, transaction_id, payment_date, failed_reason, pay_url, created_at, updated_at) VALUES
((SELECT id FROM orders WHERE order_code = 'GLA20250117005'), 
 (SELECT id FROM payment_methods WHERE name = 'VNPay'), 
 274000, 'CANCELLED', NULL, NULL, 'Order cancelled by user', 
 'https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_TxnRef=14151234...',
 CURRENT_TIMESTAMP - INTERVAL '3 days 10 minutes', CURRENT_TIMESTAMP - INTERVAL '3 days');


-- ============================================
-- PRODUCT REVIEWS - Đánh giá sản phẩm
-- ============================================
INSERT INTO product_reviews (product_id, variant_id, user_id, order_id, rating, comment, is_verified_purchase, is_deleted, created_at) VALUES
((SELECT product_id FROM product_variants WHERE sku = 'ATBC-DEN-M'), (SELECT id FROM product_variants WHERE sku = 'ATBC-DEN-M'), (SELECT id FROM users WHERE email = 'vovantri204@gmail.com'), (SELECT id FROM orders WHERE order_code = 'GLA20250101001'), 5, 'Áo rất đẹp, chất lượng tốt, form dáng vừa vặn. Shop giao hàng nhanh!', true, false, CURRENT_TIMESTAMP - INTERVAL '9 days'),
((SELECT product_id FROM product_variants WHERE sku = 'JEAN-DEN-30'), (SELECT id FROM product_variants WHERE sku = 'JEAN-DEN-30'), (SELECT id FROM users WHERE email = 'phamducan@gmail.com'), (SELECT id FROM orders WHERE order_code = 'GLA20250118003'), 4, 'Quần jean đẹp, nhưng hơi dài một chút. Nhìn chung ok.', true, false, CURRENT_TIMESTAMP - INTERVAL '12 hours'),
((SELECT product_id FROM product_variants WHERE sku = 'MAXI-HOA-M'), (SELECT id FROM product_variants WHERE sku = 'MAXI-HOA-M'), (SELECT id FROM users WHERE email = 'lethihoa@gmail.com'), (SELECT id FROM orders WHERE order_code = 'GLA20250115002'), 5, 'Đầm xinh lắm, chất vải mềm mại thoáng mát!', true, false, CURRENT_TIMESTAMP - INTERVAL '1 day');
