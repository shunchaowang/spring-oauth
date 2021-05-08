package edu.osumc.bmi.oauth2.auth.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public enum DeferredResultBuilder {
  INSTANCE;

  public DeferredResult<ResponseEntity<?>> build() {

    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>(5000L);

    result.onTimeout(
        () ->
            result.setResult(
                ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request Timeout")));

    result.onError(
        (Throwable t) ->
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(t.getMessage()));

    return result;
  }
}
