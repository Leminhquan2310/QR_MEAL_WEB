# 🍽️ QR Meal Web

QR Meal Web là một **web application hỗ trợ gọi món bằng mã QR** dành cho nhà hàng/quán ăn.  
Khách hàng chỉ cần **quét QR code tại bàn** để xem menu và đặt món trực tiếp trên điện thoại mà không cần nhân viên phục vụ.

Mục tiêu của dự án là xây dựng một hệ thống **order đồ ăn thông minh, giảm thời gian phục vụ và tăng trải nghiệm khách hàng**.

---

# 🚀 Features

## 👤 User
- Quét **QR code tại bàn** để truy cập menu
- Xem danh sách món ăn
- Xem chi tiết món ăn
- Thêm món vào giỏ hàng
- Đặt món trực tiếp trên web
- Theo dõi trạng thái đơn hàng

## 🛠️ Admin
- Quản lý danh mục món ăn
- Quản lý món ăn
- Quản lý đơn hàng
- Cập nhật trạng thái đơn hàng
- Quản lý bàn trong nhà hàng

---

# 🧩 System Overview

Hệ thống hoạt động theo quy trình:

1. Mỗi bàn có một **QR Code**
2. Khách hàng quét QR bằng điện thoại
3. QR dẫn đến trang menu của bàn đó
4. Khách chọn món và gửi order
5. Order được gửi đến hệ thống để nhà hàng xử lý

---

# 🏗️ Tech Stack

## Backend
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- RESTful API

## Frontend
- HTML
- CSS
- JavaScript

## Database
- MySQL

## Other
- QR Code Integration
- REST API
- MVC Architecture

---

# ⚙️ Installation

1. Clone project

```bash
git clone https://github.com/Leminhquan2310/QR_MEAL_WEB.git
```
2. Database
Tạo database:
```bash
CREATE DATABASE qr_meal;
```
3. Backend setup
Chạy project bằng IDE (IntelliJ / Eclipse) hoặc Maven
```bash
./mvnw spring-boot:run
```
Cấu hình trong application.properties:
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/qr_meal
spring.datasource.username=root
spring.datasource.password=yourpassword
```
4. Frontend
Mở file `index.html` hoặc chạy bằng local server.

📸 Demo

Scan QR -> Khách hàng quét QR tại bàn để truy cập menu.

Menu -> Hiển thị danh sách món ăn của nhà hàng.

Order -> Khách chọn món và gửi order trực tiếp trên web.

🎯 Learning Outcomes

Thông qua dự án này tôi đã thực hành:
  
  - Thiết kế RESTful API với Spring Boot
  
  - Sử dụng Spring Security
  
  - Làm việc với Spring Data JPA & Hibernate
  
  - Xây dựng frontend với JavaScript
  
  - Thiết kế hệ thống order bằng QR Code

  - Quản lý dữ liệu với MySQL
