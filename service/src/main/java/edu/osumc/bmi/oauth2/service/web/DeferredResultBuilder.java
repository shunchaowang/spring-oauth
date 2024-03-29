package edu.osumc.bmi.oauth2.service.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public enum DeferredResultBuilder {
  INSTANCE;

  public DeferredResult<ResponseEntity<?>> build() {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>(5000L); // 5s timeout
    result.onTimeout(
        () -> result.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)));
    result.onError(
        (Throwable t) ->
            result.setErrorResult(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(t.getMessage())));

    return result;
  }
}
