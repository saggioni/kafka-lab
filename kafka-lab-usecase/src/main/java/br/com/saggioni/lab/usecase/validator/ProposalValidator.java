package br.com.saggioni.lab.usecase.validator;

import br.com.saggioni.lab.domain.Proposal;
import br.com.saggioni.lab.usecase.exception.ProposalValidationException;

public class ProposalValidator {
    public static void validateCreateProposal(final Proposal proposal) {
        if (proposal == null)
            throw new ProposalValidationException("Proposal should not be null");

        if (proposal.getConditions().isBlank() || proposal.getConditions() == null)
            throw new ProposalValidationException("Conditions should not be null");

        if (proposal.getValue() <= 0)
            throw new ProposalValidationException("Value should be greater than zero");
    }
}