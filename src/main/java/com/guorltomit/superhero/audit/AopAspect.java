package com.guorltomit.superhero.audit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Aspect
@Component
public class AopAspect {

	private String host;

	private String url;

	// Volcado sobre el servicio rest
	private RestTemplate restTemplate = new RestTemplate();

	public AopAspect(@Value("${audit.url}") String urlAudit) {

		url = urlAudit;
		try {
			host = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// si falla le damos el nombre del thread
			host = Thread.currentThread().getName();
		}
	}

	@Around("@annotation(com.guorltomit.superhero.audit.SmartAudit)")
	public Object auditing(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();

		Object proceed = joinPoint.proceed();

		long executionTime = System.currentTimeMillis() - start;
		TracedItem trace = new TracedItem();
		trace.setHost(host);
		trace.setClsName(joinPoint.getSignature().getDeclaringTypeName());
		trace.setMethodName(joinPoint.getSignature().getName().replaceAll(trace.getClsName(), ""));
		trace.setLap(executionTime);
		trace.setCreated(LocalDateTime.now());

		HttpEntity<TracedItem> request = new HttpEntity<>(trace);
		restTemplate.postForObject(url, request, TracedItem.class);
		return proceed;
	}

}
