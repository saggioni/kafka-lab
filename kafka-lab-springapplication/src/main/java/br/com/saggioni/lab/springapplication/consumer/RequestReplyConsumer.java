package br.com.saggioni.lab.springapplication.consumer;

import br.com.saggioni.lab.adapter.controller.ProposalController;
import br.com.saggioni.lab.domain.Proposal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
public class RequestReplyConsumer {
    @Autowired
    private ProposalController proposalController;

    @KafkaListener(topics = "${kafka.topic.proposal.request}", containerFactory = "requestReplyListenerContainerFactory")
    @SendTo()
    public Proposal consume(String proposalId) {
            //System.out.println("Consuming Request/Command from Kafka Topic (step 2): " + proposalId);
            Proposal proposal = proposalController.getProposal(proposalId);
            //System.out.println("Producing Reply/Response to Kafka Topic (step 3): " + proposalId);
            return proposal;
    }
}
