package i.t.pocketbloodbank;

class donor {
    public String bloodgroup,needlocation , contact;

    public donor(){}

    public donor(String bloodgroup, String needlocation, String contact) {
        this.bloodgroup = bloodgroup;
        this.needlocation = needlocation;
        this.contact = contact;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getNeedlocation() {
        return needlocation;
    }

    public void setNeedlocation(String needlocation) {
        this.needlocation = needlocation;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
