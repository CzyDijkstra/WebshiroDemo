package cn.czy15.webshirodemo.controller;

import cn.czy15.webshirodemo.exception.BusinessException;
import cn.czy15.webshirodemo.exception.code.BaseResponseCode;
import cn.czy15.webshirodemo.utils.DataResult;
import cn.czy15.webshirodemo.vo.req.TestReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Zhuoyu Chen
 * @date 2019/12/27 0027 - 20:12
 */
@RestController
@Api(tags = "测试页接口")
@RequestMapping("/test")
public class TestController {

    @GetMapping("/index")
    @ApiOperation(value="测试页引导接口")
    public String TestHelloWorld()
    {
        //System.out.println("已经接收到了testhello请求");
        return "hello";
    }

    @GetMapping("/home")
    @ApiOperation(value = "测试DataResult统一返回格式的接口")
    public DataResult<String> gethome()
    {
        DataResult<String> result=DataResult.success("测试成功");
        return result;
    }

    @GetMapping("/business/error")
    @ApiOperation(value = "测试主动抛出业务异常接口")
    public DataResult<String> testBusinessError(@RequestParam String type)
    {
        if(!type.equals("1")||type.equals("2")||type.equals("3"))
        {
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        DataResult<String> result=new DataResult(0,type);
        return result;
    }

    @PostMapping("/valid/error")
    @ApiOperation(value = "测试Validator抛出业务异常")
    public DataResult testValid(@RequestBody @Valid TestReqVO vo)
    {
        DataResult result=DataResult.success();
        return result;
    }
}
