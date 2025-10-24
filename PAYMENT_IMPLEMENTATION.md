# Payment System Documentation

## 📋 Payment Status

```java
public enum PaymentStatus {
  PENDING,    // Chờ thanh toán
  SUCCESS,    // Thanh toán thành công
  FAILED,     // Thanh toán thất bại
  CANCELLED,  // Đơn hàng bị hủy
  EXPIRED     // Payment URL hết hạn (VNPay - 15 phút)
}
```

## 🔌 API Endpoints

### Payment APIs (`/user/payments`)

#### 1. Tạo Payment cho Order

```http
POST /user/payments
Content-Type: application/json

{
  "orderId": 1,
  "paymentMethodId": 2
}
```

**Response:**

```json
{
  "message": "Create payment successfully",
  "data": {
    "id": 1,
    "orderId": 1,
    "orderCode": "ORD20250124001",
    "paymentMethod": {
      "id": 2,
      "name": "VNPay",
      "description": "Thanh toán qua VNPay - Hỗ trợ thẻ ATM, Visa, MasterCard",
      "logoUrl": "https://i.imgur.com/vnpay-logo.png",
      "isActive": true
    },
    "status": "PENDING",
    "amount": 500000.0,
    "transactionId": null,
    "paymentDate": null,
    "failedReason": null,
    "payUrl": "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?...",
    "createdAt": "2025-01-24T10:30:00",
    "updatedAt": "2025-01-24T10:30:00"
  }
}
```

**Lưu ý:**

- Nếu là VNPay: `payUrl` sẽ được tạo, client redirect user đến URL này để thanh toán
- Nếu là COD: `payUrl` = null, status = PENDING

#### 2. Lấy Payment theo Order ID

```http
GET /user/payments/order/{orderId}
```

**Response:** Tương tự response của POST

#### 3. VNPay Return Callback (VNPay redirect về sau khi thanh toán)

```http
GET /user/payments/vnpay-return?vnp_Amount=50000000&vnp_ResponseCode=00&...
```

**Hành vi:**

- Backend verify payment và cập nhật status
- Redirect về frontend: `http://localhost:3000/payment-result?status=SUCCESS&orderId=1&amount=500000&orderCode=ORD20250124001`

#### 4. Hủy Payment

```http
PUT /user/payments/cancel/order/{orderId}
```

**Response:**

```json
{
  "message": "Payment cancelled successfully",
  "data": {
    "id": 1,
    "status": "CANCELLED",
    "failedReason": "Payment cancelled by user or system",
    ...
  }
}
```

**Lưu ý:** Chỉ cho phép hủy payment ở trạng thái PENDING

#### 5. Check Payment Expired

```http
PUT /user/payments/check-expired/{paymentId}
```

**Response:**

```json
{
  "message": "Payment status checked successfully",
  "data": {
    "id": 1,
    "status": "EXPIRED",
    "failedReason": "Payment URL expired after 15 minutes",
    ...
  }
}
```

**Lưu ý:**

- Chỉ check cho VNPay payment (có payUrl)
- Tự động set EXPIRED nếu > 15 phút từ lúc tạo

## 🔄 Luồng thanh toán

### VNPay Flow

```
1. Client tạo payment
   POST /user/payments { "orderId": 1, "paymentMethodId": 2 }

2. Server tạo Payment với status=PENDING và payUrl
   Response chứa payUrl

3. Client redirect user đến payUrl

4. User thanh toán trên trang VNPay

5. VNPay redirect về backend endpoint (từ config .env)
   GET http://localhost:8080/payments/vnpay-return?vnp_Amount=...&vnp_ResponseCode=00&...

6. Backend verify signature và cập nhật Payment status
   - vnp_ResponseCode = "00" → SUCCESS
   - vnp_ResponseCode = "24" → CANCELLED
   - Khác → FAILED với failedReason cụ thể

7. Backend redirect về frontend với kết quả
   http://localhost:3000/payment-result?status=SUCCESS&orderId=1&amount=500000&orderCode=ORD20250124001

8. Frontend hiển thị kết quả thanh toán cho user
```

### VNPay Response Codes

| Code   | Status    | Mô tả                     |
| ------ | --------- | ------------------------- |
| 00     | SUCCESS   | Giao dịch thành công      |
| 24     | CANCELLED | User hủy giao dịch        |
| 11, 12 | FAILED    | Lỗi thẻ/tài khoản bị khóa |
| 51     | FAILED    | Không đủ số dư            |
| 65     | FAILED    | Vượt hạn mức giao dịch    |
| 75     | FAILED    | Ngân hàng bảo trì         |
| 79     | FAILED    | Ngân hàng từ chối         |
| Khác   | FAILED    | Lỗi khác                  |

### COD Flow

```
1. Client tạo payment
   POST /user/payments { "orderId": 1, "paymentMethodId": 1 }

2. Server tạo Payment với status=PENDING, payUrl=null

3. Order được tạo, chờ giao hàng

4. Shipper giao hàng thành công → Admin cập nhật Payment status → SUCCESS
```

## 🔄 Payment Status Transitions

### PENDING → SUCCESS

- **VNPay:** User thanh toán thành công (response code 00)
- **COD:** Admin xác nhận đã nhận tiền

### PENDING → FAILED

- VNPay trả về mã lỗi (11, 12, 51, 65, 75, 79...)
- System update status = FAILED với failedReason cụ thể

### PENDING → CANCELLED

- User hủy trên trang VNPay (response code 24)
- Hoặc call API: `PUT /user/payments/cancel/order/{orderId}`

### PENDING → EXPIRED

- VNPay payment URL quá 15 phút
- Background job tự động check (mỗi 5 phút)
- Hoặc call API: `PUT /user/payments/check-expired/{paymentId}`

## 🧪 Testing

### 1. Test VNPay Success Flow

- Tạo payment → Copy `payUrl`
- Mở URL trong browser
- Thanh toán test trên VNPay Sandbox
- Verify redirect về frontend với status=SUCCESS

### 2. Test VNPay Cancelled Flow

- Tạo payment → Copy `payUrl`
- Mở URL → Click "Hủy giao dịch"
- Verify redirect về frontend với status=CANCELLED

### 3. Test Payment Expired

- Tạo payment VNPay
- Đợi > 15 phút hoặc call API check-expired
- Verify status = EXPIRED

### 4. Test COD Flow

- Tạo payment với paymentMethodId = COD
- Verify payUrl = null, status = PENDING
- Admin cập nhật status → SUCCESS
