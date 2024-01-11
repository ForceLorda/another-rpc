package com.forcelorda.rpc.spring.server;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class EnableRpcServerImportSelector implements ImportSelector{

	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return new String[] {
				SpringServiceMappingBeanPostProcessor.class.getName(),
				RpcServerAutoConfiguration.class.getName()};
	}

}
