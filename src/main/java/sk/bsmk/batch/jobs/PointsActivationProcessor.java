package sk.bsmk.batch.jobs;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import sk.bsmk.batch.points.Points;
import sk.bsmk.batch.points.PointsService;

@Component
public class PointsActivationProcessor implements ItemProcessor<Points, Points> {

  private final PointsService pointsService;

  public PointsActivationProcessor(PointsService pointsService) {
    this.pointsService = pointsService;
  }

  @Override
  public Points process(Points item) throws Exception {
    return pointsService.activate(item.id());
  }

}
