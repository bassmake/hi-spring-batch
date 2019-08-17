package sk.bsmk.batch.parser;

import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.annotations.UpperCase;

public class PointsImportRowTypeB implements PointsImportRow {

  @Parsed(index = 0)
  private String type;

  @Parsed(index = 1)
  private Integer points;

  @Parsed(index = 2)
  @UpperCase
  private String firstName;

  @Parsed(index = 3)
  @UpperCase
  private String lastName;

  @Override
  public String toString() {
    return "PointsImportRowTypeB{"
        + "type='"
        + type
        + '\''
        + ", points="
        + points
        + ", firstName='"
        + firstName
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + '}';
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getPoints() {
    return points;
  }

  public void setPoints(Integer points) {
    this.points = points;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
