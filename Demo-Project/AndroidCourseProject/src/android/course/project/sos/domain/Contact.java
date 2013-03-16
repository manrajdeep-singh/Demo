package android.course.project.sos.domain;

public class Contact {
	private String ID;
    private String name;
    private String phoneNumber;
    private String email;
    private boolean isSms;
    private boolean isEmail;
    private boolean isPhone;
    private boolean checked = false;

    public Contact() {

    }
    
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isSms() {
		return isSms;
	}

	public void setSms(boolean sms) {
		this.isSms = sms;
	}

	public boolean isEmail() {
		return isEmail;
	}

	public void setEmail(boolean email) {
		this.isEmail = email;
	}

	public boolean isPhone() {
		return isPhone;
	}

	public void setPhone(boolean phone) {
		this.isPhone = phone;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public boolean isChecked() {
		return checked;
	}

	public void toggleChecked() {
	      checked = !checked ;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setChecked(boolean checked) {
	      this.checked = checked;
	    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
