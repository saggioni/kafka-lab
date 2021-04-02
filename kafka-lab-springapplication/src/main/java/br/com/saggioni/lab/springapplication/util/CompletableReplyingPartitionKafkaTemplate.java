package br.com.saggioni.lab.springapplication.util;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.GenericMessageListenerContainer;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

public class CompletableReplyingPartitionKafkaTemplate<K, V, R> extends ReplyingPartitionKafkaTemplate<K, V, R>
    implements CompletableReplyingKafkaOperations<K, V, R> {

  public CompletableReplyingPartitionKafkaTemplate(ProducerFactory<K, V> producerFactory,
                                                   GenericMessageListenerContainer<K, R> replyContainer) {
    super(producerFactory, replyContainer);
  }

  @Override
  public CompletableFuture<R> requestReplyDefault(V value) {
    return adapt(sendAndReceiveDefault(value));
  }

  @Override
  public CompletableFuture<R> requestReplyDefault(K key, V value) {
    return adapt(sendAndReceiveDefault(key, value));
  }

  @Override
  public CompletableFuture<R> requestReplyDefault(Integer partition, K key, V value) {
    return adapt(sendAndReceiveDefault(partition, key, value));
  }

  @Override
  public CompletableFuture<R> requestReplyDefault(Integer partition, Long timestamp, K key, V value) {
    return adapt(sendAndReceiveDefault(partition, timestamp, key, value));
  }

  @Override
  public CompletableFuture<R> requestReply(String topic, V value) {
    return adapt(sendAndReceive(topic, value));
  }

  @Override
  public CompletableFuture<R> requestReply(String topic, K key, V value) {
    return adapt(sendAndReceive(topic, key, value));
  }

  @Override
  public CompletableFuture<R> requestReply(String topic, Integer partition, K key, V value) {
    return adapt(sendAndReceive(topic, partition, key, value));
  }

  @Override
  public CompletableFuture<R> requestReply(String topic, Integer partition, Long timestamp, K key, V value) {
    return adapt(sendAndReceive(topic, partition, timestamp, key, value));
  }

  private CompletableFuture<R> adapt(RequestReplyFuture<K, V, R> requestReplyFuture) {
    CompletableFuture<R> completableResult = new CompletableFuture<>() {
      @Override
      public boolean cancel(boolean mayInterruptIfRunning) {
        boolean result = requestReplyFuture.cancel(mayInterruptIfRunning);
        super.cancel(mayInterruptIfRunning);
        return result;
      }
    };
    // request
    requestReplyFuture.getSendFuture().addCallback(new ListenableFutureCallback<>() {
      @Override
      public void onSuccess(SendResult<K, V> sendResult) {
        // Do Nothing
      }

      @Override
      public void onFailure(Throwable t) {
        completableResult.completeExceptionally(t);
      }
    });
    // reply
    requestReplyFuture.addCallback(new ListenableFutureCallback<>() {
      @Override
      public void onSuccess(ConsumerRecord<K, R> result) {
        completableResult.complete(result.value());
      }

      @Override
      public void onFailure(Throwable t) {
        completableResult.completeExceptionally(t);
      }
    });
    return completableResult;
  }

  public void setReplyTimeout(Long replyTimeout) {
    super.setDefaultReplyTimeout(Duration.ofMillis(replyTimeout));
  }
}
