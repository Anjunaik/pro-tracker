package com.acoderpro.services;

import com.acoderpro.dto.OtpMailRequest;

import reactor.core.publisher.Mono;

public interface NotificationClient {
  public void sendOtp(OtpMailRequest mailRequest);
}
