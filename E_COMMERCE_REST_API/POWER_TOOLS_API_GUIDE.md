# 🔧 Power Tools E-commerce API - Complete Usage Guide

## 🚀 **Your API is Now Running!**

Your E-commerce API is successfully running with **20 professional power tools** from your JSON data. Here's how to use it:

### **Access Points:**
- **API Base URL**: http://localhost:8080
- **Swagger Documentation**: http://localhost:8080/swagger-ui/index.html
- **H2 Database Console**: http://localhost:8080/h2-console

---

## 📦 **Available Products**

Your API now contains **20 professional power tools** across **3 categories**:

### **Power Tools (17 products):**
- +GSC 2,8 'SHEAR - $129.99
- Professional Impact Driver GDR 18V-160 - $199.99
- Professional Angle Grinder GWS 7-115 - $89.99
- Professional Hammer Drill GBH 2-28 F - $349.99
- Professional Jigsaw GST 150 BCE - $159.99
- Professional Rotary Hammer GBH 18V-26 F - $399.99
- Professional Multi-Tool GOP 18V-28 - $229.99
- Professional Circular Saw GKS 190 - $189.99
- Professional Cordless Screwdriver GSR 18V-50 - $159.99
- Professional Planer GHO 26-82 - $219.99
- Impact Drill GSB20-2RE - $279.99
- Professional Cordless Rotary Hammer GBH 18V-26 - $389.99
- Professional Heat Gun GHG 20-63 - $119.99
- Professional Cordless Reciprocating Saw GSA 18 V-LI - $229.99
- MARBLE CUTTING SAW - $99.99
- ROTARY HAMMER GBH 12-52 DV - $239.99
- DEMOLITION HAMMER GSH 5 - $179.99

### **Measurement Tools (2 products):**
- Laser Reciver LR7 - $149.99
- GLL 12-22 G LI: Laser Level Green - $179.99

### **Accessories (1 product):**
- Dust Extractor GDE 230 FC-S - $179.99

---

## 🔥 **Quick Start Examples**

### **1. Browse All Products**
```bash
# Get first 10 products
curl "http://localhost:8080/api/products?page=0&size=10"

# Get all products with pagination
curl "http://localhost:8080/api/products?page=0&size=20"
```

### **2. Filter by Category**
```bash
# Get only Power Tools
curl "http://localhost:8080/api/products?category=Power%20Tools&page=0&size=10"

# Get Measurement Tools
curl "http://localhost:8080/api/products?category=Measurement%20Tools"

# Get Accessories
curl "http://localhost:8080/api/products?category=Accessories"
```

### **3. Search Products**
```bash
# Search for "drill"
curl "http://localhost:8080/api/products?name=drill"

# Search for "professional"
curl "http://localhost:8080/api/products?name=professional"
```

### **4. Price Filtering**
```bash
# Products under $200
curl "http://localhost:8080/api/products?maxPrice=200"

# Products between $100-$300
curl "http://localhost:8080/api/products?minPrice=100&maxPrice=300"

# Sort by price (lowest first)
curl "http://localhost:8080/api/products?sortBy=price&sortDir=ASC"
```

### **5. Get Specific Product**
```bash
# Get product with ID 1 (+GSC 2,8 'SHEAR)
curl "http://localhost:8080/api/products/1"

# Get product with ID 4 (Professional Hammer Drill)
curl "http://localhost:8080/api/products/4"
```

---

## 🛒 **Shopping Cart Operations**

### **1. Register a New User**
```bash
curl -X POST "http://localhost:8080/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "contractor",
    "email": "contractor@example.com",
    "password": "securepass123"
  }'
```

### **2. Login to Get JWT Token**
```bash
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user",
    "password": "user123"
  }'
```

### **3. Add Items to Cart**
```bash
# Save the token from login response
TOKEN="your_jwt_token_here"

# Add +GSC 2,8 'SHEAR to cart
curl -X POST "http://localhost:8080/api/cart/add" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "productId": 1,
    "quantity": 2
  }'

# Add Professional Hammer Drill
curl -X POST "http://localhost:8080/api/cart/add" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "productId": 4,
    "quantity": 1
  }'
```

