package model;

/**
 * @description: This is the class of person, including investors and managers
 **/
public abstract class Person {
    private String id;
    private String passWord;

    public Person() {}

    public Person(String id, String passWord) {
        this.id = id;
        this.passWord = passWord;
    }

    /**
     * below are getter and setter method of person's id and password.
     * @return
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

}
