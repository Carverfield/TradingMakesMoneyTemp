package model;

import java.util.ArrayList;

/**
 * Default setting manager's id=123, password=123.
 */
public class ManagerList {
    private ArrayList<Manager> managerList;

    public ManagerList() {
        Manager manager = new Manager("123", "123");
        this.managerList.add(manager);
    }


    /**
     * Check manager's id and password. Return the instance of manager. If id and password doesn't match, return null.
     * default setting: manager's id =  123, password = 123.
     * @param id
     * @param password
     * @return
     */
    public Manager checkManagerIdentity(String id, String password) {
        for (Manager manager : managerList) {
            if (manager.getId().equals(id) && manager.getPassWord().equals(password) ) {
                return manager;
            }
        }
        return null;
    }

}