### **4. View Cart**
```bash
# Get cart contents
curl -X GET "http://localhost:8080/api/cart" \
  -H "Authorization: Bearer $TOKEN"

# Get cart total
curl -X GET "http://localhost:8080/api/cart/total" \
  -H "Authorization: Bearer $TOKEN"

# Get cart item count
curl -X GET "http://localhost:8080/api/cart/count" \
  -H "Authorization: Bearer $TOKEN"
```

### **5. Update Cart Items**
```bash
# Update quantity of item with ID 1
curl -X PUT "http://localhost:8080/api/cart/item/1?quantity=3" \
  -H "Authorization: Bearer $TOKEN"
```

### **6. Remove Items from Cart**
```bash
# Remove item with ID 1
curl -X DELETE "http://localhost:8080/api/cart/item/1" \
  -H "Authorization: Bearer $TOKEN"

# Clear entire cart
curl -X DELETE "http://localhost:8080/api/cart/clear" \
  -H "Authorization: Bearer $TOKEN"
```

---

## 🛠️ **Advanced API Features**

### **Combined Filtering**
```bash
# Power Tools under $200, sorted by price
curl "http://localhost:8080/api/products?category=Power%20Tools&maxPrice=200&sortBy=price&sortDir=ASC"

# Search for "professional" in Power Tools category
curl "http://localhost:8080/api/products?category=Power%20Tools&name=professional"
```

### **Get Available Categories**
```bash
curl "http://localhost:8080/api/products/categories"
```

### **Admin Operations** (if you login as admin)
```bash
# Login as admin
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'

# Create new product (admin only)
curl -X POST "http://localhost:8080/api/products" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ADMIN_TOKEN" \
  -d '{
    "name": "Professional Drill Bit Set",
    "shortDescription": "Complete drill bit set for all materials",
    "fullDescription": "Professional grade drill bit set including bits for wood, metal, concrete, and masonry",
    "price": 89.99,
    "stockQuantity": 100,
    "category": "Accessories",
    "imageUrl": "images/drill_bits.jpg"
  }'
```

---

## 📊 **Sample API Responses**

### **Product Response:**
```json
{
  "id": 1,
  "name": "+GSC 2,8 'SHEAR",
  "shortDescription": "Compact and powerful cordless drill with 12V battery.",
  "fullDescription": null,
  "price": 129.99,
  "stockQuantity": 50,
  "category": "Power Tools",
  "imageUrl": "images/product_1/product_1.jpg",
  "images": [],
  "technicalSpecifications": null
}
```

### **Cart Item Response:**
```json
{
  "id": 1,
  "productId": 1,
  "productName": "+GSC 2,8 'SHEAR",
  "productDescription": "Compact and powerful cordless drill with 12V battery.",
  "productPrice": 129.99,
  "productImageUrl": "images/product_1/product_1.jpg",
  "quantity": 2,
  "totalPrice": 259.98,
  "createdAt": "2025-08-02T21:45:31.519263",
  "updatedAt": null
}
```

---

## 🌐 **Using Swagger UI (Recommended)**

1. **Open your browser** and go to: http://localhost:8080/swagger-ui/index.html

2. **You'll see all endpoints** organized by:
   - **Authentication** - Register and login
   - **Products** - Browse and search power tools
   - **Cart** - Manage shopping cart

3. **Click "Try it out"** on any endpoint to test it

4. **For authenticated endpoints:**
   - First use the login endpoint to get a JWT token
   - Click the "Authorize" button at the top
   - Enter: `Bearer YOUR_JWT_TOKEN`
   - Now you can test cart endpoints

---

## 🔧 **Product Categories Available**

- **Power Tools** - 17 professional power tools
- **Measurement Tools** - 2 laser and measurement devices  
- **Accessories** - 1 dust extraction accessory

---

## 💡 **Pro Tips**

1. **Use Swagger UI** for the easiest testing experience
2. **Save your JWT token** after login for cart operations
3. **Filter by category** to find specific types of tools
4. **Use price filters** to find tools within your budget
5. **Search by name** to find specific tools quickly

---

## 🚀 **Ready to Use!**

Your Power Tools E-commerce API is now fully functional with:
- ✅ 20 professional power tools loaded
- ✅ Complete shopping cart functionality
- ✅ User authentication and authorization
- ✅ Product filtering and search
- ✅ Interactive API documentation

**Start exploring your API at**: http://localhost:8080/swagger-ui/index.html 