package com.elderlycare.controller;

import com.elderlycare.pay.BillingPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PayCallbackController {

    private final BillingPayService billingPayService;

    /**
     * 支付宝服务器 -> 你：异步通知，必须返回 “success”
     */
    @PostMapping("/alipay/notify")
    @ResponseBody
    public String notify(HttpServletRequest req) throws Exception {
        Map<String, String> params = extract(req);
        return billingPayService.handleNotify(params) ? "success" : "fail";
    }

    /**
     * 用户支付后浏览器跳回来的同步通知，简单展示结果即可
     */
    @GetMapping("/alipay/return")
    public String payReturn(HttpServletRequest req) throws Exception {
        Map<String, String> p = extract(req);

//        // ① 同步回跳也包含 sign，可验签（演示可省）
//        String aliPubKey = Files.readString(Path.of(props.getAlipayPublicKeyPath().getURI()));
//        boolean ok = AlipaySignature.rsaCheckV1(p, aliPubKey, "utf-8", "RSA2");
//
//        if (ok && "TRADE_SUCCESS".equals(p.get("trade_status"))) {
//
//        }
        billingPayService.markPaidIfNotYet(p.get("out_trade_no"), p.get("gmt_payment"));
        // ② 直接回到账单列表；刷新即可看到“已支付”
        return "redirect:/billing";
    }

    private Map<String, String> extract(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        request.getParameterMap()
                .forEach((k, v) -> map.put(k, v[0]));
        return map;
    }
}
