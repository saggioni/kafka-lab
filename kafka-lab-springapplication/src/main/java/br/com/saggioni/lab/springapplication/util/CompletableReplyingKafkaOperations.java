package br.com.saggioni.lab.springapplication.util;

import java.util.concurrent.CompletableFuture;

public interface CompletableReplyingKafkaOperations<K, V, R> {

    CompletableFuture<R> requestReplyDefault(V value);

    CompletableFuture<R> requestReplyDefault(K key, V value);

    CompletableFuture<R> requestReplyDefault(Integer partition, K key, V value);

    CompletableFuture<R> requestReplyDefault(Integer partition, Long timestamp, K key, V value);

    CompletableFuture<R> requestReply(String topic, V value);

    CompletableFuture<R> requestReply(String topic, K key, V value);

    CompletableFuture<R> requestReply(String topic, Integer partition, K key, V value);

    CompletableFuture<R> requestReply(String topic, Integer partition, Long timestamp, K key, V value);

}