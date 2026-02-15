package haru;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a list of contacts.
 * Provides operations to add, remove, and search contacts.
 */
public class ContactList {
    private final ArrayList<Contact> contacts;

    /**
     * Constructs an empty contact list.
     */
    public ContactList() {
        this.contacts = new ArrayList<>();
    }

    /**
     * Constructs a contact list from an existing ArrayList of contacts.
     *
     * @param contacts The ArrayList of contacts to initialize with.
     */
    public ContactList(ArrayList<Contact> contacts) {
        assert contacts != null : "Contacts list should not be null";
        this.contacts = contacts;
    }

    public void add(Contact contact) {
        contacts.add(contact);
    }

    public Contact remove(int index) {
        assert index >= 0 && index < contacts.size() : "Contact index out of bounds";
        return contacts.remove(index);
    }

    public Contact get(int index) {
        assert index >= 0 && index < contacts.size() : "Contact index out of bounds";
        return contacts.get(index);
    }

    public int size() {
        return contacts.size();
    }

    /**
     * Returns an unmodifiable view of the contacts.
     *
     * @return An unmodifiable List of contacts.
     */
    public List<Contact> getContacts() {
        return Collections.unmodifiableList(contacts);
    }

    /**
     * Finds contacts whose name contains the given keyword (case-insensitive).
     *
     * @param keyword The keyword to search for.
     * @return A new ContactList containing matching contacts.
     */
    public ContactList findContacts(String keyword) {
        ArrayList<Contact> matching = new ArrayList<>();
        for (Contact contact : contacts) {
            if (contact.getName().toLowerCase().contains(keyword.toLowerCase())) {
                matching.add(contact);
            }
        }
        return new ContactList(matching);
    }
}
