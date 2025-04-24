package com.nuhi.Nuhi.service;


import com.nuhi.Nuhi.enums.PaymentStatus;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class StripService {


    private final RestClient.Builder builder;
    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @Value("${stripe.currency}")
    private String currency;

//    public StripService(RestClient.Builder builder) {
//        this.builder = builder;
//    }

    @PostConstruct
    public void init(){
        Stripe.apiKey=stripeSecretKey;
    }

    public PaymentIntent createPaymentIntent(BigDecimal amount , String paymentMethodId) throws StripeException {

        PaymentIntentCreateParams params= PaymentIntentCreateParams.builder()
                .setAmount(convertToStripAmount(amount))
                .setCurrency(currency)
                .setPaymentMethod(paymentMethodId)
                .setConfirm(true)
                .setOffSession(true)
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .build();

        return PaymentIntent.create(params);


    }

    private long convertToStripAmount(BigDecimal amount){
        return  amount.multiply(BigDecimal.valueOf(100)).longValue();
    }



}
