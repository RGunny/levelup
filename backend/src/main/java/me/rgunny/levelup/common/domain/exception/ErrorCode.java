package me.rgunny.levelup.common.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

//TODO: 도메인 별 에러 확장시, ErrorCode 라는 별도의 인터페이스를 만들어 현 ErrorCode 를 CommonErrorCode 로 변경 후 ErrorCode 상속하여 추상화
@Getter
public enum ErrorCode {

    RESOURCE_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "일치하는 데이터를 찾을 수 없습니다.")
    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
