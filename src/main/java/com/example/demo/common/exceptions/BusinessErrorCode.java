package com.example.demo.common.exceptions;

import lombok.Getter;

@Getter
public enum BusinessErrorCode {
    //clients
    BAD_REQUEST("C400", "잘못된 요청입니다. 요청내용을 확인하세요."),
    NOT_FOUND("C404", "요청내용을 찾을 수 없습니다. 요청내용을 확인하세요."),
    UNAUTHORIZED("C401", "인증되지 않았습니다. 인증을 확인하세요."),
    FORBIDDEN("C403", "권한이 없습니다. 권한을 확인하세요."),

    //Server
    INTERNAL_SERVER_ERROR("S500", "시스템 내부오류가 발생했습니다. 담당자에게 문의바랍니다."),

    //Business
    DUPLICATED_PRODUCT_CODE("T001", "중복된 상품 코드가 존재합니다."),
    INVALID_STOCK_QUANTITY("T002", "재고가 충분하지 않습니다."),
    NOT_FOUND_STOCK("T003", "재고 정보가 존재하지 않습니다."),
    NOT_FOUND_PRODUCT("T004", "상품 정보가 존재하지 않습니다."),

    //External
    EXTERNAL_API_ERROR("E001", "API 호출 중 오류가 발생했습니다."),
    ;

    private final String code;
    private final String message;

    BusinessErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
