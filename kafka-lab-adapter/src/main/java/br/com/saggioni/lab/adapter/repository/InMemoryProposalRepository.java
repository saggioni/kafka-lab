package br.com.saggioni.lab.adapter.repository;


import br.com.saggioni.lab.domain.Proposal;
import br.com.saggioni.lab.usecase.port.ProposalRepository;

import java.util.*;

public class InMemoryProposalRepository implements ProposalRepository {
    private final Map<String, Proposal> inMemoryDb = new HashMap<>();

    public InMemoryProposalRepository() {
        inMemoryDb.put("1", new Proposal("1", "Teste1", 1000));
        inMemoryDb.put("2", new Proposal("2", "Teste2", 2000));
        inMemoryDb.put("3", new Proposal("3", "Teste3", 3000));
    }

    @Override
    public Proposal create(final Proposal proposal) {
        inMemoryDb.put(proposal.getId(), proposal);
        return proposal;
    }

    @Override
    public Optional<Proposal> findById(final String id) {
        return Optional.ofNullable(inMemoryDb.get(id));
    }

    @Override
    public List<Proposal> findAllProposals() {
        return new ArrayList<>(inMemoryDb.values());
    }
}
