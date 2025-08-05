#!/bin/bash

# Product Management Script for E-commerce API
# This script helps manage product data updates

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
API_BASE_URL="http://localhost:8080"
ADMIN_USERNAME="admin"
ADMIN_PASSWORD="admin123"

echo -e "${BLUE}=== E-commerce Product Management Script ===${NC}"

# Function to get JWT token
get_jwt_token() {
    echo -e "${YELLOW}Getting JWT token for admin user...${NC}"
    
    TOKEN=$(curl -s -X POST "$API_BASE_URL/api/auth/login" \
        -H "Content-Type: application/json" \
        -d "{
            \"username\": \"$ADMIN_USERNAME\",
            \"password\": \"$ADMIN_PASSWORD\"
        }" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
    
    if [ -z "$TOKEN" ]; then
        echo -e "${RED}Failed to get JWT token. Please check if the application is running and credentials are correct.${NC}"
        exit 1
    fi
    
    echo -e "${GREEN}JWT token obtained successfully!${NC}"
}

# Function to reload products
reload_products() {
    echo -e "${YELLOW}Reloading products from JSON file...${NC}"
    
    RESPONSE=$(curl -s -X POST "$API_BASE_URL/api/products/reload" \
        -H "Authorization: Bearer $TOKEN" \
        -H "Content-Type: application/json")
    
    if [[ $RESPONSE == *"successfully"* ]]; then
        echo -e "${GREEN}Products reloaded successfully!${NC}"
        echo -e "${BLUE}Response: $RESPONSE${NC}"
    else
        echo -e "${RED}Failed to reload products: $RESPONSE${NC}"
        exit 1
    fi
}

# Function to get product count
get_product_count() {
    echo -e "${YELLOW}Getting current product count...${NC}"
    
    COUNT=$(curl -s -X GET "$API_BASE_URL/api/products/count")
    
    if [ -n "$COUNT" ] && [ "$COUNT" -ge 0 ]; then
        echo -e "${GREEN}Current product count: $COUNT${NC}"
    else
        echo -e "${RED}Failed to get product count${NC}"
    fi
}

# Function to validate JSON file
validate_json() {
    echo -e "${YELLOW}Validating products.json file...${NC}"
    
    if [ ! -f "src/main/resources/products.json" ]; then
        echo -e "${RED}products.json file not found in src/main/resources/${NC}"
        exit 1
    fi
    
    if command -v jq &> /dev/null; then
        if jq empty src/main/resources/products.json 2>/dev/null; then
            echo -e "${GREEN}JSON file is valid!${NC}"
        else
            echo -e "${RED}JSON file is invalid!${NC}"
            exit 1
        fi
    else
        echo -e "${YELLOW}jq not found, skipping JSON validation${NC}"
    fi
}

# Function to backup current products
backup_products() {
    echo -e "${YELLOW}Creating backup of current products...${NC}"
    
    BACKUP_FILE="products_backup_$(date +%Y%m%d_%H%M%S).json"
    
    curl -s -X GET "$API_BASE_URL/api/products" \
        -H "Content-Type: application/json" > "$BACKUP_FILE"
    
    if [ -s "$BACKUP_FILE" ]; then
        echo -e "${GREEN}Backup created: $BACKUP_FILE${NC}"
    else
        echo -e "${RED}Failed to create backup${NC}"
    fi
}

# Main script logic
case "${1:-help}" in
    "reload")
        validate_json
        get_jwt_token
        reload_products
        get_product_count
        ;;
    "count")
        get_product_count
        ;;
    "backup")
        get_jwt_token
        backup_products
        ;;
    "validate")
        validate_json
        ;;
    "help"|*)
        echo -e "${BLUE}Usage: $0 [command]${NC}"
        echo ""
        echo -e "${YELLOW}Commands:${NC}"
        echo -e "  ${GREEN}reload${NC}    - Reload products from JSON file"
        echo -e "  ${GREEN}count${NC}     - Get current product count"
        echo -e "  ${GREEN}backup${NC}    - Create backup of current products"
        echo -e "  ${GREEN}validate${NC}  - Validate JSON file format"
        echo -e "  ${GREEN}help${NC}      - Show this help message"
        echo ""
        echo -e "${YELLOW}Examples:${NC}"
        echo -e "  $0 reload    # Reload products from JSON"
        echo -e "  $0 count     # Check product count"
        echo -e "  $0 backup    # Backup current products"
        ;;
esac

echo -e "${BLUE}=== Script completed ===${NC}" 