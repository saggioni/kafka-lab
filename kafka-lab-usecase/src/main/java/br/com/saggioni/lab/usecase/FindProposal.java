package br.com.saggioni.lab.usecase;

import br.com.saggioni.lab.domain.Proposal;
import br.com.saggioni.lab.usecase.port.ProposalRepository;

import java.util.List;
import java.util.Optional;

public final class FindProposal {

	private final ProposalRepository repository;

	public FindProposal(final ProposalRepository repository) {
		this.repository = repository;
	}

	public Optional<Proposal> findById(final String id) {
		return repository.findById(id);
	}

	public List<Proposal> findAllProposals() {
		return repository.findAllProposals();
	}
}
