package ddf.training;
/**
 * Created by smorris on 6/3/16.
 */
public class HelloWorld {

    private String description;
    private String name;

    public HelloWorld(String name ) {
        this.name = name;
        System.out.println("Hello World " + name);
        description = "default";
    }

    public void setDescription(String description) {
        System.out.println("Setting description to: " + description);
        this.description = description;
    }

    public void setName(String name) {
        System.out.println("Setting name to: " + name);
        this.name = name;
    }

}
