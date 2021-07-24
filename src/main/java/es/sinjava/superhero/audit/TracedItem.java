package es.sinjava.superhero.audit;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "trace")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TracedItem {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String host;
	
	@Column(nullable = false)
	private String clsName;

	@Column(nullable = false)
	private String methodName;

	@Column(nullable = false)
	private Long lap;
	
	@Column(nullable = false)
	private LocalDateTime created ;
	
}
