package sk.bsmk.batch.points;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PointsService {

  private final Random random;
  private final Clock clock;
  private final Map<UUID, Points> repo = new ConcurrentHashMap<>();

  @Autowired
  public PointsService(Random random, Clock clock) {
    this.random = random;
    this.clock = clock;
  }

  /**
   * Returns points that should be activated.
   */
  public List<Points> findPointsToActivate() {
    return IntStream.range(1, randomInt(10))
      .mapToObj(this::points)
      .map(points -> repo.put(points.id(), points))
      .collect(Collectors.toList());
  }

  /**
   * Activates points with
   */
  public Points activate(UUID pointId) {
    final Points points = repo.get(pointId);
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
