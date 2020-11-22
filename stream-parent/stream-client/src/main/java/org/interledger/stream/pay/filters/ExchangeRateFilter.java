package org.interledger.stream.pay.filters;

import java.util.Objects;
import org.interledger.core.fluent.FluentUnsignedLong;
import org.interledger.stream.pay.filters.chain.StreamPacketFilterChain;
import org.interledger.stream.pay.model.ModifiableStreamPacketRequest;
import org.interledger.stream.pay.model.SendState;
import org.interledger.stream.pay.model.StreamPacketReply;
import org.interledger.stream.pay.model.StreamPacketRequest;
import org.interledger.stream.pay.trackers.ExchangeRateTracker;

/**
 * Compute the realized exchange rate from STREAM replies.
 */
public class ExchangeRateFilter implements StreamPacketFilter {

  // Static because these filters will be constructed a lot.
//  private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRateFilter.class);

  private final ExchangeRateTracker exchangeRateTracker;

  public ExchangeRateFilter(final ExchangeRateTracker exchangeRateTracker) {
    this.exchangeRateTracker = Objects.requireNonNull(exchangeRateTracker);
  }

  @Override
  public SendState nextState(final ModifiableStreamPacketRequest streamPacketRequest) {
    Objects.requireNonNull(streamPacketRequest);
    return SendState.Ready;
  }

  @Override
  public StreamPacketReply doFilter(
    final StreamPacketRequest streamRequest, final StreamPacketFilterChain filterChain
  ) {
    Objects.requireNonNull(streamRequest);
    Objects.requireNonNull(filterChain);

    final StreamPacketReply streamPacketReply = filterChain.doFilter(streamRequest);

    // Discard 0 amount packets
    if (FluentUnsignedLong.of(streamRequest.sourceAmount()).isNotPositive()) {
      return streamPacketReply;
    }

    // Only track the rate for authentic STREAM replies
    if (streamPacketReply.isAuthentic()) {
      // Only if there's a dest amount claimed.
      streamPacketReply.destinationAmountClaimed()
        .ifPresent(claimedReceivedAmount -> this.exchangeRateTracker
          .updateRate(streamRequest.sourceAmount(), claimedReceivedAmount)
        );
    }

    return streamPacketReply;
  }

}