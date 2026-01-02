# User Guide

## Table of Contents
1. [Getting Started](#getting-started)
2. [Admin Features](#admin-features)
3. [Customer Features](#customer-features)
4. [Common Operations](#common-operations)
5. [Tips & Best Practices](#tips--best-practices)

## Getting Started

### First Time Launch

1. **Start the application** using one of these methods:
   - Run `mvn javafx:run`
   - Use `run.bat` (Windows)
   - Run from IDE
   
2. **Landing Page** appears showing available products

3. **Sign In** or **Sign Up** buttons are in the top-right corner

### Default Credentials

**Admin Account**:
- Username: `admin`
- Password: `admin123`

**Customer Accounts**:
- Username: `john_doe`, Password: `password123`
- Username: `jane_smith`, Password: `password123`

## Admin Features

### Dashboard Overview

After logging in as admin, you'll see the main dashboard with:

#### Quick Stats Cards
- **Total Sales**: Revenue from all orders
- **Total Orders**: Number of orders placed
- **Total Products**: Products in catalog
- **Total Users**: Registered customers

#### Charts & Analytics
- **Sales Trend Chart**: Line chart showing sales over time
- **Category Distribution**: Pie chart of products by category
- **Top Products**: Bar chart of best-selling items

#### Recent Activity
- Latest orders with status
- Recent user registrations
- Low stock alerts

### Product Management

Navigate to **Products** from the sidebar.

#### View Products

- **Table View**: Shows all products with columns:
  - Product ID
  - Name
  - Category
  - Price
  - Stock
  - Status (In Stock/Out of Stock)
  - Actions

- **Search**: Filter products by name or SKU
- **Category Filter**: Show products from specific category
- **Price Range**: Filter by price range

#### Add New Product

1. Click **"Add Product"** button
2. Fill in the form:
   - **Name**: Product name (required)
   - **Description**: Detailed description
   - **Price**: Product price (e.g., 99.99)
   - **SKU**: Stock Keeping Unit (auto-generated if empty)
   - **Category**: Select from dropdown
   - **Stock**: Initial stock quantity
   - **Image URL**: Product image URL or local path
   - **Brand**: Product brand
   - **Weight**: Product weight (kg)
   - **Dimensions**: Length x Width x Height (cm)

3. Click **"Save"**

#### Edit Product

1. Click **"Edit"** button in product row
2. Modify fields as needed
3. Click **"Update"**

#### Delete Product

1. Click **"Delete"** button in product row
2. Confirm deletion in dialog
3. Product is removed from catalog

**Note**: Deleting a product doesn't affect historical orders.

### Category Management

Navigate to **Categories** from the sidebar.

#### View Categories

- Hierarchical tree view of categories
- Parent categories can have subcategories
- Shows product count per category

#### Add Category

1. Click **"Add Category"** button
2. Fill in details:
   - **Name**: Category name (required)
   - **Description**: Category description
   - **Parent Category**: Select parent (optional for top-level)
   - **Image**: Category icon/image

3. Click **"Save"**

#### Edit Category

1. Right-click category or click **"Edit"**
2. Modify details
3. Click **"Update"**

#### Delete Category

1. Click **"Delete"** button
2. Confirm deletion

**Note**: Cannot delete category with products. Move or delete products first.

### Order Management

Navigate to **Orders** from the sidebar.

#### View Orders

- **All Orders**: Complete order list
- **Filter by Status**:
  - Pending
  - Processing
  - Shipped
  - Delivered
  - Cancelled

- **Order Details**:
  - Order ID
  - Customer name
  - Order date
  - Total amount
  - Status
  - Items ordered

#### Update Order Status

1. Click **"Change Status"** button
2. Select new status from dropdown:
   - **Processing**: Order is being prepared
   - **Shipped**: Order has been dispatched
   - **Delivered**: Order received by customer
   - **Cancelled**: Order cancelled

3. Click **"Update"**
4. Customer sees updated status

#### View Order Details

1. Click **"View Details"** button
2. See:
   - Customer information
   - Shipping address
   - Items with quantities
   - Subtotals
   - Tax and shipping
   - Total amount
   - Payment method
   - Order notes

#### Print Order

1. Click **"Print"** button
2. Order details open in print dialog
3. Print or save as PDF

### User Management

Navigate to **Users** from the sidebar.

#### View Users

- **Customer List**: All registered customers
- **Admin List**: System administrators
- Columns:
  - User ID
  - Username
  - Email
  - Phone
  - Registration date
  - Status (Active/Inactive)

#### Add User

1. Click **"Add User"** button
2. Fill in form:
   - **Username**: Unique username
   - **Email**: Valid email address
   - **Password**: Strong password
   - **First Name**
   - **Last Name**
   - **Phone**
   - **Address**
   - **User Type**: Admin or Customer

3. Click **"Create"**

#### Edit User

1. Click **"Edit"** button
2. Modify user details
3. **Cannot change username**
4. Click **"Update"**

#### Delete User

1. Click **"Delete"** button
2. Confirm deletion

**Note**: Cannot delete users with active orders.

#### Reset User Password

1. Click **"Reset Password"** button
2. Enter new password
3. Confirm password
4. Click **"Save"**

### Analytics & Reports

Navigate to **Analytics** from the sidebar.

#### Sales Reports

- **Daily Sales**: Revenue by day
- **Monthly Sales**: Revenue by month
- **Yearly Sales**: Annual comparison
- **Export**: Download as CSV or Excel

#### Product Reports

- **Best Sellers**: Top 10 products by sales
- **Slow Movers**: Products with low sales
- **Out of Stock**: Products needing restock
- **Category Performance**: Sales by category

#### Customer Reports

- **New Customers**: Registration trends
- **Customer Lifetime Value**: Top spending customers
- **Customer Activity**: Order frequency
- **Geographic Distribution**: Customers by location

#### Generate Custom Report

1. Click **"Custom Report"**
2. Select:
   - Report type
   - Date range
   - Filters (category, product, customer)
   - Format (PDF, CSV, Excel)

3. Click **"Generate"**
4. View or download report

## Customer Features

### Landing Page

Public page showing all available products.

#### Browse Products

- **Grid View**: Products displayed in cards
- **Search Bar**: Find products by name
- **Category Filters**: Filter by category
- **Price Range**: Set min/max price
- **Sort Options**:
  - Newest First
  - Price: Low to High
  - Price: High to Low
  - Popular
  - Name (A-Z)

#### View Product Details

1. Click on product card
2. See:
   - Product images (main + gallery)
   - Description
   - Price
   - Availability
   - Specifications
   - Reviews and ratings
   - Related products

3. Click **"Add to Cart"**

### Shopping Cart

Access cart by clicking cart icon (top-right).

#### Cart Summary

Shows:
- Product thumbnail
- Product name
- Price per unit
- Quantity (editable)
- Subtotal
- Remove button

#### Update Quantity

1. Click quantity field
2. Enter new quantity
3. Press Enter or click elsewhere
4. Subtotal updates automatically

#### Remove Item

1. Click **"Remove"** or trash icon
2. Item removed immediately
3. Cart total updates

#### Apply Discount Code

1. Enter code in **"Discount Code"** field
2. Click **"Apply"**
3. Discount shown in summary

#### Proceed to Checkout

1. Review cart items
2. Click **"Proceed to Checkout"**
3. If not logged in, prompted to login/signup

### Checkout Process

#### Step 1: Shipping Information

- Pre-filled if logged in
- Edit if needed:
  - Full name
  - Address
  - City, State, ZIP
  - Phone number

#### Step 2: Payment Method

Select payment method:
- **Credit Card**: Enter card details
- **Debit Card**: Enter card details
- **PayPal**: Redirect to PayPal
- **Cash on Delivery**: Pay when delivered

#### Step 3: Review Order

- Review all items
- Check shipping address
- Verify payment method
- See order summary:
  - Subtotal
  - Tax (calculated)
  - Shipping (calculated)
  - Discount (if applied)
  - **Total**

#### Step 4: Place Order

1. Click **"Place Order"**
2. Order confirmation shown
3. Order ID displayed
4. Confirmation email sent (if configured)

### Customer Dashboard

After login, customers see their dashboard.

#### My Orders

- **Order History**: All past orders
- **Filter by Status**:
  - All Orders
  - Pending
  - In Progress
  - Completed
  - Cancelled

- **Order Actions**:
  - View Details
  - Track Shipment
  - Cancel Order (if pending)
  - Reorder

#### Track Order

1. Click **"Track"** button
2. See order timeline:
   - Order Placed
   - Processing
   - Shipped
   - Out for Delivery
   - Delivered

3. Estimated delivery date shown

#### Cancel Order

1. Click **"Cancel"** button (only for pending orders)
2. Select cancellation reason
3. Confirm cancellation
4. Refund initiated (if paid)

#### My Profile

- View/Edit personal information:
  - Name
  - Email
  - Phone
  - Address
  - Password change

#### Saved Addresses

- Add multiple shipping addresses
- Set default address
- Edit/Delete addresses

#### Payment Methods

- Save credit/debit cards
- Set default payment method
- Secure storage (tokens only)

#### Wishlist

- Save products for later
- Move to cart
- Share wishlist

## Common Operations

### Search

**Global Search** (top navigation):
1. Click search icon or press `Ctrl+F`
2. Type product name, category, or SKU
3. Results appear as you type
4. Click result to navigate

### Filters

**Product Filters**:
- **Category**: Select one or multiple
- **Price Range**: Use slider or enter values
- **Availability**: In Stock / Out of Stock
- **Brand**: Select brands
- **Rating**: Minimum rating

**Apply Filters**:
1. Select filter options
2. Click **"Apply Filters"**
3. Products refresh automatically

**Clear Filters**:
- Click **"Clear All"** to reset

### Sorting

Available sort options:
- **Relevance**: Most relevant first (with search)
- **Newest**: Recently added products
- **Price: Low to High**: Cheapest first
- **Price: High to Low**: Most expensive first
- **Popular**: Most purchased
- **Rating**: Highest rated first
- **Name**: Alphabetical order

### Pagination

- **Items per page**: 12, 24, 48, or All
- **Navigation**: Previous, Next, Page numbers
- **Jump to page**: Enter page number

## Tips & Best Practices

### For Admins

1. **Regular Backups**: Export data regularly
2. **Stock Management**: Set low stock alerts
3. **Image Optimization**: Use compressed images for faster loading
4. **Category Organization**: Keep category tree organized
5. **Order Processing**: Update order status promptly
6. **Analytics Review**: Check reports weekly
7. **Customer Support**: Respond to inquiries quickly

### For Customers

1. **Account Security**: Use strong, unique password
2. **Save Addresses**: Store multiple shipping addresses
3. **Wishlist**: Save items you're interested in
4. **Order Tracking**: Check order status regularly
5. **Reviews**: Leave reviews to help others
6. **Notifications**: Enable email notifications

### Performance

1. **Clear Cache**: If products not updating, clear browser cache
2. **Stable Connection**: Ensure stable internet for checkout
3. **Image Loading**: Large images may take time to load
4. **Filters**: Use filters to narrow search results

### Security

1. **Logout**: Always logout on shared computers
2. **Password**: Change password periodically
3. **Email**: Verify account via email
4. **Payment**: Never share card details via email/chat

## Keyboard Shortcuts

### Global
- `Ctrl+F`: Search
- `Ctrl+H`: Home/Landing page
- `Ctrl+L`: Logout
- `F5`: Refresh current page
- `Esc`: Close dialog/modal

### Admin Dashboard
- `Ctrl+D`: Dashboard
- `Ctrl+P`: Products
- `Ctrl+C`: Categories
- `Ctrl+O`: Orders
- `Ctrl+U`: Users
- `Ctrl+A`: Analytics

### Shopping
- `Ctrl+B`: View Cart
- `Ctrl+Shift+C`: Checkout
- `Ctrl+W`: Wishlist
- `Ctrl+M`: My Orders

## Troubleshooting

### Common Issues

**Issue**: Products not loading
- **Solution**: Check internet connection, refresh page

**Issue**: Cannot add to cart
- **Solution**: Ensure you're logged in, check product availability

**Issue**: Checkout failing
- **Solution**: Verify shipping information, check payment details

**Issue**: Forgot password
- **Solution**: Click "Forgot Password" on login page

**Issue**: Order not appearing
- **Solution**: Refresh orders page, check email confirmation

### Getting Help

- **In-App Help**: Click question mark icon
- **FAQ**: See [FAQ.md](FAQ.md)
- **Contact Support**: support@smartecommerce.com
- **Documentation**: Check [docs](.) folder

---

**Happy Shopping! 🛍️**

For more detailed technical information, see the [Developer Guide](DEVELOPER_GUIDE.md).

