# ABND Inventory App - Stage 2

About the app...

## How To Use

## Task list

### Layout

- **Overall Layout**

- [x] The app contains activities and/or fragments
- [x] Add inventory
- [x] See Product Details
- [x] Edit Product Details
- [x] See a list of all inventory from a Main Activity
- [x] Using a single activity
- [x] Using navigation pattern (Navigation Drawer, View Pager, Up/Back Navigation or Intents)

- **List Item Layout in the Main Activity**

- [x] Each list item displays the Product Name, Price and Quantity
- [x] Each list item contains a Sale `button` that reduce the total quantity of that particular product by one
- [x] Handling the negative product quantity

- **Product Detail Layout**

- [x] The Product Detail Layout displays the Product Name, Price, Quantity, Supplier Name, and Supplier Phone Number that's stored in the database
- [x] The Product Detail Layout also contains buttons that increase and decrease the available quantity displayed
- [x] Add a check in the code to ensure that no negative quantities display (zero is the lowest amount)
- [x] The Product Detail Layout contains a button to delete the product record entirely
- [x] The Product Detail Layout contains a button to order from the supplier. In other words, there exists a button to contains a button for the user to contact the supplier via an intent to a phone app using the Supplier Phone Number stored in the database

- **Default TextView**

- [x] When there is no information to display in the database, the layout displays a TextView with instructions on how to populate the database

### Functionality

- **Runtime Errors**
  
- [x] The code runs without errors. For example, when user inputs product information (quantity, price, name), instead of erroring out, the app includes logic to validate that no null values are accepted. If a null value is inputted, add a Toast that prompts the user to input the correct information before they can continue.

- **ListView Population**

- [x] The Main Activity displaying the list of current inventory contains a ListView that populates with the current products stored in the table.

- **Add Product Button**

- [x] The Main Activity contains an Add Product Button prompts the user for product information and supplier information which are then properly stored in the table.
- [x] Before the information is added to the table, it must be validated - In particular, empty product information is not accepted. If user inputs invalid product information (name, price, quantity, supplier name, supplier phone number), instead of erroring out, the app includes logic to validate that no null values are accepted. If a null value is inputted, add a Toast that prompts the user to input the correct information before they can continue.

- **Input Validation**

- [x] In the Edit Product Activity, user input is validated. In particular, empty product information is not accepted. If user inputs invalid product information (name, price, quantity, supplier name, supplier phone number), instead of erroring out, the app includes logic to validate that no null values are accepted. If a null value is inputted, add a Toast that prompts the user to input the correct information before they can continue.

- **Sale Button**

- [x] In the Main Activity that displays a list of all available inventory, each List Item contains a Sale Button which reduces the available quantity for that particular product by one (include logic so that no negative quantities are displayed).

- **Detail View Intent**

- [x] When a user clicks on a List Item from the Main Activity, it opens up the detail screen for the correct product.

- **Modify Quantity Buttons**

- [x] In the Detail View for each item, there are Buttons that correctly increase or decrease the quantity for the correct product.
- [x] Add a check in the code to ensure that no negative quantities display (zero is the lowest amount).
- [ ] The student may also add input for how much to increase or decrease the quantity by if not using the default of 1.

- **Order Button**

- [x] The Detail Layout contains a button for the user to contact the supplier via an intent to a phone app using the Supplier Phone Number stored in the database.

- **Delete Button**

- [ ] In the Detail Layout, there is a Delete Button that prompts the user for confirmation and, if confirmed, deletes the product record entirely and sends the user back to the main activity.

## Knowing bugs

## License
