package sk.bsmk.batch.person;

public class Person {

  private String lastName;
  private String firstName;
  private Integer points;

  public Person() {}

  public Person(String firstName, String lastName, Integer points) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.points = points;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getFirstName() {
    return firstName;
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

  @Override
  public String toString() {
    return "Person{"
        + "lastName='"
        + lastName
        + '\''
        + ", firstName='"
        + firstName
        + '\''
        + ", points="
        + points
        + '}';
  }
}
