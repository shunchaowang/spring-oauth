package edu.osumc.bmi.oauth2.service.login;

import org.springframework.web.context.request.async.DeferredResult;

public interface CallbackHandler<T> {

  DeferredResult<T> respond(String code);
}
