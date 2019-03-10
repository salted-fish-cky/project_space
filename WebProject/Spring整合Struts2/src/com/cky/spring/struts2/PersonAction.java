package com.cky.spring.struts2;

public class PersonAction {
    private PersonService personService;

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public String execute(){

        personService.save();
        System.out.println("execute...");
        return "success";
    }
}
