package com.elderlycare.pay;

import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.elderlycare.config.AlipayProperties;
import com.elderlycare.model.Billing;
import com.elderlycare.service.BillingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 只关心“生成表单”和“回调后标记已付”两个动作
 */
@Service
@RequiredArgsConstructor
public class BillingPayService {

    private final AlipayClient alipayClient;
    private final AlipayProperties props;
    private final BillingService billingService;

    /**
     * 生成跳转到支付宝收银台的 HTML 表单
     */
    public String buildPayForm(Long billId) throws Exception {
        Billing b = billingService.get(billId);
        if (b == null || !"未支付".equals(b.getStatus())) {
            throw new IllegalStateException("账单不存在或状态不允许支付");
        }

        // 1. 构造请求
        AlipayTradePagePayRequest req = new AlipayTradePagePayRequest();
        req.setReturnUrl(props.getReturnUrl());
        req.setNotifyUrl(props.getNotifyUrl());

        Map<String, String> biz = new HashMap<>();
        biz.put("out_trade_no", "BILL" + billId);        // 简单起见，直接用 billId 做唯一单号
        biz.put("product_code", "FAST_INSTANT_TRADE_PAY");
        biz.put("total_amount", b.getAmount().toPlainString());
        biz.put("subject", "老人" + b.getElderId() + "-" + b.getItemName());

        req.setBizContent(new ObjectMapper().writeValueAsString(biz));

        // 2. SDK 返回 <form>…</form>，浏览器马上跳转
        return alipayClient.pageExecute(req).getBody();
    }

    /**
     * 支付宝“异步通知”验签 + 更新账单
     */
    public boolean handleNotify(Map<String, String> params) throws Exception {

        /* ---------- 1.  验签 ---------- */
        // 把公钥文件内容读成 String
        String alipayPubKey = Files.readString(
                Path.of(props.getAlipayPublicKeyPath().getURI()));

        boolean signOk = AlipaySignature.rsaCheckV1(
                params,
                alipayPubKey,                     // String 类型
                StandardCharsets.UTF_8.name(),
                "RSA2");

        if (!signOk) return false;
        if (!"TRADE_SUCCESS".equals(params.get("trade_status"))) return false;

        /* ---------- 2.  根据 out_trade_no 定位账单 ---------- */
        Long billId = Long.valueOf(params.get("out_trade_no").substring(4));
        Billing b = billingService.get(billId);
        if (b == null) return false;

        /* ---------- 3.  更新状态与付款时间 ---------- */
        b.setStatus("已支付");

        // gmt_payment 示例：2025-06-07 12:34:56
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse(params.get("gmt_payment"), fmt);

        // 若 Billing.paidAt 为 java.util.Date
        b.setPaidAt(Date.from(ldt.atZone(ZoneId.of("Asia/Shanghai")).toInstant()));

        billingService.update(b);
        return true;
    }

    public void markPaidIfNotYet(String outTradeNo, @Nullable String gmtPayment) {
        Long billId = Long.valueOf(outTradeNo.substring(4));
        Billing b = billingService.get(billId);
        if (b == null || "已支付".equals(b.getStatus())) return;

        b.setStatus("已支付");

        if (gmtPayment != null) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime ldt = LocalDateTime.parse(gmtPayment, fmt);
            b.setPaidAt(Timestamp.valueOf(ldt));
        } else {
            b.setPaidAt(new Timestamp(System.currentTimeMillis()));   // 同步回跳默认现在
        }

        billingService.update(b);
    }


}
