package es.sinjava.superhero.audit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Aspect
public class GatheredClient {

	private RestTemplate restTemplate = new RestTemplate();
	private String url;
	private String host;

	public GatheredClient(String urlAudit) {

		url = urlAudit;
		try {
			host = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// si falla le damos el nombre del thread
			host = Thread.currentThread().getName();
		}
	}
	
	@Around("@annotation(org.springframework.web.bind.annotation.GetMapping)")
	public Object auditingGet(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();

		Object proceed = joinPoint.proceed();

		auditing(joinPoint, start);
		return proceed;
	}

	@Around("@annotation(org.springframework.web.bind.annotation.PostMapping)")
	public Object auditingPost(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();

		Object proceed = joinPoint.proceed();

		auditing(joinPoint, start);
		return proceed;
	}
	
	@Around("@annotation(org.springframework.web.bind.annotation.PutMapping)")
	public Object auditingPut(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();

		Object proceed = joinPoint.proceed();

		auditing(joinPoint, start);
		return proceed;
	}
	
	@Around("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
	public Object auditingDelete(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		Object proceed = joinPoint.proceed();
		auditing(joinPoint, start);
		return proceed;
	}

	private void auditing(ProceedingJoinPoint joinPoint, long start) {
		long executionTime = System.currentTimeMillis() - start;
		TracedItem trace = new TracedItem();
		trace.setHost(host);
		trace.setClsName(joinPoint.getSignature().getDeclaringTypeName());
		trace.setMethodName(joinPoint.getSignature().getName().replaceAll(trace.getClsName(), ""));
		trace.setLap(executionTime);
		trace.setCreated(LocalDateTime.now());
		try {
			HttpEntity<TracedItem> request = new HttpEntity<>(trace);
			restTemplate.postForObject(url, request, TracedItem.class);
		} catch (RestClientException ex) {
			System.out.printf("El gathered no está preparado %s %n", ex.getMessage());
		}
	}

}
