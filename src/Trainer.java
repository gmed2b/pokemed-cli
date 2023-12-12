import java.io.Serializable;

public class Trainer implements Serializable {
  private String name;

  public Trainer(String name) {
    setName(name);
  }

  @Override
  public String toString() {
    return "Trainer [name=" + name + "]";
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    if (name.length() < 2) {
      throw new IllegalArgumentException("Name must be at least 2 characters long");
    }
    this.name = name;
  }
}
