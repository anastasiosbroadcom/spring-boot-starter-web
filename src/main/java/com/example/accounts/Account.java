package com.example.accounts;

public class Account {

    private Long Id;
	private String name;
	private String email;
	private String password;
	private String status;

    public Account(Long id, String name) {
        this.Id = id;
        this.name = name;
    }

    public Long getId() {
		return Id;
	}

    public void setId(Long id) {
        this.Id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
