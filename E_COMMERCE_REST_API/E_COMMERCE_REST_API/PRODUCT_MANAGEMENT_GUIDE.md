# Product Management Guide

## Overview

This guide explains how to manage product data in the E-commerce REST API using JSON files. The system provides a flexible way to update products without code changes.

## File Structure

### JSON File Location
```
src/main/resources/products.json
```

### JSON Structure
Each product in the JSON file follows this structure:

```json
{
  "id": "1",
  "name": "Product Name",
  "price": 129.99,
  "shortDescription": "Brief description",
  "fullDescription": "Detailed description",
  "images": ["image1.jpg", "image2.jpg"],
  "technicalSpecifications": {
    "Spec1": "Value1",
    "Spec2": "Value2"
  },
  "stockQuantity": 50,
  "category": "Category Name"
}
```

## Field Descriptions

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `id` | String | Yes | Unique product identifier |
| `name` | String | Yes | Product name |
| `price` | Number | Yes | Product price (decimal) |
| `shortDescription` | String | No | Brief product description |
| `fullDescription` | String | No | Detailed product description |
| `images` | Array | No | List of image URLs |
| `technicalSpecifications` | Object | No | Key-value pairs for technical specs |
| `stockQuantity` | Number | Yes | Available stock quantity |
| `category` | String | No | Product category |

## How to Update Products

### Method 1: Edit JSON File and Reload

1. **Edit the JSON file**:
   ```bash
   # Open the JSON file in your preferred editor
   nano src/main/resources/products.json
   # or
   code src/main/resources/products.json
   ```

2. **Reload via API** (requires admin access):
   ```bash
   # Get admin JWT token
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username": "admin", "password": "admin123"}'
   
   # Reload products (replace YOUR_JWT_TOKEN with actual token)
   curl -X POST http://localhost:8080/api/products/reload \
     -H "Authorization: Bearer YOUR_JWT_TOKEN"
   ```

### Method 2: Use Management Script

```bash
# Make script executable (first time only)
chmod +x scripts/update-products.sh

# Reload products from JSON
./scripts/update-products.sh reload

# Check current product count
./scripts/update-products.sh count

# Validate JSON file
./scripts/update-products.sh validate

# Create backup of current products
./scripts/update-products.sh backup
```

### Method 3: Restart Application

Products are automatically loaded from the JSON file when the application starts:

```bash
# Stop the application
# Edit src/main/resources/products.json
# Start the application
./mvnw spring-boot:run
```

## Example Product Updates

### Adding a New Product

Add this to the JSON array:

```json
{
  "id": "21",
  "name": "New Professional Tool",
  "price": 299.99,
  "shortDescription": "Latest professional tool for heavy-duty work",
  "fullDescription": "This new professional tool offers superior performance and durability for the most demanding applications.",
  "images": ["images/product_21/product_21.jpg"],
  "technicalSpecifications": {
    "Power": "1500W",
    "Weight": "3.2 kg",
    "Warranty": "3 years"
  },
  "stockQuantity": 15,
  "category": "Power Tools"
}
```

### Updating an Existing Product

Find the product by ID and modify its fields:

```json
{
  "id": "1",
  "name": "Updated Product Name",
  "price": 149.99,  // Updated price
  "shortDescription": "Updated description",
  "fullDescription": "Updated full description",
  "images": ["images/product_1/product_1.jpg", "images/product_1/product_1.1.jpeg"],
  "technicalSpecifications": {
    "Battery Voltage": "12 V",
    "Max Torque": "35 Nm",  // Updated value
    "Weight": "0.9 kg",
    "Chuck Size": "10 mm",
    "No Load Speed": "0-1300 rpm"
  },
  "stockQuantity": 75,  // Updated stock
  "category": "Power Tools"
}
```

### Removing a Product

Simply delete the product object from the JSON array.

## Best Practices

### 1. Data Validation
- Ensure all required fields are present
- Use valid JSON syntax
- Validate prices are positive numbers
- Check stock quantities are non-negative

### 2. Image Management
- Use consistent image naming conventions
- Store images in organized folders
- Include multiple images for better product presentation

### 3. Categories
- Use consistent category names
- Consider creating a category hierarchy
- Keep categories relevant to your product catalog

### 4. Technical Specifications
- Use consistent units (e.g., always use "kg" for weight)
- Include relevant specifications for each product type
- Keep specifications concise but informative

### 5. Version Control
- Commit JSON changes to version control
- Use meaningful commit messages
- Consider branching for major catalog updates

## Troubleshooting

### Common Issues

1. **JSON Syntax Errors**
   ```bash
   # Validate JSON syntax
   ./scripts/update-products.sh validate
   ```

2. **File Not Found**
   - Ensure `products.json` is in `src/main/resources/`
   - Check file permissions

3. **Authentication Errors**
   - Verify admin credentials
   - Check JWT token expiration
   - Ensure proper Authorization header

4. **Database Errors**
   - Check database connection
   - Verify JPA configuration
   - Review application logs

### Error Messages

| Error | Cause | Solution |
|-------|-------|----------|
| "products.json file not found" | File missing or wrong location | Place file in `src/main/resources/` |
| "JSON file is invalid" | Syntax error in JSON | Validate JSON syntax |
| "Failed to get JWT token" | Authentication failed | Check admin credentials |
| "Error reloading products" | Database or parsing error | Check application logs |

## API Endpoints

### Product Management Endpoints

| Endpoint | Method | Description | Auth Required |
|----------|--------|-------------|---------------|
| `/api/products/reload` | POST | Reload products from JSON | Admin |
| `/api/products/count` | GET | Get product count | No |
| `/api/products` | GET | List all products | No |
| `/api/products/{id}` | GET | Get single product | No |

### Authentication Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/auth/login` | POST | Get JWT token |
| `/api/auth/register` | POST | Register new user |

## Security Considerations

1. **Admin Access Only**: Product reload requires admin privileges
2. **JWT Authentication**: All admin operations require valid JWT token
3. **Input Validation**: JSON data is validated before processing
4. **Error Handling**: Graceful fallback to hardcoded products if JSON fails

## Performance Notes

- Product reload clears and recreates all products
- Large JSON files may take time to process
- Consider batching updates for large catalogs
- Monitor database performance during reloads

## Support

For issues or questions:
1. Check application logs
2. Validate JSON syntax
3. Test with management script
4. Review this guide
5. Check the main README.md file 