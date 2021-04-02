package br.com.saggioni.lab.adapter.idgenerator;


import br.com.saggioni.lab.usecase.port.IdGenerator;
import java.util.UUID;

public class UuidGenerator implements IdGenerator {
	@Override
	public String generate() {
		return UUID.randomUUID().toString();
	}
}
