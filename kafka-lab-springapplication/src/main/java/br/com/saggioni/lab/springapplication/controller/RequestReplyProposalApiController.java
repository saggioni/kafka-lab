package br.com.saggioni.lab.springapplication.controller;

import br.com.saggioni.lab.domain.Proposal;
import br.com.saggioni.lab.springapplication.consumer.SimpleConsumer;
import br.com.saggioni.lab.springapplication.util.CompletableReplyingKafkaOperations;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class RequestReplyProposalApiController {
    @Autowired
    private CompletableReplyingKafkaOperations<String, String, Proposal> requestReplyClient;

    @Value("${kafka.topic.proposal.request}")
    private String requestTopic;

    @GetMapping(value = "/rr-proposals/{proposalId}")
    public Proposal getProposal(@PathVariable("proposalId") String proposalId) {
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            Proposal proposal = getProposalAsync(proposalId).get();
            stopWatch.stop();
            System.out.printf("%s: %s - %s%n", RequestReplyProposalApiController.class.getName(), proposal.getId(), stopWatch.toString());
            return proposal;
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to get Proposal", e);
        }
    }

    public CompletableFuture<Proposal> getProposalAsync(String proposalId) {
        return requestReplyClient.requestReply(requestTopic, proposalId)
                .exceptionally(ex -> {
                    System.out.println("Request-Reply Exception: " + ex.getMessage());
                    return null;
                });
    }
}


