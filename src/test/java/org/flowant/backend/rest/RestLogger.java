package org.flowant.backend.rest;

import org.springframework.test.web.reactive.server.EntityExchangeResult;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@UtilityClass
@Log4j2
public class RestLogger {
    public static void log(EntityExchangeResult<byte[]> result) {
        log.info("Url:{}\nMethod:{}\nReqHeader:\n{}\nReqBody:\n{}\nRespStatus:{}\nRespHeader:\n{}\nRespBody:\n{}",
                result::getUrl, result.getMethod()::toString, result.getRequestHeaders()::toString,
                () -> new String(result.getRequestBodyContent()),
                result.getStatus()::toString, result.getResponseHeaders()::toString,
                () -> new String(result.getResponseBodyContent()));
    }
}
