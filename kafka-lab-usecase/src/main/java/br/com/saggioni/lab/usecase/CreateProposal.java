package br.com.saggioni.lab.usecase;

import br.com.saggioni.lab.domain.Proposal;
import br.com.saggioni.lab.usecase.port.IdGenerator;
import br.com.saggioni.lab.usecase.port.ProposalRepository;
import br.com.saggioni.lab.usecase.exception.ProposalAlreadyExistsException;
import br.com.saggioni.lab.usecase.validator.ProposalValidator;

public final class CreateProposal {
    private final ProposalRepository repository;
    private final IdGenerator idGenerator;

    public CreateProposal(final ProposalRepository repository, final IdGenerator idGenerator) {
        this.repository = repository;
        this.idGenerator = idGenerator;
    }

    public Proposal create(final Proposal proposal) {
        ProposalValidator.validateCreateProposal(proposal);
        if (repository.findById(proposal.getId()).isPresent()) {
            throw new ProposalAlreadyExistsException(proposal.getId());
        }
        Proposal newProposal = new Proposal(idGenerator.generate(), proposal.getConditions(), proposal.getValue());
        return repository.create(newProposal);
    }
}