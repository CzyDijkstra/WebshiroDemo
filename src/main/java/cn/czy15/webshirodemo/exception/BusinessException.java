package cn.czy15.webshirodemo.exception;

import cn.czy15.webshirodemo.exception.code.BaseResponseCode;

/**
 * @ClassName: BusinessException
 */
public class BusinessException extends RuntimeException{
    /**
     *  异常 code
     */
    private final int code;

    /**
     *  异常提示
     */
    public final String defaultMessage;

    public BusinessException(int code, String defaultMessage) {
        super(defaultMessage);
        this.code = code;
        this.defaultMessage = defaultMessage;
    }


    public BusinessException(BaseResponseCode baseResponseCode){
        this(baseResponseCode.getCode(), baseResponseCode.getMsg());
    }
    public int getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
