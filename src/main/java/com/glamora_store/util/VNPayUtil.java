package com.glamora_store.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.glamora_store.enums.ErrorMessage;

public class VNPayUtil {

  private VNPayUtil() {
    throw new UnsupportedOperationException(ErrorMessage.UTILITY_CLASS_SHOULD_NOT_BE_INSTANTIATED.getMessage());
  }

  /**
   * Generates HMAC-SHA512 hash for secure data signing.
   * Được dùng cho hàm hashAllFields ở dưới
   *
   * @param key  the secret key for HMAC
   * @param data the data to be hashed
   * @return the HMAC-SHA512 hash as hexadecimal string
   *
   *         Example:
   *         Input: key="secret", data="hello"
   *         Output: "e4f27c0d532376b3e7e5..."
   */
  public static String hmacSHA512(String key, String data) {
    try {
      Mac hmac512 = Mac.getInstance("HmacSHA512");
      SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
      hmac512.init(secretKey);
      byte[] result = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
      StringBuilder sb = new StringBuilder();
      for (byte b : result) {
        sb.append(String.format("%02x", b));
      }
      return sb.toString();
    } catch (Exception e) {
      throw new RuntimeException("Failed to generate HMAC-SHA512", e);
    }
  }

  /**
   * Creates secure hash for all VNPay request fields.
   * Hàm hashAllFields(...) này chính là bước tạo chữ ký (signature) cho dữ liệu
   * gửi đến VNPAY
   * Nó đảm bảo rằng dữ liệu của bạn không bị chỉnh sửa trên đường truyền giữa
   * server của bạn và VNPAY.
   *
   * @param fields    the map of VNPay parameters
   * @param secretKey the VNPay secret key
   * @return the HMAC-SHA512 hash of all fields
   *
   *         Example:
   *         Input: fields={"vnp_TxnRef":"123456","vnp_OrderInfo":"Test payment"},
   *         secretKey="SECRET"
   *         Output: "a1b2c3d4e5f6..."
   */
  public static String hashAllFields(Map<String, String> fields, String secretKey) {
    List<String> fieldNames = new ArrayList<>(fields.keySet());
    Collections.sort(fieldNames);
    StringBuilder sb = new StringBuilder();
    Iterator<String> itr = fieldNames.iterator();
    while (itr.hasNext()) {
      String fieldName = itr.next();
      String fieldValue = fields.get(fieldName);
      if ((fieldValue != null) && (fieldValue.length() > 0)) {
        sb.append(fieldName);
        sb.append("=");
        sb.append(fieldValue);
      }
      if (itr.hasNext()) {
        sb.append("&");
      }
    }
    return hmacSHA512(secretKey, sb.toString());
  }

  /**
   * Builds VNPay payment URL with encoded parameters.
   *
   * @param params   the map of VNPay parameters
   * @param vnpayUrl the base VNPay URL
   * @return the complete payment URL with query parameters
   *
   *         Example:
   *         Input: params={"vnp_TxnRef":"123456","vnp_OrderInfo":"Test"},
   *         vnpayUrl="https://sandbox.vnpayment.vn/paymentv2/vpcpay.html"
   *         Output:
   *         "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_OrderInfo=Test&vnp_TxnRef=123456"
   */
  public static String getPaymentURL(Map<String, String> params, String vnpayUrl) {
    StringBuilder query = new StringBuilder();
    try {
      List<String> fieldNames = new ArrayList<>(params.keySet());
      Collections.sort(fieldNames);
      Iterator<String> itr = fieldNames.iterator();
      while (itr.hasNext()) {
        String fieldName = itr.next();
        String fieldValue = params.get(fieldName);
        if ((fieldValue != null) && (fieldValue.length() > 0)) {
          query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString()));
          query.append('=');
          query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
          if (itr.hasNext()) {
            query.append('&');
          }
        }
      }
      return vnpayUrl + "?" + query.toString();
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Failed to create payment URL", e);
    }
  }

  /**
   * Generates random numeric string of specified length.
   *
   * @param len the length of the random string
   * @return a random numeric string
   *
   *         Example:
   *         Input: len=8
   *         Output: "12345678"
   */
  public static String getRandomNumber(int len) {
    Random rnd = new Random();
    String chars = "0123456789";
    StringBuilder sb = new StringBuilder(len);
    for (int i = 0; i < len; i++) {
      sb.append(chars.charAt(rnd.nextInt(chars.length())));
    }
    return sb.toString();
  }
}
