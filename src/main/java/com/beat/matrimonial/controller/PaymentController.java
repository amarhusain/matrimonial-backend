package com.beat.matrimonial.controller;


import com.beat.matrimonial.payload.request.OrderRequest;
import com.beat.matrimonial.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

  @Value("${razorpay.key.id}")
  private String razorpayKeyId;

  @Value("${razorpay.key.secret}")
  private String razorpayKeySecret;

  private final PaymentService paymentService;

  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @PostMapping("/create-order")
  public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest) {
    try {
      RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
      JSONObject orderOptions = new JSONObject();
      orderOptions.put("amount", orderRequest.getAmount() * 100); // amount in paise
      orderOptions.put("currency", "INR");
      orderOptions.put("receipt", "txn_123456");

      Order order = razorpay.orders.create(orderOptions);

      // Save order details to database
      paymentService.saveOrder(order, orderRequest);
      return ResponseEntity.ok(order.toString());
    } catch (RazorpayException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

//  @PostMapping("/verify-signature")
//  public ResponseEntity<Boolean> verifySignature(@RequestBody PaymentVerificationRequest request) {
//    try {
//      String data = request.getOrderId() + "|" + request.getPaymentId();
//      boolean isValid = Utils.verifySignature(data, request.getRazorpaySignature(),
//          razorpayKeySecret);
//      return ResponseEntity.ok(isValid);
//    } catch (Exception e) {
//      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
//    }
//  }


  @PostMapping("/verify-payment")
  public ResponseEntity<?> verifyPayment(@RequestBody Map<String, Object> paymentData) {
    try {
      // Extract payment details from the request body
      String razorpayOrderId = (String) paymentData.get("razorpay_order_id");
      String razorpayPaymentId = (String) paymentData.get("razorpay_payment_id");
      String razorpaySignature = (String) paymentData.get("razorpay_signature");

      // Create the data to generate the signature
      String generatedSignature = razorpayOrderId + "|" + razorpayPaymentId;

      // Verify the signature using Razorpay SDK
      boolean isValidSignature = Utils.verifySignature(generatedSignature, razorpaySignature,
          razorpayKeySecret);

      // If the signature is valid, handle the successful payment
      if (isValidSignature) {
        // You can now mark the order as paid in your database
        paymentService.completeOrder(razorpayOrderId, razorpayPaymentId);
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Payment verified successfully.");
        return ResponseEntity.ok(response);
      } else {
        // Signature mismatch, payment verification failed
        return ResponseEntity.badRequest()
            .body("Payment verification failed due to invalid signature.");
      }

    } catch (Exception e) {
      // Handle any exception
      return ResponseEntity.status(500).body("An error occurred while verifying the payment.");
    }
  }

}
