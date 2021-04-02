package br.com.saggioni.lab.usecase.exception;

public class ProposalValidationException extends RuntimeException {
	public ProposalValidationException(final String message) {
		super(message);
	}
}
