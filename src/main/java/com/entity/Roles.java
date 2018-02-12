package com.entity;

public class Roles {
    private boolean designer = false;
    private boolean developer = false;
    private boolean tester = false;

    public boolean isTester() {
        return tester;
    }

    public void setTester(boolean tester) {
        this.tester = tester;
    }

    public boolean isDeveloper() {
        return developer;
    }

    public void setDeveloper(boolean developer) {
        this.developer = developer;
    }

    public boolean isDesigner() {
        return designer;
    }

    public void setDesigner(boolean designer) {
        this.designer = designer;
    }
}
