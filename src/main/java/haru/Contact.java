package haru;

/**
 * Represents a contact with a name, phone number, and email address.
 */
public class Contact {
    private String name;
    private String phone;
    private String email;

    /**
     * Constructs a new Contact.
     *
     * @param name The contact's name.
     * @param phone The contact's phone number.
     * @param email The contact's email address.
     */
    public Contact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return name + " (phone: " + phone + ", email: " + email + ")";
    }
}
