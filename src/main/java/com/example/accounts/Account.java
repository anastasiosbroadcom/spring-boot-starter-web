package com.example.accounts;

public class Account {

    private Long Id;
	private String Name;
	private String Email;
	private String Password;
	private String Status;

    public Long getId() {
		return Id;
	}

    public void setId(Long id) {
        this.Id = id;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

}
