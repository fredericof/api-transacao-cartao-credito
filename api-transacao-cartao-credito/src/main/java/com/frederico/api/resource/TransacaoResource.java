package com.frederico.api.resource;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.frederico.api.model.Pagamento;

@RestController
@RequestMapping("/v1/public/transacao")
public class TransacaoResource {
	
	@PostMapping()
	public ResponseEntity<Boolean> realizarPagamento(@RequestBody Pagamento pagamento) {
		
		if (pagamento.getNumeroCartao().equals("4485 5258 0903 7288")) {
			
			// Realiza a auditoria da transação
			processaAuditoria(pagamento);
			
			return ResponseEntity.status(HttpStatus.OK).body(new Boolean(true));
		} else {
			
			return ResponseEntity.status(HttpStatus.OK).body(new Boolean(false));
		}
		
	}
	
	private Boolean processaAuditoria(Pagamento pagamento) {
		HttpHeaders headers = new HttpHeaders();
	    headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
	    headers.setContentType(MediaType.APPLICATION_JSON);
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Pagamento> requestBody = new HttpEntity<>(new Pagamento(
				pagamento.getUsuario(), pagamento.getNumeroCartao(), pagamento.getValorDebito()),headers);
		
		  Boolean response = restTemplate.postForObject("http://localhost:8083/v1/public/auditoria",
				  requestBody, Boolean.class);
		  
		  return response;
	}

}
