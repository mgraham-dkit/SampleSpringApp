package web_patterns.business;

import lombok.*;

/**
 *
 * @author michelle
 */

// Add getter methods
@Getter
// Add a toString method
@ToString
// Add equals and hashcode methods - only include the specifically noted variables
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
// Add the ability to build object with any components in any order
@Builder
// Add an all-args constructor
@AllArgsConstructor
public class Customer implements Comparable<Customer> {
    // Annotate all fields that cannot be null with NonNull
    // Don't include any auto-generating primary key fields as these may not be known when the object is created
    @EqualsAndHashCode.Include
    private int customerNumber;
    @NonNull
    private String customerName;
    @NonNull
    private String contactLastName;
    @NonNull
    private String contactFirstName;
    @NonNull
    private String phone;
    @NonNull
    private String addressLine1;
    private String addressLine2;
    @NonNull
    private String city;
    private String state;
    private String postalCode;
    @NonNull
    private String country;
    private int salesRepEmployeeNumber;
    private double creditLimit;

    public String format() {
        String formattedText = customerNumber + ": " + customerName
                + "\n\t" + contactLastName + ", " + contactFirstName
                + "\n\tPhone: " + phone
                + "\n\t\t" + addressLine1
                + "\n\t\t" + addressLine2
                + "\n\t\t" + city
                + "\n\t\t" + state
                + "\n\t\t" + postalCode
                + "\n\t\t" + country;
        if (salesRepEmployeeNumber != 0) {
            formattedText = formattedText + "\n\tSales Rep: " + salesRepEmployeeNumber;
        } else {
            formattedText = formattedText + "\n\tSales Rep: None";
        }
        formattedText = formattedText + "\n\tCredit limit: " + creditLimit;

        return formattedText;
    }

    @Override
    public int compareTo(Customer c) {
        if (customerNumber < c.customerNumber) {
            return -1;
        } else if (customerNumber > c.customerNumber) {
            return 1;
        }
        return 0;
    }
}
