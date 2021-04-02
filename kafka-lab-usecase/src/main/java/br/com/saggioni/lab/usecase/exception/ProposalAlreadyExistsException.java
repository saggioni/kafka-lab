package br.com.saggioni.lab.usecase.exception;

public class ProposalAlreadyExistsException extends RuntimeException {
	public ProposalAlreadyExistsException(final String id) {
		super(id);
	}
}
