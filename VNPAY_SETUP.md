## 🔄 VNPay Payment Flow

```
User clicks "Thanh toán VNPay"
    ↓
Frontend → Backend: POST /user/orders (tạo order)
    ↓
Frontend → Backend: POST /user/payments (tạo payment)
    ↓
Backend: Generate VNPay URL with secure hash
    ↓
Backend → Frontend: Return payUrl
    ↓
Frontend: Redirect user to VNPay sandbox
    ↓
User nhập thông tin thẻ test trên VNPay
    ↓
VNPay → Backend: GET /user/payments/vnpay-return?vnp_ResponseCode=...
    ↓
Backend: Verify hash, update payment status
    ↓
Backend → Frontend: Redirect to /payment-result?status=...
    ↓
Frontend: Show success/failure message
```
  