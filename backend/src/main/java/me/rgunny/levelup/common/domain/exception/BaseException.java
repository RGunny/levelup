package me.rgunny.levelup.common.domain.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public BaseException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseException(String datasource, Long id, ErrorCode errorCode) {
        super(datasource + "에서 ID" + id + " 을 찾을 수 없습니다.");
    }

}
