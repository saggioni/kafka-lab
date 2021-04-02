package br.com.saggioni.lab.springapplication.config;

import br.com.saggioni.lab.adapter.controller.ProposalController;
import br.com.saggioni.lab.adapter.idgenerator.UuidGenerator;
import br.com.saggioni.lab.adapter.repository.InMemoryProposalRepository;
import br.com.saggioni.lab.usecase.CreateProposal;
import br.com.saggioni.lab.usecase.FindProposal;
import br.com.saggioni.lab.usecase.port.ProposalRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public ProposalRepository proposalRepository() {
        return new InMemoryProposalRepository();
    }

    @Bean
    public ProposalController proposalController() {
        return new ProposalController(new CreateProposal(proposalRepository(), new UuidGenerator())
                , new FindProposal(proposalRepository()));
    }

}