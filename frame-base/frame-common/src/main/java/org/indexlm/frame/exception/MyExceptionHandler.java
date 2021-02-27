package org.indexlm.frame.exception;

import com.google.common.collect.ImmutableMap;
import org.indexlm.frame.bean.common.AuthCode;
import org.indexlm.frame.bean.common.CommonCode;
import org.indexlm.frame.bean.common.ResultCode;
import org.indexlm.frame.bean.common.exception.LogErrorException;
import org.indexlm.frame.bean.common.exception.LogInfoException;
import org.indexlm.frame.bean.common.response.RespRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;


/**
 * 全局的异常拦截器
 *
 * @author LiuMing
 * @since 2021/2/27
 */
@RestControllerAdvice
public class MyExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(MyExceptionHandler.class);

    /**
     * 使用EXCEPTIONS存放异常类型和错误代码的映射，
     * ImmutableMap的特点的一旦创建不可改变，并且线程安全
     */
    public static ImmutableMap<Class<? extends Exception>, ResultCode> EXCEPTION;
    /**
     * 使用builder来构建一个异常类型和错误代码的异常
     */
    public static ImmutableMap.Builder<Class<? extends Exception>, ResultCode> builder = ImmutableMap.builder();

    static {
        //在类初始化时，map构造器中加入一些基础的异常类型判断
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID_PARAM);
        //请求方式不正确
        builder.put(HttpRequestMethodNotSupportedException.class, CommonCode.WRONG_WAY_TO_REQUEST);
        //缺少必要请求头
//        builder.put(MissingRequestHeaderException.class, CommonCode.NULL_AUTH_HEADER);
//        //数据库记录重复
//        builder.put(DuplicateKeyException.class, CommonCode.DATABASE_RECORD_REPETITION);
//        //一项操作成功地执行，但在释放数据库资源时发生异常
//        builder.put(CleanupFailureDataAccessException.class, CommonCode.DATABASE_ERROR);
//        //数据访问资源彻底失败，例如不能连接数据库
//        builder.put(DataAccessResourceFailureException.class, CommonCode.DATABASE_ERROR);
//        //Insert或Update数据时违反了完整性，例如违反了惟一性限制
//        builder.put(DataIntegrityViolationException.class, CommonCode.DATABASE_ERROR);
//        //某些数据不能被检测到，例如不能通过关键字找到一条记录
//        builder.put(DataRetrievalFailureException.class, CommonCode.DATABASE_ERROR);
//        //错误使用数据访问资源，例如用错误的SQL语法访问关系型数据库
//        builder.put(InvalidDataAccessResourceUsageException.class, CommonCode.DATABASE_ERROR);
//        //有错误发生，但无法归类到某一更为具体的异常中
//        builder.put(UncategorizedDataAccessException.class, CommonCode.DATABASE_ERROR);
    }

    /**
     * 自定义异常 公共异常 （打印info级别日志）
     */
    @ExceptionHandler(LogInfoException.class)
    public RespRes<Object> customInfoException(LogInfoException e) {
        log.info("捕获自定义认证异常,状态码{},状态信息{}",e.getResultCode().code(),e.getMessage());
        RespRes<Object> respRes = RespRes.code(e.getResultCode(),e.getMessage());
        respRes.setData(null);
        return respRes;
    }

    /**
     * 自定义异常 公共异常 （打印error级别日志）
     */
    @ExceptionHandler(LogErrorException.class)
    public RespRes<Object> customErrorException(LogErrorException e) {
        log.error("捕获自定义异常", e);
        RespRes<Object> respRes = RespRes.code(e.getResultCode());
        respRes.setData(null);
        return respRes;
    }

    /**
     * 请求参数校验(输出message信息)异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RespRes<Object> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("请求参数校验(输出message信息)异常: {}", e.getMessage(), e);
        RespRes<Object> objectRespRes = RespRes.code(CommonCode.INVALID_PARAM);
        objectRespRes.setData(null);
        if (e.getBindingResult().getFieldError() != null) {
            objectRespRes.setMessage(this.getBindingResultMessage(e.getBindingResult()));
        } else {
            objectRespRes.setMessage("参数校验不通过");
        }
        return objectRespRes;
    }

    /**
     * 请求参数校验(类型转换)异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public RespRes<Object> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("请求参数校验(类型转换)异常:{}", e.getMessage(), e);
        RespRes<Object> objectRespRes = RespRes.code(CommonCode.INVALID_PARAM);
        objectRespRes.setData(null);
        objectRespRes.setMessage("参数（" + e.getName() + "）类型转换异常");
        return objectRespRes;
    }

    /**
     * 请求参数校验(缺少)异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public RespRes<Object> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("请求参数校验(缺少)异常:{}", e.getMessage(), e);
        RespRes<Object> objectRespRes = RespRes.code(CommonCode.INVALID_PARAM);
        objectRespRes.setData(null);
        objectRespRes.setMessage(e.getParameterName() + "为必须参数");
        return objectRespRes;
    }

    /**
     * 请求参数校验(缺失)异常
     */
    @ExceptionHandler(UnexpectedTypeException.class)
    public RespRes<Object> unexpectedTypeException(UnexpectedTypeException e) {
        log.error("请求参数校验(缺少)异常:{}", e.getMessage(), e);
        RespRes<Object> objectRespRes = RespRes.code(CommonCode.INVALID_PARAM);
        objectRespRes.setData(null);
        objectRespRes.setMessage("请求参数校验(缺失)异常");
        return objectRespRes;
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(BindException.class)
    public RespRes<Object> bindException(BindException e) {
        log.error("参数校验异常:{}", e.getMessage(), e);
        RespRes<Object> objectRespRes = RespRes.code(CommonCode.INVALID_PARAM);
        objectRespRes.setData(null);
        objectRespRes.setMessage(this.getBindingResultMessage(e.getBindingResult()));
        return objectRespRes;
    }

    /**
     * 请求参数校验(校验数值)异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public RespRes<Object> constraintViolationException(ConstraintViolationException e) {
        log.error("请求参数校验(校验数值)异常:{}", e.getMessage(), e);
        RespRes<Object> objectRespRes = RespRes.code(CommonCode.INVALID_PARAM);
        objectRespRes.setData(null);
        objectRespRes.setMessage(e.getMessage());
        return objectRespRes;
    }

    /**
     * 权限不足
     */
    @ExceptionHandler(AccessDeniedException.class)
    public RespRes<Object> accessDeniedException(AccessDeniedException e) {
        log.info("权限不足:{}", e.getMessage(), e);
        RespRes<Object> objectRespRes = RespRes.code(AuthCode.UN_AUTHORISE);
        objectRespRes.setData(null);
        return objectRespRes;
    }

    private String getBindingResultMessage(BindingResult bindingResult) {
        return "[" + bindingResult.getFieldError().getField() + "] " + bindingResult.getFieldError().getDefaultMessage();
    }

    /**
     * 所有异常捕获
     */
    @ExceptionHandler({Exception.class})
    public RespRes<Object> exception(Exception e) {
        log.error("捕获异常:",e);
        RespRes<Object> respRes;
        //判断MAP是否为空
        if (ObjectUtils.isEmpty(EXCEPTION)) {
            EXCEPTION = builder.build();
        }
        ResultCode resultCode = EXCEPTION.get(e.getClass());
        if (ObjectUtils.isEmpty(resultCode)) {
            respRes = RespRes.code(CommonCode.SERVER_ERROR);
        } else {
            respRes = RespRes.code(resultCode);
        }
        respRes.setData(null);
        return respRes;
    }

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     *
     * @param binder {@link WebDataBinder}
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
    }
}
