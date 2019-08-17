package sk.bsmk.batch.parser;

import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.annotations.UpperCase;

public class PointsImportRowTypeA implements PointsImportRow {

  @Parsed(index = 0)
  private String type;

  @Parsed(index = 1)
  @UpperCase
  private String firstName;

  @Parsed(index = 2)
  @UpperCase
  private String lastName;

  @Parsed(index = 3)
  private Integer points;

  @Override
  public String toString() {
    return "PointsImportRow{"
        + "type='"
        + type
        + '\''
        + ", firstName='"
        + firstName
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + ", points="
        + points
        + '}';
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
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

  public Integer getPoints() {
    return points;
  }

  public void setPoints(Integer points) {
    this.points = points;
  }
}
