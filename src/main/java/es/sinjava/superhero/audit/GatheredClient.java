package es.sinjava.superhero.audit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.EnumSet;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Aspect
public class GatheredClient {

	public enum Strategy {
		GET, POST, PUT, DELETE, ALLREST, CUSTOM
	}

	private RestTemplate restTemplate = new RestTemplate();
	private String url;
	private String host;

	// Por defecto todos los rest
	private EnumSet<Strategy> strategies = EnumSet.of(Strategy.ALLREST);

	private final static EnumSet<Strategy> ALLREST = EnumSet.of(Strategy.GET, Strategy.PUT, Strategy.POST,
			Strategy.DELETE);

	public GatheredClient(String urlAudit) {
		this(urlAudit, ALLREST);
	}

	public GatheredClient(String urlAudit, Strategy strategiesIn) {
		this(urlAudit, EnumSet.of(strategiesIn));
	}

	public GatheredClient(String urlAudit, EnumSet<Strategy> strategiesIn) {
		url = urlAudit;
		strategies = strategiesIn;
		if (strategiesIn.contains(Strategy.ALLREST)) {
			strategies.addAll(ALLREST);
		}
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
		if (strategies.contains(Strategy.GET)) {
			auditing(joinPoint, start);
		}
		return proceed;
	}

	@Around("@annotation(org.springframework.web.bind.annotation.PostMapping)")
	public Object auditingPost(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();

		Object proceed = joinPoint.proceed();
		if (strategies.contains(Strategy.POST)) {
			auditing(joinPoint, start);
		}
		return proceed;
	}

	@Around("@annotation(org.springframework.web.bind.annotation.PutMapping)")
	public Object auditingPut(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();

		Object proceed = joinPoint.proceed();
		if (strategies.contains(Strategy.PUT)) {
			auditing(joinPoint, start);
		}
		return proceed;
	}

	@Around("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
	public Object auditingDelete(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		Object proceed = joinPoint.proceed();
		if (strategies.contains(Strategy.DELETE)) {
			auditing(joinPoint, start);
		}
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
