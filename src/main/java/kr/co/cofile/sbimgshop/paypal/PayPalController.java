package kr.co.cofile.sbimgshop.paypal;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PayPalController {
    private final PayPalService payPalService;
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;

    @GetMapping("/checkout/{productId}")
    public String checkout(@PathVariable Long productId) {
        try {
            Product product = productMapper.findById(productId);
            if (product == null) {
                throw new RuntimeException("Product not found");
            }

            Order order = new Order();
            order.setProductId(productId);
            order.setAmount(product.getPrice());
            order.setStatus("PENDING");
            order.setCreatedAt(LocalDateTime.now());

            Payment payment = payPalService.createPayment(
                    product.getPrice(),
                    "USD",
                    "Payment for " + product.getName(),
                    "http://localhost:8080/pay/cancel",
                    "http://localhost:8080/pay/success"
            );

            order.setPaypalOrderId(payment.getId());
            orderMapper.insert(order);

            return "redirect:" + payment.getLinks()
                    .stream()
                    .filter(link -> link.getRel().equals("approval_url"))
                    .findFirst()
                    .orElseThrow()
                    .getHref();
        } catch (PayPalRESTException e) {
            // 에러 처리
            return "error";
        }
    }

    @GetMapping("/success")
    public String success(@RequestParam String paymentId, @RequestParam String PayerID) {
        try {
            Payment payment = payPalService.executePayment(paymentId, PayerID);
            orderMapper.updateStatus(paymentId, "COMPLETED");
            return "success";
        } catch (PayPalRESTException e) {
            // 에러 처리
            return "error";
        }
    }

    @GetMapping("/cancel")
    public String cancel(@RequestParam String paymentId) {
        orderMapper.updateStatus(paymentId, "CANCELLED");
        return "cancel";
    }
}