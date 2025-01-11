package com.namagong.travel_route_back.configure;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

/**
 * @since 2025. 1. 11.
 * @author 황보경
 * <PRE>
 * --------------------------
 * 개정이력
 * 2025. 1. 11. 황보경 : Bean생성 시 이름은 패키지까지 포함하는 코드 최초작성
 */
public class CustomBeanNameGenerator implements BeanNameGenerator {

	@Override
	public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		// TODO Auto-generated method stub
		final String result;
		result = generateFullBeanName((AnnotatedBeanDefinition) definition);
		return result;
	}
	
	private String generateFullBeanName(final AnnotatedBeanDefinition def) {
		return def.getMetadata().getClassName();
	}

}
