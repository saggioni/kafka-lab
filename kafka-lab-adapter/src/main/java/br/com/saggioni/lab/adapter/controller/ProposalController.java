package br.com.saggioni.lab.adapter.controller;

import br.com.saggioni.lab.domain.Proposal;
import br.com.saggioni.lab.usecase.CreateProposal;
import br.com.saggioni.lab.usecase.FindProposal;

import java.util.List;

public class ProposalController {
    private final CreateProposal createProposal;
    private final FindProposal findProposal;

    public ProposalController(final CreateProposal createProposal, final FindProposal findProposal) {
        this.createProposal = createProposal;
        this.findProposal = findProposal;
    }

    public Proposal createProposal(final Proposal proposal) {
        return createProposal.create(proposal);
    }

    public Proposal getProposal(final String proposalId) {
        return findProposal.findById(proposalId).orElseThrow(() -> new RuntimeException("Proposal not found"));
    }

    public List<Proposal> allProposals() {
        return findProposal.findAllProposals();
    }
}
