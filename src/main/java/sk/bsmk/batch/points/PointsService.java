package sk.bsmk.batch.points;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PointsService {

  private static final Logger log = LoggerFactory.getLogger(PointsService.class);

  private final AtomicInteger numberOfActivations = new AtomicInteger();
  private final AtomicInteger numberOfGenerations = new AtomicInteger();
  private final Random random;
  private final Clock clock;
  private final Map<UUID, Points> repo = new ConcurrentHashMap<>();
  private final Map<Integer, Integer> generations = new ConcurrentHashMap<>();

  @Autowired
  public PointsService(Random random, Clock clock) {
    this.random = random;
    this.clock = clock;
  }

  public Integer numberOfActivations() {
    return numberOfActivations.get();
  }

  public Integer numberOfGenerations() {
    return numberOfGenerations.get();
  }

  public Map<Integer, Integer> generations() {
    return generations;
  }

  /**
   * Returns points that should be activated.
   */
  public List<Points> findPointsToActivate() {
    final int rowsToGenerate = randomInt(10);
    log.info("Going to generate {} rows", rowsToGenerate);
    generations.put(numberOfGenerations.incrementAndGet(), rowsToGenerate);
    return IntStream.range(1, rowsToGenerate)
      .mapToObj(this::points)
      .peek(points -> repo.put(points.id(), points))
      .collect(Collectors.toList());
  }

  /**
   * Activates points with
   */
  public Points activate(UUID pointId) {
    final Points points = repo.get(pointId);

    numberOfActivations.incrementAndGet();

    return ImmutablePoints.copyOf(points)
      .withState(PointState.ACTIVE)
      .withUpdatedAt(clock.instant());
  }

  private Points points(int index) {
    final Instant instant = clock.instant();
    return ImmutablePoints
      .builder()
      .owner("owner-" + index)
      .state(PointState.PENDING)
      .amount(randomInt(100))
      .createdAt(instant)
      .updatedAt(instant)
      .build();
  }

  private int randomInt(int base) {
    return random.nextInt(base) + base;
  }

}
