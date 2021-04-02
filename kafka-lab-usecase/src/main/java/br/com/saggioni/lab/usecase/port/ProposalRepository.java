package br.com.saggioni.lab.usecase.port;

import br.com.saggioni.lab.domain.Proposal;
import java.util.List;
import java.util.Optional;

public interface ProposalRepository {
    Proposal create(Proposal proposal);

    Optional<Proposal> findById(String id);

    List<Proposal> findAllProposals();
}
