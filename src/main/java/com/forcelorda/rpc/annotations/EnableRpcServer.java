package com.forcelorda.rpc.annotations;
import java.lang.annotation.*;
import org.springframework.context.annotation.Import;

import com.forcelorda.rpc.spring.server.EnableRpcServerImportSelector;
@Target(value={java.lang.annotation.ElementType.TYPE})
@Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(value={EnableRpcServerImportSelector.class})
public @interface EnableRpcServer {

}
